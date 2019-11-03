package Controller;

import Server.Main;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class ProjectDatabase {

//----------------------------------------------------------------------------------------------------------------//

    private static void getUserinfo(int UserID) {

        try {

            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, DateJoined, Followers, Following, Email from Information where userID =" + UserID + "");

            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                UserID = results.getInt(1);
                String firstName = results.getString(2);
                String lastName = results.getString(3);
                String DateJoined = results.getString(4);
                int Followers = results.getInt(5);
                int Following = results.getInt(6);
                String email = results.getString(7);
                System.out.println("Id: " + UserID + ",  ");
                System.out.println("firstName: " + firstName + ",  ");
                System.out.println("lastName: " + lastName + ",  ");
                System.out.println("DateJoined: " + DateJoined + ",  ");
                System.out.println("Followers: " + Followers + ",  ");
                System.out.println("Following: " + Following + ",  ");
                System.out.println("Email: " + email + ",  ");
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    private static void updateInformation(int UserID, String FirstName, String LastName, String Bio, String Email){

        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE information SET FirstName = ? LastName = ? Bio = ? Email = ? WHERE UserID = ?");
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, Bio);
            ps.setString(4, Email);
            ps.setInt(5, UserID);
            ps.execute();
        }
        catch (Exception exception) {
        System.out.println("Database error: " + exception.getMessage());
    }

}


// ------------------------------------------------------------------------------------------------------//

    private static void getfollowers(int followID) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("Select UserID from FollowTable where followID =" + followID + "");
            ResultSet results = (ps).executeQuery();
            int Followers = 0;
            while (results.next()) {
                Followers++;
            }
            System.out.println("Followers = " + Followers);
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void getfollowing(int UserID) {

        try {
            PreparedStatement ps = Main.db.prepareStatement("Select followID from FollowTable where UserID =" + UserID + "");
            ResultSet results = (ps).executeQuery();
            int following = 0;
            while (results.next()) {
                following++;
            }
            System.out.println("Following = " + following);
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }


// ------------------------------------------------------------------------------------------------------//

    private static void getSocialLinks(int UserID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("select LinkId, Link, UserID From SocialLinks Where UserID = " + UserID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                int LinkID = results.getInt(1);
                String Link = results.getString(2);
                UserID = results.getInt(3);
                System.out.println("Link ID: " + LinkID);
                System.out.println("Link : " + Link);
                System.out.println("UserID: " + UserID);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void createSocialLink(int UserID, String Link) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO SocialLinks (UserID, Link) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Link);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void deleteSocialLink(int LinkID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM SocialLinks WHERE LinkID = " + LinkID);


            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------//

    private static void createPost(int UserID, String Type, String FileReference, Date DateAdded) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Posts (UserID, Type, FileReference, DateAdded) VALUES (?,?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, Type);
            ps.setString(3, FileReference);
            ps.setDate(4, (java.sql.Date) DateAdded);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());

        }
    }
    public static void deletePost(int PostID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Posts WHERE followID = "+PostID);
            ps.setInt(1, PostID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void getPosts(int PostID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("select PostID, Type, FileReference, DateAdded, UserID from posts where PostID = " + PostID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                PostID = results.getInt(1);
                String PostId = results.getString(2);
                String FileReference = results.getString(3);
                Date DateAdded = results.getDate(4);
                int UserID = results.getInt(5);
            }
        }
        catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
            }

        }

//-------------------------------------------------------------------------------------------------------------------//

    public static void createUser(String username,String password){
        try{
            PreparedStatement ps = Main.db.prepareStatement("Insert Into Passwords (username, password) Values (?, ?)");

            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }
    private static void updatePassword(int UserID, String Password){
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Passwords SET Password = ? WHERE UserID = ?");
            ps.setString(1, Password);
            ps.setInt(2, UserID);
            ps.execute();
        }
        catch (Exception exception) {
        System.out.println("Database error: " + exception.getMessage());
    }

}

//------------------------------------------------------------------------------------------------------------------//

    private static void createEvent(int UserID, String Title, String Description, String ImageReference) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Events (UserID, Title, Description, ImageReference) VALUES (?, ?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, Title);
            ps.setString(3, Description);
            ps.setString(4, ImageReference);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void deleteEvent(int EventID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Events WHERE followID = "+EventID);
            ps.setInt(1, EventID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void getEvent(int EventID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("select EventID, Title, Description, ImageReference , UserID from Events where EventID = " + EventID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {

                EventID = results.getInt(1);
                String Title = results.getString(2);
                String Description = results.getString(3);
                String ImageReference = results.getString(4);
                int UserID = results.getInt(5);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());


        }
    }

    //--------------------------------------------------------------------------------------------------------------//

    private static void createGenre(int UserID, String Genre) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Genres (UserID, Genre, Description) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Genre);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
    public static void deleteGenre(int GenreID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Genres WHERE GenreID = "+GenreID);
            ps.setInt(1, GenreID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    private static void getGenre(int UserID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("select GenreID, UserID, Genre from Genres where UserID = " + UserID);
            ResultSet results = (ps).executeQuery();
            while (results.next()) {
                int GenreID = results.getInt(1);
                UserID = Integer.parseInt(results.getString(2));
                String Genre = results.getString(3);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }



    }