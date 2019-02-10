package com.ajsherrell.android.popularmovies2.utilities;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.ajsherrell.android.popularmovies2.Constants;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// referenced code from Udacity's Toy App https://github.com/ajsherrell/ud851-Exercises
// /blob/student/Lesson09b-ToDo-List-AAC/T09b.10-Solution-AddViewModelToAddTaskActivity
// /app/src/main/java/com/example/android/todolist/AppExecutors.java

public class AppExecutor {

    // For Singleton instantiation
    private static AppExecutor sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private AppExecutor(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance() {
        if (sInstance == null) {
            synchronized (Constants.LOCK) {
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
