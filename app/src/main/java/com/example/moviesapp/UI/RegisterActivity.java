package com.example.moviesapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.ServiceCallBacks.UserCallBack;
import com.example.moviesapp.ServiceConnection.ServiceConnection;
import com.example.moviesapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText userIDText = findViewById(R.id.name_register);
        final EditText passText = findViewById(R.id.pass_register);
        Button button = findViewById(R.id.register_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userIDText.getText().toString().length() > 3 && passText.getText().toString().length() > 7) {
                    final String userId = userIDText.getText().toString();
                    final String password = passText.getText().toString();
                    // put ServiceCallBacks to service
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServiceConnection serviceConnection = ServiceConnection.getInstance();
                                serviceConnection.addUser(userId, password, new UserCallBack() {
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
                                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
                                        prefs.edit().putString("id",userId).commit();
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }});
                                    }

                                    @Override
                                    public void onAddedFailed() {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(),"Found ServiceCallBacks with same name",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Exception ex) {
                                        
                                    }
                                });
                            }

                    }).start();

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"short username or password",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}