package Communication;

import android.os.AsyncTask;

import Model.CommandFacade;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

/**
 * Created by emilyprigmore on 10/4/17.
 */

/*
    This class implements an AsyncTask for logging in a user.
 */
public class LoginTask extends AsyncTask<LoginRegisterRequest, Void, LoginRegisterResult> {

    protected LoginRegisterResult doInBackground(LoginRegisterRequest... loginRegisterRequests) {
        ClientCommunicator CC = ClientCommunicator.instance();
        LoginRegisterResult loginRegisterResult = CC.login(loginRegisterRequests[0]);
        return loginRegisterResult;
    }

    protected void onPostExecute(LoginRegisterResult loginRegisterResult) {
        // sends the result obj to the CommandFacade which will then update the GUI/model
        CommandFacade.instance().resultReturn(loginRegisterResult);
    }
}

