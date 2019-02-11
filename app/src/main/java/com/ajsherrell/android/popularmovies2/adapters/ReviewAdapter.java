package com.ajsherrell.android.popularmovies2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.MovieDetails;
import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.Viewholder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> reviewList;
    private MovieDetails MovieDetails;

    public ReviewAdapter(MovieDetails MovieDetails, ArrayList<Review> reviews) {
        this.MovieDetails = MovieDetails;
        this.reviewList = reviews;

        Log.d(TAG, "ReviewAdapter: !!!" + Integer.toString(reviews.size()));
    }

    @NonNull
    @Override
    public ReviewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review, viewGroup, false);
        return new ReviewAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.Viewholder viewholder, int i) {
        final Review review = reviewList.get(i);
        viewholder.mReviewAuthor.setText(review.getAuthor());
        viewholder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return this.reviewList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView mReviewAuthor;
        public TextView mReviewContent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.author);
            mReviewContent = itemView.findViewById(R.id.content);
        }
    }
}
