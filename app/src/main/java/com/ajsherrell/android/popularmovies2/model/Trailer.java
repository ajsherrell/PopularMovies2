package com.ajsherrell.android.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String mName;
    private String mKey;
    private String mSite;
    private String mUrl;

    public Trailer(String name, String key, String site, String url) {
        this.mName = name;
        this.mKey = key;
        this.mSite = site;
        this.mUrl = url;
    }

    protected Trailer(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
        mSite = in.readString();
        mUrl = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mKey);
        dest.writeString(mSite);
        dest.writeString(mUrl);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
}
