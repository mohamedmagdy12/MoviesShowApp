package com.example.moviesapp.UI;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Movie> mMovies;
    private static final String ImageDir = "https://image.tmdb.org/t/p/w500/";
    public MoviesAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mMovies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
    public void appendMovies(List<Movie> movies){
       this.mMovies.addAll(movies);
       notifyItemRangeChanged(mMovies.size() - movies.size(),movies.size());
    }
    public boolean appendMovie(Movie movie){
        if(!this.mMovies.contains(movie)) {
            this.mMovies.add(movie);
            notifyItemRangeChanged(this.mMovies.size() - 1, 1);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.movie_view,parent,false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
       // holder.mMovieName.setText(movie.getTitle());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.override(500,500);

        synchronized (this) {
            Glide.with(mContext).load(ImageDir +movie.getPosterPath()).apply(requestOptions).into(holder.mMoviePhoto);
        }

        holder.mId = movie.getId();
    }

    @Override
    public int getItemCount() {
        return (mMovies == null ?  0 : mMovies.size());
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMoviePhoto;
        TextView mMovieName;
        Button AddToListBtn;
        Long mId;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePhoto = itemView.findViewById(R.id.movie_photo);
            itemView.setOnClickListener(this);
            //mMovieName = itemView.findViewById(R.id.movie_name);
            //AddToListBtn = itemView.findViewById(R.id.add_to_fav_list);
            /*
            AddToListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServiceConnection serviceConnection = ServiceConnection.getInstance();
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("MoviesPreferences",Context.MODE_PRIVATE);
                            String id = sharedPreferences.getString("id",null);
                            if(id != null){
                                serviceConnection.addFavMovieForUser(id, String.valueOf(mId), new FavouriteCallBack() {
                                    @Override
                                    public void onAddedSuccess() {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext,"ServiceCallBacks added!",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void displayMovies(String[] IDs) {

                                    }
                                });
                            }
                        }
                    }).start();
                }
            });

             */
        }

        @Override
        public void onClick(View view) {
           int position = this.getLayoutPosition();
           Movie movie = mMovies.get(position);
           Intent intent = new Intent(mContext,MovieActivity.class);
           intent.putExtra("movie",movie);
           mContext.startActivity(intent);
        }
    }
}
