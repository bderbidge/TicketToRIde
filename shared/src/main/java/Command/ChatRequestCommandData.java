package Command;

import Communication.Chat;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class ChatRequestCommandData extends Command {

    String authToken;
    Chat chatMessage;
    String gameID;

    public ChatRequestCommandData(String authToken, Chat chatMessage, String gameID) {
        this.setType("ChatRequestCommand");
        this.authToken = authToken;
        this.chatMessage = chatMessage;
        this.gameID = gameID;
        setSuccess(true);
    }

    public ChatRequestCommandData() {
        this.setType("ChatRequestCommand");
    }

    public ChatRequestCommandData(String message) {
        this.setType("ChatRequestCommand");
        setMessage(message);
        setSuccess(false);
    }

    public String getAuthToken() {
        return authToken;
    }

    public Chat getChatMessage() {
        return chatMessage;
    }

    public String getGameID() {
        return gameID;
    }
}
