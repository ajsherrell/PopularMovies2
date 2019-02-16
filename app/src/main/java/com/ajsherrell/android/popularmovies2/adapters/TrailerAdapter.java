package com.ajsherrell.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajsherrell.android.popularmovies2.MovieDetails;
import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Trailer;

import java.util.ArrayList;
import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter
        <TrailerAdapter.ViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<Trailer> trailerList;
    private MovieDetails MovieDetails;
    private OnClickListener mOnClickListener;


    // interface for clicklistener
    public interface OnClickListener {
        void onClick(Trailer clickedTrailer);
    }

    public TrailerAdapter(MovieDetails MovieDetails, ArrayList<Trailer> trailers, OnClickListener onClickListener) {
        this.MovieDetails = MovieDetails;
        this.trailerList = trailers;
        this.mOnClickListener = onClickListener;

        Log.d(TAG, "TrailerAdapter: !!!" + Integer.toString(trailers.size()));
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder viewHolder, int i) {
        Trailer trailer = trailerList.get(i);
        viewHolder.trailerListView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnClickListener {
        public TextView trailerListView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerListView.findViewById(R.id.trailerListView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(Trailer clickedTrailer) {

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onClick(trailerList.get(adapterPosition));
            Log.d(TAG, "onClick: !!!!" + v);
        }
    }

    public void add(ArrayList<Trailer> data) {
        this.trailerList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        trailerList.clear();
    }
}
