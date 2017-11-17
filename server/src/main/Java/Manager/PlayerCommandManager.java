package Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import Command.ChatResponseCommandData;
import Command.ICommand;
import Command.Command;
import Command.StartGameResponseCommandData;
import Command.UpdateGameListResponseCommandData;
import Command.UpdateGameStateResponseCommandData;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class PlayerCommandManager {

    HashMap<String, List<Command>> lists = new HashMap<>();

    public PlayerCommandManager(){}

    public void addUpdateGameStateCommand(UpdateGameStateResponseCommandData data, String username){
        List<Command> list = lists.get(username);
        list.add(data);
        lists.put(username, list);
    }

    public void addUpdateGameListCommand(UpdateGameListResponseCommandData data, String username){
        List<Command> list = lists.get(username);
        list.add(0, data);
        lists.put(username, list);
    }

    public void addPlayertoGame(String userName){
        List<Command> empty = new ArrayList<>();
        lists.put(userName, empty);
    }

    public void addChatCommands(ChatResponseCommandData data){
        for(String key : lists.keySet()){
            List use = lists.get(key);
            use.add(data);
            lists.put(key, use);

        }
    }

    public void addGSCommand(UpdateGameStateResponseCommandData data){
        for(String key : lists.keySet()){
            List use = lists.get(key);
            use.add(data);
            lists.put(key, use);

        }
    }

    public void addStartGame(List<StartGameResponseCommandData> data){
        for (StartGameResponseCommandData s: data) {
            String userName = s.getUser().getUsername();
            List use = lists.get(userName);
            use.add(s);
            lists.put(userName, use);
        }
    }

    public void addCommands(String userName, List<Command> a){
        Command forUser = a.get(0);
        Command forOther = a.get(1);
        for (Map.Entry<String, List<Command>> e :lists.entrySet()) {
            if(e.getKey().equals(userName)){
                e.getValue().add(forUser);
            } else{
                e.getValue().add(forOther);
            }
        }
    }

    public void addCommands(Command data){
        for(List<Command> value : lists.values()){
            value.add(data);
        }
    }

    //get the commands to return and ensures that the list is empty afterward
    public List<Command> getCommands(String userName){

        List<Command> toEmpty = new ArrayList<>();
        List<Command> commandList = lists.get(userName);


        List<Command> toRet = commandList;
        lists.put(userName, toEmpty);

        return toRet;
    }
}
