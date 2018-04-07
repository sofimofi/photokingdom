package ca.senecacollege.prj666.photokingdom.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.PhotowarFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.adapters.LiveFeedsAdapter;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotowarView;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.FeedEntry;
import ca.senecacollege.prj666.photokingdom.models.LiveFeed;
import ca.senecacollege.prj666.photokingdom.utils.LiveFeedDbHelper;


/**
 * Fragment for LiveFeed
 *
 * @author Wonho
 */
public class LiveFeedFragment extends Fragment {
    private static final String TAG = "LiveFeedFragment";

    // RecyclerView
    private RecyclerView mRecyclerView;
    private LiveFeedsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Photowar
    private List<AttractionPhotowarWithDetails> mPhotowars;
    private AttractionPhotowarUploadForPhotowarView mPhotowarUpload1;
    private AttractionPhotowarUploadForPhotowarView mPhotowarUpload2;
    private List<LiveFeed> mFeeds;

    // Refresh LiveFeed data
    private Handler mHandler;
    private Runnable mRefreshData;
    private long mCurrentId;

    public LiveFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.livefeed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_live_feed, container, false);

        // RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Set RecyclerView layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // LiveFeeds
        mFeeds = new ArrayList<LiveFeed>();

        // Reset current id
        mCurrentId = 0;

        // Refresh data evrery second
        mHandler = new Handler();
        mRefreshData = new Runnable() {
            @Override
            public void run() {
                loadPhotowarsData();
                mHandler.postDelayed(mRefreshData, 1000);
            }
        };
        mHandler.post(mRefreshData);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Stop refresh data
        if (mHandler != null) {
            mHandler.removeCallbacks(mRefreshData);
        }
    }

    /**
     * Load Photowars data from local database
     * Set the data to RecyclerView
     */
    private void loadPhotowarsData() {
        if (hasNewData()) {
            LiveFeedDbHelper dbHelper = new LiveFeedDbHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Projection
            String[] projection = {
                    FeedEntry._ID,
                    FeedEntry.COLUMN_PHOTOWAR_ID,
                    FeedEntry.COLUMN_START_DATE,
                    FeedEntry.COLUMN_ATTRACTION_NAME,
                    FeedEntry.COLUMN_PHOTO_PATH1,
                    FeedEntry.COLUMN_PHOTO_PATH2,
                    FeedEntry.COLUMN_RESIDENT_NAME1,
                    FeedEntry.COLUMN_RESIDENT_NAME2
            };

            // Sort
            String sortOrder = FeedEntry.COLUMN_PHOTOWAR_ID + " DESC";

            // Query
            Cursor cursor = db.query(
                    FeedEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            // Add data
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
                int photowarId = cursor.getInt(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_PHOTOWAR_ID));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_START_DATE));
                String attractionName = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_ATTRACTION_NAME));
                String photoPath1 = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_PHOTO_PATH1));
                String photoPath2 = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_PHOTO_PATH2));
                String residentName1 = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_RESIDENT_NAME1));
                String residentName2 = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_RESIDENT_NAME2));

                // LiveFeed
                LiveFeed feed = new LiveFeed();
                feed.setPhotowarId(photowarId);
                feed.setDate(startDate);
                feed.setMsg("New Photowar on " + attractionName);
                feed.setPhotoPath1(photoPath1);
                feed.setPhotoPath2(photoPath2);
                feed.setName1(residentName1);
                feed.setName2(residentName2);
                mFeeds.add(feed);
            }
            cursor.close();

            // Set RecyclerView
            if (mFeeds.size() > 0) {
                mAdapter = new LiveFeedsAdapter(getContext(), mFeeds);

                // Photowar click
                mAdapter.setOnItemClickListener(new LiveFeedsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int photowarId = mFeeds.get(position).getPhotowarId();

                        // Show Photowar view
                        PhotowarFragment photowarFragment = PhotowarFragment.newInstance(photowarId);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, photowarFragment )
                                .addToBackStack(null)
                                .commit();
                    }
                });

                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    /**
     * Get last stored LiveFeed id
     * @return long
     */
    private long getLastId() {
        LiveFeedDbHelper dbHelper = new LiveFeedDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                FeedEntry._ID
        };

        String sortOrder = FeedEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                "1"
        );

        if (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
            return id;
        }
        cursor.close();

        return 0;
    }

    /**
     * Check new data exists
     * @return boolean
     */
    private boolean hasNewData() {
        long lastId = getLastId();
        if (mCurrentId < lastId) {
            mCurrentId = lastId;
            return true;
        } else {
            return false;
        }
    }
}
