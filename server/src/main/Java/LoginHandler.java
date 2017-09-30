import com.google.gson.JsonObject;

import java.io.StringReader;

import Communication.Decoder;
import Communication.Encoder;
import RequestResult.LoginRequest;
import RequestResult.Request;
import RequestResult.Result;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class LoginHandler extends HandlerBase {

    @Override
    protected JsonObject Execute(String reqData){

        Encoder E = new Encoder();
        Decoder D = new Decoder();
        //do I need a try catch here, what can fail?
        try{
            ServerFacade SF = new ServerFacade();
            //Request req = D.decodeLoginRegisterRequest?(new StringReader(reqData));
            Result result = SF.login(req.getUser());
            //Results result = new Results(true, null, null);
            JsonObject jsontoRet = E.encode(result);
            return jsontoRet;

        } catch (Exception e){
            Result result = new Result(false, null, null);
            JsonObject jsontoRet = E.encode(result);
            return jsontoRet;
        }

    }

}
