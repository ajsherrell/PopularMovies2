package com.ajsherrell.android.popularmovies2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ajsherrell.android.popularmovies2.data.FavoriteMovie;
import com.ajsherrell.android.popularmovies2.data.MovieDatabase;

import java.util.List;

// Referenced code from Udacity's https://github.com/ajsherrell/ud851-Exercises/blob
// /student/Lesson09b-ToDo-List-AAC/T09b.10-Solution-AddViewModelToAddTaskActivity
// /app/src/main/java/com/example/android/todolist/MainViewModel.java

public class ViewModel extends AndroidViewModel {

    private static final String TAG = ViewModel.class.getSimpleName();

    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public ViewModel(@NonNull Application application, String movieId) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        favoriteMovies = database.movieDao().loadAllMovies();
        Log.d(TAG, "ViewModel: !!!!" + database);
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
