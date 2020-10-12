package com.example.moviesapp.APIservice;



import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.APIResponses.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAPIService {
    String API_KEY = "api_key";
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query(API_KEY) String apiKey, @Query("page") int page);
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query(API_KEY) String apiKey, @Query("page") int page);
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(@Query(API_KEY) String apiKey, @Query("page") int page);
    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") String id, @Query(API_KEY) String apiKey);
}
