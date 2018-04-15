package ca.senecacollege.prj666.photokingdom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.PhotowarFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.AttractionPhotowarHistoryAdapter;
import ca.senecacollege.prj666.photokingdom.adapters.PingsAdapter;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueue;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;


/**
 * Fragment for Attraction Photowar History
 *
 * @author sofia
 */
public class AttractionPhotowarHistoryFragment extends Fragment {
    private static final String TAG = "PhotowarHistoryFragment";
    private static final String ATTRACTION_ID = "attractionId";
    private static final String ATTRACTION_NAME = "attractionName";

    private int mAttractionId;
    private String mAttractionName;
    private List<AttractionPhotowarWithDetails> mAttractionPhotowarList;

    private TextView banner;
    private Button mButtonQueue;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private AttractionPhotowarHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private OnFragmentInteractionListener mListener;

    public AttractionPhotowarHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param attractionId
     * @param attractionName Parameter 2.
     * @return A new instance of fragment AttractionPhotowarHistoryFragment.
     */
    public static AttractionPhotowarHistoryFragment newInstance(int attractionId, String attractionName) {
        AttractionPhotowarHistoryFragment fragment = new AttractionPhotowarHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ATTRACTION_ID, attractionId);
        args.putString(ATTRACTION_NAME, attractionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.fragment_attraction_photowar_history, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.photowar_history);

        banner = (TextView) view.findViewById(R.id.photoWarHistoryBannerTextView);
        mButtonQueue = view.findViewById(R.id.buttonQueue);

        // RecyclerView
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getPhotowars();

        return view;
    }

    public void getPhotowars(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<AttractionPhotowarWithDetails>> call = service.getAttractionPhotowarHistory(mAttractionId);
        call.enqueue(new Callback<List<AttractionPhotowarWithDetails>>() {
            @Override
            public void onResponse(Call<List<AttractionPhotowarWithDetails>> call, Response<List<AttractionPhotowarWithDetails>> response) {
                if(response.isSuccessful()){
                    mAttractionPhotowarList = response.body();

                    Log.d(TAG, "Photowar History List came back: " + mAttractionPhotowarList);
                    Log.d(TAG, "PhotowarHistory List count: " + mAttractionPhotowarList.size());
                    setPhotowarHistoryData();
                    checkHasQueue();
                } else {
                    try {
                        Log.d(TAG, response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "API call is unsuccessful!");
                }
            }

            @Override
            public void onFailure(Call<List<AttractionPhotowarWithDetails>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void setPhotowarHistoryData(){
        if (mAttractionPhotowarList != null) {
            banner.setText(getString(R.string.photowar_history_for_attraction, mAttractionName));

            // Set data to RecyclerView
            mAdapter = new AttractionPhotowarHistoryAdapter(getContext(), mAttractionPhotowarList);
            mAdapter.setOnItemClickListener(new AttractionPhotowarHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int photowarId = mAttractionPhotowarList.get(position).getId();
                    Log.d(TAG, "Clicked on photowar " + photowarId);
                    // Move to AttractionDetailsFragment
                    PhotowarFragment photowarFragment = PhotowarFragment.newInstance(photowarId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photowarFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void checkHasQueue() {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<PhotowarQueue>> call = service.getQueueForAttraction(mAttractionId);
        call.enqueue(new Callback<List<PhotowarQueue>>() {
            @Override
            public void onResponse(Call<List<PhotowarQueue>> call, Response<List<PhotowarQueue>> response) {
                if (response.isSuccessful()) {
                    List<PhotowarQueue> photowarQueues = response.body();
                    if (photowarQueues.size() > 0) {
                        enableQueueButton();
                    }
                } else {
                    try {
                        Log.d(TAG, "[checkHasQueue:onResponse] " + response.errorBody().toString());
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

    private void enableQueueButton() {
        if (mButtonQueue != null) {
            mButtonQueue.setVisibility(VISIBLE);
            mButtonQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Move to PhotowarQueueFragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, PhotowarQueueFragment.newInstance(mAttractionId, mAttractionName))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
