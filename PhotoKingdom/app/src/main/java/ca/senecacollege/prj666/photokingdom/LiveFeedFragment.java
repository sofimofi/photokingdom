package ca.senecacollege.prj666.photokingdom;

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

import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.adapters.LiveFeedsAdapter;
import ca.senecacollege.prj666.photokingdom.models.LiveFeed;


/**
 *
 */
public class LiveFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "LiveFeedFragment";

    // RecyclerView
    private RecyclerView mRecyclerView;
    private LiveFeedsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    private OnFragmentInteractionListener mListener;

    public LiveFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveFeedFragment newInstance(String param1, String param2) {
        LiveFeedFragment fragment = new LiveFeedFragment();
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
        actionBar.setTitle(R.string.livefeed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_live_feed, container, false);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set data
        // TODO: Change to real data from PhotoKingdomAPI
        List<LiveFeed> feeds = new ArrayList<LiveFeed>();
        for (int i = 0; i < 20; i++) {
            LiveFeed feed = new LiveFeed();
            feed.setDate("3/" + (i + 1));
            if (i % 3 == 0) {
                feed.setMsg("Photowar " + i);
                feed.setImgRes1(R.mipmap.ic_launcher_round);
                feed.setImgRes2(R.mipmap.ic_launcher);
            } else {
                feed.setMsg("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ac enim vel enim malesuada gravida.");
            }
            feed.setName1("Name1 - " + i);
            feed.setName2("Name2 - " + i);

            feeds.add(feed);
        }

        // Set RecyclerView adapter
        mAdapter = new LiveFeedsAdapter(feeds);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
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
            Log.d(TAG,"Live feed fragment created");
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
}
