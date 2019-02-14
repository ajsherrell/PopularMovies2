package com.ajsherrell.android.popularmovies2;

import com.ajsherrell.android.popularmovies2.model.Movie;

public class Constants {

    // my api key
    public static final String MY_API_KEY = BuildConfig.API_KEY;
    //<<<<<<<<<<<<<<<<<<< get your own api key >>>>>>>>>>>>>>>>
    public static final String API_KEY = "api_key";

    // urls from themoviedb.org
    public static final String MOVIE_DATABASE_BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    // query strings
    public static final String LANGUAGE = "language";
    public static final String MY_LANGUAGE = "en-US";
    public static final String SORT_BY_POPULAR = "popular";
    public static final String SORT_BY_RATING = "top_rated";
    public static final String SORT_BY_FAVORITE = "favorite";
    public static final String RESULTS = "results";
    public static final String PAGE = "page";
    public static final String REVIEW = "reviews";
    public static final String VIDEO = "videos";

    // poster size
    public static final String POSTER_SIZE_THUMBNAIL = "w185/";
    public static final String POSTER_SIZE_REGULAR = "w500/";

    // movie ID
    public static final String MOVIE_ID = Movie.getId();

    //database strings
    public static final Object LOCK = new Object();

}
