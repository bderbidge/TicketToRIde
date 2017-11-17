package Model.Exceptions;

/**
 * Created by pbstr on 10/18/2017.
 */

/**
 * exception thrown when an action is attempted that is not valid based off the state of the model
 */
public class InvalidOperationException extends Exception {
    private String mOperationType;
    public InvalidOperationException(String operationType) {
        mOperationType = operationType;
    }
    public String getOperationType() {
        return mOperationType;
    }
}

