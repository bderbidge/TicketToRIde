import com.google.gson.JsonObject;

import java.io.StringReader;

import Communication.Decoder;
import Communication.Encoder;
import RequestResult.RegisterRequest;
import RequestResult.Result;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class RegisterHandler extends HandlerBase {

    @Override
    protected JsonObject Execute(String reqData){

        Encoder E = new Encoder();
        Decoder D = new Decoder();
        //do I need a try catch here, what can fail?
        try{
            ServerFacade SF = new ServerFacade();
            RegisterRequest req = D.decodeLoginRegister(new StringReader(reqData));
            Result result = SF.register(req.getUser());
            JsonObject jsontoRet = E.encode(result);
            return jsontoRet;

        } catch (Exception e){
            Result result = new Result(false, null, null);
            JsonObject jsontoRet = E.encode(result);
            return jsontoRet;
        }

    }

}
