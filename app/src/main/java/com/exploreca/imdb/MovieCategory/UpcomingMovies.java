package com.exploreca.imdb.MovieCategory;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exploreca.imdb.HttpManager;
import com.exploreca.imdb.MovieAdapter;
import com.exploreca.imdb.MovieDetailActivity;
import com.exploreca.imdb.MyMovies;
import com.exploreca.imdb.R;
import com.exploreca.imdb.db.MoviesDataSource;
import com.exploreca.imdb.model.Movie;
import com.exploreca.imdb.parser.MovieJSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 1/15/2015.
 */
public class UpcomingMovies extends ListActivity {

    public static final String LOGGERSTR = "HELLO";
    public static final String API_VERSION = "3";
    public static final String CONTEXT_PATH = "movie/upcoming";
    public static final String API_KEY = "f47dd4de64c6ef630c2b0d50a087cc33";

    Context context;
    ProgressBar pb;
    List<MyTask> tasks;
    List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        this.registerForContextMenu(getListView());

        pb = (ProgressBar) findViewById(R.id.progressBar1);

        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();

        if (isOnline()) {
            requestData("http://api.themoviedb.org/");
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addFavourite){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            Movie movie = movieList.get(index);
            MoviesDataSource dataSource = new MoviesDataSource(context);
            dataSource.open();
            dataSource.addToMyMovies(movie);
            dataSource.close();
        }
        return true;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.findItem(R.id.menu_upcoming);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.menu_now_playing).setVisible(false);
//        menu.findItem(R.id.menu_latest).setVisible(false);
//        menu.findItem(R.id.menu_popular).setVisible(false);
//        menu.findItem(R.id.menu_top_rated).setVisible(false);
//        menu.findItem(R.id.menu_upcoming).setVisible(true);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_latest:
                intent = new Intent(this, LatestMovies.class);
                startActivity(intent);
                break;
            case R.id.menu_upcoming:
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
                intent = new Intent(this, MyMovies.class);
                startActivity(intent);
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateDisplay(){
        MovieAdapter adapter = new MovieAdapter(this, R.layout.item_movie, movieList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Movie movie = movieList.get(position);
        String value = String.valueOf(position);
        Log.i("PositionNo", movie.getPoster_path());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(".model.Movie", movie);
        startActivity(intent);
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, List<Movie>> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            String content = HttpManager.getData(params[0], API_VERSION, CONTEXT_PATH, API_KEY);
            movieList = MovieJSONParser.parseFeedMovie(content);
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(UpcomingMovies.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }
            movieList = result;
            updateDisplay();
        }

    }
}
