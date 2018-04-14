package ca.senecacollege.prj666.photokingdom.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.PhotoFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.UserFragment;
import ca.senecacollege.prj666.photokingdom.adapters.PhotowarQueueAdapter;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueue;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for photowar queue
 *
 * @author Wonho
 */
public class PhotowarQueueFragment extends Fragment {
    private static final String TAG = "PhotowarQueueFragment";

    // Argument key
    private static final String ATTRACTION_ID = "attractionId";
    private static final String ATTRACTION_NAME = "attractionName";

    // Argument value
    private int mAttractionId;
    private String mAttractionName;
    private List<PhotowarQueue> mPhotowarQueueds;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private PhotowarQueueAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar mProgressBar;

    public PhotowarQueueFragment() {
    }

    public static PhotowarQueueFragment newInstance(int attractionId, String attractionName) {
        // Create an instance
        PhotowarQueueFragment fragment = new PhotowarQueueFragment();

        // Set arguments
        Bundle args = new Bundle();
        args.putInt(ATTRACTION_ID, attractionId);
        args.putString(ATTRACTION_NAME, attractionName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAttractionId = getArguments().getInt(ATTRACTION_ID);
            mAttractionName = getArguments().getString(ATTRACTION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photowar_queue, container, false);

        // Set the title
        String title = getString(R.string.photowar_queue);
        if (mAttractionName != null && !mAttractionName.isEmpty()) {
            title += " of " + mAttractionName;
        }
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(title);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Get Photowar queue
        getPhotowarQueues();

        return rootView;
    }

    /**
     * Call PhotoKingdomAPI to get Photowar queues data for an attraction
     */
    private void getPhotowarQueues() {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<PhotowarQueue>> call = service.getQueueForAttraction(mAttractionId);
        call.enqueue(new Callback<List<PhotowarQueue>>() {
            @Override
            public void onResponse(Call<List<PhotowarQueue>> call, Response<List<PhotowarQueue>> response) {
                if (response.isSuccessful()) {
                    mPhotowarQueueds = response.body();
                    setPhotowarQueuesData();
                } else {
                    try {
                        Log.d(TAG, "[getPhotoQueue:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhotowarQueue>> call, Throwable t) {
                Log.e(TAG, "[checkHasQueue:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Set Photowar queue data to UI (RecyclerView)
     */
    private void setPhotowarQueuesData() {
        if (mPhotowarQueueds != null) {
            // Set pings data to RecyclerView
            mAdapter = new PhotowarQueueAdapter(getContext(), mPhotowarQueueds);

            // Photo and resident name click
            mAdapter.setOnItemClickListener(new PhotowarQueueAdapter.OnItemClickListener() {
                @Override
                public void onPhotoItemClick(View view, int photoId) {
                    // Move to PhotoFragment
                    PhotoFragment fragment = PhotoFragment.newInstance(photoId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .addToBackStack(null)
                            .commit();
                }

                @Override
                public void onResidentItemClick(View view, int residentId) {
                    // Move to UserFragment
                    UserFragment userFragment = UserFragment.newInstance(residentId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, userFragment )
                            .addToBackStack(null)
                            .commit();
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
