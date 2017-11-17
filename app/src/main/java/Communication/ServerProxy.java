package Communication;

import Command.Command;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

public class ServerProxy implements IServer {

    private ClientCommunicator mComm;

    public ServerProxy() {
        mComm = ClientCommunicator.instance();
    }

    public ServerProxy(String serverIP, String serverPort) {
        mComm = ClientCommunicator.instance();
        mComm.setServerHost(serverIP);
        mComm.setServerPort(serverPort);
    }

    public void setServerIP (String serverIP) {
        mComm.setServerHost(serverIP);
    }

    public void setServerPort(String serverPort) {
        mComm.setServerPort(serverPort);
    }

    /*  This method creates a LoginRegisterRequest and then starts a LoginTask to be
        completed asynchronously.
        It implements IServer and so the method must return an object, however it will always return
        null. Future work will be to fix the async task such that it returns a result object to
        this method.
     */
    @Override
    public LoginRegisterResult login(String username, String password) {
        LoginRegisterRequest loginRegisterRequest = new LoginRegisterRequest(username, password);
        LoginTask loginTask= new LoginTask();
        loginTask.execute(loginRegisterRequest);
        return null;
    }

    /*  This method creates a LoginRegisterRequest and then starts a RegisterTask to be
        completed asynchronously.
        It implements IServer and so the method must return an object, however it will always return
        null. Future work will be to fix the async task such that it returns a result object to
        this method.
     */
    @Override
    public LoginRegisterResult register(String username, String password) {
        LoginRegisterRequest loginRegisterRequest = new LoginRegisterRequest(username, password);
        new RegisterTask().execute(loginRegisterRequest);
        return null;
    }

    /*  This method receives a Command obj and then starts a CommandTask to be completed
        asynchronously. It passes the Command it receives to the async task.
        It implements IServer and so the method must return an object, however it will always return
        null. Future work will be to fix the async task such that it returns a result object to
        this method.
     */
    @Override
    public Command command(Command command) {
        new CommandTask().execute(command);
        return null;
    }
}

