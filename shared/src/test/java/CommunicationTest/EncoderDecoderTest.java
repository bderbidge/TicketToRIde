package CommunicationTest;

import org.junit.Before;
import org.junit.Test;
import java.lang.Object;

import Command.Command;
import Communication.EncoderDecoder;
import Communication.User;
import RequestResult.LoginRegisterResult;
import RequestResult.LoginRegisterRequest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertEquals;
/**
 * Created by emilyprigmore on 9/27/2017.
 */
public class EncoderDecoderTest {

    private EncoderDecoder ED = new EncoderDecoder();

    private class CommandDataTest extends Command {
        private String gameID;
        private boolean gameState;

        public CommandDataTest() {}
        public CommandDataTest(String gameID, boolean gameState) {
            this.gameID = gameID;
            this.gameState = gameState;
        }

        public String getGameID() {return gameID;}
        public void setGameID(String id) {gameID = id;}
        public boolean isGameState() {return gameState;}
        public void setGameState(boolean state) { gameState = state; }
    }
    private class CommandTest extends CommandDataTest {

        public CommandTest() {super();}
        public String execute() {
            return "this command was executed";
        }
    }

    @Before
    public void setUp() throws Exception {
        assertNotNull(ED);
    }

    @Test
    public void encodeTest() throws Exception {
        User u1 = new User("Emily", "pswd");
        String u1Str = ED.encode(u1, "");
        assertEquals(u1Str, "{\"mUsername\":\"Emily\",\"mPassword\":\"pswd\"}");

        User u2 = new User("", "");
        String u2Str = ED.encode(u2, "");
        assertEquals(u2Str, "{\"mUsername\":\"\",\"mPassword\":\"\"}");

        //LoginRegisterRequest req1 = new LoginRegisterRequest(u1);
        //String req1Str = ED.encode(req1,"");
        //assertEquals(req1Str, "{\"mUser\":{\"mUsername\":\"Emily\",\"mPassword\":\"pswd\"}}");

        //LoginRegisterResult res1 = new LoginRegisterResult(false, "this is a test", "12345");
        //String res1Str = ED.encode(res1,"");
        //assertEquals(res1Str, "{\"mSuccess\":false,\"mMessage\":\"this is a test\",\"mAuthToken\":\"12345\"}");
    }

    @Test
    public void decodeResult() throws Exception {
        String r1Str = "{\"mSuccess\":false,\"mMessage\":\"this is a test\",\"mAuthToken\":\"12345\"}";
        Object o1 = ED.decode(new LoginRegisterResult(), r1Str);
        LoginRegisterResult r1 = (LoginRegisterResult)o1;
        assertEquals(r1.getMessage(), "this is a test");
        assertEquals(r1.getAuthToken(), "12345");
        assertFalse(r1.isSuccess());
    }

    @Test
    public void decodeRequest() throws Exception {
        String r1Str = "{\"mUser\":{\"mUsername\":\"Emily\",\"mPassword\":\"pswd\"}}";
        User u1 = new User("Emily", "pswd");
        Object o1 = ED.decode(new LoginRegisterRequest(), r1Str);
        LoginRegisterRequest r1 = (LoginRegisterRequest)o1;
        //assertEquals(r1.getUser().getPassword(), u1.getPassword());
        //assertEquals(r1.getUser().getUsername(), u1.getUsername());
    }

    @Test
    public void decodeCommand() throws Exception {
        CommandDataTest cmdData = new CommandDataTest("this game", true);
        //cmdData.setType("good");
        //CommandTest cmd = new CommandTest();
        //cmd.setGameID("this game");
        //cmd.setGameState(true);
        //cmd.setType("good");
        String json = ED.encode(cmdData, "");
        assertEquals(json, "{\"gameID\":\"this game\",\"gameState\":true,\"type\":\"good\"}");
         //= ED.decode(new Command(), json);

        Object o = ED.decode(new CommandTest(), json);
        CommandTest cmd = (CommandTest)o;
        String type = cmd.getType();
        if (type.equals("good")) {
            Object o2 = ED.decode(new CommandTest(), json);
            CommandTest cmdTest = (CommandTest)o2;
            assertEquals(cmd.getGameID(), cmdTest.getGameID());
            assertEquals(cmd.getType(), cmdTest.getType());
            assertEquals(cmd.isGameState(), cmdTest.isGameState());
        }

        String json2 = "{\"gameID\":\"that game\",\"gameState\":false,\"type\":\"good\"}";
        Object o3 = ED.decode(new Command(), json2);
        Command cmd2 = (Command)o;
        String type2 = cmd2.getType();
        if (type.equals("good")) {
            Object o4 = ED.decode(new CommandTest(), json2);
            CommandTest cmdTest = (CommandTest)o4;
            assertFalse(cmd.getGameID().equals(cmdTest.getGameID()));
            assertEquals(cmd.getType(), cmdTest.getType());
            assertFalse(cmd.isGameState() == cmdTest.isGameState());
        }
    }
}