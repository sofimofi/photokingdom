package ca.senecacollege.prj666.photokingdom.services;

import android.util.Log;

import com.google.android.gms.common.api.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.AttractionForMapView;
import ca.senecacollege.prj666.photokingdom.models.LatLngBoundaries;

/**
 * Service to get Attractions from database for use in map view
 */
public class AttractionsForMapViewManager {
    private static final String TAG = "AttractionsMapViewMgr";
    private static final String BASE_URL = "https://photokingdom-api.azurewebsites.net/";
    private static final String URI = "api/attractions/mapview?";
    private static final String MAX_LAT = "maxLat=";
    private static final String MIN_LAT = "&minLat=";
    private static final String MAX_LNG = "&maxLng=";
    private static final String MIN_LNG = "&minLng=";


    public List<AttractionForMapView> getAttractionsForMapView(LatLngBoundaries latLngBoundaries) throws ApiException{
        if(latLngBoundaries == null) return null;
        List<AttractionForMapView> attractionList = new ArrayList<>();

        HttpURLConnection urlConnection;
        URL url;
        StringBuilder urlString = new StringBuilder();
        urlString.append(BASE_URL).append(URI);
        urlString.append(MAX_LAT).append(latLngBoundaries.getMaxLat());
        urlString.append(MIN_LAT).append(latLngBoundaries.getMinLat());
        urlString.append(MAX_LNG).append(latLngBoundaries.getMaxLng());
        urlString.append(MIN_LNG).append(latLngBoundaries.getMinLng());

        try{
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

            JSONArray json = (JSONArray) new JSONTokener(
                    response.toString()
            ).nextValue();

            for(int i = 0; i < json.length(); i++){
                JSONObject attractionObj = json.getJSONObject(i);

                int id = attractionObj.getInt("Id");
                String googlePlaceId = attractionObj.getString("googlePlaceId");
                String name = attractionObj.getString("Name");
                Double lat = attractionObj.getDouble("Lat");
                Double lng = attractionObj.getDouble("Lng");
                int isActive = attractionObj.getInt("IsActive");
                int cityId = attractionObj.getInt("CityId");

                JSONArray owners = attractionObj.getJSONArray("Owners");
                JSONObject currentOwner = owners.getJSONObject(0);
                String residentTitle = currentOwner.getString("Title");

                JSONObject resident = currentOwner.getJSONObject("Resident");
                String residentUserName = resident.getString("UserName");
                String residentAvatar = resident.getString("AvatarImagePath") == null ? "" : resident.getString("AvatarImagePath");

                AttractionForMapView attraction = new AttractionForMapView(id, googlePlaceId, name, lat, lng, isActive, cityId,
                        residentUserName, residentAvatar, residentTitle);
                attractionList.add(attraction);
            }

        } catch ( IOException | JSONException e){
            Log.e(TAG, e.getMessage());
        }

        Log.d(TAG, "-----> Number of attractions fetched from database: " + attractionList.size());
        return attractionList;
    }
}
