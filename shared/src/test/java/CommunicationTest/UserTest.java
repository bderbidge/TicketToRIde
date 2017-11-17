package CommunicationTest;

import org.junit.Before;
import org.junit.Test;

import Communication.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by emmag on 9/27/2017.
 */
public class UserTest {

    private User u = new User("Emily", "pswd");

    @Before
    public void setUp() throws Exception {
        assertNotNull(u);
    }

    @Test
    public void checkPasswordsMatch() {
        String p1 = "pswd";
        String p2 = "pswd";
        String p3 = "psdw";
        String p4 = "notSame";

        assertTrue(u.checkPasswordsMatch(p1, p2));
        assertFalse(u.checkPasswordsMatch(p1, p3));
        assertFalse(u.checkPasswordsMatch(p3, p4));
    }
}