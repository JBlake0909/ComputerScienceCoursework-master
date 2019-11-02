package Controller;

import Server.Main;
import com.sun.jersey.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("SocialLink/")
public class SocialLink {


    //------------------------------------------Gets all social Links from an inputted UserID------------------------//
    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSocialLinks(int UserID) {
        System.out.println("SocialLink/get");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select LinkId, Link, UserID From SocialLinks Where UserID = " + UserID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("Linkid", results.getInt(1));
                item.put("Link", results.getString(2));
                item.put("UserID", results.getInt(3));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------Creates a new Social Link under the inputted UserID---------------------//
    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createSocialLink(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("Link") String Link) {
        try {
            if (UserID == null || Link == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("SocialLink/create id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO SocialLinks (UserID, Link) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Link);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------Deletes the Social Link under the inputted LinkID-----------------------//
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteThing(@FormDataParam("LinkId") Integer LinkID) {

        try {
            if (LinkID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("SocialLink/delete id=" + LinkID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM SocialLinks WHERE LinkID = " + LinkID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
//--------------------------------------------------------------------------------------------------------------------//











}
