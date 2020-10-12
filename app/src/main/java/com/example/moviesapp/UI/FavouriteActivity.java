package com.example.moviesapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.APIResponses.MoviesResponse;
import com.example.moviesapp.APIservice.MoviesRepository;
import com.example.moviesapp.ServiceCallBacks.MoviesCallBack;
import com.example.moviesapp.R;
import com.example.moviesapp.ServiceConnection.ServiceConnection;
import com.example.moviesapp.ServiceCallBacks.FavouriteCallBack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FavouriteActivity extends AppCompatActivity {

    private FavouriteAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoutire);
        RecyclerView favouriteRecycler = findViewById(R.id.favourite_item_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMoviesAdapter = new FavouriteAdapter(this);
        favouriteRecycler.setLayoutManager(linearLayoutManager);
        favouriteRecycler.setAdapter(mMoviesAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceConnection serviceConnection = ServiceConnection.getInstance();
                SharedPreferences sharedPreferences = getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
                String id = sharedPreferences.getString("id",null);
                if(id == null)
                    return;
                serviceConnection.getFavMoviesForUser(id, new FavouriteCallBack() {
                    @Override
                    public void onAddedSuccess() {

                    }

                    @Override
                    public void displayMovies(String[] IDs) {
                        for(String id : IDs){
                            MoviesRepository moviesRepository = MoviesRepository.getInstance();
                            moviesRepository.getMovieByID(id, new MoviesCallBack() {
                                @Override
                                public void handleResponse(Movie movie) {
                                   mMoviesAdapter.appendMovie(movie);
                                }

                                @Override
                                public void handleResponse(MoviesResponse moviesResponse) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onDeletedSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).start();
    }
}