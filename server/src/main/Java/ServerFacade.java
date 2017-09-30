import Communication.IServer;
import RequestResult.LoginRequest;
import RequestResult.RegisterRequest;
import RequestResult.Result;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class ServerFacade implements IServer {

    //private ServerModelRoot SMR;
    //private CommandManager comManager

    public ServerFacade(){

    }

    @Override
    public Result login(User user) {
        //return SMR.login(user)
        return null;
    }

    @Override
    public Result register(User user) {
        //return SMR.login(user)
        return null;
    }

//    public Result login(LoginRequest req){
//        //return SMR.login(req);
//        return null;
//    }
//
//    public Result register(RegisterRequest req){
//        //return SMR.register(req);
//        return null;
//    }


}
