package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Event/")
public class Event {
    // create event//
    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEvent(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("Title") String Title, @FormDataParam("Description") String Description, @FormDataParam("ImageReference") String ImageReference) {
        try {
            if (UserID == null ||Title == null ||Description == null ||ImageReference == null  ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Event/Create UserId=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Events (UserID, Title, Description, ImageReference) VALUES (?, ?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, Title);
            ps.setString(3, Description);
            ps.setString(4, ImageReference);
            ps.execute();



            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    // Delete event//
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEvent(@FormDataParam("EventID") Integer EventID) {

        try {
            if (EventID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("post/delete id=" + EventID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Events WHERE followID = "+EventID);
            ps.setInt(1, EventID);
            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
    // Get event (based on eventID //
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEvent(@PathParam("id")int EventID) {
        System.out.println("event/get");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select EventID, Title, Description, ImageReference, UserID from Events where EventID = "+EventID);
                    ResultSet results = (ps).executeQuery();


            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("EventID", results.getInt(1));
                item.put("Title", results.getString(2));
                item.put("Description", results.getString(3));
                item.put("ImageReference", results.getString(4));
                item.put("UserID", results.getInt(5));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}


