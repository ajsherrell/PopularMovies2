package com.ajsherrell.android.popularmovies2.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class FavoriteMovie {

    @PrimaryKey
    private String mId;
    private String mOriginalTitle;
    private String mPosterThumbnail;
    private String mPlotOverview;
    private String mUserRating;
    private String mReleaseDate;

    public FavoriteMovie(String id, String originalTitle, String posterThumbnail,
                         String plotOverview, String userRating, String releaseDate) {
        this.mId = id;
        this.mOriginalTitle = originalTitle;
        this.mPosterThumbnail = posterThumbnail;
        this.mPlotOverview = plotOverview;
        this. mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        this.mId = Id;
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
