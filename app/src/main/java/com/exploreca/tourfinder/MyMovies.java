package com.exploreca.tourfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.exploreca.tourfinder.MovieCategory.LatestMovies;
import com.exploreca.tourfinder.MovieCategory.NowPlayingMovies;
import com.exploreca.tourfinder.MovieCategory.PopularMovies;
import com.exploreca.tourfinder.MovieCategory.TopRatedMovies;
import com.exploreca.tourfinder.MovieCategory.UpcomingMovies;
import com.exploreca.tourfinder.db.MoviesDataSource;
import com.exploreca.tourfinder.model.Movie;

import java.util.List;

/**
 * Created by ALOKNATH on 1/17/2015.
 */
public class MyMovies extends ListActivity {

    private List<Movie> movies;
    MoviesDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource = new MoviesDataSource(this);
        dataSource.open();
        display();
    }

    private void display() {
        movies = dataSource.getMyMovies();
        MovieAdapter adapter = new MovieAdapter(this, R.layout.item_movie, movies);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Movie movie = movies.get(position);
        String value = String.valueOf(position);
        Log.i("PositionNo", movie.getPoster_path());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(".model.Movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){

            case R.id.menu_latest:
                intent = new Intent(this, LatestMovies.class);
                startActivity(intent);
                break;
            case R.id.menu_upcoming:
                intent = new Intent(this, UpcomingMovies.class);
                startActivity(intent);
                break;
            case R.id.menu_now_playing:
                intent = new Intent(this, NowPlayingMovies.class);
                startActivity(intent);
                break;
            case R.id.menu_top_rated:
                intent = new Intent(this, TopRatedMovies.class);
                startActivity(intent);
                break;

            case R.id.menu_popular:
                intent = new Intent(this, PopularMovies.class);
                startActivity(intent);
                break;

            case R.id.myFavourite:
                break;

            case R.id.delete:
                dataSource.deleteMovies();
                display();
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

}
