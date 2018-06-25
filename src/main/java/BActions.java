public class BActions {
    public int user_id;
    public String action;
    public String status;
    public String comment;
    public String link;
    public static String[] actions = new String[]{
            "baction_bitcointalk",
            "baction_telegram",
            "baction_slack",
            "baction_twitter",
            "baction_facebook",
            "baction_instagram",
            "baction_reddit",
            "baction_linkedin",
            "baction_medium",
            "baction_blog",
            "baction_youtube",
            "baction_media",
            "baction_website1000users",
            "baction_extraordinary"};
    public static int getId(String bactionName){
        for(int i = 0;i< actions.length;i++)
            if(actions[i].equals(bactionName)) return i;
        return -1;
    }
    public int getId() {
        return getId(action);
    }
    public BActions(int user_id, String action, String status, String comment, String link) {
        this.user_id = user_id;
        this.action = action;
        System.out.println(action);
        this.status = status;
        this.comment = comment;
        this.link = link;
    }
}
