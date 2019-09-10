
import org.sqlite.SQLiteConfig;
import java.sql.*;


public class Main {

    public static void main(String[] args) {

        openDatabase("Project Database.db");

        // code using the database goes here!
        getUserinfo(2);
        getfollowers(2);
        getfollowing(2);

        getSocialLinks(1);

        deleteSocialLink(4);
        getSocialLinks(4);

        closeDatabase();
    }


//database opening and closing--------------------------------------------------------------------------------//

    public static Connection db = null;

    private static void openDatabase(String dbFile) {

        try {

            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");

        } catch (Exception exception) {

            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    private static void closeDatabase() {
        try {

            db.close();
            System.out.println("Disconnected from database.");

        } catch (Exception exception) {

            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
// ------------------------------------------------------------------------------------------------------//

    private static void getUserinfo(int UserID){

        try {

            PreparedStatement ps = Main.db.prepareStatement("select UserID, firstName, lastName, DateJoined, Followers, Following, Email from Information where userID =" +UserID+ "");

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

// ------------------------------------------------------------------------------------------------------//

    private static void getfollowers(int followID) {

        try{
            PreparedStatement ps = Main.db.prepareStatement("Select UserID from FollowTable where followID =" + followID + "");
            ResultSet results = (ps).executeQuery();
            int Followers = 0;
            while (results.next()) {
            Followers++;
            }
            System.out.println("Followers = "+Followers);
        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void getfollowing(int UserID){

        try{
        PreparedStatement ps = Main.db.prepareStatement("Select followID from FollowTable where UserID ="+UserID+"");
                ResultSet results = (ps).executeQuery();
        int following = 0;
        while (results.next()) {
            following++;
        }
        System.out.println("Following = "+following);
        }
        catch (Exception exception) {
        System.out.println("Database error: " + exception.getMessage());
    }

}


// ------------------------------------------------------------------------------------------------------//

    private static void getSocialLinks(int UserID){
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

        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void createSocialLink(int UserID, String Link){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO SocialLinks (UserID, Link) VALUES (?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Link);
            ps.execute();

        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    private static void deleteSocialLink(int LinkID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM SocialLinks WHERE LinkID = "+LinkID);


            ps.execute();

        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


//----------------------------------------------------------------------------------------------------------------\\


}

