package com.exploreca.tourfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.exploreca.tourfinder.MovieCategory.LatestMovies;
import com.exploreca.tourfinder.MovieCategory.NowPlayingMovies;
import com.exploreca.tourfinder.MovieCategory.PopularMovies;
import com.exploreca.tourfinder.MovieCategory.TopRatedMovies;
import com.exploreca.tourfinder.MovieCategory.UpcomingMovies;

public class MainActivity extends ListActivity {

	public static final String USERNAME="pref_username";
	public static final String VIEWIMAGE="pref_viewimages";
    public static final String PHOTOS_BASE_URL = "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/w500";

    Intent intent;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        intent = new Intent(this, NowPlayingMovies.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
}
