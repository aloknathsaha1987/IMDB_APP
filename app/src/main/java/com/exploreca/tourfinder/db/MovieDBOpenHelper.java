package com.exploreca.tourfinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ALOKNATH on 1/17/2015.
 */
public class MovieDBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "my_cool_movies.db";
    private static final int DATABASE_VERSION =2;

    public static final String TABLE_MY_MOVIES = "my_movies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_VOTE_AVG = "vote_average";
    public static final String COLUMN_VOTE_CNT = "vote_count";
    public static final String COLUMN_ADULT = "adult";
    public static final String COLUMN_BACKDROP = "backdrop_path";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_VIDEO = "video";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MY_MOVIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_POSTER_PATH + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_RELEASE_DATE + " TEXT, " +
                    COLUMN_VOTE_AVG + " BLOB, " +
                    COLUMN_VOTE_CNT + " INTEGER, " +
                    COLUMN_ADULT + " INTEGER, " +
                    COLUMN_BACKDROP + " TEXT, " +
                    COLUMN_ORIGINAL_TITLE + " TEXT, " +
                    COLUMN_POPULARITY + " BLOB, " +
                    COLUMN_VIDEO + " BLOB " +
                    ")";

    public MovieDBOpenHelper(Context context){
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        database.execSQL("DROP TABLE IF EXISTS" + TABLE_MY_MOVIES);
        onCreate(database);
    }
}
