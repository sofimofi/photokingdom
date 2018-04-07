package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.database.Cursor;
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
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "LiveFeed.db";

    // Photowar table create
    private static final String SQL_CREATE_PHOTOWARS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_PHOTOWARS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_PHOTOWAR_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_ATTRACTION_NAME + " TEXT," +
                    FeedEntry.COLUMN_PHOTO_PATH1 + " TEXT," +
                    FeedEntry.COLUMN_PHOTO_PATH2 + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME1 + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME2 + " TEXT)";
    // Photowar table delete
    private static final String SQL_DELETE_PHOTOWARS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_PHOTOWARS;

    // Attraction own table create
    private static final String SQL_CREATE_ATTRACTION_OWNS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_ATTRACTION_OWNS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_OWN_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_ID + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME + " TEXT," +
                    FeedEntry.COLUMN_TITLE + " TEXT)";
    // Attraction own table delete
    private static final String SQL_DELETE_ATTRACTION_OWNS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_ATTRACTION_OWNS;

    // City own table create
    private static final String SQL_CREATE_CITY_OWNS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_CITY_OWNS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_OWN_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_ID + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME + " TEXT," +
                    FeedEntry.COLUMN_TITLE + " TEXT)";
    // City own table delete
    private static final String SQL_DELETE_CITY_OWNS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_CITY_OWNS;

    // Province own table create
    private static final String SQL_CREATE_PROVINCE_OWNS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_PROVINCE_OWNS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_OWN_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_ID + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME + " TEXT," +
                    FeedEntry.COLUMN_TITLE + " TEXT)";
    // Province own table delete
    private static final String SQL_DELETE_PROVINCE_OWNS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_PROVINCE_OWNS;

    // Country own table create
    private static final String SQL_CREATE_COUNTRY_OWNS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_COUNTRY_OWNS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_OWN_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_ID + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME + " TEXT," +
                    FeedEntry.COLUMN_TITLE + " TEXT)";
    // Country own table delete
    private static final String SQL_DELETE_COUNTRY_OWNS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_COUNTRY_OWNS;

    // Continent own table create
    private static final String SQL_CREATE_CONTINENT_OWNS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_CONTINENT_OWNS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_OWN_ID + " TEXT," +
                    FeedEntry.COLUMN_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_ID + " TEXT," +
                    FeedEntry.COLUMN_RESIDENT_NAME + " TEXT," +
                    FeedEntry.COLUMN_TITLE + " TEXT)";
    // Continent own table delete
    private static final String SQL_DELETE_CONTINENT_OWNS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_CONTINENT_OWNS;

    public LiveFeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PHOTOWARS);
        sqLiteDatabase.execSQL(SQL_CREATE_CONTINENT_OWNS);
        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRY_OWNS);
        sqLiteDatabase.execSQL(SQL_CREATE_PROVINCE_OWNS);
        sqLiteDatabase.execSQL(SQL_CREATE_CITY_OWNS);
        sqLiteDatabase.execSQL(SQL_CREATE_ATTRACTION_OWNS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_PHOTOWARS);
        sqLiteDatabase.execSQL(SQL_DELETE_CONTINENT_OWNS);
        sqLiteDatabase.execSQL(SQL_DELETE_COUNTRY_OWNS);
        sqLiteDatabase.execSQL(SQL_DELETE_PROVINCE_OWNS);
        sqLiteDatabase.execSQL(SQL_DELETE_CITY_OWNS);
        sqLiteDatabase.execSQL(SQL_DELETE_ATTRACTION_OWNS);
        onCreate(sqLiteDatabase);
    }

    /**
     * Get last stored id in passed table
     * @return long
     */
    public long getLastId(String table, String column) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                column
        };

        String sortOrder = column + " DESC";

        Cursor cursor = db.query(
                table,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                "1"
        );

        if (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(column));
            return id;
        }
        cursor.close();

        return 0;
    }
}