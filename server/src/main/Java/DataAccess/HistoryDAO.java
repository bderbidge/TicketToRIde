package DataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.History;


/**
 * Created by autumnchapman on 10/17/17.
 */

public class HistoryDAO {
    private ServerModelRoot db;

    public HistoryDAO(ServerModelRoot db){
        this.db = db;
    }

    public void addHistory(String gameID, History h){
        Map<String, List<History>> gameLogs = db.getGameLogs();
        if(gameLogs.containsKey(gameID)){
            List<History> use = gameLogs.get(gameID);
            use.add(h);
            gameLogs.put(gameID, use);
        }
        else{
            List<History> use = new ArrayList<>();
            use.add(h);
            gameLogs.put(gameID, use);
        }
        //db.setGameLogs(gameLogs);
    }
}
