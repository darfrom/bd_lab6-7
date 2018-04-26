import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;

public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE if not exists `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` text);");

        PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO `users` (name) VALUES (?)"
        );

//        ps1.setString(1, "Ivan"); // вместо первого "?" подставляется строка "Ivan"
//        ps1.executeUpdate();
//        ps1.setString(1, "Petr"); // вместо первого "?" подставляется строка "Petr"
//        ps1.executeUpdate();


        int userId = 1;
        PreparedStatement ps2 = conn.prepareStatement(
                "SELECT * FROM `users` WHERE `name` = ?"
        );
        ps2.setString(1, "Ivan");
        ResultSet rs = ps2.executeQuery();

        while(rs.next()) {
            System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
        }



        Gson gson = new GsonBuilder().create();
    }



    class User {
        public int id;
        public String name;
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}

