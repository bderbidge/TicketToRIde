package Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

import Command.ICommand;
import Command.Command;
import Command.UpdateGameListResponseCommand;
import Command.UpdateGameStateResponseCommand;
import Command.StartGameResponseCommand;
import Command.ChatResponseCommand;
import Command.OtherDestinationRemovalResponseCommand;
import Command.CallerDestinationRemovalResponseCommand;
import Command.ErrorCommand;
import Command.CallerDrawDestinationResponseCommand;
import Command.OtherDrawDestinationResponseCommand;
import Command.StartTurnResponseCommand;
import Command.*;

/*
    This class opens connections with the server and receives responses from the server as well.
    It has methods for registering and logging in. It also has a method to handle commands being
    sent from the client to the server and then receiving certain kinds of commands in return.
*/

public class ClientCommunicator {

    private static ClientCommunicator mInstance = new ClientCommunicator();

    private String mServerHost = "10.0.2.2"; //local host of android client
    //private String mServerHost = "192.168.1.174"; //local host of android client
    private String mServerPort = "8080";
    private EncoderDecoder mED = new EncoderDecoder();

    public static ClientCommunicator instance() { return mInstance; };

    public void setServerHost(String serverHost) {
        mServerHost = serverHost;
    }

    public void setServerPort(String serverPort) {
        mServerPort = serverPort;
    }

    public String getServerHost() {
        return mServerHost;
    }

    public String getServerPort() {
        return mServerPort;
    }

    /*  This method sends a request to login a user
        It will create the URL but delegate sending the request and receiving the result to other
        methods.
     */
    public LoginRegisterResult login(LoginRegisterRequest loginRegisterRequest) {
        LoginRegisterResult loginRegisterResult;
        try {
            // making the URL
            URL url = new URL("http://" + mServerHost + ":" + mServerPort + "/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.setConnectTimeout(3000); // timing out to see if server up
            //http.setReadTimeout(5000); // timing out to test bad network along the way

            // sending and receiving the http request/response
            sendRequest(http, loginRegisterRequest);
            loginRegisterResult = receiveResult(http);
        } catch (IOException e) {
            // sendRequest and receiveResult throw IOExceptions as well
            loginRegisterResult = new LoginRegisterResult("ClientCommunicator.login(): " +
                    e.getMessage());
        }
        return loginRegisterResult;
    }

    /*  This method sends a request to register a user
        It will create the URL but delegate sending the request and receiving the result to other
        methods.
    */
    public LoginRegisterResult register(LoginRegisterRequest loginRegisterRequest) {
        LoginRegisterResult loginRegisterResult;
        try {
            // making the URL
            URL url = new URL("http://" + mServerHost + ":" + mServerPort + "/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            //http.setConnectTimeout(3000); // timing out to see if server up
            //http.setReadTimeout(5000); // timing out to test bad network along the way

            // sending and receiving the http request/response
            sendRequest(http, loginRegisterRequest);
            loginRegisterResult = receiveResult(http);

        } catch (IOException e) {
            // sendRequest and receiveResult throw IOExceptions as well
            loginRegisterResult = new LoginRegisterResult("ClientCommunicator.register(): " +
                    e.getMessage());
        }
        return loginRegisterResult;
    }

    /*  This method sends a command to the server, right now those commands are create/joinGame and
        PollerCommand
        It will create the URL but delegate sending the request and receiving the result to other
        methods.
     */
    public List<ICommand> sendCommand(Command command) {
        try {
            // making the URL
            URL url = new URL("http://" + mServerHost + ":" + mServerPort + "/command");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.setConnectTimeout(3000); // timing out to see if server up
            //http.setReadTimeout(5000); // timing out to test bad network along the way

            // sending and receiving the http request/response
            sendRequest(http, command);
            return receiveCommand(http);
        } catch (IOException e) {
            // if something went wrong in communication or in the server, an ErrorCommand is returned
            ErrorCommand cmd = new ErrorCommand("ClientCommunicator.receiveCommand(): " +
                    e.getMessage(), false);

            List<ICommand> queue = new ArrayList<>();

            queue.add(cmd);
            return queue;
        }
    }

    /*  This method reads the the contents of a reader to a String.
        It is necessary for reading the contents of the http response body into a string.
     */
    private String readStream(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    /*  This method connects the http connection and writes the request/command object (o) into json
        using the encode method in the EncoderDecoder class.
     */
    private void sendRequest(HttpURLConnection http, Object o) throws IOException {
        try {
            http.connect();

            Writer writer = new OutputStreamWriter(http.getOutputStream());
            String jsonStr = mED.encode(o, "");
            writer.write(jsonStr);
            writer.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /*  This method checks to see if the http response code is 200 OK.
        If so, it will put the http response body into a BufferedReader and then use the readStream
        method to get the response body as a String. It will then decode that response string into a
        LoginRegister reuslt object to return.
     */
    private LoginRegisterResult receiveResult(HttpURLConnection http) {
        LoginRegisterResult loginRegisterResult = new LoginRegisterResult();
        try {
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(http.getInputStream());    // get the resp body
                BufferedReader bReader = new BufferedReader(reader);
                String jsonStr = readStream(bReader);   // get the contents of the resp body as String

                Object o = mED.decode(loginRegisterResult, jsonStr);
                return (LoginRegisterResult)o;
            } else {    // http response code was not 200 OK
                return new LoginRegisterResult(false, Integer.toString(http.getResponseCode()), "", null);
            }
        } catch(IOException e) {
            return new LoginRegisterResult("ClientCommunicator.receiveResult(): " + e.getMessage());
        }
    }

    /*  This method checks to see if the http response code is 200 OK.
        If so, it will put the http response body into a BufferedReader and then use the readStream
        method to get the response body as a String. It will then decode that response string into
        the correct type of command object. For now, the only commands coming back from the server
        are UpdateGameState and UpdateGameListResponse commands.
     */
    private List<ICommand> receiveCommand(HttpURLConnection http) {

        List<ICommand> retList = new ArrayList<>();
        try {
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(http.getInputStream());    // get the resp body
                BufferedReader bReader = new BufferedReader(reader);
                String jsonStr = readStream(bReader);      // get the contents of the resp body as String


                CommandList list = mED.decodeQueue(new CommandList(), jsonStr);
               if(list != null) {
                   List<String> objects = list.getCommands();

                   if (objects != null && objects.size() > 0) {
                       for (int i = 0; i < objects.size(); i++) {


                           Command command = (Command) mED.decode(new Command(), objects.get(i));      // cast as a Command object to get its type
                           String type = command.getType();

                           // give the correct object instance to the EncoderDecoder in order to decode
                           // the json string into the correct Command object
                           if (type.equals("UpdateGameListResponseCommand")) {
                               Object updateGameListObj = mED.decode(new UpdateGameListResponseCommand(), objects.get(i));
                               retList.add((UpdateGameListResponseCommand) updateGameListObj);
                           }
                           if (type.equals("UpdateGameStateResponseCommand")) {
                               Object updateGameStateObj = mED.decode(new UpdateGameStateResponseCommand(), objects.get(i));
                               retList.add((UpdateGameStateResponseCommand) updateGameStateObj);
                           }
                           if (type.equals("StartGameResponseCommand")) {
                               Object StartGameResponseCommandObj = mED.decode(new StartGameResponseCommand(), objects.get(i));
                               retList.add((StartGameResponseCommand) StartGameResponseCommandObj);
                           }
                           if (type.equals("OtherDestinationRemovalResponseCommand")) {
                               Object OtherDestinationRemovalResponseCommandObj = mED.decode(new OtherDestinationRemovalResponseCommand(), objects.get(i));
                               retList.add((OtherDestinationRemovalResponseCommand) OtherDestinationRemovalResponseCommandObj);
                           }
                           if (type.equals("ChatResponseCommand")) {
                               Object ChatResponseCommandObj = mED.decode(new ChatResponseCommand(), objects.get(i));
                               retList.add((ChatResponseCommand) ChatResponseCommandObj);
                           }
                           ;
                           if (type.equals("CallerDestinationRemovalResponseCommand")) {
                               Object CallerDestinationRemovalResponseCommandObj = mED.decode(new CallerDestinationRemovalResponseCommand(), objects.get(i));
                               retList.add((CallerDestinationRemovalResponseCommand) CallerDestinationRemovalResponseCommandObj);
                           }
                           if (type.equals("CallerDrawDestinationResponseCommand")) {
                               Object CallerDrawDestinationResponseCommand = mED.decode(new CallerDrawDestinationResponseCommand(), objects.get(i));
                               retList.add((CallerDrawDestinationResponseCommand) CallerDrawDestinationResponseCommand);
                           }
                           if (type.equals("OtherDrawDestinationResponseCommand")) {
                               Object OtherDrawDestinationResponseCommand = mED.decode(new OtherDrawDestinationResponseCommand(), objects.get(i));
                               retList.add((OtherDrawDestinationResponseCommand) OtherDrawDestinationResponseCommand);
                           }
                           if (type.equals("StartTurnResponseCommand")) {
                               Object StartTurnResponseCommand = mED.decode(new StartTurnResponseCommand(), objects.get(i));
                               retList.add((StartTurnResponseCommand) StartTurnResponseCommand);
                           }
                           if (type.equals("CallerDrawFaceDownTResponseCommand")) {
                               Object CallerDrawFaceDownTResponseCommand = mED.decode(new CallerDrawFaceDownTResponseCommand(), objects.get(i));
                               retList.add((CallerDrawFaceDownTResponseCommand) CallerDrawFaceDownTResponseCommand);
                           }
                           if (type.equals("OtherDrawFaceDownTResponseCommand")) {
                               Object OtherDrawFaceDownTResponseCommand = mED.decode(new OtherDrawFaceDownTResponseCommand(), objects.get(i));
                               retList.add((OtherDrawFaceDownTResponseCommand) OtherDrawFaceDownTResponseCommand);
                           }
                           if (type.equals("CallerDrawFaceUpTResponseCommand")) {
                               Object CallerDrawFaceUpTResponseCommand = mED.decode(new CallerDrawFaceUpTResponseCommand(), objects.get(i));
                               retList.add((CallerDrawFaceUpTResponseCommand) CallerDrawFaceUpTResponseCommand);
                           }
                           if (type.equals("OtherDrawFaceUpTResponseCommand")) {
                               Object OtherDrawFaceUpTResponseCommand = mED.decode(new OtherDrawFaceUpTResponseCommand(), objects.get(i));
                               retList.add((OtherDrawFaceUpTResponseCommand) OtherDrawFaceUpTResponseCommand);
                           }
                           if (type.equals("CallerClaimRouteResponseCommand")) {
                               Object CallerClaimRouteResponseCommand = mED.decode(new CallerClaimRouteResponseCommand(), objects.get(i));
                               retList.add((CallerClaimRouteResponseCommand) CallerClaimRouteResponseCommand);
                           }
                           if (type.equals("OtherClaimRouteResponseCommand")) {
                               Object OtherClaimRouteResponseCommand = mED.decode(new OtherClaimRouteResponseCommand(), objects.get(i));
                               retList.add((OtherClaimRouteResponseCommand) OtherClaimRouteResponseCommand);
                           }
                           if (type.equals("LastRoundResponseCommand")) {
                               Object LastRoundResponseCommand = mED.decode(new LastRoundResponseCommand(), objects.get(i));
                               retList.add((LastRoundResponseCommand) LastRoundResponseCommand);
                           }
                           if (type.equals("EndGameResponseCommand")) {
                               Object EndGameResponseCommand = mED.decode(new EndGameResponseCommand(), objects.get(i));
                               retList.add((EndGameResponseCommand) EndGameResponseCommand);
                           }
                       }

                   }
               }
                return retList;

            } else {    // http response code was not 200 OK
                // if something went wrong in communication or in the server, an ErrorCommand is returned

                ErrorCommand command = new ErrorCommand(Integer.toString(http.getResponseCode()),
                        false);
                retList.add(command);
                return retList;
            }
        } catch (IOException e) {
            // if something went wrong in communication or in the server, an ErrorCommand is returned
            ErrorCommand command = new ErrorCommand("ClientCommunicator.receiveCommand(): " +
                    e.getMessage(), false);
            retList.add(command);
            return retList;
        }
    }
}
