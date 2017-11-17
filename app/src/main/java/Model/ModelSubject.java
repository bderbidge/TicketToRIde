package Model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by emmag on 9/27/2017.
 */

public class ModelSubject extends Observable{

    private List<Observer> mObservers;

    public ModelSubject() {
        mObservers = new ArrayList<>();
    }

    /**
     * attach an observer to the Client Model
     * @param observer observer to attach
     */
    public void attach(Observer observer) {
        if(observer != null) {
            mObservers.add(observer);
        }
    }

    /**
     * detach an observer from the Client Model
     * @param observer observer to detach
     */
    public void detach(Observer observer) {
        if(observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    /**
     * notify observers of a change to model
     */
    public void notifyObservers() {
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).update(this, "update"); //don't know why you have to pass in an argument??
        }
    }

    /**
     * notify the observers of an error
     * @param error error message to be reported
     */
    public void notifyObserversOfError(String error) {
        for(Observer observer : mObservers) {
            observer.update(this, error);
        }
    }

    /**
     * get observer list
     * @return Set of observers
     */
    public List<Observer> getObservers() {
        return mObservers;
    }

    /**
     * get the number of observers the Model has
     * @return int
     */
    public int getNumObservers() {
        return mObservers.size();
    }

}
