package ca.senecacollege.prj666.photokingdom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import ca.senecacollege.prj666.photokingdom.fragments.PhotoAlbumFragment;
import ca.senecacollege.prj666.photokingdom.fragments.PingsFragment;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;
import ca.senecacollege.prj666.photokingdom.utils.UploadManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Fragment for User
 *
 * @author zhihao
 */
public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    private OnFragmentInteractionListener mListener;

    // Resident
    private static final String ARG_RESIDENT_ID = "residentId";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int ACTION_PICK_REQUEST = 1;


    private int mResidentId;
    private Resident mResident;
    private Uri mAvatarUri;
    private PhotoKingdomService service;

    // Session manager for current resident
    private ResidentSessionManager mSessionManager;

    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private TextView mTextViewGender;
    private TextView mTextViewCity;
    private TextView mTextViewTitle;
    private TextView mTextViewPoint;
    private ImageView mImageViewAvatar;
    private Button mButtonPingList;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * We can use this method when other resident's profile is displayed
     * instead of current logged-in resident's profile
     * e.g.) UserFragment fragment = UserFragment.newInstance(1);
     *
     * @param residentId
     * @return A new instance of fragment UserFragment.
     */
    public static UserFragment newInstance(int residentId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RESIDENT_ID, residentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Id for other resident
            mResidentId = getArguments().getInt(ARG_RESIDENT_ID);
        } else {
            // Session manager for current resident
            mSessionManager = new ResidentSessionManager(getContext());
        }

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.userprofile);
    }

    /**
     * call api/resident/{id}
     * @param residentId
     */
    private void getResident(int residentId){
        service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<Resident> call = service.getResident(residentId);

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

                    Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                Log.d(TAG, "[getResident:onFailure] " + t.getMessage());
            }
        });
    }

    private void setUserProfile() {
        mTextViewName.setText(mResident.getUserName());
        mTextViewEmail.setText(mResident.getEmail());
        if(mResident.getGender().equals("M")) {
            mTextViewGender.setText("Male");
        }else {
            mTextViewGender.setText("Female");
        }
        mTextViewCity.setText(mResident.getCity());
        mTextViewTitle.setText(mResident.getTitle());
        mTextViewPoint.setText(String.format(Locale.CANADA, "%d", mResident.getTotalPoints()));

        if (mResident.getAvatarImagePath() != null) {
            loadImage(mResident.getAvatarImagePath());
        }
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
        mButtonPingList = (Button)rootView.findViewById(R.id.buttonPingList);

        // load user
        if (mSessionManager != null && mSessionManager.isLoggedIn()) {
            // Current resident
            getResident(mSessionManager.getResident().getId());
        } else {
            // Other resident
            getResident(mResidentId);
            mButtonPingList.setVisibility(View.GONE);
        }

        // Button click
        mButtonPingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResidentSessionManager manager = new ResidentSessionManager(getContext());
                int residentId = manager.getResident().getId();
                // Move to PingsFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, PingsFragment.newInstance(residentId))
                        .addToBackStack(null)
                        .commit();
            }
        });

        mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Update your avatar?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                requestImagePermission();
                                updateAvatar();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            }
        });

        Button buttonphotoAlbum = rootView.findViewById(R.id.buttonPhotoAlbum);
        buttonphotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, PhotoAlbumFragment.newInstance(mResident.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    private void requestImagePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateAvatar();
            }
        }
    }

    private void updateAvatar(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                mAvatarUri = data.getData();
                uploadAvatar();
            }
        }
    }

    private void uploadAvatar(){
        if (mAvatarUri != null) {
            UploadManager manager = new UploadManager(getActivity());
            manager.setOnUploadListener(new UploadManager.OnUploadListener() {
                @Override
                public void onUploaded(String path) {
                    // TODO: remove current avatar file on server, saving new path to db

                    if(mResident.getAvatarImagePath() != null) {
                        String curAvatarPath = mResident.getAvatarImagePath();
                        Log.i("Current Avatar Path ", curAvatarPath);

                        String curAvatarUrl = RetrofitServiceGenerator.getBaseUrl() + curAvatarPath;
                        Log.i("Current Avatar Url ", curAvatarUrl);

                    }
                    // store in shared preference
                    mResident.setAvatarImagePath(path);
                    mSessionManager.setResident(mResident);
                    loadImage(path);
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(getContext(), R.string.error_avatar_upload, Toast.LENGTH_LONG).show();
                }
            });

            manager.uploadImage(mAvatarUri);
        } else {
            Toast.makeText(getContext(), R.string.error_avatar_upload, Toast.LENGTH_LONG).show();
        }
    }

    private void loadImage(String imagePath){
        String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
        Picasso.with(getContext()).load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(mImageViewAvatar, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //Toast.makeText(getContext(), R.string.error_avatar_upload, Toast.LENGTH_SHORT).show();
                    }
                });
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
