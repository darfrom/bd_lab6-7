import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;


public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ProcessBuilder process = new ProcessBuilder();
        if (process.environment().get("PORT") != null) {
            port(Integer.parseInt(process.environment().get("PORT")));
        } else {
            port(8080);
        }


        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE if not exists `Users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `name` text, `surname` text, `patronymic` text, `meansOfCommunication_id` integer," +
                    " FOREIGN KEY(meansOfCommunication_id) REFERENCES MeansOfCommunication(id));");

        stmt.execute("CREATE TABLE if not exists `Studios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `name` text, `address_id` integer," +
                " FOREIGN KEY(address_id) REFERENCES Address(id));");

        stmt.execute("CREATE TABLE if not exists `MeansOfCommunication` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `phoneNomber` text, `email` text, `user_id` integer," +
                    " FOREIGN KEY(user_id) REFERENCES Users(id));");

        stmt.execute("CREATE TABLE if not exists `Address` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `city` text, `street` text, `building` text, `studio_id` integer," +
                    " FOREIGN KEY(studio_id) REFERENCES Studio(id));");


        Gson gson = new GsonBuilder().create();

        PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO `users` (name, surname, patronymic, meansOfCommunication_id) VALUES (?, ?, ?, ?)"
        );

        User user = new User(1, "Masha", "Vasileva" , "Vitalevna", 10);
        System.out.println(gson.toJson(user));

        String userStr = gson.toJson(user);
        User user2 = gson.fromJson(userStr, User.class);

        ps1.setString(1, user2.name);
        ps1.setString(2, user2.surname);
        ps1.setString(3, user2.patronymic);
        ps1.setInt(4, user2.meansOfCommunication_id);
        ps1.executeUpdate();




        get("/users", (request,response) -> {
            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users`"
            );

            ResultSet rs = stmnt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getInt("meansOfCommunication_id"))
                );
            }
            return users;
        }, gson::toJson);


        get("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));

            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ?"
            );
            stmnt.setInt(1, userID);
            ResultSet rs = stmnt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getInt("meansOfCommunication_id"))
                );
            }
            return users;
        },gson::toJson);


        delete("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));
            PreparedStatement stmnt = conn.prepareStatement(
                    "DELETE FROM `users` WHERE `id` = ?"
            );
            stmnt.setInt(1, userID);
            stmnt.executeUpdate();
            return "DELETE";
        });


        post("/users",(request,response)->{
            User userPost = gson.fromJson(request.body(), User.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `users` (name, surname, patronymic, meansOfCommunication_id) VALUES (?, ?, ?, ?)"
            );

            stmnt.setString(1, userPost.name);
            stmnt.setString(2, userPost.surname);
            stmnt.setString(3, userPost.patronymic);
            stmnt.setInt(4, userPost.meansOfCommunication_id);
            stmnt.executeUpdate();


            // добавление новой записив таблицу `users`
            return "INSERT";
        });


        put("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));
            User userPut = gson.fromJson(request.body(), User.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "UPDATE `users` SET `name`=?, `surname`=?, `patronymic`=?, `meansOfCommunication_id`=? WHERE `id`=?"
            );
            stmnt.setInt(5, userID);

            stmnt.setString(1, userPut.name);
            stmnt.setString(2, userPut.surname);
            stmnt.setString(3, userPut.patronymic);
            stmnt.setInt(4, userPut.meansOfCommunication_id);
            stmnt.executeUpdate();

            // добавление новой записив таблицу `users`
            return "UPDATE";
        });







        PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO `studios` (name, address_id)  VALUES (?, ?)"
        );

        Studios studio = new Studios(1, "Str", 5);
        System.out.println(gson.toJson(studio));


        String studioStr = gson.toJson(studio);
        Studios studio2 = gson.fromJson(studioStr, Studios.class);

        ps2.setString(1, studio2.name);
        ps2.setInt(2, studio2.address_id);
        ps2.executeUpdate();



        get("/studios", (request,response) -> {
            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `studios`"
            );

            ResultSet rs = stmnt.executeQuery();
            List<Studios> studios = new ArrayList<>();
            while (rs.next()){
                studios.add(new Studios(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("address_id"))
                );
            }
            return studios;
        }, gson::toJson);


        get("/studios/:id",(request,response)->{
            int studioID = Integer.parseInt(request.params("id"));

            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `studios` WHERE `id` = ?"
            );
            stmnt.setInt(1, studioID);
            ResultSet rs = stmnt.executeQuery();
            List<Studios> studios = new ArrayList<>();
            while (rs.next()){
                studios.add(new Studios(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("address_id"))
                );
            }
            return studios;
        },gson::toJson);


        delete("/studios/:id",(request,response)->{
            int studioID = Integer.parseInt(request.params("id"));
            PreparedStatement stmnt = conn.prepareStatement(
                    "DELETE FROM `studios` WHERE `id` = ?"
            );
            stmnt.setInt(1, studioID);
            stmnt.executeUpdate();
            return "DELETE";
        });


        post("/studios",(request,response)->{
            Studios studioPost = gson.fromJson(request.body(), Studios.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `studios` (name, address_id) VALUES (?, ?)"
            );

            stmnt.setString(1, studioPost.name);;
            stmnt.setInt(2, studioPost.address_id);
            stmnt.executeUpdate();


            // добавление новой записив таблицу `users`
            return "INSERT";
        });


        put("/studios/:id",(request,response)->{
            int studioID = Integer.parseInt(request.params("id"));
            Studios studioPut = gson.fromJson(request.body(), Studios.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "UPDATE `studios` SET `name`=?, `address_id`=? WHERE `id`=?"
            );
            stmnt.setInt(3, studioID);

            stmnt.setString(1, studioPut.name);
            stmnt.setInt(2, studioPut.address_id);
            stmnt.executeUpdate();

            // добавление новой записив таблицу `users`
            return "UPDATE";
        });

//        ps1.setString(1, "Ivan"); // вместо первого "?" подставляется строка "Ivan"
//        ps1.executeUpdate();
//        ps1.setString(1, "Petr"); // вместо первого "?" подставляется строка "Petr"
//        ps1.executeUpdate();

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

