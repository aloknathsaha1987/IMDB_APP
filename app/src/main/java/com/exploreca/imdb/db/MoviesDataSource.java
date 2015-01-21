package com.exploreca.imdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exploreca.imdb.model.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ALOKNATH on 1/17/2015.
 */
public class MoviesDataSource {
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    List<Movie> movies_orig = new ArrayList<>();

    private static final String[] allColumns = {
            MovieDBOpenHelper.COLUMN_ID,
            MovieDBOpenHelper.COLUMN_TITLE,
            MovieDBOpenHelper.COLUMN_POSTER_PATH,
            MovieDBOpenHelper.COLUMN_RELEASE_DATE,
//            MovieDBOpenHelper.COLUMN_DESC,
            MovieDBOpenHelper.COLUMN_VOTE_CNT,

            MovieDBOpenHelper.COLUMN_VOTE_AVG,
            MovieDBOpenHelper.COLUMN_ADULT,
           MovieDBOpenHelper.COLUMN_BACKDROP,
           MovieDBOpenHelper.COLUMN_ORIGINAL_TITLE,
            MovieDBOpenHelper.COLUMN_POPULARITY,
           MovieDBOpenHelper.COLUMN_VIDEO};

    public MoviesDataSource(Context context){
        sqLiteOpenHelper = new MovieDBOpenHelper(context);
    }

    public void open(){
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteOpenHelper.close();
    }

    public boolean addToMyMovies(Movie movie){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBOpenHelper.COLUMN_ID, movie.getId());
        contentValues.put(MovieDBOpenHelper.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieDBOpenHelper.COLUMN_POSTER_PATH, movie.getPoster_path());
        contentValues.put(MovieDBOpenHelper.COLUMN_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieDBOpenHelper.COLUMN_VOTE_AVG, movie.getVote_average());
        contentValues.put(MovieDBOpenHelper.COLUMN_VOTE_CNT, movie.getVote_count());
        contentValues.put(MovieDBOpenHelper.COLUMN_ADULT, movie.getAdult());
        contentValues.put(MovieDBOpenHelper.COLUMN_BACKDROP, movie.getBackdrop_path());
        contentValues.put(MovieDBOpenHelper.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        contentValues.put(MovieDBOpenHelper.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieDBOpenHelper.COLUMN_VIDEO, movie.getVideo());

//        contentValues.put(MovieDBOpenHelper.COLUMN_VIDEO, (movie.getVideo())== true ? 1 : 0);

        long insertId = sqLiteDatabase.insert(MovieDBOpenHelper.TABLE_MY_MOVIES, null, contentValues);

        return (insertId != -1);

    }

    public List<Movie> getMyMovies(){

        Cursor cursor = sqLiteDatabase.query(MovieDBOpenHelper.TABLE_MY_MOVIES, allColumns,
                null, null, null, null, null);
        List<Movie> movies = cursorToList(cursor);
        movies_orig = movies;
        return movies;
    }


    public boolean deleteMovie(String movieName){

          return sqLiteDatabase.delete(MovieDBOpenHelper.TABLE_MY_MOVIES, MovieDBOpenHelper.COLUMN_TITLE + "= '" + movieName + "'", null) > 0;
    }

    private List<Movie> cursorToList(Cursor cursor) {

        List<Movie> movies = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_POSTER_PATH)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_RELEASE_DATE)));
                movie.setVote_count(cursor.getInt(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_VOTE_CNT)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_VOTE_AVG)));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_BACKDROP)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_ORIGINAL_TITLE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_POPULARITY)));
                movie.setAdult(cursor.getInt(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_ADULT)) == 1 ? true : false);
                movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieDBOpenHelper.COLUMN_VIDEO)) == 1 ? true : false);
                movies.add(movie);
            }
        }
        return movies;
    }

}
