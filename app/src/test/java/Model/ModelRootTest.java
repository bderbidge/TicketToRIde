package Model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by emmag on 9/27/2017.
 */
public class ModelRootTest {
    ModelRoot mModelRoot;

    @Before
    public void setUp() throws Exception {
        mModelRoot = ModelRoot.instance();
        assertNotNull(mModelRoot);
    }

    @Test
    public void basicCase() {
        assertFalse(mModelRoot.hasAuthToken());
        mModelRoot.setAuthToken("sample token");
        assertTrue(mModelRoot.hasAuthToken());
    }

}