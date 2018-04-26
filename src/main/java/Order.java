public class Order {
    public int id;
    public int cost;
    public int user_id;
    public int photographer_id;
    public int studio_id;

    public Order(int id, int cost, int user_id, int photographer_id, int studio_id) {
        this.id = id;
        this.cost = cost;
        this.user_id = user_id;
        this.photographer_id = photographer_id;
        this.studio_id = studio_id;
    }
}
