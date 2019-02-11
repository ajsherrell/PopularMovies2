package com.ajsherrell.android.popularmovies2;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.adapters.TrailerAdapter;
import com.ajsherrell.android.popularmovies2.data.FavoriteMovie;
import com.ajsherrell.android.popularmovies2.data.MovieDatabase;
import com.ajsherrell.android.popularmovies2.databinding.ActivityMovieDetailsBinding;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.ajsherrell.android.popularmovies2.utilities.AppExecutor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ajsherrell.android.popularmovies2.R.layout.activity_movie_details;

public class MovieDetails extends AppCompatActivity {

    ActivityMovieDetailsBinding mBinding;

    private static final String TAG = MovieDetails.class.getSimpleName();

    private Movie moviePage;
    private ArrayList<Review> reviews;
    private ArrayList<Trailer> trailers;

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

    // trailers
    private ListView trailer;
    private TrailerAdapter tAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_movie_details);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent != null) {
            moviePage = intent.getParcelableExtra("Movie");
            reviews = intent.getParcelableArrayListExtra("Review");
            trailers = intent.getParcelableArrayListExtra("Trailer");
        }

        mDb = MovieDatabase.getInstance(getApplicationContext());

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<FavoriteMovie> favoriteMovie = mDb.movieDao().loadMovieById(Integer.parseInt(String.valueOf(moviePage.getId())));
                setFavorite((favoriteMovie != null)? true : false);
            }
        });

        populateUI();
        Log.d(TAG, "onCreate: !!!!" + moviePage + reviews + trailers);
    }

    private void setFavorite(Boolean favorite) {
        if (favorite) {
            isFavortie = true;
            mBinding.star.setImageResource(R.drawable.ic_star);
        } else {
            isFavortie = false;
            mBinding.star.setImageResource(R.drawable.ic_star_border);
        }
    }

    public void populateUI() {
        if (moviePage != null && reviews != null && trailers != null) {
            if (originalTitle != null) {
                mBinding.originalTitle.setText(moviePage.getOriginalTitle());
            }

            if (plotOverview != null) {
                mBinding.plotOverview.setText(moviePage.getPlotOverview());
            }

            if (userRating != null) {
                mBinding.userRating.setText(moviePage.getUserRating());
            }

            if (releaseDate != null) {
                mBinding.releaseDate.setText(moviePage.getReleaseDate());
            }

            if (author != null) {
                author.findViewById(R.id.author);
                author.setText(reviews.getAuthor());
            }

            if (content != null) {
                content.findViewById(R.id.content);
                content.setText(reviews.getContent());
            }

            if (trailer != null) {
                trailer.findViewById(R.id.trailerListView);
                trailer.setAdapter(tAdapter);
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

}
