package ca.senecacollege.prj666.photokingdom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotoDetails;
import ca.senecacollege.prj666.photokingdom.models.PhotoWithDetails;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;
import ca.senecacollege.prj666.photokingdom.utils.LoadImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final String PHOTO_ID = "photoUploadId";

    private int mPhotoId;
    private PhotoWithDetails mPhoto;
    private AttractionPhotowarUploadForPhotoDetails mMostRecentAttractionPhotowar;

    // View elements
    private TextView mPhotoAttractionName;
    private ImageView mPhotoImageview;
    private ImageView mResidentAvatar;
    private TextView mResidentName;
    private TextView mPtsTextView;
    private TextView mPhotoPoints;
    private TextView mPhotoInfoTextView;
    private ImageView mWinningTrophy;
    private Button mViewPhotowarButton;

//    private OnFragmentInteractionListener mListener;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photoId
     * @return A new instance of fragment PhotoFragment.
     */
    public static PhotoFragment newInstance(int photoId) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(PHOTO_ID, photoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotoId = getArguments().getInt(PHOTO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.photo);

        // Reference to view elements
        mPhotoAttractionName = view.findViewById(R.id.photoAttractionNameTextView);
        mPhotoImageview = view.findViewById(R.id.photoImageView);
        mResidentAvatar = view.findViewById(R.id.residentAvatarImageView);
        mResidentName = view.findViewById(R.id.residentName);
        mPtsTextView = view.findViewById(R.id.pts);
        mPhotoPoints = view.findViewById(R.id.pointsTextView);
        mPhotoInfoTextView = view.findViewById(R.id.photoInfoTextView);
        mWinningTrophy = view.findViewById(R.id.winningTrophy);
        mViewPhotowarButton = view.findViewById(R.id.viewPhotowarButton);

        // get photo from database
        getPhoto();
        return view;
    }

    private void getPhoto(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<PhotoWithDetails> call = service.getPhoto(mPhotoId);
        call.enqueue(new Callback<PhotoWithDetails>(){
            @Override
            public void onResponse(Call<PhotoWithDetails> call, Response<PhotoWithDetails> response) {
                if(response.isSuccessful()){
                    mPhoto = response.body();
                    Log.d(TAG, "Photo came back: " + mPhoto.getId() + ", # of PhotowarUploads" +
                            mPhoto.getAttractionPhotowarUploads().size());

                    setPhotoData();

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
            public void onFailure(Call<PhotoWithDetails> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void setPhotoData(){
        if(mPhoto != null){
            // Set Photo
            LoadImage.loadImage(getContext(), mPhotoImageview, mPhoto.getPhotoFilePath());
            // Set Avatar
            LoadImage.loadImage(getContext(), mResidentAvatar, mPhoto.getResidentAvatarImagePath());
            // Set Resident Name
            mResidentName.setText(mPhoto.getResidentUserName());

            // Make Resident clickable
            mResidentAvatar.setTag(mPhoto.getResidentId());
            mResidentName.setTag(mPhoto.getResidentId());
            mResidentAvatar.setOnClickListener(onResidentClickListener);
            mResidentName.setOnClickListener(onResidentClickListener);

            // find out photowar status of Photo
            List<AttractionPhotowarUploadForPhotoDetails> uploadList = mPhoto.getAttractionPhotowarUploads();
            // sort by descending ID
            Collections.sort(uploadList);
            // get the most recent attractionPhotowar;
            if(uploadList.size() > 0){
                mMostRecentAttractionPhotowar = uploadList.get(0);
            } else {
                mMostRecentAttractionPhotowar = null;
            }

            if(mMostRecentAttractionPhotowar != null){
                // Set Photo Name to most recent Attraction Name
                mPhotoAttractionName.setText(mMostRecentAttractionPhotowar.getAttractionPhotowarAttractionName());

                String endDate = mMostRecentAttractionPhotowar.getAttractionPhotowar().getEndDate();

                if(!DateUtil.isBeforeNow(endDate)){
                    // AttractionPhotowar is still going on
                    mPhotoInfoTextView.setText(R.string.photo_currently_competing);
                    mPtsTextView.setText(R.string.current_photowar_points);
                    mPhotoPoints.setText(String.valueOf(mMostRecentAttractionPhotowar.getResidentVotesCount()));

                    // Activate "View Photowar" Button
                    final PhotowarFragment photowarFragment = PhotowarFragment.newInstance(mMostRecentAttractionPhotowar.getAttractionPhotowarId());
                    mViewPhotowarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frameLayout, photowarFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });

                } else {
                    // TODO: Set Photo isWinner or isLoser
                    if(mMostRecentAttractionPhotowar.getIsWinner() == 1){
                        mWinningTrophy.setVisibility(VISIBLE);
                        String endDateString = DateUtil.ISO8601ToLongDateAndTimeString(mMostRecentAttractionPhotowar.getAttractionPhotowar().getEndDate());
                        mPhotoInfoTextView.setText(getString(R.string.photo_won_war_on_date, endDateString));
                    } else {
                        String endDateString = DateUtil.ISO8601ToLongDateAndTimeString(mMostRecentAttractionPhotowar.getAttractionPhotowar().getEndDate());
                        mPhotoInfoTextView.setText(getString(R.string.photo_lost_war_on_date, endDateString));
                    }

                    mPtsTextView.setText(R.string.total_photo_points_to_date);

                    // add up all the points from all photowars
                    int totalPts = 0;
                    for(AttractionPhotowarUploadForPhotoDetails upload : uploadList){
                        totalPts += upload.getResidentVotesCount();
                    }
                    mPhotoPoints.setText(String.valueOf(totalPts));

                    // Disactivate "View Photowar" Button
                    mViewPhotowarButton.setVisibility(View.INVISIBLE);
                }
            } else {
                // this Photo never participated in a Photowar - just set Photo name as Resident name
                mPhotoAttractionName.setText(mPhoto.getResidentUserName());
                mPhotoInfoTextView.setText("This photo has not participated in any photowars.");
                mPhotoPoints.setText("0");
                // Disactivate "View Photowar" Button
                mViewPhotowarButton.setVisibility(View.INVISIBLE);
            }
        }

    }

    private View.OnClickListener onResidentClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            int residentId = (int) view.getTag();
            Log.d(TAG, "Clicked on Resident " + residentId);

            UserFragment userFragment = UserFragment.newInstance(residentId);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, userFragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
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
*/
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
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
