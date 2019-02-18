package com.ajsherrell.android.popularmovies2.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY mId")
    LiveData<List<FavoriteMovie>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteMovie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovie movie);

    @Delete
    void deleteMovie(FavoriteMovie movie);

    @Query("SELECT * FROM movies WHERE mId = :id")
    LiveData<FavoriteMovie> loadMovieById(String id);

}
