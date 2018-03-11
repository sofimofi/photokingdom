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
import ca.senecacollege.prj666.photokingdom.adapters.PhotowarQueueAdapter;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueueItem;

/**
 * Fragment for photowar queue
 *
 * @author Wonho
 */
public class PhotowarQueueFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PhotowarQueueAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PhotowarQueueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photowar_queue, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.photowar_queue);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set data
        // TODO: Change to real data from PhotoKingdomAPI
        List<PhotowarQueueItem> photowarQueueItems = new ArrayList<PhotowarQueueItem>();
        for (int i = 0; i < 20; i++) {
            PhotowarQueueItem photowarQueueItem = new PhotowarQueueItem();
            photowarQueueItem.setDate("3/" + (i + 1));
            photowarQueueItem.setImgResId(R.mipmap.ic_launcher);
            photowarQueueItem.setName("Name " + i);

            photowarQueueItems.add(photowarQueueItem);
        }

        // Set RecyclerView adapter
        mAdapter = new PhotowarQueueAdapter(photowarQueueItems);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
