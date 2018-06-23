public class BActions {
    public int user_id;
    public int action_id;
    public String status;
    public String comment;
    public String link;


    public BActions(int user_id, int action_id, String status, String comment, String link) {
        this.user_id = user_id;
        this.action_id = action_id;
        this.status = status;
        this.comment = comment;
        this.link = link;
    }
}
