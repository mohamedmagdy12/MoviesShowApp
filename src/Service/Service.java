package Service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
@Path("/service")
public class Service {

	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/addUser")
	@Produces({MediaType.TEXT_PLAIN})
	public Response addUser(@FormParam("id") String id , @FormParam("pass") String password ) throws IOException, ParseException{
		 boolean res = false;
	     DBConnection dbConnection = new DBConnection();
	    try {
			res = dbConnection.addUser(id,password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    if(res == false)
	    	return Response.status(Status.CONFLICT).entity("Unavailable user name").build();
	    
	    

         return Response.status(Status.OK).entity("Done adding user").build();
	}
	
	@GET
	@Path("/getUser")
	public Response getUser(@QueryParam("id") String id,@QueryParam("pass") String password) throws IOException, ParseException {
	     boolean res = false;
	     DBConnection dbConnection = new DBConnection();
		try {
			res = dbConnection.getUser(id,password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res == false)
			return Response.status(Status.NOT_FOUND).entity("User not found").build();
		
		 return Response.status(Status.OK).entity("Done getting user").build();
	}
	
	@GET
	@Path("/getPhoto")
	public Response getPHoto(@QueryParam("id") String id) throws IOException {
		File file = new File("C:\\Users\\magdy\\eclipse-workspace\\JavaAPI\\storage\\"+id+".PNG");
		if(!file.exists())
			return Response.status(Status.NO_CONTENT).build();

		    byte [] bytes = Files.readAllBytes((file).toPath());
		    return Response.ok(Base64.getEncoder().encode(bytes)).build();
	}
	
	@POST
	@Path("/addPhoto")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.TEXT_PLAIN})
	public Response addPhoto(@FormParam("id") String id , @FormParam("image") String image) throws IOException{
		  image = image.replaceAll(" ","+");
	      byte [] decodedImg = Base64.getDecoder().decode(image.getBytes(StandardCharsets.UTF_8));
		  try {
	            OutputStream  os  = new FileOutputStream(new File("C:\\Users\\magdy\\eclipse-workspace\\JavaAPI\\storage\\" + id + ".PNG")); 
	            os.write(decodedImg); 
	            os.close(); 
	            return Response.status(Status.OK).entity("Done adding photo").build();
	        } 
	        catch (Exception e) { 
	            System.out.println("Exception: " + e); 
	            return Response.status(Status.EXPECTATION_FAILED).entity("Can't add photo").build();
	        } 
		 
	}
	
	
	@GET
	@Path("/getUserMovies")
	public Response getUserMovies(@QueryParam("id") String id) throws IOException, ParseException {
		 Response response = null;
	     JSONObject jsonObject = null;
	     DBConnection dbConnection = new DBConnection();
	     String result = dbConnection.getUserMovies(id);
		 return Response.status(Status.OK).entity(result).build();
	}
	
	@POST
	@Path("/addUserMovie")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.TEXT_PLAIN})
	public Response addUserMovie(@FormParam("id") String id , @FormParam("movie") String movie) throws IOException, ParseException {
		Response response = null;
	     JSONObject jsonObject = null;
	     DBConnection dbConnection = new DBConnection();
	     dbConnection.addMovie(id, movie);
	     return Response.status(Status.OK).entity("Done adding movie").build();
	}
	
	@POST
	@Path("/deleteUserMovie")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.TEXT_PLAIN})
	public Response deleteUserMovie(@FormParam("id") String id , @FormParam("movie") String movie) throws IOException, ParseException {
		Response response = null;
	     JSONObject jsonObject = null;
	     DBConnection dbConnection = new DBConnection();
	     dbConnection.deleteMovie(id, movie);
	     return Response.status(Status.OK).entity("Done deleting movie").build();
	}
	
	@POST
	@Path("/addReview")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.TEXT_PLAIN})
	public Response addReview(@FormParam("movieID") String movieID, @FormParam("userID") String userID
			, @FormParam("review") String review
			,@FormParam("time") String time) throws IOException, ParseException {
		
		 Response response = null;
	     JSONObject jsonObject = null;
	     DBConnection dbConnection = new DBConnection();
	     dbConnection.addReview(movieID, userID, review, time);
	     return Response.status(Status.OK).entity("Done adding review").build();
	}
	
	@GET
	@Path("/getReview")
	public Response getReview(@QueryParam("movieID") String movieID) throws IOException, ParseException {
		 Response response = null;
	     JSONObject jsonObject = null;
	     DBConnection dbConnection = new DBConnection();
	     JSONArray result = dbConnection.getReviews(movieID);
		 return Response.status(Status.OK).entity(result.toString()).build();
		
		 
	}
	
	
	
	
	
	
}
