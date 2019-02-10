package com.ajsherrell.android.popularmovies2.utilities;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ajsherrell.android.popularmovies2.Constants;
import com.ajsherrell.android.popularmovies2.model.Movie;
import com.ajsherrell.android.popularmovies2.model.Review;
import com.ajsherrell.android.popularmovies2.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    // private constructor
    private JSONUtils() {}

    // Extract Movie JSON
    public static ArrayList<Movie> extractMovieDataFromJson(Context context, String moviesJSON) {

        // if the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        //create an empty ArrayList to add movies to
        ArrayList<Movie> data = new ArrayList<>();
        String[] movies = null;

        try {
            //create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);

            // extract the array "results" key from a JSONObject
            JSONArray jsonResultsArray = baseJsonResponse.getJSONArray(Constants.RESULTS);

            movies = new String[jsonResultsArray.length()];

            // for each movie in the jsonResultsArray, create and
            // {@link Movie} object
            for (int i = 0; i < jsonResultsArray.length(); i++) {


                // get single movie at position 1 in array list
                JSONObject currentMovie = jsonResultsArray.getJSONObject(i);

                // extract the value for the key called "id"
                int id = currentMovie.getInt("id");

                // extract the value for the key called "original_title"
                String originalTitle = currentMovie.getString("original_title");

                // extract the value for the key called "poster_path"
                String posterThumbnail = currentMovie.getString("poster_path");

                // extract the value for the key called "overview"
                String plotOverview = currentMovie.getString("overview");

                // extract the value for the key called "vote_count"
                String userRating = currentMovie.getString("vote_average");

                // extract the value for the key called "release_date"
                String releaseDate = currentMovie.getString("release_date");

                // create a new {@link Movie} object with the JSON response
                Movie moviesList = new Movie(id, originalTitle, posterThumbnail,
                        plotOverview, userRating, releaseDate);

                data.add(moviesList);

                // add the new {@link Movie} to the list of movies
                movies[i] = String.valueOf(new ArrayList<Movie>());
                Log.d(TAG, "extractDataFromJson: movies:" + moviesList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "extractDataFromJson: problem with parsing movie JSON!!!!!", e);
        }
        // return list of movie data
        return data;
    }

    // Extract JSON Review
    public static ArrayList<Review> extractReviewDataFromJson(Context context, String reviewsJSON){
        // add return early statement for null/empty
        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }

        // create an empty arraylist to add reviews to
        ArrayList<Review> data = new ArrayList<>();
        String[] reviews = null;

        try {
            //create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(reviewsJSON);

            // extract the array "results" from the JSON Object
            JSONArray jsonResultsArray = baseJsonResponse.getJSONArray(Constants.RESULTS);

            reviews = new String[jsonResultsArray.length()];

            // for each review in the jsonResultArray, create a
            //{@link Review} object
            for (int i = 0; i < jsonResultsArray.length(); i++) {

                // get single review at position 1 in the arraylist
                JSONObject currentReview = jsonResultsArray.getJSONObject(i);

                // extract the value for the key called "id"
                int id = currentReview.getInt("id");
                // extract the value for the key called "author"
                String author = currentReview.getString("author");
                // extract the value for the key called "content"
                String content = currentReview.getString("content");
                // extract the value for the key called "url"
                String url = currentReview.getString("url");

                //create a new {@link Review} object with the JSON response
                Review reviewList = new Review(id, author, content, url);

                data.add(reviewList);

                // add the new {@link Review} to the list of reviews
                reviews[i] = String.valueOf(new ArrayList<Review>());
                Log.d(TAG, "extractReviewDataFromJson: " + reviewList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "extractReviewDataFromJson: problem parsing JSON reviews!!!!", e);
        }
        // return list of movie data
        return data;
    }

    // Extract JSON Trailer
    public static ArrayList<Trailer> extractTrailerDataFromJson(Context context, String trailersJSON){
        // add return early statement for null/empty
        if (TextUtils.isEmpty(trailersJSON)) {
            return null;
        }

        // create an empty arraylist to add trailers to
        ArrayList<Trailer> data = new ArrayList<>();
        String[] trailers = null;

        try {
            //create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(trailersJSON);

            // extract the array "results" from the JSON Object
            JSONArray jsonResultsArray = baseJsonResponse.getJSONArray(Constants.RESULTS);

            trailers = new String[jsonResultsArray.length()];

            // for each trailer in the jsonResultArray, create a
            //{@link Review} object
            for (int i = 0; i < jsonResultsArray.length(); i++) {

                // get single trailer at position 1 in the arraylist
                JSONObject currentTrailer = jsonResultsArray.getJSONObject(i);

                // extract the value for the key called "id"
                String name = currentTrailer.getString("name");
                // extract the value for the key called "author"
                String key = currentTrailer.getString("key");
                // extract the value for the key called "content"
                String site = currentTrailer.getString("site");
                // todo: make sure I don't need to do something with "url"
                //create a new {@link Review} object with the JSON response
                Trailer trailerList = new Trailer(name, key, site);

                data.add(trailerList);

                // add the new {@link Review} to the list of trailers
                trailers[i] = String.valueOf(new ArrayList<Trailer>());
                Log.d(TAG, "extractReviewDataFromJson: " + trailerList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "extractTrailerDataFromJson: problem parsing JSON reviews!!!!", e);
        }
        // return list of movie data
        return data;
    }

}
