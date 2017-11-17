package Handler;

import Command.UpdateGameListResponseCommandData;
import Command.UpdateGameStateResponseCommandData;
import Communication.EncoderDecoder;
import Communication.GameState;
import Communication.GamesList;
import Communication.ServerFacade;
import Manager.GameCommandManager;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class LoginHandler extends HandlerBase {

    /**
     * Calls the login method on the ServerFacade
     * @param reqData String
     * @return String of json to representing a LoginRegisterResult
     */
    @Override
    protected String Execute(String reqData){

        EncoderDecoder ED = new EncoderDecoder();
        GameCommandManager M = GameCommandManager.instance();
        try{
            System.out.println("in loginhandler");
            ServerFacade SF = new ServerFacade();
            Object o = ED.decode(new LoginRegisterRequest(), reqData);
            LoginRegisterRequest req = (LoginRegisterRequest) o;
            LoginRegisterResult loginRegisterResult = SF.login(req.getUsername(), req.getPassword());

            M.addToNonStart(req.getUsername());

            GamesList gamelist = SF.getGameList();
            String gameID = SF.getGameID(req.getUsername());

            M.addCommandToNon(new UpdateGameListResponseCommandData(gamelist), req.getUsername(), gameID);

            if(gameID != null)
            {
                GameState state = SF.getGameState(gameID);
                M.addUpdateGameState(new UpdateGameStateResponseCommandData(gameID, state), req.getUsername(), gameID);
            }

            String jsontoRet = ED.encode(loginRegisterResult, "");
            return jsontoRet;

        } catch (Exception e){

            LoginRegisterResult loginRegisterResult = new LoginRegisterResult("Failed to login");

            String jsontoRet = ED.encode(loginRegisterResult, "");
            return jsontoRet;
        }

    }

}
