
package service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {
    private static final String USERS = "users";
    private static final String MOVIES = "Movies";
    private static final String REVIEWS = "Reviews";
    private static final String mPath = "C:\\Users\\magdy\\eclipse-workspace\\JavaAPI\\database.json";
    private  JSONObject mDBObject;
    

    public DBConnection() throws IOException, ParseException {
        File file = new File(mPath);
        if(!file.createNewFile()){
            mDBObject = (JSONObject) new JSONParser().parse(new FileReader(mPath));
        }else{
            mDBObject = new JSONObject();
        }
        PrintWriter pw = new PrintWriter(mPath);
        pw.write(mDBObject.toJSONString());
        pw.flush();
        pw.close();
    }


    private void addBranch(String nameOFBranch){
        Object branchObject =  mDBObject.get(nameOFBranch);
        if(branchObject == null){
            mDBObject.put(nameOFBranch,new JSONObject());
        }
    }

    public boolean addUser(String id , String password) throws IOException, ParseException {
        addBranch(USERS);
        JSONObject usersObject = (JSONObject) mDBObject.get(USERS);
        String pass = (String) usersObject.get(id);
        if(pass != null){
        	return false;
        }else {
        	  usersObject.put(id, password);
        	  PrintWriter pw = new PrintWriter(mPath);
              pw.write(mDBObject.toJSONString());
              pw.flush();
              pw.close();
              return true;
        }
    }

    public boolean getUser(String id,String password) throws IOException, ParseException {
    	 addBranch(USERS);
        JSONObject usersMap = (JSONObject) mDBObject.get(USERS);
         String pass =  (String) usersMap.get(id);
        if(pass == null || !pass.equals(password))
        	return false;
        return true;
    }
    

    


    public String getUserMovies(String userID ) throws FileNotFoundException{
         addBranch(MOVIES);
         JSONObject moviesObj = (JSONObject) mDBObject.get(MOVIES);
         String userMovies =  (String)moviesObj.get(userID);
         if(userMovies == null)
        	 return "";
        	
         return userMovies;
    }

    public void addMovie(String userID , String movieID) throws FileNotFoundException {
        addBranch(MOVIES);
        JSONObject moviesObj = (JSONObject) mDBObject.get(MOVIES);
        Object userMoviesObj =  moviesObj.get(userID);
        String userMovies;
        if(userMoviesObj != null)
            userMovies = (String)userMoviesObj;
        else
        	userMovies = "";
        
        userMovies += movieID + " ";
        moviesObj.put(userID,userMovies);
        PrintWriter pw = new PrintWriter(mPath);
        pw.write(mDBObject.toJSONString());
        pw.flush();
        pw.close();
    }
    
    public void deleteMovie(String userID , String movieID) throws FileNotFoundException {
        addBranch(MOVIES);
        JSONObject moviesObj = (JSONObject) mDBObject.get(MOVIES);
        String userMovies = (String) moviesObj.get(userID);
        if(userMovies == null)
        	userMovies = "";
       userMovies = userMovies.replace(movieID, "");
       moviesObj.put(userID,userMovies);
       PrintWriter pw = new PrintWriter(mPath);
       pw.write(mDBObject.toJSONString());
       pw.flush();
       pw.close();
    }
    
    public void addReview(String movieID , String userID , String review , String time) throws FileNotFoundException {
    	  addBranch(REVIEWS);
    	  JSONObject reviewsObj = (JSONObject) mDBObject.get(REVIEWS);
    	  JSONArray movieReviews = (JSONArray) reviewsObj.get(movieID);
    	  if(movieReviews == null) {
    		  reviewsObj.put(movieID, new JSONArray());
    		  movieReviews = (JSONArray) reviewsObj.get(movieID);
    	  }
    	  JSONObject reviewObj = new JSONObject();
    	  reviewObj.put("time", time);
    	  reviewObj.put("review", review);
    	  reviewObj.put("id", userID);
    	  movieReviews.add(reviewObj);
    	  System.out.println(reviewObj.toString());
    	  System.out.println(movieReviews.toString());
    	  PrintWriter pw = new PrintWriter(mPath);
          pw.write(mDBObject.toJSONString());
          pw.flush();
          pw.close();
    }
    
    public JSONArray getReviews(String movieID) {
    	  addBranch(REVIEWS);
    	  JSONObject reviewsObj = (JSONObject) mDBObject.get(REVIEWS);
    	  JSONArray movieReviews = (JSONArray) reviewsObj.get(movieID);
    	  if(movieReviews == null)
    		  movieReviews = new JSONArray();
    	  
    	  return movieReviews;
    }
    
}
