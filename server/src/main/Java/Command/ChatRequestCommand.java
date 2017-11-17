package Command;


import java.util.List;

import Communication.Chat;
import Communication.CommandList;
import Communication.Game;
import Communication.ServerFacade;
import Manager.GameCommandManager;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class ChatRequestCommand extends ChatRequestCommandData implements ICommand {

    public ChatRequestCommand(){ super();}


    public ChatRequestCommand(String authToken, Chat chatMessage, String gameID){
        super(authToken, chatMessage,gameID);
    }

    @Override
    public List<Command> execute(){

        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        ChatResponseCommandData chatResponse = new ChatResponseCommandData(this.chatMessage);
        SF.logChat(this.gameID, this.chatMessage);
        List<Command> response = manager.addChatCommands(this.gameID, chatResponse);
        return response;
    }

}
