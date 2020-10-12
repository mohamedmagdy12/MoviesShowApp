package com.example.moviesapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.APIResponses.Movie;
import com.example.moviesapp.ServiceCallBacks.FavouriteCallBack;
import com.example.moviesapp.ServiceCallBacks.ReviewsCallBack;
import com.example.moviesapp.R;
import com.example.moviesapp.ServiceConnection.ServiceConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.parser.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private ImageView mCoverPhoto;
    private ImageView mPosterPhoto;
    private TextView mTitle;
    private TextView mReleaseDate;
    private RatingBar mRatingBar;
    private TextView mOverview;
    private final String imagesPath = "https://image.tmdb.org/t/p/original";
    private Button mFavBtn;
    private Button mAddReviewBtn;
    private TextView mReviewText;
    private View.OnClickListener mAddToFavListener;
    private View.OnClickListener mDeleteListener;

    private RecyclerView mReviewsRecycler;
    private ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager mReviewsLayoutManager;
    private Movie mMovie;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_scrollable);
        mCoverPhoto = findViewById(R.id.movie_back);
        mPosterPhoto = findViewById(R.id.movie_poster);
        mTitle = findViewById(R.id.movie_name);
        mReleaseDate = findViewById(R.id.movie_date);
        mRatingBar = findViewById(R.id.movie_rate);
        mOverview = findViewById(R.id.movie_overview);
        mFavBtn = findViewById(R.id.add_to_fav_btn);

        mReviewsRecycler = findViewById(R.id.reviews_list);
        mReviewsAdapter = new ReviewsAdapter(this);
        mReviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecycler.setAdapter(mReviewsAdapter);
        mReviewsRecycler.setLayoutManager(mReviewsLayoutManager);

        displayMovie();
        displayReviews();

        mAddReviewBtn = findViewById(R.id.add_review_btn);
        mReviewText = findViewById(R.id.add_review_text);
        mAddReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = mReviewText.getText().toString();
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String time = (String.valueOf(calendar.get(Calendar.YEAR))) + "\\" +
                                (String.valueOf(calendar.get(Calendar.MONTH)))+ "\\" +
                                  (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
                 String id = prefs.getString("id", null);
                 if(id == null)
                     return;

                 Review reviewInstance = new Review();
                 reviewInstance.setReview(review);
                 reviewInstance.setUserID(id);
                 reviewInstance.setDate(time);
                 addReview(String.valueOf(mMovie.getId()),reviewInstance);
            }
        });


    }


    private void displayMovie() {
        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra("movie");
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mRatingBar.setRating(mMovie.getRating() / 2);
        mOverview.setText(mMovie.getOverView());
        Glide.with(this).load(imagesPath + mMovie.getPosterPath()).into(mPosterPhoto);
        Glide.with(this).load(imagesPath + mMovie.getBackDropPath()).into(mCoverPhoto);


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MoviesPreferences", Context.MODE_PRIVATE);
        final String id = prefs.getString("id", null);

        initializeAddListener(mMovie, id);
        initializeDeleteListener(mMovie, id);



        new Thread(new Runnable() {
            @Override
            public void run() {
                final ServiceConnection serviceConnection = ServiceConnection.getInstance();

                serviceConnection.getFavMoviesForUser(id, new FavouriteCallBack() {
                            @Override
                            public void onAddedSuccess() {

                            }

                            @Override
                            public void displayMovies(String[] IDs) {
                                boolean found = false;
                                for (String movieID : IDs) {
                                    if (String.valueOf(mMovie.getId()).equals(movieID)) {
                                        found = true;
                                        break;
                                    }
                                }

                                if (found) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                           // mFavBtn.setText("DeleteFromFav");
                                            mFavBtn.setBackgroundDrawable(getDrawable(R.drawable.ic_baseline_star_24));
                                            mFavBtn.setOnClickListener(mDeleteListener);
                                        }
                                    });
                                }else{
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mFavBtn.setBackgroundDrawable(getDrawable(R.drawable.ic_baseline_star_border_24));
                                            mFavBtn.setOnClickListener(mAddToFavListener);
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onDeletedSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        }
                );
            }
        }).start();

    }

    private void initializeDeleteListener(final Movie movie, final String id) {
        mDeleteListener = new View.OnClickListener() {
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ServiceConnection serviceConnection = ServiceConnection.getInstance();
                        serviceConnection.deleteFavMovieForUser(id, String.valueOf(movie.getId()), new FavouriteCallBack() {
                            @Override
                            public void onAddedSuccess() {

                            }

                            @Override
                            public void displayMovies(String[] IDs) {

                            }

                            @Override
                            public void onDeletedSuccess() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "done deleting", Toast.LENGTH_SHORT).show();
                                        mFavBtn.setOnClickListener(mAddToFavListener);
                                        mFavBtn.setBackgroundDrawable(getDrawable(R.drawable.ic_baseline_star_border_24));
                                    }
                                });
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }).start();
            }
        };
    }

    private void initializeAddListener(final Movie movie, final String id) {
        mAddToFavListener = new View.OnClickListener() {
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ServiceConnection serviceConnection = ServiceConnection.getInstance();
                        serviceConnection.addFavMovieForUser(id, String.valueOf(movie.getId()), new FavouriteCallBack() {
                            @Override
                            public void onAddedSuccess() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "done adding", Toast.LENGTH_SHORT).show();
                                        mFavBtn.setOnClickListener(mDeleteListener);
                                        mFavBtn.setBackgroundDrawable(getDrawable(R.drawable.ic_baseline_star_24));
                                    }
                                });
                            }

                            @Override
                            public void displayMovies(String[] IDs) {

                            }

                            @Override
                            public void onDeletedSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }

                }).start();
            }
        };
    }

    private void displayReviews() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               ServiceConnection serviceConnection = ServiceConnection.getInstance();
               try {
                   serviceConnection.getReviews(String.valueOf(mMovie.getId()), new ReviewsCallBack() {
                       @Override
                       public void displayReviews(final List<Review> reviews) {
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {
                                   mReviewsAdapter.appendReviews(reviews);
                               }
                           });
                       }

                       @Override
                       public void onAddedSuccess() {

                       }

                       @Override
                       public void onError(Exception e) {

                       }
                   });
               } catch (ParseException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }


    private void addReview(final String movieID, final Review review){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceConnection serviceConnection = ServiceConnection.getInstance();
                    serviceConnection.addReview(movieID, review, new ReviewsCallBack() {
                        @Override
                        public void displayReviews(List<Review> reviews) {

                        }

                        @Override
                        public void onAddedSuccess() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mReviewsAdapter.appendReview(review);
                                    mReviewText.setText("");
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

            }
        }).start();
    }

}