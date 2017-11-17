package CommunicationTest;

import org.junit.Before;
import org.junit.Test;

import Communication.ClientCommunicator;
import Communication.User;
import RequestResult.LoginRegisterRequest;
import RequestResult.LoginRegisterResult;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

/**
 * Created by emilyprigmore on 9/27/2017.
 */
public class ClientCommunicatorTest {

    private ClientCommunicator CC = ClientCommunicator.instance();

    @Before
    public void setUp() throws Exception {
        assertNotNull(CC);
    }

    @Test
    public void loginTest() {
        User u1 = new User("Emily", "1234");
        LoginRegisterRequest r1 = new LoginRegisterRequest(u1.getUsername(), u1.getPassword());
        LoginRegisterResult loginRegisterResult = CC.login(r1);
        assertNotNull(loginRegisterResult);
        //assertFalse(loginRegisterResult.getAuthToken().equals(""));
        //assertFalse(loginRegisterResult.getMessage().equals(""));
        //assertTrue(loginRegisterResult.isSuccess());
    }

    @Test
    public void registerTest() {
        User u1 = new User("Emily", "1234");
        LoginRegisterRequest r1 = new LoginRegisterRequest(u1.getUsername(), u1.getPassword());
        LoginRegisterResult loginRegisterResult = CC.login(r1);
        assertNotNull(loginRegisterResult);
        //assertFalse(loginRegisterResult.getAuthToken().equals(""));
        //assertFalse(loginRegisterResult.getMessage().equals(""));
        //assertTrue(loginRegisterResult.isSuccess());
    }

    @Test
    public void sendRequestTest() {}

    @Test
    public void receiveResultTest() {}
}