package com.ajsherrell.android.popularmovies2.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.ajsherrell.android.popularmovies2.Constants;

@Database(entities = {FavoriteMovie.class}, version = 4, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favoriteslist";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Constants.LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                Log.d(TAG, "getInstance: database!!!!!");
            }
        }
        return sInstance;
    }
    
    public abstract MovieDao movieDao();
}


