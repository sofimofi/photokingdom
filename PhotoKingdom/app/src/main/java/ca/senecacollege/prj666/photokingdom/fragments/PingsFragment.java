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

import java.io.IOException;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.PingsAdapter;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for ping list
 *
 * @author Wonho
 */
public class PingsFragment extends Fragment {
    private static final String TAG = "PingsFragment";

    // Argument key
    private static final String RESIDENT_ID = "residentId";

    // Resident id for pings
    private int mResidentId;

    // Ping list data
    private List<Ping> mPings;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private PingsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar mProgressBar;

    public static PingsFragment newInstance(int id) {
        // Create an instance
        PingsFragment fragment = new PingsFragment();

        // Set arguments
        Bundle args = new Bundle();
        args.putInt(RESIDENT_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mResidentId = getArguments().getInt(RESIDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pings, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.ping_list);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Get resident's pings
        if (mResidentId > 0) {
            getPings(mResidentId);
        }

        return rootView;
    }

    private void getPings(final int residentId) {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);

        showProgressBar();

        Call<List<Ping>> call = service.getPings(residentId);
        call.enqueue(new Callback<List<Ping>>() {
            @Override
            public void onResponse(Call<List<Ping>> call, Response<List<Ping>> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    mPings = response.body();
                    setPingsData();
                } else {
                    try {
                        Log.d(TAG, "[getPings:onResponse] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Ping>> call, Throwable t) {
                Log.d(TAG, "[getPings:onFailure]" + t.getMessage());
                hideProgressBar();
            }
        });
    }

    private void setPingsData() {
        if (mPings != null) {
            // Set pings data to RecyclerView
            mAdapter = new PingsAdapter(mPings);
            mAdapter.setOnItemClickListener(new PingsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String attractionName = mPings.get(position).getAttraction().getName();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", attractionName);
                    bundle.putBoolean("isPinged", true);
                    // Move to AttractionDetailsFragment
                    AttractionDetailsFragment fragment = AttractionDetailsFragment.newInstance(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment)
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