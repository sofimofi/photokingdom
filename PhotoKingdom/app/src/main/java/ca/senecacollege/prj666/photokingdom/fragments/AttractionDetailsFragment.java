package ca.senecacollege.prj666.photokingdom.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.senecacollege.prj666.photokingdom.R;

/**
 * Fragment for attraction details
 *
 * @author Wonho
 */
public class AttractionDetailsFragment extends Fragment {


    public AttractionDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attraction_details, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.attraction);

        return rootView;
    }

}
