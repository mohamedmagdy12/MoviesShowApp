package com.example.moviesapp.ServiceConnection;



import com.example.moviesapp.ServiceCallBacks.FavouriteCallBack;
import com.example.moviesapp.ServiceCallBacks.ReviewsCallBack;
import com.example.moviesapp.ServiceCallBacks.UserCallBack;
import com.example.moviesapp.UI.Review;


import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class ServiceConnection {
   private static final String BASE_URL = "http://192.168.0.106:8080/JavaAPI/services/service/";
   private static ServiceConnection sServiceConnection = null;
     private ServiceConnection(){};


     public static ServiceConnection getInstance(){
         if(sServiceConnection == null)
             return new ServiceConnection();
         else
             return sServiceConnection;
     }

     public synchronized void addUser ( String id,String password, UserCallBack userCallBack){
        String url = BASE_URL + "addUser?";
        String parameters = "id=" + id  + "&pass=" + password ;
         String[] result = new String[0];
         try {
             result = openPostConnection(url, parameters);
             if (result[0].equals("200")) {
                 userCallBack.onAddedSuccess();
             } else if(result[0].equals("409")){
                 userCallBack.onAddedFailed();
             }
         } catch (IOException e) {
             userCallBack.onError(e);
         }
      }

      public synchronized void getUser (String id,String password , UserCallBack userCallBack) throws ParseException, JSONException, ParseException {
        String url = BASE_URL + "getUser?";
        String parameters = "id=" + id + "&pass=" + password;
        url += parameters;
          String[] result = new String[0];
          try {
              result = openGetConnection(url);
          } catch (IOException e) {
              userCallBack.onError(e);
              return;
          }
            if (result[0].equals("200")) {
                userCallBack.userFound();
            } else if(result[0].equals("404")){
                userCallBack.noSuchUser();
            }
      }

      public synchronized void addPhoto (String id , byte[] bytes,UserCallBack callBack){
         String encodedImage = Base64.getEncoder().encodeToString(bytes);
          String url = BASE_URL + "addPhoto?";
          String parameters = "id=" + id + "&image=" + encodedImage;
          String [] result = new String[0];
          try {
              result = openPostConnection(url,parameters);
          } catch (IOException e) {
              callBack.onError(e);
              return;
          }
              if(result[0].equals("200")){
                 callBack.onAddedSuccess();
              }else{
                  callBack.onAddedFailed();
              }

     }

        public synchronized void getPhoto (String id, UserCallBack userCallBack) {
            String url = BASE_URL + "getPhoto?";
            String parameters = "id=" + id;
            url += parameters;
            String[] result = new String[0];
            try {
                result = openGetConnection(url);
            } catch (IOException e) {
                userCallBack.onError(e);
                return;
            }
            if (result[0].equals("200")) {
                byte[] bytes = Base64.getDecoder().decode(result[1]);
                userCallBack.displayPhoto(bytes);
            } else if (result[0].equals("204")) {
                userCallBack.displayPhoto(null);
            }
        }

        public synchronized void addFavMovieForUser(String id , String movieID, FavouriteCallBack callBcak){
         String url = BASE_URL + "addUserMovie?";
            String parameters = "id=" + id + "&movie=" + movieID;
            String [] result = new String[0];
            try {
                result = openPostConnection(url,parameters);
            } catch (IOException e) {
               callBcak.onError(e);
               return;
            }

            callBcak.onAddedSuccess();
        }

    public synchronized void deleteFavMovieForUser(String id , String movieID,FavouriteCallBack callBack){
        String url = BASE_URL + "deleteUserMovie?";
        String parameters = "id=" + id + "&movie=" + movieID;
        String [] result = new String[0];
        try {
            result = openPostConnection(url,parameters);
        } catch (IOException e) {
          callBack.onError(e);
          return;
        }

        callBack.onDeletedSuccess();
    }

        public synchronized void getFavMoviesForUser(String id, FavouriteCallBack favouriteCallBack){
            String url = BASE_URL + "getUserMovies?";
            String parameters = "id=" + id;
            url += parameters;
            String [] result = new String[0];
            try {
                result = openGetConnection(url);
            } catch (IOException e) {
                favouriteCallBack.onError(e);
                return;
            }

            favouriteCallBack.displayMovies(result[1].split(" "));
        }

        public synchronized void addReview(String movieID, Review review, ReviewsCallBack callBack) {
            String url = BASE_URL + "addReview?";
            String paramaters = "movieID=" + movieID + "&userID=" + review.getUserID() + "&review=" + review.getReview()
                    + "&time=" + review.getDate();
            String [] result = new String[0];
            try {
                result = openPostConnection(url,paramaters);
            } catch (IOException e) {
               callBack.onError(e);
               return;
            }
            callBack.onAddedSuccess();
        }

        public synchronized void getReviews(String movieID, ReviewsCallBack callBack) throws ParseException {
            String url = BASE_URL + "getReview?";
            String parameters = "movieID=" + movieID;
            url += parameters;
            String [] result = new String[0];
            try {
                result = openGetConnection(url);
                List<Review> reviews = new ArrayList<>();
                JSONParser parser = new JSONParser();
                JSONArray array = (JSONArray) parser.parse(result[1]);
                Iterator i = array.iterator();
                while (i.hasNext()){
                    JSONObject reviewObj = (JSONObject) i.next();
                    Review review = new Review();
                    review.setDate((String) reviewObj.get("time"));
                    review.setUserID((String) reviewObj.get("id"));
                    review.setReview((String) reviewObj.get("review"));
                    reviews.add(review);
                }
                callBack.displayReviews(reviews);
            } catch (IOException e) {
                callBack.onError(e);
            }

     }



        private String[] openPostConnection (String url, String parameters) throws IOException {

            URL Url = new URL(url);
            HttpURLConnection con = (HttpURLConnection)
                    Url.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(parameters.length()));
            con.setDoOutput(true);
            con.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            System.out.println(wr.size());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            if(responseCode == 409)
                return new String[]{String.valueOf(responseCode),null};

            BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inString;
            StringBuilder sb = new StringBuilder();
            while ((inString = is.readLine()) != null) {
                sb.append(inString);
            }

            is.close();
            String text = sb.toString();

            return new String[]{String.valueOf(responseCode), text};

    }

    private String[] openGetConnection (String url) throws IOException {

            URL Url = new URL(url);
            HttpURLConnection con = (HttpURLConnection)
                    Url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();



            if(responseCode == 404)
                return new String[]{String.valueOf(responseCode),null};
            BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inString;
            StringBuilder sb = new StringBuilder();
            while ((inString = is.readLine()) != null) {
                sb.append(inString);
            }

            is.close();
            String text = sb.toString();

            return new String[]{String.valueOf(responseCode), text};


    }
}

