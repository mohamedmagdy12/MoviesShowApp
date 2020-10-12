package com.example.moviesapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.APIResponses.MoviesResponse;
import com.example.moviesapp.APIservice.MoviesRepository;
import com.example.moviesapp.R;
import com.example.moviesapp.ServiceCallBacks.MoviesCallBack;


public class MainActivity extends AppCompatActivity {

    private MoviesAdapter mPopularAdapter;
    private MoviesAdapter mTopRatedAdapter;
    private MoviesAdapter mUpcomingAdapter;
    private RecyclerView mPopularRecycler;
    private RecyclerView mTopRatedRecycler;
    private RecyclerView mUpcomingRecycler;
    private LinearLayoutManager mPopularLayoutManager;
    private LinearLayoutManager mTopRatedLayoutManager;
    private LinearLayoutManager mUpcomingLayoutManager;
    private Button mProfileBtn;
    private int mPopularPageNum = 1;
    private int mTopRatedPageNum = 1;
    private int mUpcomingPageNum = 1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPopularRecycler = findViewById(R.id.popular_list);
        mPopularAdapter = new MoviesAdapter(this);
        mPopularLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mPopularRecycler.setAdapter(mPopularAdapter);
        mPopularRecycler.setLayoutManager(mPopularLayoutManager);

        mTopRatedRecycler = findViewById(R.id.top_rated_list);
        mTopRatedAdapter = new MoviesAdapter(this);
        mTopRatedLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mTopRatedRecycler.setAdapter(mTopRatedAdapter);
        mTopRatedRecycler.setLayoutManager(mTopRatedLayoutManager);

        mUpcomingRecycler = findViewById(R.id.upcoming_list);
        mUpcomingAdapter = new MoviesAdapter(this);
        mUpcomingLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mUpcomingRecycler.setAdapter(mUpcomingAdapter);
        mUpcomingRecycler.setLayoutManager(mUpcomingLayoutManager);
        getPopularMovies();
        getTopMovies();
        getUpcomingMovies();



    }


    private void getPopularMovies() {
        final MoviesRepository moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getPopularMovies(mPopularPageNum, new MoviesCallBack() {
            @Override
            public void handleResponse(Movie movie) {

            }

            @Override
            public void handleResponse(MoviesResponse moviesResponse) {
                mPopularAdapter.appendMovies(moviesResponse.getMovies());
                attachOnScrollListenerForPopular();
                Log.i("pages tracker","Added" + mPopularPageNum + "page POP");
            }


        });

    }
    private void attachOnScrollListenerForPopular(){
        mPopularRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemsCnt = mPopularLayoutManager.getItemCount();
                int visibleItemsCnt = mPopularLayoutManager.getChildCount();
                int firstItemVisiblePosition = mPopularLayoutManager.findFirstVisibleItemPosition();

                if(firstItemVisiblePosition + visibleItemsCnt >= totalItemsCnt /2){
                    mPopularRecycler.removeOnScrollListener(this);
                    mPopularPageNum++;
                    getPopularMovies();
                }
            }

        });
    }

    private void getTopMovies() {
        final MoviesRepository moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getTopRatedMovies(mTopRatedPageNum, new MoviesCallBack() {
            @Override
            public void handleResponse(Movie movie) {

            }

            @Override
            public void handleResponse(MoviesResponse moviesResponse) {
                mTopRatedAdapter.appendMovies(moviesResponse.getMovies());
                attachOnScrollListenerForTopRated();
                Log.i("pages tracker","Added" + mTopRatedPageNum + "page TOP");
            }


        });

    }
    private void attachOnScrollListenerForTopRated(){
        mTopRatedRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemsCnt = mTopRatedLayoutManager.getItemCount();
                int visibleItemsCnt = mTopRatedLayoutManager.getChildCount();
                int firstItemVisiblePosition = mTopRatedLayoutManager.findFirstVisibleItemPosition();

                if(firstItemVisiblePosition + visibleItemsCnt >= totalItemsCnt /2){
                    mTopRatedRecycler.removeOnScrollListener(this);
                    mTopRatedPageNum++;
                    getTopMovies();
                }
            }

        });
    }

    private void getUpcomingMovies() {
        final MoviesRepository moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getUpcomingMovies(mUpcomingPageNum, new MoviesCallBack() {
            @Override
            public void handleResponse(Movie movie) {

            }

            @Override
            public void handleResponse(MoviesResponse moviesResponse) {
                mUpcomingAdapter.appendMovies(moviesResponse.getMovies());
                attachOnScrollListenerForUpcoming();
                Log.i("pages tracker","Added" + mUpcomingPageNum + "page UP");
            }


        });
    }
    private void attachOnScrollListenerForUpcoming(){
        mUpcomingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemsCnt = mUpcomingLayoutManager.getItemCount();
                int visibleItemsCnt = mUpcomingLayoutManager.getChildCount();
                int firstItemVisiblePosition = mUpcomingLayoutManager.findFirstVisibleItemPosition();

                if(firstItemVisiblePosition + visibleItemsCnt >= totalItemsCnt /2){
                    mUpcomingRecycler.removeOnScrollListener(this);
                    mUpcomingPageNum++;
                    getUpcomingMovies();
                }
            }

        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_view_favuorite){
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_view_profile){
            Intent intent = new Intent(getBaseContext(),ProfileActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_log_out){
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
             prefs.edit().remove("id").commit();
            Intent intent = new Intent(getBaseContext(),LogInActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}