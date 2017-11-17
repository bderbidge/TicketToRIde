package Command;

import Communication.Chat;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class ChatResponseCommandData extends Command {

    Chat chat;

    public ChatResponseCommandData(){ this.setType("ChatResponseCommand");}

    public ChatResponseCommandData(Chat chat) {
        this.setType("ChatResponseCommand");
        this.setSuccess(true);
        this.chat = chat;
    }

    public ChatResponseCommandData(String mess) {
        setSuccess(false);
        setMessage(mess);
        this.setType("ChatResponseCommand");
    }

    public Chat getChat() {
        return chat;
    }
}
