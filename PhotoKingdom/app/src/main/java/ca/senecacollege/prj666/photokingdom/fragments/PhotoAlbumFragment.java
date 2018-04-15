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
import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.PhotoAlbumAdapter;
import ca.senecacollege.prj666.photokingdom.models.Photo;
import ca.senecacollege.prj666.photokingdom.models.PhotoWithDetails;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for Photo Album
 *
 * @author zhihao
 */
public class PhotoAlbumFragment extends Fragment {
    private static final String TAG = "PhotoAlbumFragment";
    private static final String RESIDENT_ID = "residentId";

    private int mResidentId;
    private List<PhotoWithDetails> mPhotoList;
    private ArrayList<Photo> albumList;
    private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;

    public PhotoAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PhotoAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoAlbumFragment newInstance(int residentId) {
        PhotoAlbumFragment fragment = new PhotoAlbumFragment();
        Bundle args = new Bundle();
        args.putInt(RESIDENT_ID, residentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.photo_album);
        if (getArguments() != null) {
            mResidentId = getArguments().getInt(RESIDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo_album, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photoalbum);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);

        getPhotos(mResidentId);
        return rootView;
    }

    private void getPhotos(int mResidentId) {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<PhotoWithDetails>> call = service.getPhotosByResidentId(mResidentId);
        call.enqueue(new Callback<List<PhotoWithDetails>>() {
            @Override
            public void onResponse(Call<List<PhotoWithDetails>> call, Response<List<PhotoWithDetails>> response) {
                if(response.isSuccessful()){
                    mPhotoList = response.body();
                    albumList = prepareData();

                    PhotoAlbumAdapter adapter = new PhotoAlbumAdapter(getContext(), albumList);
                    mRecyclerView.setAdapter(adapter);

                }else{
                    Log.d(TAG, "API - get photo by resident id failed!");
                }
            }

            @Override
            public void onFailure(Call<List<PhotoWithDetails>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private ArrayList<Photo> prepareData() {
        ArrayList<Photo> list = new ArrayList<>();
        for(int i = 0; i< mPhotoList.size(); i++){
            Photo p = new Photo();
            p.setId(mPhotoList.get(i).getId());
            p.setPhotoFilePath(mPhotoList.get(i).getPhotoFilePath());
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
