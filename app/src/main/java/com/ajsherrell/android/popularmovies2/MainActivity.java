package com.ajsherrell.android.popularmovies2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.adapters.MovieAdapter;
import com.ajsherrell.android.popularmovies2.adapters.ReviewAdapter;
import com.ajsherrell.android.popularmovies2.adapters.TrailerAdapter;
import com.ajsherrell.android.popularmovies2.data.FavoriteMovie;
import com.ajsherrell.android.popularmovies2.data.MovieDatabase;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.ajsherrell.android.popularmovies2.utilities.JSONUtils;
import com.ajsherrell.android.popularmovies2.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ajsherrell.android.popularmovies2.Constants.MOVIE_ID;
import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createMovieUrl;
import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createReviewUrl;
import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createTrailerUrl;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Movie> data = new ArrayList<>();
    private List<FavoriteMovie> favoriteData = new ArrayList<>();

    private static RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;
    private static TextView mErrorMessageDisplay;
    private static ProgressBar mProgressBar;

    private MovieDatabase mDb;

    private static ArrayList<Review> reviewData = new ArrayList<>();
    private static ArrayList<Trailer> trailerData = new ArrayList<>();

    private static ReviewAdapter rAdapter;
    private static TrailerAdapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // finders
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView)findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns());

        mRecyclerView.setLayoutManager(layoutManager);

        // set if no UI change
        mRecyclerView.setHasFixedSize(true);

        // adapter links movie data
        mMovieAdapter = new MovieAdapter(this, data, this);

        // attach adapter to recyclerView
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(Constants.SORT_BY_POPULAR);
        loadReviewData(MOVIE_ID);
        loadTrailerData(MOVIE_ID);



        mDb = MovieDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favoriteMovies) {
                Log.d(TAG, "onChanged: Updating list of movies from LiveData in ViewModel");
                if (favoriteMovies.size() > 0) {
                    favoriteData.clear();
                    favoriteData = favoriteMovies;
                }
                loadMovieData(Constants.SORT_BY_FAVORITE);
                loadReviewData(MOVIE_ID);
                loadTrailerData(MOVIE_ID);
            }
        });
    }

    public void loadReviewData(String movieId) {
        FetchReviewTask reviewTask = new FetchReviewTask();
        reviewTask.execute(movieId);
    }

    public void loadTrailerData(String movieId) {
        FetchTrailerTask trailerTask = new FetchTrailerTask();
        trailerTask.execute(movieId);
    }

    private void loadMovieData(String sortBy) {
        FetchMovieTask task = new FetchMovieTask();
        task.execute(sortBy);
        showMoviePosterData();
    }

    private static void showMoviePosterData() {
        mErrorMessageDisplay.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private static void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("Movie", clickedMovie);
        startActivity(intent);
    }

    //asyncTask
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String movie = strings[0];
            URL movieRequestUrl = createMovieUrl(movie);

            try {
                String jsonMovieResponse = NetworkUtils.makeHttpRequest(movieRequestUrl);

                data = JSONUtils.extractMovieDataFromJson(MainActivity.this, jsonMovieResponse);

                return data;
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: is not working right" + movie);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mMovieAdapter.clear();
            if (movieData != null) {
                showMoviePosterData();
                data = movieData;
                mMovieAdapter.add(movieData);
            } else {
                showErrorMessage();
                Log.d(TAG, "onPostExecute: is not working!!!!!!??");
            }
            mMovieAdapter.notifyDataSetChanged();
            super.onPostExecute(movieData);
            Log.d(TAG, "onPostExecute: something is wrong!!!!!" + movieData);
        }
    }

    //review asynctask
    public class FetchReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String review = strings[0];
            URL reviewRequestUrl = createReviewUrl(review);

            try {
                String jsonReviewResponse = NetworkUtils.makeHttpRequest(reviewRequestUrl);

                reviewData = JSONUtils.extractReviewDataFromJson(MainActivity.this, jsonReviewResponse);

                return reviewData;
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: !!!!" + reviewData);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> review) {
            rAdapter.clear();
            if (review != null) {
                reviewData = review;
                rAdapter.add(review);
            }
            rAdapter.notifyDataSetChanged();
            super.onPostExecute(review);
            Log.d(TAG, "onPostExecute: !!!!" + review);
        }
    }

    //trailer asynctask
    public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        @Override
        protected ArrayList<Trailer> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String trailer = strings[0];
            URL trailerRequestUrl = createTrailerUrl(trailer);

            try {
                String jsonTrailerResponse = NetworkUtils.makeHttpRequest(trailerRequestUrl);

                trailerData = JSONUtils.extractTrailerDataFromJson(MainActivity.this, jsonTrailerResponse);

                return trailerData;
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: !!!!" + trailerData);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailer) {
            tAdapter.clear();
            if (trailer != null) {
                trailerData = trailer;
                tAdapter.add(trailer);
            }
            tAdapter.notifyDataSetChanged();
            super.onPostExecute(trailer);
            Log.d(TAG, "onPostExecute: !!!" + trailer);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isOnline()) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular:
                loadMovieData(Constants.SORT_BY_POPULAR);
                mMovieAdapter.clear();
                Log.d(TAG, "onOptionsItemSelected: " + Constants.SORT_BY_POPULAR);
                break;
            case R.id.action_rating:
                loadMovieData(Constants.SORT_BY_RATING);
                mMovieAdapter.clear();
                Log.d(TAG, "onOptionsItemSelected: " + Constants.SORT_BY_RATING);
                break;
            case R.id.action_favorite:
                loadMovieData(Constants.SORT_BY_FAVORITE);
                mMovieAdapter.clear();
                Log.d(TAG, "onOptionsItemSelected: " + Constants.SORT_BY_FAVORITE);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // referenced https://developer.android.com/reference/android/util/DisplayMetrics
    private int numColumns() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // change this divider to adjust the size of poster
        int divider = 500;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;
        return columns;
    }

    // referenced https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }
}
