package com.example.moviesapp.APIResponses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> movies;
    @SerializedName("total_pages")
    private int pages;

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getPages() {
        return pages;
    }
}
