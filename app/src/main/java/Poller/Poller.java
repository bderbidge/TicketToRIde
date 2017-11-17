package Poller;

import android.os.AsyncTask;
import android.os.Handler;

import java.net.URL;
import java.util.*;



import Command.ICommand;

import Communication.ClientCommunicator;
import Command.PollerCommandData;
import Model.GUIFacade;
import Model.IGUIFacade;


public class Poller {

    Timer timer;


    IGUIFacade mGUIFacade;

    public Poller() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        mGUIFacade = GUIFacade.instance();

        //This creates a new async task.

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {


                            new GameStatus().execute();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(doAsynchronousTask, 2000, 5000); //execute in every 50000 ms
    }
    public class GameStatus extends AsyncTask<URL, Void,  List<ICommand>> {


        protected List<ICommand> doInBackground(URL... urls) {

            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            //This sends a poller command to the client communicator
            PollerCommandData pollerCommand = new PollerCommandData(mGUIFacade.getAuthToken(), mGUIFacade.getCurrentGameVersion());

            List<ICommand> commands = ClientCommunicator.instance().sendCommand(pollerCommand);

            return commands;
        }

        protected void onPostExecute(List<ICommand> commands) {

            if (commands != null) {
                for (ICommand command : commands) {
                    command.execute();
                }
            }

        }
    }
}