package com.exploreca.imdb;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.exploreca.imdb.MovieCategory.LatestMovies;
import com.exploreca.imdb.MovieCategory.NowPlayingMovies;
import com.exploreca.imdb.MovieCategory.PopularMovies;
import com.exploreca.imdb.MovieCategory.TopRatedMovies;
import com.exploreca.imdb.MovieCategory.UpcomingMovies;
import com.exploreca.imdb.db.MoviesDataSource;
import com.exploreca.imdb.model.Movie;

import java.util.List;

/**
 * Created by ALOKNATH on 1/17/2015.
 */
public class MyMovies extends ListActivity {

    private List<Movie> movies;
    MoviesDataSource dataSource;
    Context context;
    private final int DELETE_ID = R.id.delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        this.registerForContextMenu(getListView());
        dataSource = new MoviesDataSource(this);
        dataSource.open();
        display();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,DELETE_ID,0,R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            Movie movie = movies.get(index);
            Log.i("Movie_Name", movie.getTitle());
            if(dataSource.deleteMovie(movie.getTitle())){
                Toast.makeText(this, "Movie Deleted", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Movie Not Deleted", Toast.LENGTH_LONG).show();
            }
            display();
        }
        else
        {
            Toast.makeText(this, "Context Menu Item not selected", Toast.LENGTH_LONG).show();
        }

        return true;
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
        //String value = String.valueOf(position);
        Log.i("PositionNo", movie.getPoster_path());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(".model.Movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.findItem(R.id.myFavourite);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
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
