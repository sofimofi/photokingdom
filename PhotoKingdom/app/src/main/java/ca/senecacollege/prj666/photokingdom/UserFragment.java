package ca.senecacollege.prj666.photokingdom;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "UserFragment";

    private OnFragmentInteractionListener mListener;

    private Resident mResident;

    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private TextView mTextViewGender;
    private TextView mTextViewCity;
    private TextView mTextViewTitle;
    private TextView mTextViewPoint;
    private ImageView mImageViewAvatar;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        actionBar.setTitle(R.string.userprofile);
    }

    /**
     * call api/resident/{id}
     * @param residentId
     */
    private void getResident(int residentId){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<Resident> call = service.getResident(residentId);

        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Resident>() {
            @Override
            public void onResponse(Call<Resident> call, Response<Resident> response) {

                if (response.isSuccessful() && response.body() != null) {
                    mResident = response.body();
                    setUserProfile();
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "[getResident:onFailure] " + t.getMessage());
            }
        });
    }

    private void setUserProfile() {
        mTextViewName.setText(mResident.getUserName());
        mTextViewEmail.setText(mResident.getEmail());
        if(mResident.getGender().equals("M"))
            mTextViewGender.setText("Male");
        else
            mTextViewGender.setText("Female");
        mTextViewCity.setText(mResident.getCity());
        mTextViewTitle.setText(mResident.getTitle());
        mTextViewPoint.setText("0");

        String imgPath = mResident.getAvatarImagePath();
        String imgUrl = RetrofitServiceGenerator.getBaseUrl() + "/" + imgPath;
        Picasso.with(getContext()).load(imgUrl)
                .error(R.mipmap.ic_launcher)
                .into(mImageViewAvatar, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Error loading avatar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // references to widgets
        mTextViewName = rootView.findViewById(R.id.name_user);
        mTextViewEmail = rootView.findViewById(R.id.email_user);
        mTextViewGender = rootView.findViewById(R.id.gender_user);
        mTextViewCity = rootView.findViewById(R.id.city_user);
        mTextViewTitle = rootView.findViewById(R.id.title_user);
        mTextViewPoint = rootView.findViewById(R.id.point_user);
        mImageViewAvatar = rootView.findViewById(R.id.avatar_user);

        mProgressBar = rootView.findViewById(R.id.progressBar_user);
        mLinearLayout = rootView.findViewById(R.id.container_user);
        mLinearLayout.setVisibility(View.INVISIBLE);

        // load user
        getResident(3);

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
            Log.d(TAG,"User fragment created");
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
