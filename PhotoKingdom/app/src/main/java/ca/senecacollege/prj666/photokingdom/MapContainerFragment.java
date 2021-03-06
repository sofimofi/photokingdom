package ca.senecacollege.prj666.photokingdom;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ca.senecacollege.prj666.photokingdom.adapters.MapMarkerInfoWindowAdapter;
import ca.senecacollege.prj666.photokingdom.fragments.AttractionDetailsFragment;
import ca.senecacollege.prj666.photokingdom.models.AttractionForMapView;
import ca.senecacollege.prj666.photokingdom.models.GooglePlace;
import ca.senecacollege.prj666.photokingdom.models.LatLngBoundaries;
import ca.senecacollege.prj666.photokingdom.services.AttractionsForMapViewManager;
import ca.senecacollege.prj666.photokingdom.services.GooglePlacesApiManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapContainerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapContainerFragment extends Fragment implements OnMapReadyCallback, OnGooglePlacesApiTaskCompleted,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "MapContainerFragment";
    static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    // Current location
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;

    // Default location York University in case cannot get device current location
    private final LatLng mDefaultLocation = new LatLng(43.773111, -79.498842);

    // Zoom settings
    private static final double EQUATOR_LENGTH = 40075004; // in meters
    private static final int DEFAULT_ZOOM = 15;
    private static final double MAP_WIDTH_METERS = 4000; // default size of map width
    private int screenWidthPixels;
    private int customZoom;

    private GoogleMap mGoogleMap;
    private OnFragmentInteractionListener mListener;

    // handle opening and closing marker
    Marker lastOpened = null;

    public MapContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapContainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapContainerFragment newInstance(String param1, String param2) {
        MapContainerFragment fragment = new MapContainerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.map);

        // Get current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // get the screen width pixels to set zoom level later
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidthPixels = metrics.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_container, container, false);

        // Initialize a map if permissions are granted
        //initMap();
        if (checkPermission()) {
            Log.d(TAG, "PERMISSION GIVEN");
            initMapWithCurrentLocation();
        }

        return rootView;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);

        // check if null
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.mapFragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    /**
     * Get device's current location
     * Initialize Google Map
     */
    public void initMapWithCurrentLocation() {
        try {
            if (mFusedLocationClient != null) {
                mFusedLocationClient.getLastLocation()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    mCurrentLocation = task.getResult();
                                    Log.d(TAG, "Current location is " + mCurrentLocation.toString());
                                    initMap();
                                }
                            }
                        });
            } else {
                initMap();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // move to current/default location
        updateLocationUI();

        getNearbyAttractions(MAP_WIDTH_METERS, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        // TODO: custom Info Window (to include avatar image?)
//        mGoogleMap.setInfoWindowAdapter(new MapMarkerInfoWindowAdapter());
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        //mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
    }

    public void getNearbyAttractions(double radiusMeters, double lat, double lng){
        // get the nearby google places
        GooglePlacesRequest task = new GooglePlacesRequest(this, getActivity().getApplicationContext(), lat, lng, radiusMeters);

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        List<GooglePlace> googlePlaces = new ArrayList<GooglePlace>();
        try{
            googlePlaces = task.get();
            Log.d(TAG, "result from GooglePlaceRequest task: " + googlePlaces.toString());
        } catch (InterruptedException | ExecutionException e){
            Log.e(TAG, e.getMessage());
        }

        LatLng currLatLng = new LatLng(lat, lng);
        LatLngBoundaries latlngbounds = getLatLngBoundaries(currLatLng, radiusMeters );
        Log.d(TAG, "LatLng boundaries to search attractions: " + latlngbounds.toString());

        // get attractions that exist in database in same boundaries
        AttractionsForMapViewRequest attractionTask = new AttractionsForMapViewRequest(/*this,*/ /*getActivity().getApplicationContext(),*/ latlngbounds);
        attractionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        List<AttractionForMapView> existingAttractions = null;
        try{
            existingAttractions = attractionTask.get();
            Log.d(TAG, "result from AttractionsForMapViewRequest task: " + existingAttractions.toString());
        } catch (InterruptedException | ExecutionException e){
            Log.e(TAG, e.getMessage());
        }

        // Add markers on map
        BitmapDescriptor throne = BitmapDescriptorFactory.fromResource(R.drawable.throne);
        for(GooglePlace place : googlePlaces){
            LatLng latLng = new LatLng(place.getLat(), place.getLng());

            // look for this google place in the attraction list
            AttractionForMapView attraction = getAttractionByGooglePlaceId(place.getPlace_id(), existingAttractions);

            if(attraction != null){
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .title(attraction.getResidentUserName())
                        .position(latLng)
                        .snippet(attraction.getResidentTitle())
                        .icon(throne));
                marker.setTag(attraction.getGooglePlaceId());
            } else {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .title(place.getName())
                        .position(latLng));
                marker.setTag(place.getPlace_id());
            }
        }
    }

    /**
     * Checks for googlePlace in list of AttractionForMapView
     */
    public AttractionForMapView getAttractionByGooglePlaceId(String googlePlaceId, List<AttractionForMapView> attractionList){
        if(attractionList != null){
            for(AttractionForMapView a : attractionList){
                if(a.getGooglePlaceId().equals(googlePlaceId)){
                    return a;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(final Marker marker){
        String googlePlaceId = (String) marker.getTag();
        Log.d(TAG, "---> Clicked on Marker for place " + googlePlaceId);

        if(lastOpened != null){
            // close the info window from last marker
            lastOpened.hideInfoWindow();

            // if same marker as previously, leave it open
            if(lastOpened.equals(marker)){
                lastOpened = null;
                return true;
            }
        }

        // open info window
        marker.showInfoWindow();
        lastOpened = marker;
        return true; // = use our own code, do not use default behaviour (no auto-centering)
    }

    @Override
    public void onInfoWindowClick(Marker marker){
        String googlePlaceId = (String) marker.getTag();
        Log.d(TAG, "---> Clicked on Info Window for place " + googlePlaceId);

        // Open attraction details view with the title
        openAttractionDetailsView(marker.getTitle());
    }

    @Override
    public void onCameraMove(){
        LatLng center = mGoogleMap.getCameraPosition().target;
        LatLng radius = getScreenRadius();
        Double radiusMeters = toRadiusMeters(center, radius);

        Log.d(TAG, "OnCameraMove ---> new radius meters : " + radiusMeters);

        getNearbyAttractions(radiusMeters, center.latitude, center.longitude);
    }

    @Override
    public void onCameraIdle() {
        LatLng center = mGoogleMap.getCameraPosition().target;
        LatLng radius = getScreenRadius();
        Double radiusMeters = toRadiusMeters(center, radius);

        Log.d(TAG, "onCameraIdle ---> new radius meters : " + radiusMeters);

        getNearbyAttractions(radiusMeters, center.latitude, center.longitude);
    }

    private LatLng getScreenRadius(){
        View view = getChildFragmentManager().findFragmentById(R.id.mapFragment).getView();
        LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(
                new Point(view.getHeight(), view.getWidth())
        );
        return latLng;
    }

    private double toRadiusMeters(LatLng center, LatLng radius){
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i("checkPermission", "Not enough permission");
            // add request permission on runtime if failed
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    PERMISSIONS_REQUEST_ACCESS_LOCATION);

            return false;
        }
        Log.d(TAG, "ALREADY HAVE PERMISSION!");
        return true;
    }

    private void updateLocationUI(){
        Log.d(TAG, "UPDATE LOCATION UI, current location:" + mCurrentLocation.toString());
        if(mGoogleMap != null){
            //checkPermission();
            try {
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

                if (mCurrentLocation != null) {
                    /*
                        Zoom level:
                            1: World
                            5: Landmass/continent
                            10: City
                            15: Streets
                            20: Buildings
                        https://developers.google.com/maps/documentation/android-api/views#zoom
                     */

                    // Get default zoom level based on desired meters on width
                    double zoomLevel = getZoomForMetersWide((double) MAP_WIDTH_METERS, (double) screenWidthPixels, mCurrentLocation.getLatitude());
                    customZoom = (int) zoomLevel;
                    Log.d(TAG, "Camera Zoom level: " + customZoom);

                    LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, customZoom));
                } else {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAttractionDetailsView(String name) {
        // Move to AttractionDetailsFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, AttractionDetailsFragment.newInstance(name, false))
                .addToBackStack(null)
                .commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.d(TAG,"Map container fragment created");
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void OnTaskCompleted(List<GooglePlace> googlePlaces) {
        Log.d(TAG, "Completed GooglePlacesAPI Task");
    }

    /**
     * Makes async request for google places
     */
    public static class GooglePlacesRequest extends AsyncTask<String, Void, List<GooglePlace>> {
        private OnGooglePlacesApiTaskCompleted listener;
        private Context context;
        private double lat;
        private double lng;
        private double metersToSearch;

        private ApiException e;

        public GooglePlacesRequest(OnGooglePlacesApiTaskCompleted listener,
                                   Context context,
                                   double lat,
                                   double lng,
                                   double metersToSearch){
            this.listener = listener;
            this.context = context;
            this.lat = lat;
            this.lng = lng;
            this.metersToSearch = metersToSearch;
        }

        @Override
        protected List<GooglePlace> doInBackground(String... params){

            List<GooglePlace> googlePlaces = new ArrayList<GooglePlace>();
            try{
                GooglePlacesApiManager manager = new GooglePlacesApiManager(this.lat, this.lng, this.metersToSearch);
                googlePlaces = manager.getGooglePlaces();
            } catch (ApiException e){
                this.e = e;
            }
            return googlePlaces;
        }

        @Override
        protected void onPostExecute(List<GooglePlace> googlePlaces){
            super.onPostExecute(googlePlaces);

            if(this.e != null){
                // tell user something went wrong
                Toast toast = Toast.makeText(context, "Could not get nearby attractions", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            listener.OnTaskCompleted(googlePlaces);
        }
    }

    /**
     * Makes async request for attractions in app database
     */
    public static class AttractionsForMapViewRequest extends AsyncTask<String, Void, List<AttractionForMapView>> {
//        private OnAttractionsForMapViewTaskCompleted listener;
//        private Context context;
        private LatLngBoundaries latLngBoundaries;

        private ApiException e;

        public AttractionsForMapViewRequest(/*OnAttractionsForMapViewTaskCompleted listener,*/
                                   /*Context context,*/ LatLngBoundaries latLngBoundaries ){
//            this.listener = listener;
//            this.context = context;
            this.latLngBoundaries = latLngBoundaries;
        }

        @Override
        protected List<AttractionForMapView> doInBackground(String... params){

            List<AttractionForMapView> attractionsForMapView = new ArrayList<AttractionForMapView>();
            try{
                AttractionsForMapViewManager attractionManager = new AttractionsForMapViewManager();
                attractionsForMapView = attractionManager.getAttractionsForMapView(latLngBoundaries);
            } catch (ApiException e){
                this.e = e;
            }
            return attractionsForMapView;
        }

        @Override
        protected void onPostExecute(List<AttractionForMapView> attractionsForMapView){
            super.onPostExecute(attractionsForMapView);

            if(this.e != null){
                // tell user something went wrong
//                Toast toast = Toast.makeText(context, "Error getting attractions", Toast.LENGTH_SHORT);
//                toast.show();
                Log.e(TAG, "-----> EXCEPTION from AttractionsForMapViewRequest task : " + e.getMessage());
                return;
            }
//            listener.OnTaskCompleted(attractionsForMapView);
        }
    }

    /**
     * Gets Camera Zoom Level for View width distance in meters
     */
    public static double getZoomForMetersWide (
            final double desiredMeters,
            final double mapWidth,
            final double latitude )
    {
        final double latitudinalAdjustment = Math.cos( Math.PI * latitude / 180.0 );

        final double arg = EQUATOR_LENGTH * mapWidth * latitudinalAdjustment / ( desiredMeters * 256.0 );

        return Math.log( arg ) / Math.log( 2.0 );
    }

    /**
     * Creates the LatLngBoundaries for a region defined by the meters offset from a current location
     */
    public LatLngBoundaries getLatLngBoundaries(LatLng currentLatLng, double offsetMeters){
        // get northernmost point
        LatLng north = SphericalUtil.computeOffset(currentLatLng, offsetMeters, 0);
        LatLng east = SphericalUtil.computeOffset(currentLatLng, offsetMeters, 90);
        LatLng south = SphericalUtil.computeOffset(currentLatLng, offsetMeters, 180);
        LatLng west = SphericalUtil.computeOffset(currentLatLng, offsetMeters, 270);
        Log.d("LATLNG BOUNDS --->", "north: {" + north.latitude + "," + north.longitude +
        "}, east: {" + east.latitude + "," + east.longitude + "}, south: {" + south.latitude + "," +
        south.longitude + "}, west: {" + west.latitude + "," + west.longitude + "}");
        LatLngBoundaries latLngBoundaries = new LatLngBoundaries(north.latitude, south.latitude, east.longitude, west.longitude  );
        return latLngBoundaries;
    }
}
