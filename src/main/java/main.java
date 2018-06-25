import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;


public class main {
    private static void addBAction(ResultSet rs, JsonWriter js) throws IOException, SQLException {
//        System.out.println(rs.);
        js.beginObject()
                .name("status").value(rs.getString("status"))
                .name("actionID").value(rs.getInt("action_id"))
                .name("link").value(rs.getString("link"));
        js.name("comment").value(rs.getString("comment"));
        js.endObject();
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        ProcessBuilder process = new ProcessBuilder();
        if (process.environment().get("PORT") != null) {
            port(Integer.parseInt(process.environment().get("PORT")));
        } else {
            port(8080);
        }


        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE if not exists `Users` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`email` text," +
                "`address`  text," +
                "`salt` text," +
                "`password` text);" );
        stmt.execute("CREATE TABLE if not exists `BActions` (" +
                //"`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`user_id` integer, " +
                "`action_id` integer," +
                "`status` text," +
                "`comment` text," +
                "`link` text," +
                "PRIMARY KEY (user_id, action_id)," +
                "FOREIGN KEY (user_id) REFERENCES Users(id));");

        Gson gson = new GsonBuilder().create();

        PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO `users` (email, address, salt, password) VALUES (?, ?, ?, ?)"
        );

//        User user = new User(1, "asd@m.ru", "1asdsf1484" , "xzxzxz", "d5f8r4g9" );
//        System.out.println(gson.toJson(user));
//
//        String userStr = gson.toJson(user);
//        User user2 = gson.fromJson(userStr, User.class);
//
//        ps1.setString(1, user2.email);
//        ps1.setString(2, user2.address);
//        ps1.setString(3, user2.salt);
//        ps1.setString(4, user2.password);
//        ps1.executeUpdate();




//        get("/users", (request,response) -> {
//            response.type("application/json");
//            PreparedStatement stmnt = conn.prepareStatement(
//                    "SELECT * FROM `users`"
//            );
//
//            ResultSet rs = stmnt.executeQuery();
//            List<User> users = new ArrayList<>();
//            while (rs.next()){
//                users.add(new User(
//                        rs.getInt("id"),
//                        rs.getString("email"),
//                        rs.getString("address"),
//                        rs.getString("salt"),
//                        rs.getString("password"))
//                );
//            }
//            return users;
//        }, gson::toJson);

//хз как приплести bActiond
        get("/users/:id",(request,response)->{
            response.header("Access-Control-Allow-Origin","*");
            int userID = Integer.parseInt(request.params("id"));

            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ?"
            );
            stmnt.setInt(1, userID);
            ResultSet rs = stmnt.executeQuery();
            List<User> users = new ArrayList<>();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonWriter js = new JsonWriter(new OutputStreamWriter(out,"UTF-8"));

            if (rs.next()){
                js.beginObject()
//                    .name("id").value(rs.getInt("id"))
                    .name("email").value(rs.getString("email"))
                    .name("address").value(rs.getString("address"));
                PreparedStatement bActStm = conn.prepareStatement("SELECT * FROM `bActions` WHERE `user_id` = ?");
                bActStm.setInt(1, rs.getInt("id"));
                ResultSet bActionsRows = bActStm.executeQuery();
                if(bActionsRows.next()){
                    js.name("bActions").beginArray();
                    addBAction(bActionsRows,js);
                    while (bActionsRows.next()) addBAction(bActionsRows, js);
                    js.endArray();
                }
                js.endObject().close();
            }
            String result = new String(out.toByteArray(),"UTF-8");
            return result;
        });


        delete("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));
            PreparedStatement stmnt = conn.prepareStatement(
                    "DELETE FROM `users` WHERE `id` = ?"
            );
            stmnt.setInt(1, userID);
            stmnt.executeUpdate();
            return "DELETE";
        });


        post("/registration",(request,response)->{
            User userPost = gson.fromJson(request.body(), User.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `Users` (email, password) VALUES (?, ?)"
            );

            PreparedStatement test = conn.prepareStatement(
                    "SELECT * FROM `Users` WHERE `email` = ?"
            );
            test.setString(1, userPost.email);
            ResultSet rs = test.executeQuery();
            if (!rs.next()) {
                stmnt.setString(1, userPost.email);
                stmnt.setString(2, userPost.password);
                stmnt.executeUpdate();
                return "\"done\"";
            } else {
                return "\"fail\"";
            }

        });


        post("/login",(request,response)->{
            User userPost = gson.fromJson(request.body(), User.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `Users` (email, password) VALUES (?, ?)"
            );

            PreparedStatement test = conn.prepareStatement(
                    "SELECT * FROM `Users` WHERE `email` = ? AND `password` = ?"
            );
            test.setString(1, userPost.email);
            test.setString(2, userPost.password);
            ResultSet rs = test.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return "\"fail\"";
            }

        });


//        put("/users/:id",(request,response)->{
//            int userID = Integer.parseInt(request.params("id"));
//            User userPut = gson.fromJson(request.body(), User.class);
//
//            PreparedStatement stmnt = conn.prepareStatement(
//                    "UPDATE `Users` SET `email`=?, `address`=?, `salt`=?, `password`=? WHERE `id`=?"
//
//            );
//            stmnt.setInt(5, userID);
//
//            stmnt.setString(1, userPut.email);
//            stmnt.setString(2, userPut.address);
//            stmnt.setString(3, userPut.salt);
//            stmnt.setString(4, userPut.password);
//            stmnt.executeUpdate();
//
//            // добавление новой записив таблицу `users`
//            return "UPDATE";
//        });



//--------------------------------------------------------------------------

//        get("/bactions", (request,response) -> {
//            response.type("application/json");
//            PreparedStatement stmnt = conn.prepareStatement(
//                    "SELECT * FROM `BActions`"
//            );
//
//            ResultSet rs = stmnt.executeQuery();
//            List<BActions> bactions = new ArrayList<>();
//            while (rs.next()){
//                bactions.add(new BActions(
//                        rs.getInt("user_id"),
//                        rs.getInt("action_id"),
//                        rs.getString("status"),
//                        rs.getString("comment"),
//                        rs.getString("link"))
//                );
//            }
//            return bactions;
//        }, gson::toJson);


        post("/bactions",(request,response)->{
            BActions bactionPost = gson.fromJson(request.body(), BActions.class);

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `BActions` (user_id, action_id, status, comment, link) VALUES (?, ?, ?, ?, ?)"
            );

            stmnt.setInt(1, bactionPost.user_id);
            stmnt.setInt(2, bactionPost.action_id);
            stmnt.setString(3, bactionPost.status);
            stmnt.setString(4, bactionPost.comment);
            stmnt.setString(5, bactionPost.link);
            try {
                stmnt.executeUpdate();
            } catch (Exception e){
                return "\"you already has same action_id \"";
            }

            // добавление новой записив таблицу `users`
            return "\"done\"";
        });

    }
}
