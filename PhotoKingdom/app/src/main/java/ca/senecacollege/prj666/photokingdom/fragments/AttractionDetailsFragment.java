package ca.senecacollege.prj666.photokingdom.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.Attraction;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

/**
 * Fragment for attraction details
 *
 * @author Wonho, Sofia
 */
public class AttractionDetailsFragment extends Fragment {
    private static final String TAG = "AttractionDetailsFrag";

    // Argument keys
    private static final String ATTRACTION_NAME = "name";
    private static final String ATTRACTION_LAT = "lat";
    private static final String ATTRACTION_LNG = "lng";
    private static final String PLACE_ID = "placeId";
    private static final String IS_EXISTED = "isExisted";
    private static final String IS_PINGED = "isPinged";
    private static final String HAS_WAR = "isPinged";
    private static final String IS_LIT_UP = "isLitUp";

    // Argument values
    private String mName;
    private LatLng mLatLng;
    private String mPlaceId;
    private boolean mIsExisted;
    private boolean mIsPinged;
    private boolean mHasWar;
    private boolean mIsLitUp;

    // Resident session
    private ResidentSessionManager mSessionManager;

    // Widgets
    private ProgressBar mProgressBar;
    private TextView mTextViewName;
    private TextView mTextViewWinner;
    private ImageView mImageViewAttraction;
    private TextView mHistoryTextView;
    private Button mWinningPhotosButton;
    private Button mPhotowarButton;
    private Button mPingButton;

    private Attraction mAttraction;

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
            mHasWar = getArguments().getBoolean(HAS_WAR);
            mIsLitUp = getArguments().getBoolean(IS_LIT_UP);

            mLatLng = new LatLng(
                    getArguments().getDouble(ATTRACTION_LAT),
                    getArguments().getDouble(ATTRACTION_LNG)
            );
        }

        Log.d(TAG, "Opening up Place Id " + mPlaceId);
        mSessionManager = new ResidentSessionManager(getContext());
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
                Button buttonUpload = (Button)rootView.findViewById(R.id.buttonUpload);
                buttonUpload.setVisibility(VISIBLE);
            } else {
                // From map
                if (mIsLitUp == true) {
                    Button buttonUpload = (Button)rootView.findViewById(R.id.buttonUpload);
                    buttonUpload.setVisibility(VISIBLE);

                    enablePingButton();
                }
            }
        }

        if (!mPlaceId.isEmpty()) {
            getAttractionDetails(mPlaceId);
        }

        return rootView;
    }

    /**
     * Call PhotoKingAPI to get attraction details data
     * @param placeId
     */
    private void getAttractionDetails(String placeId) {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);

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

                            //Log.d(TAG, "[getAttractionDetails:onResponse] " + response.errorBody().string());
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
        ping.setPlaceId(mPlaceId);

        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
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