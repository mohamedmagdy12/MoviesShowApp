package com.example.moviesapp.APIResponses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overView;
    @SerializedName("poster_path")
   private String posterPath;
    @SerializedName("backdrop_path")
    private String backDropPath;
    @SerializedName("vote_average")
   private Float rating;
    @SerializedName("release_date")
   private String releaseDate;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverView() {
        return overView;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public Float getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.id == ((Movie)obj).id;
    }


    public static final Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeLong(id);
         parcel.writeString(title);
         parcel.writeString(overView);
         parcel.writeString(posterPath);
         parcel.writeString(backDropPath);
         parcel.writeFloat(rating);
         parcel.writeString(releaseDate);
    }
    private Movie(Parcel parcel){
        id = parcel.readLong();
        title = parcel.readString();
        overView = parcel.readString();
        posterPath = parcel.readString();
        backDropPath = parcel.readString();
        rating = parcel.readFloat();
        releaseDate = parcel.readString();
    }
}
