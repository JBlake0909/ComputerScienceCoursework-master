package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Genre/")
public class Genre {

    //----------------------------------------Gets Genres from the inputted UserID-----------------------------------//
    @GET
    @Path("getByID/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGenre(@PathParam("id")int UserID) {
        System.out.println("Genre/get");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select GenreID, UserID, Genre from Genres where UserID = " +UserID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("GenreID", results.getInt(1));
                item.put("UserID", results.getInt(2));
                item.put("Genre", results.getString(3));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //---------------------------------------------------------------------------------------------------------------//
   //---------------------------------------------------- Creates a new Genre linked to the UserID-------------------//
    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertThing(
            @FormDataParam("UserID") Integer UserID, @FormDataParam("Genre") String Genre) {
        try {
            if (UserID == null || Genre == null ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Genre/Create id=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Genres (UserID, Genre, Description) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Genre);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    //---------------------------------------Deletes the Genre based on the inputted GenreID--------------------------//
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteThing(@FormDataParam("GenreID") Integer GenreID) {
        try {
            if (GenreID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("thing/delete id=" + GenreID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Genres WHERE GenreID = "+GenreID);
            ps.setInt(1, GenreID);
            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//

    // select userID based on genre //
    @GET
    @Path("getByGen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String filterByGenre(@PathParam("id")String Genre) {
        System.out.println("Genre/filter");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select UserID, from Genres where Genre = " +Genre);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }













}
