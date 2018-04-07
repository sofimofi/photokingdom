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
import java.util.Collections;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.PhotowarFragment;
import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.UserFragment;
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
    private long mCurrentPhotowarId;
    private long mCurrentAttractionOwnId;
    private long mCurrentCityOwnId;
    private long mCurrentProvinceOwnId;
    private long mCurrentCountryOwnId;
    private long mCurrentContinentOwnId;

    // Database
    private LiveFeedDbHelper mDbHelper;

    public LiveFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.livefeed);

        // Database
        mDbHelper = new LiveFeedDbHelper(getContext());
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
        mCurrentPhotowarId = 0;
        mCurrentAttractionOwnId = 0;
        mCurrentCityOwnId = 0;
        mCurrentProvinceOwnId = 0;
        mCurrentCountryOwnId = 0;
        mCurrentContinentOwnId = 0;

        // Refresh data evrery second
        mHandler = new Handler();
        mRefreshData = new Runnable() {
            @Override
            public void run() {
                // Load Photowars and owns data
                loadPhotowarsData();
                loadOwnsData(FeedEntry.TABLE_NAME_CONTINENT_OWNS);
                loadOwnsData(FeedEntry.TABLE_NAME_COUNTRY_OWNS);
                loadOwnsData(FeedEntry.TABLE_NAME_PROVINCE_OWNS);
                loadOwnsData(FeedEntry.TABLE_NAME_CITY_OWNS);
                loadOwnsData(FeedEntry.TABLE_NAME_ATTRACTION_OWNS);

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

        // Close database
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     * Load Photowars data from local database
     * Set the data to RecyclerView
     */
    private void loadPhotowarsData() {
        if (hasNewData(FeedEntry.TABLE_NAME_PHOTOWARS, FeedEntry.COLUMN_PHOTOWAR_ID)) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

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
                    FeedEntry.TABLE_NAME_PHOTOWARS,
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
                LiveFeed feed = new LiveFeed(FeedEntry.TYPE_PHOTOWAR);
                feed.setDate(startDate);
                feed.setMsg("New Photowar on " + attractionName);
                feed.photowar.setPhotowarId(photowarId);
                feed.photowar.setPhotoPath1(photoPath1);
                feed.photowar.setPhotoPath2(photoPath2);
                feed.photowar.setResidentName1(residentName1);
                feed.photowar.setResidentName2(residentName2);
                mFeeds.add(feed);
            }
            cursor.close();

            // Set data to view
            setRecyclerView();
        }
    }

    /**
     * Load owns data from local database
     * Set the data to RecyclerView
     */
    private void loadOwnsData(String table) {
        if (hasNewData(table, FeedEntry.COLUMN_OWN_ID)) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Projection
            String[] projection = {
                    FeedEntry._ID,
                    FeedEntry.COLUMN_OWN_ID,
                    FeedEntry.COLUMN_START_DATE,
                    FeedEntry.COLUMN_RESIDENT_ID,
                    FeedEntry.COLUMN_RESIDENT_NAME,
                    FeedEntry.COLUMN_TITLE
            };

            // Sort
            String sortOrder = FeedEntry.COLUMN_OWN_ID + " DESC";

            // Query
            Cursor cursor = db.query(
                    table,
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
                int ownId = cursor.getInt(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_OWN_ID));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_START_DATE));
                int residentId = cursor.getInt(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_RESIDENT_ID));
                String residentName = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_RESIDENT_NAME));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_TITLE));

                // LiveFeed
                LiveFeed feed = new LiveFeed(FeedEntry.TYPE_OWN);
                feed.setDate(startDate);
                feed.setMsg(residentName + " is " + title);
                feed.own.setOwnId(ownId);
                feed.own.setResidentId(residentId);
                mFeeds.add(feed);
            }
            cursor.close();

            // Set data to view
            setRecyclerView();
        }
    }

    /**
     * Check new data exists
     * @return boolean
     */
    private boolean hasNewData(String table, String column) {
        long lastId = mDbHelper.getLastId(table, column);

        switch (table) {
            case FeedEntry.TABLE_NAME_PHOTOWARS:
                if (mCurrentPhotowarId < lastId) {
                    mCurrentPhotowarId = lastId;
                    return true;
                } else {
                    return false;
                }
            case FeedEntry.TABLE_NAME_ATTRACTION_OWNS:
                if (mCurrentAttractionOwnId < lastId) {
                    mCurrentAttractionOwnId = lastId;
                    return true;
                } else {
                    return false;
                }
            case FeedEntry.TABLE_NAME_CITY_OWNS:
                if (mCurrentCityOwnId < lastId) {
                    mCurrentCityOwnId = lastId;
                    return true;
                } else {
                    return false;
                }
            case FeedEntry.TABLE_NAME_PROVINCE_OWNS:
                if (mCurrentProvinceOwnId < lastId) {
                    mCurrentProvinceOwnId = lastId;
                    return true;
                } else {
                    return false;
                }
            case FeedEntry.TABLE_NAME_COUNTRY_OWNS:
                if (mCurrentCountryOwnId < lastId) {
                    mCurrentCountryOwnId = lastId;
                    return true;
                } else {
                    return false;
                }
            case FeedEntry.TABLE_NAME_CONTINENT_OWNS:
                if (mCurrentContinentOwnId < lastId) {
                    mCurrentContinentOwnId = lastId;
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    /**
     * Set live feed data to UI (RecyclerView)
     */
    private void setRecyclerView() {
        if (mFeeds.size() > 0) {
            Collections.sort(mFeeds);

            mAdapter = new LiveFeedsAdapter(getContext(), mFeeds);

            // Photowar click
            mAdapter.setOnItemClickListener(new LiveFeedsAdapter.OnItemClickListener() {
                @Override
                public void onPhotowarItemClick(View view, int photowarId) {
                    // Show Photowar view
                    PhotowarFragment photowarFragment = PhotowarFragment.newInstance(photowarId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, photowarFragment )
                            .addToBackStack(null)
                            .commit();
                }

                @Override
                public void onOwnItemClick(View view, int residentId) {
                    // Show user profile view
                    UserFragment userFragment = UserFragment.newInstance(residentId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, userFragment )
                            .addToBackStack(null)
                            .commit();
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
