package ca.senecacollege.prj666.photokingdom.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotowarView;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.FeedEntry;
import ca.senecacollege.prj666.photokingdom.utils.DateUtil;
import ca.senecacollege.prj666.photokingdom.utils.LiveFeedDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Background service to get LiveFeed data
 * Store retrieved data to local database
 *
 * @author Wonho
 */
public class LiveFeedService extends Service {
    private static final String TAG = "LiveFeedService";

    // Photowars
    private List<AttractionPhotowarWithDetails> mPhotowars;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Get Photowars data every 30 seconds
                        getPhotowars();
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Call PhotoKingdom API to get Photowars
     */
    private void getPhotowars(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<AttractionPhotowarWithDetails>> call = service.getPhotowars();
        call.enqueue(new Callback<List<AttractionPhotowarWithDetails>>(){
            @Override
            public void onResponse(Call<List<AttractionPhotowarWithDetails>> call,
                                   Response<List<AttractionPhotowarWithDetails>> response) {
                if(response.isSuccessful()){
                    mPhotowars = response.body();
                    savePhotowarsData();
                } else {
                    try {
                        Log.d(TAG, "[getPhotowars:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AttractionPhotowarWithDetails>> call, Throwable t) {
                Log.d(TAG, "[getPhotowars:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Store retrieved Photowars data to database
     * This stores only new Photowars data after last data
     */
    private void savePhotowarsData() {
        LiveFeedDbHelper dbHelper = new LiveFeedDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long lastPhotowarId = getLastPhotowarId();

        for (AttractionPhotowarWithDetails photowar : mPhotowars) {
            if (photowar.getId() > lastPhotowarId) {
                AttractionPhotowarUploadForPhotowarView photowarUpload1 = photowar.getAttractionPhotowarUploads().get(0);
                AttractionPhotowarUploadForPhotowarView photowarUpload2 = photowar.getAttractionPhotowarUploads().get(1);

                ContentValues values = new ContentValues();
                values.put(FeedEntry.COLUMN_PHOTOWAR_ID, photowar.getId());
                values.put(FeedEntry.COLUMN_START_DATE, DateUtil.parseDateString(photowar.getStartDate()));
                values.put(FeedEntry.COLUMN_ATTRACTION_NAME, photowar.getAttraction().getName());
                values.put(FeedEntry.COLUMN_PHOTO_PATH1, photowarUpload1.getPhoto().getPhotoFilePath());
                values.put(FeedEntry.COLUMN_PHOTO_PATH2, photowarUpload2.getPhoto().getPhotoFilePath());
                values.put(FeedEntry.COLUMN_RESIDENT_NAME1, photowarUpload1.getPhotoResidentUserName());
                values.put(FeedEntry.COLUMN_RESIDENT_NAME2, photowarUpload2.getPhotoResidentUserName());

                long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * Get last stored Photowar id
     * @return long
     */
    private long getLastPhotowarId() {
        LiveFeedDbHelper dbHelper = new LiveFeedDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                FeedEntry.COLUMN_PHOTOWAR_ID
        };

        String sortOrder = FeedEntry.COLUMN_PHOTOWAR_ID + " DESC";

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
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_PHOTOWAR_ID));
            return id;
        }
        cursor.close();

        return 0;
    }
}