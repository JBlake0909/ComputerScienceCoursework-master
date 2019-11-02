package Controller;

import Server.Main;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Genre/")
public class Genre {

    //----------------------------------------Gets Genres from the inputted UserID-----------------------------------//
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("Userid") Integer Userid) throws Exception {
        if (Userid == null) {
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("Genre/get/" + Userid);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("select GenreID, UserID, Genre from Genres where UserID = " +Userid);
                    ResultSet results = (ps).executeQuery();

            while (results.next()) {
                item.put("GenreID", results.getInt(1));
                item.put("UserID", Userid);
                item.put("Genre", results.getString(3));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }
    //---------------------------------------------------------------------------------------------------------------//



















}
