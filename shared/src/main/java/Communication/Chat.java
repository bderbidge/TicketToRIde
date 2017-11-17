package Communication;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class Chat {

    private String chatMessage;
    private String userName;
    private String color;

    public Chat(){}

    public Chat(String message, String user, String col){
        this.chatMessage = message;
        this.userName = user;
        this.color = col;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getUserName() {
        return userName;
    }

    public String getColor() {
        return color;
    }
}
