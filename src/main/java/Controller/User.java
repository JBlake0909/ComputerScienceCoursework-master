package Controller;

import Server.Main;
import com.sun.jersey.multipart.FormDataParam;
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
    @Path("display")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserInfo(int UserID) {
        System.out.println("user/display");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, DateJoined, Followers, Following, Email from Information where userID =" + UserID + "");
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
    public String updateThing(
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
    //-----------------------------------------------------------------------------------------------------------------//

}
