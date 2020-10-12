package com.example.moviesapp.ServiceCallBacks;


import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.APIResponses.MoviesResponse;


public interface MoviesCallBack {
    void handleResponse(Movie movie);
    void handleResponse(MoviesResponse moviesResponse);
}
