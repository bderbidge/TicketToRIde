package DataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.Chat;


/**
 * Created by autumnchapman on 10/17/17.
 */

public class ChatDAO {

    private ServerModelRoot db;

    public ChatDAO(ServerModelRoot db){
        this.db = db;
    }

    public void addChat(String gameID, Chat c){
        Map<String, List<Chat>> conversations = db.getConversations();
        if(conversations.containsKey(gameID)){
            List<Chat> use = conversations.get(gameID);
            use.add(c);
            conversations.put(gameID, use);
        }
        else{
            List<Chat> use = new ArrayList<>();
            use.add(c);
            conversations.put(gameID, use);
        }
        //db.setConversations(conversations);
    }
}
