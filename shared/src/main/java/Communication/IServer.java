package Communication;

import RequestResult.LoginRegisterResult;
import Command.Command;

public interface IServer {

    // This function will log a user in and return a LoginRegisterResult object
    public LoginRegisterResult login(String username, String password);

    // This function will register a user and return a LoginRegisterResult object
    public LoginRegisterResult register(String username, String password);

    public Command command(Command command);
}
