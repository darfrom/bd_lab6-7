public class User {
    public int id;
    public String email;
    public String address;
    public String salt;
    public String password;

    public User(int id, String email, String address, String salt, String password) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.salt = salt;
        this.password = password;
    }
}
