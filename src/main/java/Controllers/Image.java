package Controllers;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path("image/")
public class Image {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listImages() {

        System.out.println("/image/list - Getting all image files from folder");

        File folder = new File("resources/client/img");
        File[] files = folder.listFiles();

        JSONArray images = new JSONArray();

        if (files != null) {
            for (File file : files) {
                images.add(file.getName());
            }
        }

        return images.toString();

    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImage(@FormDataParam("file") InputStream fileInputStream,
                              @FormDataParam("file") FormDataContentDisposition formData) {

        System.out.println("/image/upload - Request to upload image " + formData.getFileName());

        try {

            int read;
            byte[] bytes = new byte[1024];
            OutputStream outputStream = new FileOutputStream(new File("resources/client/img/" + formData.getFileName()));
            while ((read = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();

            return "{\"status\":\"OK\"}";

        } catch (IOException ioe) {

            System.out.println("Image upload error: " + ioe.getMessage());
            return "{\"error\":\"" + ioe.getMessage() + "\"}";

        }

    }

}
