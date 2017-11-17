package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.Player;
import Communication.Route;
import Communication.ServerFacade;
import Communication.User;
import Manager.GameCommandManager;
import Command.ClaimRouteRequestCommandData;
import Command.OtherClaimRouteResponseCommandData;
import Command.CallerClaimRouteResponseCommandData;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class ClaimRouteRequestCommand extends ClaimRouteRequestCommandData implements ICommand{

    public ClaimRouteRequestCommand(){
        super();
    }

    public ClaimRouteRequestCommand(String auth, String id, Route r, String color){
        super(auth, id, r, color);
    }

    @Override
    public List<Command> execute(){
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(authToken);
        SF.claimRoute(route, gameID, username, claimingColor);

        Game game = SF.getGame(getGameID());
        GameState state = game.getCurrentGameState();
        List<String> longUsers = state.getLongestUser();
        Player player = state.getPlayer(username);
        User user = SF.getUser(username);
        String message = username + " claimed a route from " + route.getCity1() + " to " + route.getCity2();
        History h = new History(message, username, player.getColor());

        CallerClaimRouteResponseCommandData Ccmmd = new CallerClaimRouteResponseCommandData(
                route, user.getTrainCards(), game.getTrainDeckCount(), longUsers, h);
        OtherClaimRouteResponseCommandData Ocmmd = new OtherClaimRouteResponseCommandData(
                username, route, game.getTrainDeckCount(), longUsers, h);

        List<Command> list = new ArrayList<>();
        list.add(Ccmmd);
        list.add(Ocmmd);

        List<Command> toRet = manager.addCommands(getGameID(),username,list );
        return toRet;
    }
}
