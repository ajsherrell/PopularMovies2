package com.ajsherrell.android.popularmovies2;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.adapters.ReviewAdapter;
import com.ajsherrell.android.popularmovies2.adapters.TrailerAdapter;
import com.ajsherrell.android.popularmovies2.data.FavoriteMovie;
import com.ajsherrell.android.popularmovies2.data.MovieDatabase;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.ajsherrell.android.popularmovies2.utilities.AppExecutor;
import com.squareup.picasso.Picasso;


import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createTrailerUrl;

import static com.ajsherrell.android.popularmovies2.R.layout.activity_movie_details;
import static java.lang.String.valueOf;

public class MovieDetails extends AppCompatActivity {


    private static final String TAG = MovieDetails.class.getSimpleName();

    private Movie moviePage;
    private Review reviewPage;
    private Trailer trailerPage;


    private MovieDatabase mDb;
    private ImageView star;
    private boolean isFavortie = false;

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
    private Button trailer;
    private TrailerAdapter tAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_movie_details);

        // finders
        star = findViewById(R.id.star);
        originalTitle = findViewById(R.id.original_title);
        moviePoster = findViewById(R.id.movie_poster_image_thumbnail);
        plotOverview = findViewById(R.id.plot_overview);
        userRating = findViewById(R.id.user_rating);
        releaseDate = findViewById(R.id.release_date);
        author = findViewById(R.id.author);
        content = findViewById(R.id.content);
        trailer = findViewById(R.id.trailerListView);

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
                final LiveData<FavoriteMovie> favoriteMovie = mDb.movieDao().loadMovieById(Integer.parseInt(valueOf(moviePage.getId())));
                setFavorite((favoriteMovie != null)? true : false);
            }
        });

        populateUI();
        Log.d(TAG, "onCreate: !!!!" + moviePage + reviewPage + trailerPage);
    }

    private void setFavorite(Boolean favorite) {
        if (favorite) {
            isFavortie = true;
            star.setImageResource(R.drawable.ic_star);
        } else {
            isFavortie = false;
            star.setImageResource(R.drawable.ic_star_border);
        }
    }

    public void populateUI() {
        if (moviePage != null && reviewPage != null && trailerPage != null) {
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

            if (author != null) {
                author.setText(reviewPage.getAuthor());
            }

            if (content != null) {
                content.setText(reviewPage.getContent());
            }

            if (trailer != null) {
                 openTrailer(trailer);
            }
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

    // on clicked trailer, send to youtube
    public void openTrailer(View view) {
        String urlAsString = String.valueOf((createTrailerUrl(Constants.MOVIE_ID)));
        openYouTube(urlAsString);
    }

    private void openYouTube(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
