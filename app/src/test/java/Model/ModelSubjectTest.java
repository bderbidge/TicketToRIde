package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;


/**
 * Created by emmag on 9/27/2017.
 */
public class ModelSubjectTest {
    private ModelSubject mModelSubject;


    public class TestObserver implements Observer {
        private int updateCount;
        @Override
        public void update(Observable observable, Object o) {
            updateCount++;
        }

        public int getUpdateCount() {
            return updateCount;
        }
    }

    @Before
    public void setUp() throws Exception {
        mModelSubject = new ModelSubject();
        assertNotNull(mModelSubject);
        assertNotNull(mModelSubject.getObservers());
    }

    @Test
    public void basicCase() throws Exception {
        assertTrue(mModelSubject.getNumObservers() == 0);

        TestObserver a = new TestObserver();
        mModelSubject.attach(a);
        assertTrue(mModelSubject.getNumObservers() == 1);

        TestObserver b = new TestObserver();
        mModelSubject.attach(b);
        assertTrue(mModelSubject.getNumObservers() == 2);

        mModelSubject.notifyObservers();
        assertTrue(a.getUpdateCount() == 1);
        assertTrue(b.getUpdateCount() == 1);

        mModelSubject.detach(a);
        assertTrue(mModelSubject.getNumObservers() == 1);

        mModelSubject.notifyObservers();
        assertTrue(a.getUpdateCount() == 1);
        assertTrue(b.getUpdateCount() == 2);
    }

    @Test
    public void nullCase() throws Exception {
        assertTrue(mModelSubject.getNumObservers() == 0);

        TestObserver a = null;
        mModelSubject.attach(a);
        assertTrue(mModelSubject.getNumObservers() == 0);

        mModelSubject.notifyObservers();

        mModelSubject.detach(a);
        assertTrue(mModelSubject.getNumObservers() == 0);
    }

    @Test
    public void duplicates() throws Exception {
        assertTrue(mModelSubject.getNumObservers() == 0);

        TestObserver a = new TestObserver();
        mModelSubject.attach(a);
        assertTrue(mModelSubject.getNumObservers() == 1);

        mModelSubject.attach(a);
        assertTrue(mModelSubject.getNumObservers() == 1);

        mModelSubject.notifyObservers();
        assertTrue(a.getUpdateCount() == 1);

        mModelSubject.detach(a);
        assertTrue(mModelSubject.getNumObservers() == 0);
        mModelSubject.detach(a);
        assertTrue(mModelSubject.getNumObservers() == 0);
    }

}