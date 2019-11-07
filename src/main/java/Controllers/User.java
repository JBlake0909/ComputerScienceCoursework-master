package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
                item.put("DateJoined", results.getDate(4));
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
            @FormDataParam("UserID") Integer UserID, @FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("Bio") String Bio, @FormDataParam("Email") String Email) {
        try {
            if (UserID == null || FirstName == null || LastName == null || Bio == null || Email == null ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("user/update id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE information SET FirstName = ? LastName = ? Bio = ? Email = ? WHERE UserID = ?");
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
    public String unfollow(@FormDataParam("FollowID") Integer followID) {

        try {
            if (followID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/delete id=" + followID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM FollowTable WHERE followID = "+followID);
            ps.setInt(1, followID);
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
            PreparedStatement ps = Main.db.prepareStatement("Select followID from FollowTable where UserID ="+UserID);
                    ResultSet results = (ps).executeQuery();

            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("followID", results.getInt(1));
                list.add(item);
                following++;
            }
                try {
                    Main.db.prepareStatement("UPDATE Information SET Following = ? WHERE UserID ="+UserID);
                    ps.setInt(1, following);
                    ps.execute();
                } catch (Exception exception) {
                    System.out.println("Database error: " + exception.getMessage());
                    return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
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
            PreparedStatement ps = Main.db.prepareStatement("Select UserID from FollowTable where followID =" +followID);
            ResultSet results = (ps).executeQuery();

            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userID", results.getInt(1));
                list.add(item);
                followers++;
            }
            try {
                Main.db.prepareStatement("UPDATE Information SET Followers = ? WHERE UserID ="+followID);
                ps.setInt(1, followers);
                ps.execute();
            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //login//
    @GET
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String Login(@FormDataParam("username") String Username,  @FormDataParam("password") String Password) {
        System.out.println("user/login");
        JSONArray list = new JSONArray();
        int UserID = -1;
        String gUsername = null;
        String gPassword = null;
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password, FROM Passwords WHERE Username =" +Username);
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                JSONObject item = new JSONObject();
                UserID = results.getInt(1);
                gUsername = results.getString(2);
                gPassword = results.getString(3);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
        if(gUsername == null){
            return "error Username does not exist";
        }
        else if (gPassword != Password){
            return "error Password is incorrect";
        }
        else{
            return (""+UserID);
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

            PreparedStatement ps = Main.db.prepareStatement("Insert Into Passwords (username, password) Values (?, ?)");

            ps.setString(1, Username);
            ps.setString(2, Password);
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

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Passwords SET Password = ? WHERE Id = ?");
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
