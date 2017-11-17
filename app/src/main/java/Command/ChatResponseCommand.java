package Command;

import Communication.Chat;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class ChatResponseCommand extends ChatResponseCommandData implements ICommand{

    public ChatResponseCommand() {
    }

    public ChatResponseCommand(Chat chat) {
        super(chat);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().addChat(chat);
        return null;
    }
}
