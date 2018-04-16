package ca.senecacollege.prj666.photokingdom.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.drew.lang.GeoLocation;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ca.senecacollege.prj666.photokingdom.PhotowarFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Attraction;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarAddForm;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.AttractionWithWin;
import ca.senecacollege.prj666.photokingdom.models.Constants;
import ca.senecacollege.prj666.photokingdom.models.LatLngBoundaries;
import ca.senecacollege.prj666.photokingdom.models.Locality;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueue;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.services.GooglePlacesApiManager;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.GpsMetadataUtil;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;
import ca.senecacollege.prj666.photokingdom.utils.UploadManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;

/**
 * Fragment for attraction details
 *
 * @author Wonho, Sofia, Zhihao
 */
public class AttractionDetailsFragment extends Fragment {
    private static final String TAG = "AttractionDetailsFrag";
    private static final int ACTION_PICK_REQUEST = 1;

    // Argument keys
    private static final String ATTRACTION_NAME = "name";
    private static final String ATTRACTION_LAT = "lat";
    private static final String ATTRACTION_LNG = "lng";
    private static final String PLACE_ID = "placeId";
    private static final String IS_EXISTED = "isExisted";
    private static final String IS_PINGED = "isPinged";
    private static final String PING_ID = "pingId";
    private static final String HAS_WAR = "hasWar";
    private static final String IS_LIT_UP = "isLitUp";

    // Argument values
    private String mName;
    private LatLng mLatLng;
    private String mPlaceId;
    private boolean mIsExisted;
    private boolean mIsPinged;
    private int mPingId;
    private boolean mHasWar;
    private boolean mIsLitUp;

    // Resident session
    private ResidentSessionManager mSessionManager;
    private PhotoKingdomService service;

    // Widgets
    private ProgressBar mProgressBar;
    private TextView mTextViewName;
    private TextView mTextViewWinner;
    private ImageView mImageViewAttraction;
    private TextView mHistoryTextView;
    private Button mWinningPhotosButton;
    private Button mPhotowarButton;
    private Button mPingButton;
    private Button buttonUpload;

    private Attraction mAttraction;
    private PhotowarQueue mNewPhotowarQueue;
    private Uri photoUri;
    private String photoPath;
    private LatLng photoLatLng;

    public static AttractionDetailsFragment newInstance(Bundle bundle) {
        // Create an instance
        AttractionDetailsFragment fragment = new AttractionDetailsFragment();

        // Set arguments
        Bundle args = new Bundle();
        args.putAll(bundle);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mName = getArguments().getString(ATTRACTION_NAME);
            mPlaceId = getArguments().getString(PLACE_ID);
            mIsExisted = getArguments().getBoolean(IS_EXISTED);
            mIsPinged = getArguments().getBoolean(IS_PINGED);
            mPingId = getArguments().getInt(PING_ID);
            mHasWar = getArguments().getBoolean(HAS_WAR);
            mIsLitUp = getArguments().getBoolean(IS_LIT_UP);

            mLatLng = new LatLng(
                    getArguments().getDouble(ATTRACTION_LAT),
                    getArguments().getDouble(ATTRACTION_LNG)
            );
        }

        Log.d(TAG, "Opening up Place Id " + mPlaceId);
        mSessionManager = new ResidentSessionManager(getContext());
        service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attraction_details, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.attraction);

        // Widgets
        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        mTextViewName = (TextView)rootView.findViewById(R.id.textViewName);
        mTextViewWinner = (TextView)rootView.findViewById(R.id.textViewWinner);
        mImageViewAttraction = (ImageView)rootView.findViewById(R.id.imageViewAttraction);
        mHistoryTextView = (TextView) rootView.findViewById(R.id.attractionHistoryTextView);
        mWinningPhotosButton = (Button) rootView.findViewById(R.id.buttonWinningPhotos);
        mPhotowarButton = (Button) rootView.findViewById(R.id.buttonPhotowars);
        mPingButton = (Button)rootView.findViewById(R.id.buttonPing);

        if (mHasWar) {
            ImageView imageViewWar = (ImageView)rootView.findViewById(R.id.imageViewWar);
            imageViewWar.setVisibility(VISIBLE);
        }

        // Buttons are visible if the user logged-in
        if (mSessionManager.isLoggedIn()) {
            if (mIsPinged == true) {
                // From ping list
                enableUpload(rootView);
            } else {
                // From map
                if (mIsLitUp == true) {
                    enablePingButton();
                    enableUpload(rootView);
                }
            }
        }

        if (mPlaceId != null && !mPlaceId.isEmpty()) {
            getAttractionDetails(mPlaceId);
        }

        return rootView;
    }

    // upload button handler
    private void enableUpload(View view){
        buttonUpload = (Button)view.findViewById(R.id.buttonUpload);
        buttonUpload.setVisibility(VISIBLE);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestImagePermission();
            }
        });
    }

    // check permission
    private void requestImagePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                    Constants.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission has granted
            preparePhoto();
        }
    }

    // get photo from device
    public void preparePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_PICK_REQUEST);
    }

    // result of upload
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                photoUri = data.getData();

                // check if gps exists before doing anything
                try {
                    GpsDirectory gps;
                    if ((gps = GpsMetadataUtil.getGpsDirectory(getContext(), photoUri)) != null) {
                        // get the boundaries for the attraction, and verified if the gps is in range
                        LatLngBoundaries latLngBoundaries = getLatLngBoundaries(mLatLng, 500);
                        GeoLocation imgGeoLocation = gps.getGeoLocation();
                        photoLatLng = new LatLng(imgGeoLocation.getLatitude(), imgGeoLocation.getLongitude());

                        if (latLngBoundaries.checkInBoundaries(photoLatLng)) {
                            // upload the image if in range
                            UploadManager uploadManager = new UploadManager(getActivity());
                            uploadManager.setOnUploadListener(new UploadManager.OnUploadListener() {
                                @Override
                                public void onUploaded(String path) {
                                    Log.i("image path", path);
                                    photoPath = path;
                                    showProgressBar();
                                    doStuffWithAttraction();
                                }

                                @Override
                                public void onFailure(String error) {
                                    photoPath = "";
                                    Toast.makeText(getContext(), R.string.error_photo_upload, Toast.LENGTH_LONG).show();
                                }
                            });
                            uploadManager.uploadImage(photoUri);


                        } else {
                            Toast.makeText(getContext(), R.string.msg_gps_data_not_in_range, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (MetadataException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.msg_gps_data_not_found, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void doStuffWithAttraction(){
        // attraction NOT existed
        if(mIsExisted == false){
            // 1. create and save attraction to db
            LocalityRequest task = new LocalityRequest();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Locality locality = null;

            try{
                locality = task.get();
            } catch (InterruptedException | ExecutionException e){
                Log.e(TAG, e.getMessage());
            }

            if(locality != null){
                // add a new attraction to db
                final Attraction newAttraction = new Attraction(mPlaceId, mName, mLatLng.latitude, mLatLng.longitude,
                        locality.getCity(), locality.getCountry());

                Call<Attraction> attractionCall = service.createAttraction(newAttraction);
                attractionCall.enqueue(new Callback<Attraction>() {
                    @Override
                    public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                        if (response.isSuccessful()) {
                            Log.i("attraction upload", response.body().getId()+"");

                            // Remove a ping if upload on pinged attraction
                            if (mIsPinged) {
                                removePing();
                            }

                            newAttraction.setId(response.body().getId());
                            attachImageToNewAttraction(newAttraction);
                        }else {
                            hideProgressBar();
                            try {
                                Log.d(TAG, "[createAttraction:onResponse] " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Attraction> call, Throwable t) {
                        Log.d(TAG, "[createAttraction:onFailure] " + t.getMessage());
                    }
                });
            }
        } else {
            // attraction existed
            if(mHasWar == true){
                // case 1: have current photowar, then need a queue
                createPhotowarQueue(photoPath, photoLatLng);
            }else if(mAttraction.getOwnerName() != null){
                // case 2: no current war & has current owner, then start a war
                AttractionPhotowarAddForm newAttractionPhotowar = new AttractionPhotowarAddForm(
                        mAttraction.getId(), mSessionManager.getResident().getId(), photoPath,
                        photoLatLng.latitude, photoLatLng.longitude, mPlaceId
                );

                Call<AttractionPhotowarWithDetails> attractionPhotowarCall = service.createAttractionPhotowar(newAttractionPhotowar);
                attractionPhotowarCall.enqueue(new Callback<AttractionPhotowarWithDetails>() {
                    @Override
                    public void onResponse(Call<AttractionPhotowarWithDetails> call, Response<AttractionPhotowarWithDetails> response) {
                        Log.i("new photowar created", response.body().getId()+"");

                        // Remove a ping if upload on pinged attraction
                        if (mIsPinged) {
                            removePing();
                        }

                        // switch to photowar view
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, PhotowarFragment.newInstance(response.body().getId()))
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onFailure(Call<AttractionPhotowarWithDetails> call, Throwable t) {
                        hideProgressBar();
                        Log.d(TAG, "[createPhotowar:onFailure] " + t.getMessage());
                    }
                });
            }
        }

    }

    /**
     * Call PhotoKingdomAPI to remove ping
     */
    private void removePing() {
        if (mPingId > 0) {
            Call<Ping> pingCall = service.removePing(mPingId);
            pingCall.enqueue(new Callback<Ping>() {
                @Override
                public void onResponse(Call<Ping> call, Response<Ping> response) {
                    if (!response.isSuccessful()) {
                        try {
                            Log.d(TAG, "[removePing:onResponse] " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Ping> call, Throwable t) {
                    Log.e(TAG, "[removePing:onFailure] " + t.getMessage());
                }
            });
        }
    }

    /**
     * Call PhotoKingdomAPI to create photowar queue with uploaded photo
     * @param photoPath
     * @param photoLatLng
     */
    private void createPhotowarQueue(String photoPath, LatLng photoLatLng) {
        if (mSessionManager.isLoggedIn() && mAttraction != null && photoPath != null) {
            showProgressBar();

            // Photowar queue
            PhotowarQueue photowarQueue = new PhotowarQueue();
            photowarQueue.setAttractionId(mAttraction.getId());
            photowarQueue.setPhotoPath(photoPath);
            photowarQueue.setPhotoLat(photoLatLng.latitude);
            photowarQueue.setPhotoLng(photoLatLng.longitude);
            photowarQueue.setResidentId(mSessionManager.getResident().getId());

            Call<PhotowarQueue> photowarQueueCall = service.createPhotowarQueue(photowarQueue);
            photowarQueueCall.enqueue(new Callback<PhotowarQueue>() {
                @Override
                public void onResponse(Call<PhotowarQueue> call, Response<PhotowarQueue> response) {
                    hideProgressBar();

                    if (response.isSuccessful()) {
                        // Remove a ping if upload on pinged attraction
                        if (mIsPinged) {
                            removePing();
                        }

                        mNewPhotowarQueue = response.body();
                        openPhotowarQueueView();
                    } else {
                        try {
                            Log.d(TAG, "[createPhotowarQueue:onResponse] " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PhotowarQueue> call, Throwable t) {
                    hideProgressBar();
                    Log.e(TAG, "[createPhotowarQueue:onFailure] " + t.getMessage());
                }
            });
        }
    }

    /**
     * Show photowar queue view with created photowar queue
     */
    private void openPhotowarQueueView() {
        if (mNewPhotowarQueue != null) {
            // Move to PhotowarQueueFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, PhotowarQueueFragment.newInstance(mAttraction.getId(), mAttraction.getName()))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void attachImageToNewAttraction(Attraction newAttraction) {
        AttractionWithWin attractionWithWin = new AttractionWithWin(newAttraction.getId(),
                photoPath, photoLatLng.latitude, photoLatLng.longitude, mSessionManager.getResident().getId(), mPlaceId
        );

        Call<Attraction> attractionWithWinCall = service.editAttractionWithWin(newAttraction.getId(), attractionWithWin);
        attractionWithWinCall.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                if (response.isSuccessful()) {
                    hideProgressBar();
                    Log.i("edit attraction success", response.body().getOwnerName());

                    // get the full details of newly added attraction
                    mAttraction = response.body();
                    enableHistoryButtons();
                    setAttractionDetails();
                    loadImage(mImageViewAttraction, photoPath);
                }else {
                    hideProgressBar();
                    try {
                        Log.d(TAG, "[editAttraction:onResponse] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                hideProgressBar();
                Log.d(TAG, "[editAttraction:onFailure] " + t.getMessage());
            }
        });
    }

    // async call to get the locality
    private class LocalityRequest extends AsyncTask<String, Void, Locality> {
        @Override
        protected Locality doInBackground(String... params) {
            GooglePlacesApiManager placesApiManager = new GooglePlacesApiManager(mLatLng.latitude, mLatLng.longitude, 500);
            Locality locality = null;
            if (placesApiManager.getLocalityForPlaceID(mPlaceId) != null) {
                locality = placesApiManager.getLocalityForPlaceID(mPlaceId);
            }
            return locality;
        }

        @Override
        protected void onPostExecute(Locality locality) {
            super.onPostExecute(locality);
        }
    }

    // get boundaries
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

    /**
     * Call PhotoKingAPI to get attraction details data
     * @param placeId
     */
    private void getAttractionDetails(String placeId) {
        showProgressBar();

        Call<Attraction> call = service.getAttractionByPlaceId(placeId);
        call.enqueue(new Callback<Attraction>() {
            @Override
            public void onResponse(Call<Attraction> call, Response<Attraction> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    Log.d(TAG, "Got attraction!");
                    mAttraction = response.body();
                    enableHistoryButtons();
                    setAttractionDetails();
                } else {
                    if (response.code() == 404) {
                        // Not Found
                        // Add new attraction
                        mAttraction = new Attraction();
                        mAttraction.setName(mName);
                        mAttraction.setGooglePlaceId(mPlaceId);

                        setAttractionDetails();
                    } else {
                        try {
                            String msg = "errorBody: " + response.errorBody().string() + "\n" +
                                    "message: " + response.message() + "\n" +
                                    "body: " + response.body() + "\n" +
                                    "raw: " + response.raw() + "\n" +
                                    "code: " + response.code() + "\n" +
                                    "headers: " + response.headers();
                            Log.d(TAG, msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Attraction> call, Throwable t) {
                Log.d(TAG, "[getAttractionDetails:onFailure]" + t.getMessage());
                hideProgressBar();
            }
        });
    }

    /**
     * Call PhotoKingAPI to create new ping data
     */
    private void createPing() {
        showProgressBar();

        // New ping
        Ping ping = new Ping();
        ping.setResidentId(mSessionManager.getResident().getId());
        ping.setAttractionName(mName);
        ping.setLat(mLatLng.latitude);
        ping.setLng(mLatLng.longitude);
        ping.setPlaceId(mPlaceId);

        Call<Ping> call = service.createPing(ping);
        call.enqueue(new Callback<Ping>() {
            @Override
            public void onResponse(Call<Ping> call, Response<Ping> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    showPings();
                }else {
                    try {
                        Log.d(TAG, "[createPing:onResponse] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ping> call, Throwable t) {
                Log.d(TAG, "[createPing:onFailure] " + t.getMessage());
                hideProgressBar();
            }
        });
    }

    /**
     * Open PingsFragment
     */
    private void showPings() {
        int residentId = mSessionManager.getResident().getId();
        // Move to PingsFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, PingsFragment.newInstance(residentId))
                .addToBackStack(null)
                .commit();
    }

    private void enableHistoryButtons(){
        if(mAttraction != null){
            mHistoryTextView.setVisibility(VISIBLE);

            mWinningPhotosButton.setVisibility(VISIBLE);
            final AttractionWinningPhotoHistoryFragment winningPhotosFragment = AttractionWinningPhotoHistoryFragment.newInstance(mAttraction.getId(), mAttraction.getName());
            mWinningPhotosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, winningPhotosFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            mPhotowarButton.setVisibility(VISIBLE);
            final AttractionPhotowarHistoryFragment photowarHistoryFragment = AttractionPhotowarHistoryFragment.newInstance(mAttraction.getId(), mAttraction.getName());
            mPhotowarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photowarHistoryFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            Log.d(TAG, "Attraction is null!");
        }
    }

    /**
     * Set Ping button
     */
    private void enablePingButton() {
        mPingButton.setVisibility(VISIBLE);
        mPingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPing();
            }
        });
    }

    private void setAttractionDetails() {
        if (mAttraction != null) {
            mTextViewName.setText(mAttraction.getName());
            mTextViewWinner.setText(mAttraction.getOwnerName());
            loadImage(mImageViewAttraction, mAttraction.getPhotoImagePath());
        }
    }

    private void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadImage(ImageView imageView, final String imagePath){
        if (imagePath != null) {
            String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
            Picasso.with(getContext()).load(imageUrl)
                    .error(R.drawable.noimage)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Succeeded photo of " + imagePath);
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Failed photo of " + imagePath);
                        }
                    });
        }
    }
}