package Communication;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class History {

    private String histMessage;
    private String userName;
    private String color;


    public History(){}

    public History(String mess, String user, String col){
        this.histMessage = mess;
        this.userName = user;
        this.color = col;
    }

    public String getHistMessage() {
        return histMessage;
    }

    public String getUserName() {
        return userName;
    }

    public String getColor() {
        return color;
    }

    public void setHistMessage(String histMessage) {
        this.histMessage = histMessage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
