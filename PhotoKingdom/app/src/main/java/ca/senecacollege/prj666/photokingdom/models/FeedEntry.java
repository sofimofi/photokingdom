package ca.senecacollege.prj666.photokingdom.models;

import android.provider.BaseColumns;

/**
 * Database table name and column names for LiveFeed data
 *
 * @author Wonho
 */
public class FeedEntry implements BaseColumns {
    public static final String TABLE_NAME = "feed";
    public static final String COLUMN_PHOTOWAR_ID = "photowar_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_ATTRACTION_NAME = "attraction_name";
    public static final String COLUMN_PHOTO_PATH1 = "photo_path1";
    public static final String COLUMN_PHOTO_PATH2 = "photo_path2";
    public static final String COLUMN_RESIDENT_NAME1 = "resident_name1";
    public static final String COLUMN_RESIDENT_NAME2 = "resident_name2";
}