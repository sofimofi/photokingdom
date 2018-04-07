package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.senecacollege.prj666.photokingdom.models.FeedEntry;

/**
 * Database helper for LiveFeed data
 *
 * @author Wonho
 */
public class LiveFeedDbHelper extends SQLiteOpenHelper {
    // Database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LiveFeed.db";

    // SQL
    private static final String SQL_CREATE_FEEDS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_PHOTOWAR_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_ATTRACTION_NAME + " TEXT," +
                    FeedEntry.COLUMN_PHOTO_PATH1 + " TEXT," +
                    FeedEntry.COLUMN_PHOTO_PATH2 + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME1 + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME2 + " TEXT)";

    private static final String SQL_DELETE_FEEDS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public LiveFeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FEEDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_FEEDS);
        onCreate(sqLiteDatabase);
    }
}