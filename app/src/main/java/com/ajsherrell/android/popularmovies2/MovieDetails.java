package com.ajsherrell.android.popularmovies2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.adapters.ReviewAdapter;
import com.ajsherrell.android.popularmovies2.adapters.TrailerAdapter;
import com.ajsherrell.android.popularmovies2.data.FavoriteMovie;
import com.ajsherrell.android.popularmovies2.data.MovieDatabase;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.ajsherrell.android.popularmovies2.utilities.AppExecutor;
import com.ajsherrell.android.popularmovies2.utilities.JSONUtils;
import com.ajsherrell.android.popularmovies2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createReviewUrl;
import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createTrailerUrl;


public class MovieDetails extends AppCompatActivity implements TrailerAdapter.OnClickListener {


    private static final String TAG = MovieDetails.class.getSimpleName();

    private Movie moviePage;
    private Review reviewPage;
    private Trailer trailerPage;

    private static ArrayList<Review> reviewData = new ArrayList<>();
    private static ArrayList<Trailer> trailerData = new ArrayList<>();
    private List<FavoriteMovie> favoriteData = new ArrayList<>();

    private static RecyclerView mReviewRecyclerView;
    private static RecyclerView mTrailerRecyclerView;


    private MovieDatabase mDb;
    private ImageView star;
    private boolean isFavorite = false;

    // movie
    private TextView id;
    private TextView originalTitle;
    private ImageView moviePoster;
    private TextView plotOverview;
    private TextView userRating;
    private TextView releaseDate;

    // reviews
    private TextView author;
    private TextView content;
    private ReviewAdapter rAdapter;

    // trailers
    private TextView trailer;
    private TrailerAdapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // finders
        star = findViewById(R.id.star);
        originalTitle = findViewById(R.id.original_title);
        moviePoster = findViewById(R.id.movie_poster_image_thumbnail);
        plotOverview = findViewById(R.id.plot_overview);
        userRating = findViewById(R.id.user_rating);
        releaseDate = findViewById(R.id.release_date);
        author = findViewById(R.id.author);
        content = findViewById(R.id.content);
        trailer = findViewById(R.id.trailerList);
        mReviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        mTrailerRecyclerView = findViewById(R.id.trailerRecyclerView);


        Intent intent = getIntent();
        if (intent != null) {
            moviePage = intent.getParcelableExtra("Movie");
            reviewPage = intent.getParcelableExtra("Review");
            trailerPage = intent.getParcelableExtra("Trailer");
        }

        mDb = MovieDatabase.getInstance(getApplicationContext());

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<FavoriteMovie> favoriteMovie = mDb.movieDao().loadMovieById(moviePage.getId());
                setFavorite((favoriteMovie != null)? true : false);
            }
        });

        // review & trailer layout
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager trailerLayoutManger = new LinearLayoutManager(this);

        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManger);

        mReviewRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setHasFixedSize(true);

        rAdapter = new ReviewAdapter(this, reviewData);
        tAdapter = new TrailerAdapter(this, trailerData, this);

        mReviewRecyclerView.setAdapter(rAdapter);
        mTrailerRecyclerView.setAdapter(tAdapter);

        loadReviewData(moviePage.getId());
        loadTrailerData(moviePage.getId());

        populateUI();
        setupViewModel();
        Log.d(TAG, "onCreate: !!!!" + moviePage + reviewPage + trailerPage);
    }

    private void setFavorite(Boolean favorite) {
        if (!favorite) {
            isFavorite = false;
            star.setImageResource(R.drawable.ic_star_border);
        } else {
            isFavorite = true;
            star.setImageResource(R.drawable.ic_star);
        }
    }

    public void populateUI() {
        if (moviePage != null) {
            if (originalTitle != null) {
                originalTitle.setText(moviePage.getOriginalTitle());
            }

            if (plotOverview != null) {
                plotOverview.setText(moviePage.getPlotOverview());
            }

            if (userRating != null) {
                userRating.setText(moviePage.getUserRating());
            }

            if (releaseDate != null) {
                releaseDate.setText(moviePage.getReleaseDate());
            }

            String poster = moviePage.getPosterThumbnail();
            final String POSTER_URL = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_THUMBNAIL + poster;
            if (poster != null) {
                Picasso.with(this)
                        .load(POSTER_URL.trim())
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_error_outline)
                        .into(moviePoster);

                Log.d(TAG, "populateUI: !!!" + POSTER_URL);
            }
        }

        // set favorite movie items
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FavoriteMovie movie = new FavoriteMovie(
                        moviePage.getId(),
                        moviePage.getOriginalTitle(),
                        moviePage.getPosterThumbnail(),
                        moviePage.getPlotOverview(),
                        moviePage.getUserRating(),
                        moviePage.getReleaseDate()
                );
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            mDb.movieDao().deleteMovie(movie);
                        } else {
                            mDb.movieDao().insertMovie(movie);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFavorite);
                            }
                        });
                    }
                });
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

    @Override
    public void onClick(Trailer clickedTrailer) {
        if (clickedTrailer != null) {
            openTrailer(clickedTrailer);
            Log.d(TAG, "onClick: openTrailer!!!!" + clickedTrailer);
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

                reviewData = JSONUtils.extractReviewDataFromJson(MovieDetails.this, jsonReviewResponse);

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
            Log.d(TAG, "onPostExecute: review!!!!" + review);
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

                trailerData = JSONUtils.extractTrailerDataFromJson(MovieDetails.this, jsonTrailerResponse);

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
            Log.d(TAG, "onPostExecute: trailer!!!" + trailer);
        }
    }

    private void setupViewModel() {
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favoriteMovies) {
                Log.d(TAG, "onChanged: Updating list of movies from LiveData in ViewModel!!!" + favoriteMovies);
                if (favoriteMovies.size() > 0) {
                    favoriteData.clear();
                    favoriteData = favoriteMovies;
                }
            }
        });
    }

    // on clicked trailer, send to youtube
    public void openTrailer(Trailer clickedTrailer) {
        String trailerKey = clickedTrailer.getKey();
        openYouTube(trailerKey);
        Log.d(TAG, "openTrailer: clickedTrailer!!!" + clickedTrailer);
    }

    private void openYouTube(String id) {
        Uri webPage = Uri.parse("http://www.youtube.com/watch?v=" + id);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
