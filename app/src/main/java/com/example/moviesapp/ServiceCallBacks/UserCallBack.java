package com.example.moviesapp.ServiceCallBacks;

public interface UserCallBack {
    void userFound();
    void noSuchUser();
    void displayPhoto(byte [] bytes);
    void onAddedSuccess();
    void onAddedFailed();
    void onError(Exception ex);
}
