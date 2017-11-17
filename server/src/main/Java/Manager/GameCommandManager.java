package Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Command.ChatResponseCommandData;
import Command.ICommand;
import Command.Command;
import Command.StartGameResponseCommandData;
import Command.UpdateGameListResponseCommandData;
import Command.UpdateGameStateResponseCommandData;
import Command.LastRoundResponseCommandData;
import Communication.CommandList;
import Communication.History;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class GameCommandManager {

    //GameIDs to PlayerCommandManager
    private HashMap<String, PlayerCommandManager> playerManagers = new HashMap<>();
    //userNames to Lists of Commands
    private HashMap<String, List<Command>> nonStart = new HashMap<>();

    private static GameCommandManager mInstance = new GameCommandManager();
    public static GameCommandManager instance() { return mInstance; };

    public GameCommandManager(){}



    public List<Command> Poll(String userName, String gameID){
        List<Command> toRet;
        List<Command> empty = new ArrayList<>();

        if(gameID == null){

            toRet = nonStart.get(userName);
            //nonStart.put(userName, empty);
            return toRet;
        }
        else{
            toRet = getCommands(gameID, userName);
            return toRet;
        }
    }

    public void addToNonStart(String userName){
        List<Command> empty = new ArrayList<>();
        if(!nonStart.containsKey(userName)){
            nonStart.put(userName, empty);
        }
    }

    public void addCommandToNon(UpdateGameListResponseCommandData data, String userName, String gameID){
       if(gameID!= null) {

           PlayerCommandManager pcm = playerManagers.get(gameID);
           pcm.addUpdateGameListCommand(data, userName);
           //TODO if a player is logged in and they were in a game before then we need to have the client receive an update game list and then an update game state.
       }else {
           List<Command> commands = nonStart.get(userName);
           commands.add(0, data);
       }
    }

    public void addUpdateGameState(UpdateGameStateResponseCommandData data, String username, String gameID){

        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addUpdateGameStateCommand(data,username);
    }

    public List<Command> addGameCreated(UpdateGameListResponseCommandData data, String userName, String gameID){
        List<Command> toReturn;
        for(String key : nonStart.keySet()){
            List use = nonStart.get(key);
            use.add(data);
            nonStart.put(key, use);
        }
        addGame(gameID);
        toReturn = nonStart.get(userName);

        return toReturn;
    }

    public List<Command> addJoinGame(UpdateGameStateResponseCommandData data, String userName, String gameID){
        List<Command> toReturn = new ArrayList<>();
        //remove players from other map?
        for(String key : nonStart.keySet()){
            List use = nonStart.get(key);
            use.add(data);
            nonStart.put(key, use);
        }
        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addPlayertoGame(userName);
        pcm.addGSCommand(data);
        toReturn = pcm.getCommands(userName);
        nonStart.remove(userName);


        return toReturn;
    }

    public List<Command> addStartGame(List<StartGameResponseCommandData> data, String userName, String gameID){
        List<Command> toReturn;
        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addStartGame(data);
        toReturn = pcm.getCommands(userName);


        return toReturn;
    }

    private void addGame(String gameID){
        playerManagers.put(gameID, new PlayerCommandManager());
    }

    public List<Command> addCommands(String gameID, String userName, List<Command> a){
        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addCommands(userName, a);
        List<Command> toRet = pcm.getCommands(userName);

        return toRet;
    }

    public void addLastRoundCommands(String gameID, LastRoundResponseCommandData data){
        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addCommands(data);
    }

    public List<Command> addChatCommands(String gameID, ChatResponseCommandData data){
        if(!playerManagers.containsKey(gameID)){
            addGame(gameID);
        }
        PlayerCommandManager pcm = playerManagers.get(gameID);
        pcm.addChatCommands(data);
        List<Command> toRet = pcm.getCommands(data.getChat().getUserName());
        return toRet;
    }

    public List<Command> getCommands(String gameID, String userName){
        if(!playerManagers.containsKey(gameID)){
            addGame(gameID);
        }
        PlayerCommandManager pcm = playerManagers.get(gameID);
        List<Command> toRet = pcm.getCommands(userName);
        return toRet;
    }
}
