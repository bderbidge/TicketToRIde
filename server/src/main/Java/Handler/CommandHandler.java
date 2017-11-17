package Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import Command.Command;
import Command.CreateGameCommand;
import Command.JoinGameCommand;
import Command.*;
import Communication.CommandList;
import Communication.EncoderDecoder;
import Manager.GameCommandManager;

/**
 * Created by autumnchapman on 9/30/17.
 */

public class CommandHandler extends HandlerBase {

    /**
     * Takes in and decodes a string to a command to check its type so it can be decoded into a more specific command
     * @param reqData String from the client
     * @return Json String representing a Command to return to the Client
     */
    @Override
    protected String Execute(String reqData){
        EncoderDecoder ED = new EncoderDecoder();
        Command command = (Command)ED.decode(new Command(), reqData);
        String jsontoRet;

        if(command.getType().equals("PollerCommand")) {
            PollerCommand pCommand = (PollerCommand) ED.decode(new PollerCommand(), reqData);
            List<Command> cmd = pCommand.execute();
            //Queue<ICommand> response = manager.addPollCommand(pCommand, cmd);
             jsontoRet = ED.encode(commandToJSON(cmd), "");
            return jsontoRet;
        }
        if(command.getType().equals("CreateGameCommand")){
            CreateGameCommand cgCommand = (CreateGameCommand)ED.decode(new CreateGameCommand(), reqData);
            List<Command> toRet = cgCommand.execute();
             jsontoRet = ED.encode(commandToJSON(toRet), "");

            return jsontoRet;
        }
        if(command.getType().equals("JoinGameCommand")){
            JoinGameCommand jgCommand = (JoinGameCommand)ED.decode(new JoinGameCommand(), reqData);
            List<Command> cmd = jgCommand.execute();
             jsontoRet = ED.encode(commandToJSON(cmd), "");

            return jsontoRet;
        }
        if(command.getType().equals("StartGameCommand")){
            StartGameRequestCommand sCommand = (StartGameRequestCommand)ED.decode(new StartGameRequestCommand(), reqData);
            List<Command> cmds = sCommand.execute();
            jsontoRet = ED.encode(commandToJSON(cmds), "");

            return jsontoRet;
        }
        if(command.getType().equals("DestinationRemovalRequestCommand")){
            DesintationRemovalRequestCommand drCommand = (DesintationRemovalRequestCommand)ED.decode(new DesintationRemovalRequestCommand(), reqData);
            ArrayList returned = (ArrayList<Object>)drCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        if(command.getType().equals("ChatRequestCommand")){
            ChatRequestCommand cCommand = (ChatRequestCommand)ED.decode(new ChatRequestCommand(), reqData);
            List<Command> returned = cCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        if(command.getType().equals("DrawFaceUpTCardRequestCommand")){
            DrawFaceUpTCardRequestCommand dtuCommand = (DrawFaceUpTCardRequestCommand)ED.decode(new DrawFaceUpTCardRequestCommand(), reqData);
            List<Command> returned = dtuCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        if(command.getType().equals("DrawFaceDownTCardRequestCommand")){
            DrawFaceDownTCardRequestCommand dtdCommand = (DrawFaceDownTCardRequestCommand)ED.decode(new DrawFaceDownTCardRequestCommand(), reqData);
            List<Command> returned = dtdCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        if(command.getType().equals("DrawDestinationRequestCommand")){
            DrawDestinationRequestCommand ddCommand = (DrawDestinationRequestCommand)ED.decode(new DrawDestinationRequestCommand(), reqData);
            List<Command> returned = ddCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        if(command.getType().equals("EndTurnRequestCommand")){
            EndTurnRequestCommand ddCommand = (EndTurnRequestCommand)ED.decode(new EndTurnRequestCommand(), reqData);
            List<Command> returned = ddCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }

        if(command.getType().equals("ClaimRouteRequestCommand")) {
            ClaimRouteRequestCommand ddCommand = (ClaimRouteRequestCommand)ED.decode(new ClaimRouteRequestCommand(), reqData);
            List<Command> returned = ddCommand.execute();
            jsontoRet = ED.encode(commandToJSON(returned), "");

            return jsontoRet;
        }
        return null;
    }


    private CommandList commandToJSON(List<Command> commands){

        List<String> listOfCommandStrings = new ArrayList<>();
        String json = "";
        for (Command cmmd:commands) {
            listOfCommandStrings.add(EncoderDecoder.encode(cmmd, json));

        }

        return new CommandList(listOfCommandStrings);
    }

}
