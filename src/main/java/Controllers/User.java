package Controllers;

import Server.Main;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("user/")
public class User {

    //---------Gets information about the inputted UserID for Display Purposes----------------------------------------//
    @GET
    @Path("display/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserInfo( @PathParam("id") int UserID) {
        System.out.println("user/display");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, DateJoined, Followers, Following, Email, Bio from Information where userID =" + UserID + "");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("firstName", results.getString(2));
                item.put("lastName", results.getString(3));
                item.put("DateJoined", results.getString(4));
                item.put("Followers", results.getInt(5));
                item.put("Following", results.getInt(6));
                item.put("Email", results.getString(7));
                item.put("Bio", results.getString(8));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------Updates User Information in the Database--------------------------------//
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateInformation(
             @FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("Bio") String Bio, @FormDataParam("Email") String Email, @FormDataParam("UserID") Integer UserID) {
        try {
            if (UserID == null || FirstName == null || LastName == null || Bio == null || Email == null ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/update id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE information SET FirstName = ?, LastName = ?, Bio = ?, Email = ? WHERE UserID = ?");
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, Bio);
            ps.setString(4, Email);
            ps.setInt(5, UserID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------Follow a User-------------------------------------------------------//
    @POST
    @Path("follow")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String follow(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("FollowID") Integer FollowID) {
        try {
            if (UserID == null || FollowID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/follow FollowID=" + FollowID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO FollowTable (UserID, FollowID) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setInt(2, FollowID);
            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------UnFollow a User-----------------------------------------------------//
    @POST
    @Path("unFollow")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String unFollow(@FormDataParam("FollowID") Integer FollowID, @FormDataParam("UserID") Integer UserID) {

        try {
            if (FollowID == null || UserID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/delete id=" + FollowID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM FollowTable WHERE UserID = ? AND FollowID = ?");
            ps.setInt(1, UserID);
            ps.setInt(2, FollowID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//

    // get following/update following---------------------------------------------------------------------------------//
    @GET
    @Path("getFollowing/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFollowing(@PathParam("id") int UserID) {
        System.out.println("User/getFollowing");
        JSONArray list = new JSONArray();
        try {
            int following = 0;
            //"Select followID from FollowTable where UserID ="+UserID
            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, Followers, Following from Information where UserID in(Select followID from FollowTable where UserID = ?)");
                    ps.setInt(1, UserID);
                    ResultSet results = (ps).executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("firstName", results.getString(2));
                item.put("lastName", results.getString(3));
                item.put("Followers", results.getInt(4));
                item.put("Following", results.getInt(5));
                list.add(item);
                following++;
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    // get followers/update followers---------------------------------------------------------------------------------//
    @GET
    @Path("getFollowers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFollowers(@PathParam("id")int followID) {
        System.out.println("User/getFollowers");
        JSONArray list = new JSONArray();
        try {
            int followers = 0;
            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, Followers, Following from Information where UserID in(Select UserID from FollowTable where followID = ?)");
            ps.setInt(1, followID);
            ResultSet results = (ps).executeQuery();

            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("firstName", results.getString(2));
                item.put("lastName", results.getString(3));
                item.put("Followers", results.getInt(4));
                item.put("Following", results.getInt(5));
                list.add(item);
                followers++;
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //login//
    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String Login(@FormDataParam("username") String Username,  @FormDataParam("password") String Password) {
        System.out.println("user/login "+Username);
        System.out.println("inputted Password: " +Password);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password FROM Passwords WHERE Username = ?");
            ps.setString(1,Username);
            ResultSet results = ps.executeQuery();
            if(results!=null && results.next()){
                if(Password.equals(results.getString(3))){
                    item.put("userID", results.getInt(1));
                    return item.toString();
                    //return "{\"Success\": \"User account found\"}" ;
                }else{
                    return "{\"error\": \"Password does not match the user account\"}";
                }
            }else{
                return "{\"error\": \"User account does not exist\"}";
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }

    }

    //create username/ password//
    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createUser(
            @FormDataParam("username") String Username, @FormDataParam("password") String Password) {
        try {
            if (Username == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/create username=" + Username);

            PreparedStatement ps = Main.db.prepareStatement("Insert Into Passwords (username, password) Values (?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, Username);
            ps.setString(2, Password);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int UserID = rs.getInt(1);
            ps = Main.db.prepareStatement("INSERT INTO Information (UserID, firstName, lastName, DateJoined, Followers, Following, Email, Bio ) Values (?,?,?,?,?,?,?,?) ");
            ps.setInt(1, UserID);
            ps.setString(2, null);
            ps.setString(3, null);
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String currentDateString = formatter.format(currentDate);
            ps.setString(4, currentDateString);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    // update password//
    @POST
    @Path("updatePassword")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePassword(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("Password") String Password) {
        try {
            if (UserID == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/updatePassword id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Passwords SET Password = ? WHERE UserID = ?");
            ps.setString(1, Password);
            ps.setInt(2, UserID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

}
