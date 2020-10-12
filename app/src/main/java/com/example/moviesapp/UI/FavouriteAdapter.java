package com.example.moviesapp.UI;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Movie> mMovies;
    private static final String ImageDir = "https://image.tmdb.org/t/p/w500/";
    public FavouriteAdapter(Context context) {
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
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.movie_fav_view,parent,false);
        return new FavHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mMovieName.setText(movie.getTitle());
        holder.mMovieDate.setText(movie.getReleaseDate());
        synchronized (this) {
            Glide.with(mContext).load(ImageDir +movie.getPosterPath()).into(holder.mMoviePhoto);
        }

        holder.mId = movie.getId();
    }



    @Override
    public int getItemCount() {
        return (mMovies == null ?  0 : mMovies.size());
    }

    class FavHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMoviePhoto;
        TextView mMovieName;
        TextView mMovieDate;
        Long mId;
        public FavHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePhoto = itemView.findViewById(R.id.fav_photo);
            mMovieName = itemView.findViewById(R.id.fav_name);
            mMovieDate = itemView.findViewById(R.id.fav_date);
            itemView.setOnClickListener(this);
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
