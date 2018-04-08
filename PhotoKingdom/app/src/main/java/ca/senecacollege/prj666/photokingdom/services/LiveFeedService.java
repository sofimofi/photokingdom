package ca.senecacollege.prj666.photokingdom.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarUploadForPhotowarView;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.FeedEntry;
import ca.senecacollege.prj666.photokingdom.models.ResidentOwn;
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

    // Database
    private LiveFeedDbHelper mDbHelper;

    // Photowars and owns data
    private List<AttractionPhotowarWithDetails> mPhotowars;
    private List<ResidentOwn> mAttractionOwns;
    private List<ResidentOwn> mCityOwns;
    private List<ResidentOwn> mProvinceOwns;
    private List<ResidentOwn> mCountryOwns;
    private List<ResidentOwn> mContinentOwns;

    @Override
    public void onCreate() {
        super.onCreate();
        mDbHelper = new LiveFeedDbHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Get Photowars and owns data every 30 seconds
                        getPhotowars();
                        getContinentOwns();
                        getCountryOwns();
                        getAttractionOwns();
                        getCityOwns();
                        getProvinceOwns();

                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
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
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long lastPhotowarId = mDbHelper.getLastId(
                FeedEntry.TABLE_NAME_PHOTOWARS, FeedEntry.COLUMN_PHOTOWAR_ID);

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

                long newRowId = db.insert(FeedEntry.TABLE_NAME_PHOTOWARS, null, values);
            }
        }
    }

    /**
     * Call PhotoKingdom API to get attraction owns
     */
    private void getAttractionOwns(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<ResidentOwn>> call = service.getAttractionOwns();
        call.enqueue(new Callback<List<ResidentOwn>>(){
            @Override
            public void onResponse(Call<List<ResidentOwn>> call,
                                   Response<List<ResidentOwn>> response) {
                if(response.isSuccessful()){
                    mAttractionOwns = response.body();
                    saveOwnsData(FeedEntry.TABLE_NAME_ATTRACTION_OWNS, mAttractionOwns);
                } else {
                    try {
                        Log.d(TAG, "[getAttractionOwns:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentOwn>> call, Throwable t) {
                Log.d(TAG, "[getAttractionOwns:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Call PhotoKingdom API to get city owns
     */
    private void getCityOwns(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<ResidentOwn>> call = service.getCityOwns();
        call.enqueue(new Callback<List<ResidentOwn>>(){
            @Override
            public void onResponse(Call<List<ResidentOwn>> call,
                                   Response<List<ResidentOwn>> response) {
                if(response.isSuccessful()){
                    mCityOwns = response.body();
                    saveOwnsData(FeedEntry.TABLE_NAME_CITY_OWNS, mCityOwns);
                } else {
                    try {
                        Log.d(TAG, "[getCityOwns:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentOwn>> call, Throwable t) {
                Log.d(TAG, "[getCityOwns:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Call PhotoKingdom API to get province owns
     */
    private void getProvinceOwns(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<ResidentOwn>> call = service.getProvinceOwns();
        call.enqueue(new Callback<List<ResidentOwn>>(){
            @Override
            public void onResponse(Call<List<ResidentOwn>> call,
                                   Response<List<ResidentOwn>> response) {
                if(response.isSuccessful()){
                    mProvinceOwns = response.body();
                    saveOwnsData(FeedEntry.TABLE_NAME_PROVINCE_OWNS, mProvinceOwns);
                } else {
                    try {
                        Log.d(TAG, "[getProvinceOwns:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentOwn>> call, Throwable t) {
                Log.d(TAG, "[getProvinceOwns:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Call PhotoKingdom API to get country owns
     */
    private void getCountryOwns(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<ResidentOwn>> call = service.getCountryOwns();
        call.enqueue(new Callback<List<ResidentOwn>>(){
            @Override
            public void onResponse(Call<List<ResidentOwn>> call,
                                   Response<List<ResidentOwn>> response) {
                if(response.isSuccessful()){
                    mCountryOwns = response.body();
                    saveOwnsData(FeedEntry.TABLE_NAME_COUNTRY_OWNS, mCountryOwns);
                } else {
                    try {
                        Log.d(TAG, "[getCountryOwns:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentOwn>> call, Throwable t) {
                Log.d(TAG, "[getCountryOwns:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Call PhotoKingdom API to get continent owns
     */
    private void getContinentOwns(){
        PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
        Call<List<ResidentOwn>> call = service.getContinentsOwns();
        call.enqueue(new Callback<List<ResidentOwn>>(){
            @Override
            public void onResponse(Call<List<ResidentOwn>> call,
                                   Response<List<ResidentOwn>> response) {
                if(response.isSuccessful()){
                    mContinentOwns = response.body();
                    saveOwnsData(FeedEntry.TABLE_NAME_CONTINENT_OWNS, mContinentOwns);
                } else {
                    try {
                        Log.d(TAG, "[getContinentOwns:onResponse] " + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentOwn>> call, Throwable t) {
                Log.d(TAG, "[getContinentOwns:onFailure] " + t.getMessage());
            }
        });
    }

    /**
     * Store retrieved owns data to database
     * This stores only new owns data after last data
     */
    private void saveOwnsData(String table, List<ResidentOwn> owns) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long lastOwnId = mDbHelper.getLastId(table, FeedEntry.COLUMN_OWN_ID);

        for (ResidentOwn residentOwn : owns) {
            if (residentOwn.getId() > lastOwnId) {
                ContentValues values = new ContentValues();
                values.put(FeedEntry.COLUMN_OWN_ID, residentOwn.getId());
                values.put(FeedEntry.COLUMN_START_DATE, DateUtil.parseDateString(residentOwn.getStartDate()));
                values.put(FeedEntry.COLUMN_RESIDENT_ID, residentOwn.getResidentId());
                values.put(FeedEntry.COLUMN_RESIDENT_NAME, residentOwn.getResident().getUserName());
                values.put(FeedEntry.COLUMN_TITLE, residentOwn.getTitle());

                long newRowId = db.insert(table, null, values);
            }
        }
    }
}