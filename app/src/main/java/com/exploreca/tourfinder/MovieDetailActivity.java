package com.exploreca.tourfinder;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exploreca.tourfinder.db.MoviesDataSource;
import com.exploreca.tourfinder.model.Movie;
import com.exploreca.tourfinder.parser.MovieJSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 1/15/2015.
 */
public class MovieDetailActivity extends Activity {

    Movie movie;
    MoviesDataSource dataSource;
    Button trailer_button;
   // private List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        Bundle bundle = getIntent().getExtras();

        movie = (Movie)bundle.getParcelable(".model.Movie");
        //Intent intent = getIntent();
        //movie.setBitmap((Bitmap) intent.getParcelableExtra("BitmapImage"));
        //Log.i("Movie_Poster", movie.getPoster_path());
        String imageUrl;
        String imageU = movie.getPoster_path();

        if((imageU.isEmpty() && imageU == null) || imageU.equals("null"))
        {
            imageUrl = MovieAdapter.DEFAULT_IMAGE;
        }else{
            imageUrl = MainActivity.PHOTOS_BASE_URL + movie.getPoster_path();
        }
        String ContentUrl = "http://api.themoviedb.org/3/movie/"+ movie.getId() + "?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
        ImageLoad imageLoad = new ImageLoad();
        imageLoad.execute(imageUrl, ContentUrl);

        TextView tv = (TextView) findViewById(R.id.movie_title);
        //Log.i("Movie Object", movie.getOriginal_title());
        tv.setText(movie.getTitle());

        tv = (TextView)findViewById(R.id.release_date);
        tv.setText(movie.getRelease_date());

        trailer_button = (Button)findViewById(R.id.button_trailer);
        trailer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, MovieTrailer.class);
                intent.putExtra("Movie_Name", movie);
                startActivity(intent);
            }
        });

        dataSource = new MoviesDataSource(this);
        dataSource.open();

        //refreshDisplay(movie.getPoster_path());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.myFavourite:
                Intent intent = new Intent(this, MyMovies.class);
                startActivity(intent);
                break;
            case R.id.addFavourite:
                if(dataSource.addToMyMovies(movie)) {
                    Toast.makeText(this, "Movie Added to the DataBase", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(this, "Movie Not Added to the DataBase", Toast.LENGTH_LONG).show();
                    finish();
                }
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

    private class MovieDetailContainer{
        public Bitmap bitmapMovie;
        public String movieDetailContainer;

        public Bitmap getBitmapMovie() {
            return bitmapMovie;
        }

        public void setBitmapMovie(Bitmap bitmapMovie) {
            this.bitmapMovie = bitmapMovie;
        }

        public String getMovieDetailContainer() {
            return movieDetailContainer;
        }

        public void setMovieDetailContainer(String movieDetailContainer) {
            this.movieDetailContainer = movieDetailContainer;
        }
    }

    private class ImageLoad extends AsyncTask<String, Void, MovieDetailContainer>{
        @Override
        protected MovieDetailContainer doInBackground(String... urls) {
            InputStream in = null;
            try {
                in = (InputStream) new URL(urls[0]).getContent();
                String content = HttpManager.getData(urls[1]);
                String movieDetail = MovieJSONParser.parseMovieDetail(content);
                MovieDetailContainer container = new MovieDetailContainer();
                container.setBitmapMovie( BitmapFactory.decodeStream(in));
                container.setMovieDetailContainer(movieDetail);
                in.close();
                return container;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetailContainer container) {

            ImageView image = (ImageView)findViewById(R.id.movie_poster);
            image.setImageBitmap(container.getBitmapMovie());

            TextView textView = (TextView)findViewById(R.id.movie_description);
            textView.setText(container.getMovieDetailContainer());
        }
    }

//    private void refreshDisplay(String title){
//        TextView tv = (TextView) findViewById(R.id.movie_title);
//        //Log.i("Movie Object", movie.getOriginal_title());
//        tv.setText(title);
//    }
}
