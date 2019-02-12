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
import com.ajsherrell.android.popularmovies2.model.Trailer;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter
        <TrailerAdapter.Viewholder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private ArrayList<Trailer> trailerList;
    private MovieDetails MovieDetails;

    public TrailerAdapter(MovieDetails MovieDetails, ArrayList<Trailer> trailers) {
        this.MovieDetails = MovieDetails;
        this.trailerList = trailers;

        Log.d(TAG, "TrailerAdapter: !!!" + Integer.toString(trailers.size()));
    }

    @NonNull
    @Override
    public TrailerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer, viewGroup, false);
        return new TrailerAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.Viewholder viewholder, int i) {
        final Trailer trailer = trailerList.get(i);
        viewholder.trailerListView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView trailerListView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            trailerListView.findViewById(R.id.trailerListView);
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
