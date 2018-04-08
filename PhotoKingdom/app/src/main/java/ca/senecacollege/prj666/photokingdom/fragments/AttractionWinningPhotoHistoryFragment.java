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
import android.widget.TextView;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.PhotoFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.AttractionWinningPhotoHistoryAdapter;
import ca.senecacollege.prj666.photokingdom.models.PhotoWinning;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttractionWinningPhotoHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttractionWinningPhotoHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttractionWinningPhotoHistoryFragment extends Fragment {
    private static final String TAG = "winningPhotoHistory";
    private static final String ATTRACTION_ID = "attractionId";
    private static final String ATTRACTION_NAME = "attractionName";

    private int mAttractionId;
    private String mAttractionName;
    private List<PhotoWinning> mWinningPhotos;

    private TextView banner;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private AttractionWinningPhotoHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

//    private OnFragmentInteractionListener mListener;

    public AttractionWinningPhotoHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param attractionId
     * @param attractionName
     * @return A new instance of fragment AttractionWinningPhotoHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttractionWinningPhotoHistoryFragment newInstance(int attractionId, String attractionName) {
        AttractionWinningPhotoHistoryFragment fragment = new AttractionWinningPhotoHistoryFragment();
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
        View view =  inflater.inflate(R.layout.fragment_attraction_winning_photo_history, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.winningphoto_history);

        banner = (TextView) view.findViewById(R.id.winningPhotoHistoryBannerTextView);
        banner.setText(getString(R.string.attraction_winningphotos, mAttractionName));

        // RecyclerView
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getWinningPhotos();

        return view;
    }

    private void getWinningPhotos(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<PhotoWinning>> call = service.getAttractionWinningPhotos(mAttractionId);
        call.enqueue(new Callback<List<PhotoWinning>>() {
            @Override
            public void onResponse(Call<List<PhotoWinning>> call, Response<List<PhotoWinning>> response) {
                if(response.isSuccessful()){
                    mWinningPhotos = response.body();

                    Log.d(TAG, "Photowar History List came back: " + mWinningPhotos);
                    setWinningPhotosData();
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
            public void onFailure(Call<List<PhotoWinning>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void setWinningPhotosData(){
        if(mWinningPhotos != null){
            // Set data to RecyclerView
            mAdapter = new AttractionWinningPhotoHistoryAdapter(getContext(), mWinningPhotos);
            mAdapter.setOnItemClickListener(new AttractionWinningPhotoHistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int photoId = mWinningPhotos.get(position).getId();
                    Log.d(TAG, "Clicked on photo " + photoId);
                    // Move to PhotoFragment
                    PhotoFragment photoFragment = PhotoFragment.newInstance(photoId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photoFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            mRecyclerView.setAdapter(mAdapter);
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