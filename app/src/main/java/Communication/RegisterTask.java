package Communication;

import android.os.AsyncTask;

import Model.CommandFacade;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

/**
 * Created by emilyprigmore on 10/4/17.
 */

/*
    This class implements an AsyncTask for registering a user.
 */
public class RegisterTask extends AsyncTask<LoginRegisterRequest, Void, LoginRegisterResult> {

    protected LoginRegisterResult doInBackground(LoginRegisterRequest... loginRegisterRequests) {
        ClientCommunicator CC = ClientCommunicator.instance();
        LoginRegisterResult loginRegisterResult = CC.register(loginRegisterRequests[0]);
        return loginRegisterResult;
    }

    @Override
    protected void onPostExecute(LoginRegisterResult loginRegisterResult) {
        // sends the result obj to the CommandFacade which will then update the GUI/model
        CommandFacade.instance().resultReturn(loginRegisterResult);
    }
}
