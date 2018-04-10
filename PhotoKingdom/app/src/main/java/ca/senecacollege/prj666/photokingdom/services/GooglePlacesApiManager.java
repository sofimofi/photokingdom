package ca.senecacollege.prj666.photokingdom.services;

import android.util.Log;

import com.google.android.gms.common.api.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.senecacollege.prj666.photokingdom.models.GooglePlace;
import ca.senecacollege.prj666.photokingdom.models.Locality;
import ca.senecacollege.prj666.photokingdom.models.PlaceType;

/**
 * Service to get place information from Google Places API Web Service
 */
public class GooglePlacesApiManager {
    private final String TAG = "GooglePlacesApiManager";
    private static final String GOOGLE_PLACES_WEB_SERVICE_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_PLACES_WEB_SERVICE_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static final String KEY = "key=";
    private static final String API_KEY = "AIzaSyAUHQwgoYQyDlKLzLDehrZz-3JElO_I4-Y";
    private static final String LOCATION = "&location=";
    private static final String RADIUS = "&radius=";
    private static final String TYPE = "&type=";
    private static final String PAGE_TOKEN = "&pagetoken=";
    private static final String PLACE_ID = "&placeid=";

    private static final String CITY = "locality";
    private static final String PROVINCE = "administrative_area_level_1";
    private static final String COUNTRY = "country";

    private final String[] placeTypes = {"museum","natural_feature","premise","park" };
    private final String placeType;

    private double Lat;
    private double Lng;
    private Set<GooglePlace> googlePlaces;
    private String nextPageToken;
    private double metersToSearch;

    public GooglePlacesApiManager(double lat, double lng, double metersToSearch){
        this.Lat = lat;
        this.Lng = lng;
        this.metersToSearch = metersToSearch;
        this.googlePlaces = new HashSet<>();
        this.placeType = PlaceType.ALL;
    }

    public GooglePlacesApiManager(double lat, double lng, double metersToSearch, String placeType){
        this.Lat = lat;
        this.Lng = lng;
        this.metersToSearch = metersToSearch;
        this.googlePlaces = new HashSet<>();
        this.placeType = placeType;
    }

    public Set<GooglePlace> getGooglePlaces() throws ApiException{
        if (placeType == PlaceType.ALL) {
            for (String place : placeTypes) {
                makeRequest(place);
            }
        } else {
            makeRequest(placeType);
        }

        return googlePlaces;
    }

    public Locality getCurrentLocality()throws ApiException{
        String place_id = getPlaceId();
        if(place_id == null){ return null; }

        Locality locality = getLocalityForPlaceID(place_id);
        return locality;
    }

    public String getPlaceId(){
        String place_id = null;
        try{
            HttpURLConnection urlConnection;
            URL url;
            StringBuilder urlString = new StringBuilder();
            urlString.append(GOOGLE_PLACES_WEB_SERVICE_NEARBY).append(KEY).append(API_KEY);
            urlString.append(LOCATION).append(this.Lat + "," + this.Lng);
            urlString.append(RADIUS).append(this.metersToSearch);

            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

            String tmp;
            StringBuilder response = new StringBuilder();
            while((tmp = bReader.readLine()) != null){
                response.append(tmp);
            }

            JSONObject json = (JSONObject) new JSONTokener(
                    response.toString()
            ).nextValue();

            bReader.close();
            inStream.close();

            JSONArray results = json.getJSONArray("results");
            if(results.length() > 0){
                // only use the first place as a reference
                JSONObject placeObj = results.getJSONObject(0);
                place_id = placeObj.getString("place_id");
            } else {
                // no results - need to increase radius and try again
                this.metersToSearch *= 2;
                return getPlaceId();
            }
            return place_id;
        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return place_id;
    }

    public Locality getLocalityForPlaceID(String placeId) {
        Locality locality = null;
        try {
            HttpURLConnection urlConnection;
            URL url;
            StringBuilder urlString = new StringBuilder();
            urlString.append(GOOGLE_PLACES_WEB_SERVICE_DETAILS).append(KEY).append(API_KEY);
            urlString.append(PLACE_ID).append(placeId);

            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

            String tmp;
            StringBuilder response = new StringBuilder();
            while((tmp = bReader.readLine()) != null){
                response.append(tmp);
            }

            JSONObject json = (JSONObject) new JSONTokener(
                    response.toString()
            ).nextValue();

            JSONObject result = json.getJSONObject("result");
            JSONArray addressComponents = result.getJSONArray("address_components");

            String city = "";
            String province = "";
            String country = "";
            for(int i = 0; i < addressComponents.length(); i++){
                JSONObject address = addressComponents.getJSONObject(i);
                JSONArray types = address.getJSONArray("types");
                for(int j = 0; j < types.length(); j++){
                    if(types.getString(j).equals(CITY)){
                        city = address.getString("long_name");
                    }
                    if(types.getString(j).equals(PROVINCE)){
                        province = address.getString("long_name");
                    }
                    if(types.getString(j).equals(COUNTRY)){
                        country = address.getString("long_name");
                    }
                }
            }
            locality = new Locality(city, province, country);
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return locality;
    }

    public void makeRequest(String place) throws ApiException{
        //for(String place : placeTypes){
        try{
            boolean nextToken = true;
            while(nextToken){
                HttpURLConnection urlConnection;
                URL url;
                StringBuilder urlString = new StringBuilder();
                urlString.append(GOOGLE_PLACES_WEB_SERVICE_NEARBY).append(KEY).append(API_KEY);

                if(this.nextPageToken == null || this.nextPageToken.isEmpty()){
                    // first page
                    urlString.append(LOCATION).append(this.Lat + "," + this.Lng);
                    urlString.append(RADIUS).append(this.metersToSearch);
                    urlString.append(TYPE).append(place);
                } else {
                    // there is a next page token - get next page
                    urlString.append(PAGE_TOKEN).append(this.nextPageToken);
                }

                url = new URL(urlString.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

                String tmp;
                StringBuilder response = new StringBuilder();
                while((tmp = bReader.readLine()) != null){
                    response.append(tmp);
                }

                JSONObject json = (JSONObject) new JSONTokener(
                        response.toString()
                ).nextValue();

                nextToken = parseJson(json);
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        //}
    }

    private boolean parseJson(JSONObject json) throws Exception {
        if(json == null){
            throw new Exception("parseJson received NULL json");
        }

        String nextToken = null;

        if (json.has("next_page_token")){
            nextToken = json.getString("next_page_token");
        }

        // get place results array
        JSONArray results = json.getJSONArray("results");
        for (int i = 0; i < results.length(); i++){
            JSONObject placeObj = results.getJSONObject(i);
            String place_id = placeObj.getString("place_id");
            String name = placeObj.getString("name");
            JSONObject locationObj = placeObj.getJSONObject("geometry")
                    .getJSONObject("location");
            Double lat = locationObj.getDouble("lat");
            Double lng = locationObj.getDouble("lng");

            googlePlaces.add(new GooglePlace(place_id, name, lat, lng));
        }

        if (nextToken != null){
            this.nextPageToken = nextToken;
            return true;
        } else {
            // put back to null
            this.nextPageToken = null;
            return false;
        }
    }
}
