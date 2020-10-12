package com.example.moviesapp.UI;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Review> mReviews;
    private static final String ImageDir = "https://image.tmdb.org/t/p/w500/";
    public ReviewsAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mReviews = new ArrayList<>();
    }

    public void appendReviews(List<Review> reviews){
        this.mReviews.addAll(reviews);
        notifyItemRangeChanged(this.mReviews.size() - reviews.size(),reviews.size());
    }

    public void appendReview(Review review){
        mReviews.add(review);
        notifyItemRangeChanged(mReviews.size() - 1,1);
    }



    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.review_view,parent,false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mReview.setText(review.getReview());
        holder.mDate.setText(review.getDate());
        holder.mUserID.setText(review.getUserID());
    }



    @Override
    public int getItemCount() {
        return (mReviews == null ?  0 : mReviews.size());
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView mUserID;
        TextView mDate;
        TextView mReview;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            mUserID = itemView.findViewById(R.id.review_user_id);
            mDate = itemView.findViewById(R.id.review_time);
            mReview = itemView.findViewById(R.id.review_text);
        }
    }
}
