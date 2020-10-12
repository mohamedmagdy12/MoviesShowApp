package com.example.moviesapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.ServiceCallBacks.UserCallBack;
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
import com.example.moviesapp.ServiceConnection.ServiceConnection;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        final EditText userIDText = findViewById(R.id.user_id_text);
        final EditText passText = findViewById(R.id.pass_log_in);
        Button button = findViewById(R.id.log_in_btn);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
        final String id = prefs.getString("id",null);
        if(id != null){
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userIDText.getText().toString().length() > 3 && passText.getText().toString().length() > 7) {
                    final String userId = userIDText.getText().toString();
                    final String password = passText.getText().toString();
                    // put user to service
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServiceConnection serviceConnection = ServiceConnection.getInstance();
                            try {
                                serviceConnection.getUser(userId, password, new UserCallBack() {
                                    @Override
                                    public void userFound() {
                                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
                                        prefs.edit().putString("id",userId).commit();
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void noSuchUser() {
                                       new Handler(Looper.getMainLooper()).post(new Runnable() {
                                           @Override
                                           public void run() {
                                               Toast.makeText(getBaseContext(),"Error in name or password",Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                    }

                                    @Override
                                    public void displayPhoto(byte[] bytes) {

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
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

        final Button register = findViewById(R.id.go_to_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}