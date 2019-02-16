package com.ajsherrell.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.MainActivity;
import com.ajsherrell.android.popularmovies2.MovieDetails;
import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.ViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    public static List<Review> reviewList;
    private Context mContext;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        reviewList = reviews;

        Log.d(TAG, "ReviewAdapter: !!!" + Integer.toString(reviews.size()));
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.activity_movie_details;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder viewHolder, int i) {
        Review review = reviewList.get(i);
        viewHolder.mReviewAuthor.setText(review.getAuthor());
        viewHolder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mReviewAuthor;
        public TextView mReviewContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.author);
            mReviewContent = itemView.findViewById(R.id.content);
        }
    }

    public void add(ArrayList<Review> data) {
        this.reviewList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        reviewList.clear();
    }
}
