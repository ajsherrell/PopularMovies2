package com.ajsherrell.android.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

// used code from the Udacity AndroidFlavor example https://github.com/udacity/android-custom-arrayadapter/blob/parcelable/app/src/main/java/demo/example/com/customarrayadapter/AndroidFlavor.java
public class Movie implements Parcelable {

    private static String mId;
    private String mOriginalTitle;
    private String mPosterThumbnail;
    private String mPlotOverview;
    private String mUserRating;
    private String mReleaseDate;

    public Movie(String id, String originalTitle, String posterThumbnail, String plotOverview, String userRating, String releaseDate) {
        this.mId = id;
        this.mOriginalTitle = originalTitle;
        this.mPosterThumbnail = posterThumbnail;
        this.mPlotOverview = plotOverview;
        this. mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    public Movie(Parcel in) {
        mId = in.readString();
        mOriginalTitle = in.readString();
        mPosterThumbnail = in.readString();
        mPlotOverview = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPosterThumbnail);
        parcel.writeString(mPlotOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public static String getId() {
        return mId;
    }

    public String setId(String id) {
       return this.mId = id;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String OriginalTitle) {
        this.mOriginalTitle = OriginalTitle;
    }

    public String getPosterThumbnail() {
        return mPosterThumbnail;
    }

    public void setPosterThumbnail(String PosterThumbnail) {
        this.mPosterThumbnail = PosterThumbnail;
    }

    public String getPlotOverview() {
        return mPlotOverview;
    }

    public void setPlotOverview(String PlotOverview) {
        this.mPlotOverview = PlotOverview;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String UserRating) {
        this.mUserRating = UserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String ReleaseDate) {
        this.mReleaseDate = ReleaseDate;
    }
}
