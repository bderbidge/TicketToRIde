package Communication;

import android.os.AsyncTask;

import java.util.List;

import Command.ICommand;
import Command.Command;

/**
 * Created by emilyprigmore on 10/4/17.
 */

/*
    This class implements an AsyncTask for sending a command to the server.
 */
public class CommandTask extends AsyncTask<Command, Void, List<ICommand>>{
    protected List<ICommand> doInBackground(Command ... commands) {
        ClientCommunicator CC = ClientCommunicator.instance();
       List<ICommand> commandList = CC.sendCommand(commands[0]);
        return commandList;     // right now this is either UpdateGameState/ListResponseCommand or ErrorCommand
    }


    protected void onPostExecute(List<ICommand> commands) {
        for (ICommand c: commands) {
            c.execute();
        }
    }

    protected void onPostExecute(ICommand command) {
        command.execute();      // when the task is completed, the returning command executes
    }
}
