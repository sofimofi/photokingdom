package ca.senecacollege.prj666.photokingdom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.senecacollege.prj666.photokingdom.fragments.PhotowarQueueFragment;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotowarView;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.PhotowarQueue;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;
import ca.senecacollege.prj666.photokingdom.utils.ResidentSessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * Fragment for Photowar
 *
 * @author sofia
 */
public class PhotowarFragment extends Fragment {
    private static final String TAG = "PhotowarFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ATTRACTION_PHOTOWAR_ID = "attractionPhotowarId";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private ResidentSessionManager mSessionManager;

    private int mAttractionPhotowarId;
    private Integer mResidentId;

    private TextView bannerTextView;
    private ImageView photo1;
    private ImageView photo2;
    private TextView competitor1Name;
    private TextView competitor2Name;
    private ImageView competitor1Avatar;
    private ImageView competitor2Avatar;
    private TextView competitor1Votes;
    private TextView competitor2Votes;
    private ImageButton votePhoto1Button;
    private ImageButton votePhoto2Button;
    private ImageView photo1Trophy;
    private ImageView photo2Trophy;
    private TextView photowarCountdownTextview;
    private Button mButtonQueue;

    private AttractionPhotowarWithDetails mAttractionPhotowar;
    private AttractionPhotowarUploadForPhotowarView upload1;
    private AttractionPhotowarUploadForPhotowarView upload2;
    private boolean photo1Clicked = false;
    private boolean photo2Clicked = false;

    private CountDownTimer countDownTimer;

    public PhotowarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param attractionPhotowarId Id of AttractionPhotowar
     * @return A new instance of fragment PhotowarFragment.
     */
    public static PhotowarFragment newInstance(int attractionPhotowarId) {
        PhotowarFragment fragment = new PhotowarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ATTRACTION_PHOTOWAR_ID, attractionPhotowarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttractionPhotowarId = getArguments().getInt(ARG_ATTRACTION_PHOTOWAR_ID);
        }
        mSessionManager = new ResidentSessionManager(getContext());
    }

    @Override
    public void onPause(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photowar, container, false);

        // Set the title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.attraction_photowar);

        // Get attractionPhotowar from database
        getAttractionPhotowar();

        bannerTextView = view.findViewById(R.id.photowarBannerTextView);
        photowarCountdownTextview = view.findViewById(R.id.photoWarCountdownTextView);
        photo1 = view.findViewById(R.id.competitor1PhotoImageView);
        photo2 = view.findViewById(R.id.competitor2PhotoImageView);
        competitor1Name = view.findViewById(R.id.competitor1NameTextView);
        competitor2Name = view.findViewById(R.id.competitor2NameTextView);
        competitor1Avatar = view.findViewById(R.id.competitor1AvatarImageView);
        competitor2Avatar = view.findViewById(R.id.competitor2AvatarImageView);
        competitor1Votes = view.findViewById(R.id.competitor1PhotoVotes);
        competitor2Votes = view.findViewById(R.id.competitor2PhotoVotes);
        votePhoto1Button = view.findViewById(R.id.votePhoto1Button);
        votePhoto2Button = view.findViewById(R.id.votePhoto2Button);
        photo1Trophy = view.findViewById(R.id.photo1WinningTrophy);
        photo2Trophy = view.findViewById(R.id.photo2WinningTrophy);
        mButtonQueue = view.findViewById(R.id.buttonQueue);

        return view;
    }

    public void getAttractionPhotowar(){
        if(mSessionManager.isLoggedIn()){
            Resident resident = mSessionManager.getResident();
            mResidentId = resident.getId();
        }

        Log.d(TAG, "Making call for AttractionPhotowar with ID " + mAttractionPhotowarId + ", resident ID " + mResidentId);

        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<AttractionPhotowarWithDetails> call = service.getAttractionPhotowar(mAttractionPhotowarId, mResidentId);
        call.enqueue(new Callback<AttractionPhotowarWithDetails>(){
            @Override
            public void onResponse(Call<AttractionPhotowarWithDetails> call, Response<AttractionPhotowarWithDetails> response) {
                if(response.isSuccessful()){
                    mAttractionPhotowar = response.body();
                    Log.d(TAG, "AttractionPhotowar came back: " + mAttractionPhotowar.getId() + "," +
                            mAttractionPhotowar.getAttraction().getName());

                    setAttractionPhotowarData();
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
            public void onFailure(Call<AttractionPhotowarWithDetails> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void setAttractionPhotowarData(){
        if(mAttractionPhotowar != null){
            upload1 = mAttractionPhotowar.getAttractionPhotowarUploads().get(0);
            upload2 = mAttractionPhotowar.getAttractionPhotowarUploads().get(1);

            boolean warOver = warEnded(mAttractionPhotowar.getEndDate());
            if((warOver && mAttractionPhotowar.getExtendedDate() == null) ||
                    (warOver && mAttractionPhotowar.getExtendedDate() != null && warEnded(mAttractionPhotowar.getExtendedDate()))){
                // war is over

                bannerTextView.setText(getString(R.string.photowar_ended, mAttractionPhotowar.getAttraction().getName()));

                String endDateString = DateUtil.ISO8601ToLongDateAndTimeString(mAttractionPhotowar.getEndDate());
                photowarCountdownTextview.setText(getString(R.string.photowar_endDate, endDateString));

                // remove vote buttons
                votePhoto1Button.setVisibility(INVISIBLE);
                votePhoto2Button.setVisibility(INVISIBLE);

                // determine winner
                if(upload1.getIsWinner() == 1){
                    photo1Trophy.setVisibility(VISIBLE);
                } else {
                    photo2Trophy.setVisibility(VISIBLE);
                }
            } else {
                // ongoing war

                // Check Photowar queue
                checkHasQueue();

                boolean extendedWar = false;
                // check whether it's an extended war
                if(mAttractionPhotowar.getExtendedDate() != null && !warEnded(mAttractionPhotowar.getExtendedDate())){
                    extendedWar = true;
                }

                // time countdown
                startDateCountdown(extendedWar ? mAttractionPhotowar.getExtendedDate() : mAttractionPhotowar.getEndDate());

                if(mSessionManager.isLoggedIn() && mAttractionPhotowar.getResidentInPhotowar() != 1){
                    // If Resident logged in and is not part of the Photowar, allowed to vote
                    Log.d(TAG, "Resident allowed to vote!");

                    if(extendedWar){
                        bannerTextView.setText(getString(R.string.photowar_extended_vote_resident, mAttractionPhotowar.getAttraction().getName()));
                    } else {
                        bannerTextView.setText(getString(R.string.photowar_vote_resident, mAttractionPhotowar.getAttraction().getName()));
                    }

                    // set any already voted photo
                    setVotedPhoto();

                    // activate vote buttons
                    votePhoto1Button.setTag(upload1.getId());
                    votePhoto1Button.setOnClickListener(onVoteButtonClickListener);

                    votePhoto2Button.setTag(upload2.getId());
                    votePhoto2Button.setOnClickListener(onVoteButtonClickListener);
                } else {
                    // Visitor, or a Resident who is already part of this photowar - not allowed to vote
                    Log.d(TAG, "User not allowed to vote!");

                    if(extendedWar){
                        bannerTextView.setText(getString(R.string.photowar_extended_vote_visitor, mAttractionPhotowar.getAttraction().getName()));
                    } else {
                        bannerTextView.setText(getString(R.string.photowar_vote_visitor, mAttractionPhotowar.getAttraction().getName()));
                    }

                    // remove vote buttons
                    votePhoto1Button.setVisibility(INVISIBLE);
                    votePhoto2Button.setVisibility(INVISIBLE);
                }
            }


            // photo 1 elements

            String photo1ImagePath = upload1.getPhoto().getPhotoFilePath();
            loadImage(photo1, photo1ImagePath);
            photo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Clicked on photo1!");
                    PhotoFragment photoFragment = PhotoFragment.newInstance(upload1.getPhoto().getId());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photoFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            competitor1Name.setText(upload1.getPhotoResidentUserName());
            String competitor1AvatarPath = upload1.getPhotoResidentAvatarImagePath();
            loadImage(competitor1Avatar, competitor1AvatarPath);
            int resident1Id = upload1.getPhoto().getResidentId();
            competitor1Name.setTag(resident1Id);
            competitor1Name.setOnClickListener(onResidentClickListener);
            competitor1Avatar.setTag(resident1Id);
            competitor1Avatar.setOnClickListener(onResidentClickListener);
            competitor1Votes.setText(String.valueOf(upload1.getResidentVotesCount()));

            // photo 2 elements

            String photo2ImagePath = upload2.getPhoto().getPhotoFilePath();
            loadImage(photo2, photo2ImagePath);
            photo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Clicked on photo2!");
                    PhotoFragment photoFragment = PhotoFragment.newInstance(upload2.getPhoto().getId());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photoFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            competitor2Name.setText(upload2.getPhotoResidentUserName());
            String competitor2AvatarPath = upload2.getPhotoResidentAvatarImagePath();
            loadImage(competitor2Avatar, competitor2AvatarPath);
            int resident2Id = upload2.getPhoto().getResidentId();
            competitor2Name.setTag(resident2Id);
            competitor2Name.setOnClickListener(onResidentClickListener);
            competitor2Avatar.setTag(resident2Id);
            competitor2Avatar.setOnClickListener(onResidentClickListener);
            competitor2Votes.setText(String.valueOf(upload2.getResidentVotesCount()));
        }
    }

    private void checkHasQueue() {
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<PhotowarQueue>> call = service.getQueueForAttraction(mAttractionPhotowar.getAttractionId());
        call.enqueue(new Callback<List<PhotowarQueue>>() {
            @Override
            public void onResponse(Call<List<PhotowarQueue>> call, Response<List<PhotowarQueue>> response) {
                if (response.isSuccessful()) {
                    List<PhotowarQueue> photowarQueues = response.body();
                    if (photowarQueues.size() > 0) {
                        enableQueueButton();
                    }
                } else {
                    try {
                        Log.d(TAG, "[checkHasQueue:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhotowarQueue>> call, Throwable t) {
                Log.e(TAG, "[checkHasQueue:onFailure] " + t.getMessage());
            }
        });
    }

    private void enableQueueButton() {
        if (mButtonQueue != null) {
            mButtonQueue.setVisibility(VISIBLE);
            mButtonQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int attractionId = mAttractionPhotowar.getAttractionId();
                    String attractionName = mAttractionPhotowar.getAttraction().getName();

                    // Move to PhotowarQueueFragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, PhotowarQueueFragment.newInstance(attractionId, attractionName))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    private boolean warEnded(String endDateString){
        Date endDate = DateUtil.ISO8601toDate(endDateString);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        Log.d(TAG, "EndDate < now : " + endDate.before(now));
        return endDate.before(now);
    }

    private void startDateCountdown(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date endDate = null;
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        try{
            endDate = formatter.parse(dateString);
        } catch (ParseException e){
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "Current date is " + now.toString());
        Log.d(TAG, "Date extracted is " + endDate.toString());

        final Activity activity = getActivity();
        if(activity != null && isAdded()){
            countDownTimer = new CountDownTimer(endDate.getTime() - now.getTime(), 1000) {
                @Override
                public void onTick(long millisLeft) {
                    long dayMilli = 86400000;
                    long hoursMilli = 3600000;
                    long minMilli = 60000;
                    long secMilli = 1000;
                    String daysLeft = "";
                    if(millisLeft > dayMilli){
                        long days = millisLeft / dayMilli;
                        daysLeft = String.valueOf(millisLeft / dayMilli) + " day" + ((days > 1) ? "s" : "");
                    }
                    String timeLeft = String.format(Locale.getDefault(), "%s %02d:%02d:%02d",
                            daysLeft,
                            (millisLeft % dayMilli) / hoursMilli,
                            (millisLeft % hoursMilli) / minMilli,
                            (millisLeft % minMilli) / secMilli );
                    if(isAdded()){
                        photowarCountdownTextview.setText(getString(R.string.photowar_countdown, timeLeft ));
                    }
                }

                @Override
                public void onFinish() {
                    photowarCountdownTextview.setText(R.string.photowar_vote_finished);
                    votePhoto1Button.setOnClickListener(null);
                    votePhoto2Button.setOnClickListener(null);
                }
            }.start();
        }
    }

    private void loadImage(ImageView imageView, final String imagePath){
        String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
        Picasso.with(getContext()).load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Succeeded photo upload of " + imagePath);
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Failed photo upload of " + imagePath);
                    }
                });
    }

    /**
     * Checks if resident has voted on any of the photos, and if so, change vote button to clicked
     */
    private void setVotedPhoto(){
        if(upload1.getResidentHasVoted() == 1){
            votePhoto1Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_clicked));
            photo1Clicked = true;
        } else if(upload2.getResidentHasVoted() == 1){
            votePhoto2Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_clicked));
            photo2Clicked = true;
        }
    }

    /**
     * Updates Voting data after a vote has been made or removed
     */
    public void updateVotingData(){
        for(AttractionPhotowarUploadForPhotowarView upload : mAttractionPhotowar.getAttractionPhotowarUploads()){
            // Check IDs in case AttractionPhotowarUploads collection came back a different order after API call
            if (upload1.getId() == upload.getId()){
                // replace with updated data
                upload1 = upload;
            } else if (upload2.getId() == upload.getId()){
                // replace with updated data
                upload2 = upload;
            }
        }
        // update vote counts
        competitor1Votes.setText(String.valueOf(upload1.getResidentVotesCount()));
        competitor2Votes.setText(String.valueOf(upload2.getResidentVotesCount()));
    }

    private View.OnClickListener onVoteButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Inside the vote button tag is ---> " + view.getTag());
            int photoUploadId = (int) view.getTag();
            Log.d(TAG, "PhotoUpload Id is " + photoUploadId);

            if(photoUploadId == upload1.getId()){ // photo 1 clicked
                if(photo1Clicked){
                    // unclick
                    photo1Clicked = false;
                    // un-highlight button
                    votePhoto1Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_unclicked));
                    // remove resident's vote
                    removeVote(photoUploadId);
                } else {
                    //click
                    photo1Clicked = true;
                    // highlight button
                    votePhoto1Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_clicked));
                    // make sure the other photo gets unclicked if it was clicked
                    if(photo2Clicked){
                        votePhoto2Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_unclicked));
                        photo2Clicked = false;
                    }
                    // add vote
                    addVote(photoUploadId);
                }
            } else { // photo 2 clicked
                if(photo2Clicked){
                    // unclick
                    photo2Clicked = false;
                    // un-highlight button
                    votePhoto2Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_unclicked));
                    // remove resident's vote
                    removeVote(photoUploadId);
                } else {
                    // click
                    photo2Clicked = true;
                    // highlight button
                    votePhoto2Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_clicked));
                    // make sure the other photo gets unclicked if it was clicked
                    if(photo1Clicked){
                        votePhoto1Button.setBackground(getResources().getDrawable(R.drawable.vote_photowar_unclicked));
                        photo1Clicked = false;
                    }
                    // add vote
                    addVote(photoUploadId);
                }
            }

        }
    };

    /**
     * Adds Resident Vote to photo, removes vote from other photo if it was voted
     * @param photoUploadId
     */
    public void addVote(int photoUploadId){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<AttractionPhotowarWithDetails> call = service.addVoteAttractionPhotowar(mAttractionPhotowarId, mResidentId, photoUploadId);
        call.enqueue(new Callback<AttractionPhotowarWithDetails>() {
            @Override
            public void onResponse(Call<AttractionPhotowarWithDetails> call, Response<AttractionPhotowarWithDetails> response) {
                if(response.isSuccessful()){
                    // update data
                    mAttractionPhotowar = response.body();

                    Log.d(TAG, "AttractionPhotowar came back: " + mAttractionPhotowar.getId() + "," +
                            mAttractionPhotowar.getAttraction().getName());

                    updateVotingData();
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
            public void onFailure(Call<AttractionPhotowarWithDetails> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * Removes Resident's vote from Photo
     * @param photoUploadId
     */
    public void removeVote(int photoUploadId){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<AttractionPhotowarWithDetails> call = service.removeVoteAttractionPhotowar(mAttractionPhotowarId, mResidentId, photoUploadId);
        call.enqueue(new Callback<AttractionPhotowarWithDetails>() {
            @Override
            public void onResponse(Call<AttractionPhotowarWithDetails> call, Response<AttractionPhotowarWithDetails> response) {
                if(response.isSuccessful()){
                    // update data
                    mAttractionPhotowar = response.body();

                    Log.d(TAG, "AttractionPhotowar came back: " + mAttractionPhotowar.getId() + "," +
                            mAttractionPhotowar.getAttraction().getName());

                    updateVotingData();
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
            public void onFailure(Call<AttractionPhotowarWithDetails> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "Detaching Fragment!");
        Activity activity = getActivity();
        if(activity != null && isAdded()){
            if(countDownTimer != null) {
                countDownTimer.cancel();
            }
        }
        super.onDetach();
    }
}
