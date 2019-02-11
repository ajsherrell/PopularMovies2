package com.ajsherrell.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ajsherrell.android.popularmovies2.Constants;
import com.ajsherrell.android.popularmovies2.R;
import com.ajsherrell.android.popularmovies2.model.Trailer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static com.ajsherrell.android.popularmovies2.utilities.NetworkUtils.createTrailerUrl;

public abstract class TrailerAdapter extends RecyclerView.Adapter
        <TrailerAdapter.Viewholder> implements ListAdapter {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Trailer> trailerList;
    private final TrailerAdapterOnClickHandler mClickHandler;
    private ListView mTrailerListView;

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
        Trailer trailer = trailerList.get(i);
        String TRAILER_URL = String.valueOf(createTrailerUrl(String.valueOf(Constants.MOVIE_ID)));
        Log.d(TAG, "onBindViewHolder: !!!" + trailer);

        if (!TextUtils.isEmpty(TRAILER_URL)) {
            Picasso.with(mContext)
                    .load(TRAILER_URL.trim())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error_outline)
                    .into((Target) viewholder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mTrailerListView = itemView.findViewById(R.id.trailerListView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(trailerList.get(adapterPosition));
            Log.d(TAG, "onClick: !!!!");
        }
    }

    public void add(ArrayList<Trailer> trailerData) {
        this.trailerList = trailerData;
        notifyDataSetChanged();
    }

    public void clear() {
        trailerList.clear();
    }
}
