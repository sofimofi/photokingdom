package ca.senecacollege.prj666.photokingdom.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.PingsAdapter;
import ca.senecacollege.prj666.photokingdom.models.Ping;

/**
 * Fragment for ping list
 *
 * @author Wonho
 */
public class PingsFragment extends Fragment {

    // RecyclerView
    private RecyclerView mRecyclerView;
    private PingsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pings, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.ping_list);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set data
        // TODO: Change to real data from PhotoKingdomAPI
        List<Ping> pings = new ArrayList<Ping>();
        for (int i = 0; i < 20; i++) {
            Ping ping = new Ping();
            ping.setPingDate("3/" + (i + 1));
            ping.setExpiryDate("3/" + (i + 10));
            ping.setAttractionName("Attraction " + i);
            pings.add(ping);
        }

        // Set RecyclerView adapter
        mAdapter = new PingsAdapter(pings);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
