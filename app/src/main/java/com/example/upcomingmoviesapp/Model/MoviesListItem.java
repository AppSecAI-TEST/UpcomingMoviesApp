package com.example.upcomingmoviesapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vikas.Dasade on 27/07/2017.
 */

public class MoviesListItem {

    String title,
            iv_poster,
            adult,
            releaseDate,
            id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIv_poster() {
        return iv_poster;
    }

    public void setIv_poster(String iv_poster) {
        this.iv_poster = iv_poster;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


}
