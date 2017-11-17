package Command;

import Model.CommandFacade;

/**
 * Created by emilyprigmore on 10/6/17.
 */

/*  This class is to hold error information received from the server. If something goes wrong in the
    server, an instance of this class is created in the ClientCommunicator in order to communicate
    to the client and the model that something went wrong with the command sent to the server.
 */
public class ErrorCommand extends Command implements ICommand {

    /**
     * constructor for an error command
     * @param message the error message produced
     * @param success
     */

    public ErrorCommand(String message, boolean success) {
        super();
        this.setType("ErrorCommand");
        this.setMessage(message);
        this.setSuccess(success);
    }

    /**
     * notifies the caller that an error occurred and gives the error message
     * @return nothing
     */
    @Override
    public Object execute() {
        CommandFacade.instance().notifyOfCommandError(getMessage());
        return null;
    }
}
