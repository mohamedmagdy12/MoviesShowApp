package com.example.moviesapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.ServiceCallBacks.UserCallBack;
import com.example.moviesapp.R;
import com.example.moviesapp.ServiceConnection.ServiceConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private Button mAddPhotoBtn;
    private TextView mName;
    private ImageView mPhoto;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAddPhotoBtn = findViewById(R.id.add_photo_btn);
        mName = findViewById(R.id.profile_name);
        mPhoto = findViewById(R.id.profile_image);
       mAddPhotoBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
           }
       });

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
        final String id = prefs.getString("id",null);
        if(id != null)mName.setText(id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceConnection serviceConnection = ServiceConnection.getInstance();
                serviceConnection.getPhoto(id, new UserCallBack() {


                    @Override
                    public void userFound() {

                    }

                    @Override
                    public void noSuchUser() {

                    }

                    @Override
                    public void displayPhoto(byte[] bytes) {
                       if(bytes != null) {
                           final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {
                                   mPhoto.setImageBitmap(bitmap);
                               }
                           });
                       }else{
                           // temp photo
                       }
                    }

                    @Override
                    public void onAddedSuccess() {

                    }

                    @Override
                    public void onAddedFailed() {

                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                });
            }
        }).start();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            mBitmap = null;
            Uri selectedImage = data.getData();
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPhoto.setImageBitmap(mBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(mBitmap == null)
                        return;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    mBitmap.recycle();
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
                    String id = prefs.getString("id",null);
                    if(id == null)
                        return;

                    ServiceConnection serviceConnection = ServiceConnection.getInstance();
                    serviceConnection.addPhoto(id, byteArray, new UserCallBack() {

                        @Override
                        public void userFound() {

                        }

                        @Override
                        public void noSuchUser() {

                        }

                        @Override
                        public void displayPhoto(byte[] bytes) {

                        }

                        @Override
                        public void onAddedSuccess() {
                          new Handler(Looper.getMainLooper()).post(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(getBaseContext(),"done adding photo",Toast.LENGTH_SHORT).show();
                              }
                          });
                        }

                        @Override
                        public void onAddedFailed() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(),"failed adding photo",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    });
                }
            }).start();
        }
    }
}