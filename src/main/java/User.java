public class User {
    public int id;
    public String name;
    public String surname;
    public String patronymic;
    public int meansOfCommunication_id;

    public User(int id, String name, String surname, String patronymic, int meansOfCommunication_id) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.meansOfCommunication_id = meansOfCommunication_id;
    }
}
