package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("Post/")
public class Post {
    // create post //-------------------------------------------------------------------------------------------------
    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPost(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("Type") String Type, @FormDataParam("FileReference") String FileReference, @FormDataParam("DateAdded") String DateAdded,@FormDataParam("Caption") String Caption) {
        try {
            if (UserID == null ||Type == null ||FileReference == null ||DateAdded == null  ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Post/Create id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Posts (UserID, Type, FileReference, DateAdded, Caption) VALUES (?,?,?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, Type);
            ps.setString(3, FileReference);
            ps.setString(4, DateAdded);
            ps.setString(5, Caption);
            ps.execute();


            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    // delete post //---------------------------------------------------------------------------------------------------
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost(@FormDataParam("PostID") Integer PostID) {

        try {
            if (PostID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("post/delete id=" + PostID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Posts WHERE PostID = ?");
            ps.setInt(1, PostID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


    // get post (based on postID)//-------------------------------------------------------------------------------------
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPost(@PathParam("id")int PostID) {
        System.out.println("post/get");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select PostID, Type, FileReference, DateAdded, UserID, Caption from posts where PostID = "+PostID);
                    ResultSet results = (ps).executeQuery();

            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("PostID", results.getInt(1));
                item.put("Type", results.getString(2));
                item.put("FileReference", results.getString(3));
                item.put("DateAdded", results.getString(4));
                item.put("UserID", results.getInt(5));
                item.put("Caption", results.getString(6));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}
