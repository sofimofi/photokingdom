package ca.senecacollege.prj666.photokingdom.adapters;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

// TODO
/**
 * Custom InfoWindowAdapter for Map Places
 */
public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "MapMarkerInfoWindowAdp";
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String googlePlaceId = (String) marker.getTag();
        Log.d(TAG, "clicked on ------->" + googlePlaceId);
        return null;
    }
}
