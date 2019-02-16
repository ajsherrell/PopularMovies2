package com.ajsherrell.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ajsherrell.android.popularmovies2.Constants;
import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;
    private List<Movie> movieList;

    // interface for on click messages
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie clickedMovie, Review review, Trailer trailer);

    }

    public MovieAdapter(Context context, List<Movie> movies, MovieAdapterOnClickHandler clickHandler) {
        movieList = movies;
        mClickHandler = clickHandler;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.movie_list_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(movieList.get(adapterPosition), ReviewAdapter.reviewList.get(adapterPosition), TrailerAdapter.trailerList.get(adapterPosition));
            Log.d(TAG, "onClick: was clicked!!!");
        }
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int i) {
        Movie movie = movieList.get(i);
        String POSTER_URL = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_REGULAR
                + movie.getPosterThumbnail();

        if (!TextUtils.isEmpty(POSTER_URL)) {
            Picasso.with(mContext)
                    .load(POSTER_URL.trim())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error_outline)
                    .into(viewHolder.imageView);
        }
        Log.d(TAG, "onBindViewHolder: !!!!!" + POSTER_URL);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void add(ArrayList<Movie> data) {
        this.movieList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        movieList.clear();
    }
}
