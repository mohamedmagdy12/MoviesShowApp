package com.example.moviesapp.ServiceCallBacks;

import com.example.moviesapp.UI.Review;

import java.util.List;

public interface ReviewsCallBack {
    void displayReviews(List<Review> reviews);
    void onAddedSuccess();
    void onError(Exception e);

}
