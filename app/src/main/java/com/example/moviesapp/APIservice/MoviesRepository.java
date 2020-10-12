package com.example.moviesapp.APIservice;

import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.APIResponses.MoviesResponse;
import com.example.moviesapp.ServiceCallBacks.MoviesCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private final String apiPath = "https://api.themoviedb.org/3/";
    private final String imagesPath = "https://image.tmdb.org/t/p/original";
    private final String apiKey = "0e336daa6760a0d96293599bb8a906ad";
    private IAPIService mService;
    private static MoviesRepository mMoviesRepository = null;

    private MoviesRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiPath)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(IAPIService.class);
    }

    public static MoviesRepository getInstance() {
        if (mMoviesRepository == null)
            return new MoviesRepository();
        else
            return mMoviesRepository;
    }

    public void getPopularMovies(int page, final MoviesCallBack callBackHandler) {
        mService.getPopularMovies(apiKey, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response in not successful");
                    return;
                }
                callBackHandler.handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }

    public void getTopRatedMovies(int page, final MoviesCallBack callBackHandler) {
        mService.getTopRatedMovies(apiKey, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response in not successful");
                    return;
                }
                callBackHandler.handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }

    public void getUpcomingMovies(int page, final MoviesCallBack callBackHandler) {
        mService.getUpcomingMovies(apiKey, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response in not successful");
                    return;
                }
                callBackHandler.handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }

    public void getMovieByID(String id, final MoviesCallBack callBackHandler) {
        mService.getMovie(id, apiKey).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response in not successful");
                    return;
                }
                callBackHandler.handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

}







