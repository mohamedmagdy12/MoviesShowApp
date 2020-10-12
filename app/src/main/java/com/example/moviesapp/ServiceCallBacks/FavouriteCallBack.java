package com.example.moviesapp.ServiceCallBacks;

import java.io.IOException;

public interface FavouriteCallBack {
    public void onAddedSuccess();
    public void displayMovies(String [] IDs);
    public void onDeletedSuccess();
    void onError(Exception e);
}
