public class Address {
    public int id;
    public String city;
    public String street;
    public String building;
    public int studio_id;

    public Address(int id, String city, String street, String building, int studio_id) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.building = building;
        this.studio_id = studio_id;
    }
}
