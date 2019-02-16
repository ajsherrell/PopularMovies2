package com.ajsherrell.android.popularmovies2.utilities;

import android.net.Uri;
import android.util.Log;

import com.ajsherrell.android.popularmovies2.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // private constructor
    private NetworkUtils() {}

    //create the Movie url
    public static URL createMovieUrl(String sortBy) {
        // build the URI
        Uri builtUri = Uri.parse(Constants.MOVIE_DATABASE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(Constants.API_KEY, Constants.MY_API_KEY)
                .appendQueryParameter(Constants.LANGUAGE, Constants.MY_LANGUAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "createMovieUrl: Problem building the URL!!!!");
        }
        return url;
    }

    //create the Review url
    public static URL createReviewUrl(String movieId) {
        // build the URI
        Uri builtUri = Uri.parse(Constants.MOVIE_DATABASE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(Constants.REVIEW)
                .appendQueryParameter(Constants.API_KEY, Constants.MY_API_KEY)
                .appendQueryParameter(Constants.LANGUAGE, Constants.MY_LANGUAGE)
                .appendQueryParameter(Constants.PAGE, "1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "createReviewUrl: Problem building the URL!!!!");
        }
        return url;
    }

    //create the Trailer url
    public static URL createTrailerUrl(String movieId) {
        // build the URI
        Uri builtUri = Uri.parse(Constants.MOVIE_DATABASE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(Constants.VIDEO)
                .appendQueryParameter(Constants.API_KEY, Constants.MY_API_KEY)
                .appendQueryParameter(Constants.LANGUAGE, Constants.MY_LANGUAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "createTrailerUrl: Problem building the URL!!!!");
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
