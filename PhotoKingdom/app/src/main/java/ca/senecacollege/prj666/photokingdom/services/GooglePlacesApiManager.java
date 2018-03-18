package ca.senecacollege.prj666.photokingdom.services;

import android.location.Location;
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
import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.GooglePlace;

/**
 * Service to get place information from Google Places API Web Service
 */
public class GooglePlacesApiManager {
    private final String TAG = "GooglePlacesApiManager";
    private static final String GOOGLE_PLACES_WEB_SERVICE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String KEY = "key=";
    private static final String API_KEY = "AIzaSyAUHQwgoYQyDlKLzLDehrZz-3JElO_I4-Y";
    private static final String LOCATION = "&location=";
    private static final String RADIUS = "&radius=";
    private static final String TYPE = "&type=";
    private static final String PAGE_TOKEN = "&pagetoken=";

    private final String[] placeTypes = {""};
    private double Lat;
    private double Lng;
    private List<GooglePlace> googlePlaces;
    private String nextPageToken;
    private double metersToSearch;

    public GooglePlacesApiManager(double lat, double lng, double metersToSearch){
        this.Lat = lat;
        this.Lng = lng;
        this.metersToSearch = metersToSearch;
        this.googlePlaces = new ArrayList<>();
    }

    public List<GooglePlace> getGooglePlaces() throws ApiException{
        makeRequest();
        return googlePlaces;
    }

    public void makeRequest() throws ApiException{
        try{
            boolean nextToken = true;
            while(nextToken){
                HttpURLConnection urlConnection;
                URL url;
                StringBuilder urlString = new StringBuilder();
                urlString.append(GOOGLE_PLACES_WEB_SERVICE).append(KEY).append(API_KEY);

                if(this.nextPageToken == null || this.nextPageToken.isEmpty()){
                    // first page
                    urlString.append(LOCATION).append(this.Lat + "," + this.Lng);
                    urlString.append(RADIUS).append(this.metersToSearch);
                    urlString.append(TYPE).append("park");
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
