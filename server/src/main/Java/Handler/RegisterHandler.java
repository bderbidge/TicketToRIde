package Handler;

import Command.UpdateGameListResponseCommandData;
import Communication.EncoderDecoder;
import Communication.GamesList;
import Communication.ServerFacade;
import Manager.GameCommandManager;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

/**f
 * Created by autumnchapman on 9/28/17.
 */

public class RegisterHandler extends HandlerBase {

    /**
     * Calls the register method on the ServerFacade
     * @param reqData String
     * @return String of json to representing a LoginRegisterResult
     */
    @Override
    protected String Execute(String reqData){

        System.out.println("in Register");
        EncoderDecoder ED = new EncoderDecoder();
        GameCommandManager M = GameCommandManager.instance();
        try{
            ServerFacade SF = new ServerFacade();
            Object o = ED.decode(new LoginRegisterRequest(), reqData);
            LoginRegisterRequest req = (LoginRegisterRequest) o;
            LoginRegisterResult loginRegisterResult = SF.register(req.getUsername(), req.getPassword());

            M.addToNonStart(req.getUsername());

            String gameID = null;
            GamesList gamelist = SF.getGameList();
            M.addCommandToNon(new UpdateGameListResponseCommandData(gamelist), req.getUsername(), gameID);

            String jsontoRet = ED.encode(loginRegisterResult, "");
            return jsontoRet;

        } catch (Exception e){
            LoginRegisterResult loginRegisterResult = new LoginRegisterResult("Unable to Register");
            String jsontoRet = ED.encode(loginRegisterResult, "");
            return jsontoRet;
        }

    }

}
