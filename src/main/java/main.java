import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;

public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE if not exists `Users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `name` text, `surname` text, `patronymic` text, `meansOfCommunication_id` integer," +
                    " FOREIGN KEY(meansOfCommunication_id) REFERENCES MeansOfCommunication(id));");
        stmt.execute("CREATE TABLE if not exists `MeansOfCommunication` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `phoneNomber` text, `email` text, `user_id` integer," +
                    " FOREIGN KEY(user_id) REFERENCES Users(id));");
        stmt.execute("CREATE TABLE if not exists `Studio` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `name` text, `address_id` integer," +
                " FOREIGN KEY(address_id) REFERENCES Address(id));");
        stmt.execute("CREATE TABLE if not exists `Address` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `city` text, `street` text, `building` text, `studio_id` integer," +
                " FOREIGN KEY(studio_id) REFERENCES Studio(id));");




//        PreparedStatement ps1 = conn.prepareStatement(
//                "INSERT INTO `users` (name) VALUES (?)"
//        );

//        ps1.setString(1, "Ivan"); // вместо первого "?" подставляется строка "Ivan"
//        ps1.executeUpdate();
//        ps1.setString(1, "Petr"); // вместо первого "?" подставляется строка "Petr"
//        ps1.executeUpdate();


//        int userId = 1;
//        PreparedStatement ps2 = conn.prepareStatement(
//                "SELECT * FROM `users` WHERE `name` = ?"
//        );
//        ps2.setString(1, "Ivan");
//        ResultSet rs = ps2.executeQuery();
//
//        while(rs.next()) {
//            System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
//        }
//
//
//
//        Gson gson = new GsonBuilder().create();
//
//        User user = new User(10, "Denis");
//        System.out.println(gson.toJson(user));
//
//        String userStr = gson.toJson(user);
//        User user2 = gson.fromJson(userStr, User.class);
//
//        ps1.setString(1, user2.name);
//        ps1.executeUpdate();
    }

}

