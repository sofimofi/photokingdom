package ca.senecacollege.prj666.photokingdom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.PhotoAlbumAdapter;
import ca.senecacollege.prj666.photokingdom.models.Photo;

/**
 * Fragment for Photo Album
 *
 * @author zhihao
 */
public class PhotoAlbumFragment extends Fragment {
    private static final String TAG = "PhotoAlbumFragment";

    private final String image_titles[] = {
            "CN Tower",
            "Norvan Fall",
            "Notre-Dame",
            "Galiano Island",
            "CN Tower",
            "Norvan Fall",
            "Notre-Dame",
            "Galiano Island",
            "CN Tower",
            "Norvan Fall",
            "Notre-Dame",
            "Galiano Island"
    };

    private final Integer image_ids[] = {
            R.drawable.cn_tower,
            R.drawable.norvan_fall,
            R.drawable.notre_dame,
            R.drawable.galiano_island,
            R.drawable.cn_tower,
            R.drawable.norvan_fall,
            R.drawable.notre_dame,
            R.drawable.galiano_island,
            R.drawable.cn_tower,
            R.drawable.norvan_fall,
            R.drawable.notre_dame,
            R.drawable.galiano_island
    };

    private OnFragmentInteractionListener mListener;

    public PhotoAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoAlbumFragment newInstance(String param1, String param2) {
        PhotoAlbumFragment fragment = new PhotoAlbumFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.photo_album);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo_album, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photoalbum);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Photo> albumList = prepareData();
        PhotoAlbumAdapter adapter = new PhotoAlbumAdapter(getContext(), albumList);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<Photo> prepareData() {
        ArrayList<Photo> list = new ArrayList<>();
        for(int i = 0; i< image_ids.length; i++){
            Photo p = new Photo();
            p.setImage_title(image_titles[i]);
            p.setId(image_ids[i]);
            list.add(p);
        }
        return list;
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
            Log.d(TAG,"User fragment created");
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
