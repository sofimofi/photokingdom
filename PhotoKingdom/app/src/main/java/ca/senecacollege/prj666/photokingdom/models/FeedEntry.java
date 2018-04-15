package ca.senecacollege.prj666.photokingdom.models;

import android.provider.BaseColumns;

/**
 * Database table name and column names for LiveFeed data
 *
 * @author Wonho
 */
public class FeedEntry implements BaseColumns {
    // Type
    public static final int TYPE_PHOTOWAR = 1;
    public static final int TYPE_OWN = 2;

    // Photowar
    public static final String TABLE_NAME_PHOTOWARS = "photowars";
    public static final String COLUMN_CREATED_DATE = "created_date";
    public static final String COLUMN_PHOTOWAR_ID = "photowar_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_ATTRACTION_NAME = "attraction_name";
    public static final String COLUMN_PHOTO_PATH1 = "photo_path1";
    public static final String COLUMN_PHOTO_PATH2 = "photo_path2";
    public static final String COLUMN_RESIDENT_NAME1 = "resident_name1";
    public static final String COLUMN_RESIDENT_NAME2 = "resident_name2";

    // Attraction own
    public static final String TABLE_NAME_ATTRACTION_OWNS = "attraction_owns";
    public static final String COLUMN_OWN_ID = "own_id";
    public static final String COLUMN_RESIDENT_ID = "resident_id";
    public static final String COLUMN_RESIDENT_NAME = "resident_name";
    public static final String COLUMN_TITLE = "title";

    // City own
    public static final String TABLE_NAME_CITY_OWNS = "city_owns";

    // Province own
    public static final String TABLE_NAME_PROVINCE_OWNS = "province_owns";

    // Country own
    public static final String TABLE_NAME_COUNTRY_OWNS = "country_owns";

    // Continent own
    public static final String TABLE_NAME_CONTINENT_OWNS = "continent_owns";
}