package com.ajsherrell.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter
        <TrailerAdapter.Viewholder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Trailer> trailerList = new ArrayList<>();
    private final TrailerAdapterOnClickHandler mClickHandler;

    // interface for on click
    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer clickedTrailer);
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerData, TrailerAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.trailerList = trailerData;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.Viewholder viewholder, int i) {
// todo finish from here
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
