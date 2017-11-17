package Command;

/**
 * Created by autumnchapman on 9/30/17.
 */

public class Command {

    private String type;
    private String message;
    private boolean success;

    /**
     * public constructor, initialized some of the data members as needed
     */
    public void Command(){
        message = "";
        success = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.setSuccess(false);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void Command(String t){
        this.type = t;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
