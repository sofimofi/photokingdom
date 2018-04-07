package ca.senecacollege.prj666.photokingdom.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.senecacollege.prj666.photokingdom.PhotowarFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;

/**
 * Fragment for attraction details
 *
 * @author Wonho
 */
public class AttractionDetailsFragment extends Fragment {
    // Argument keys
    private static final String ATTRACTION_NAME = "attractionName";
    private static final String IS_PINGED = "isPinged";

    private String mName;
    private boolean mIsPinged;
    private ResidentSessionManager mSessionManager;

    public static AttractionDetailsFragment newInstance(String name, boolean isPinged) {
        // Create an instance
        AttractionDetailsFragment fragment = new AttractionDetailsFragment();

        // Set arguments
        Bundle args = new Bundle();
        args.putString(ATTRACTION_NAME, name);
        args.putBoolean(IS_PINGED, isPinged);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mName = getArguments().getString(ATTRACTION_NAME);
            mIsPinged = getArguments().getBoolean(IS_PINGED);
        }

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

        TextView textView = (TextView)rootView.findViewById(R.id.textViewName);
        textView.setText(mName);

        // Buttons are visible if the user logged-in
        if (mSessionManager.isLoggedIn()) {
            if (mIsPinged == false) {
                // Ping button is visible if this fragment opened from map (not ping list)
                Button buttonPing = (Button)rootView.findViewById(R.id.buttonPing);
                buttonPing.setVisibility(View.VISIBLE);
            }

            Button buttonUpload = (Button)rootView.findViewById(R.id.buttonUpload);
            buttonUpload.setVisibility(View.VISIBLE);
        }

        // TODO: test photowars button
        Button photowarButton = (Button) rootView.findViewById(R.id.buttonPhotowars);
        // TODO: Don't hardcode the attractionPhotowarId
        final PhotowarFragment photowarFragment = PhotowarFragment.newInstance(20);
        final AttractionPhotowarHistoryFragment photowarHistoryFragment = AttractionPhotowarHistoryFragment.newInstance(57, "CN Tower");
        photowarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, photowarHistoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}