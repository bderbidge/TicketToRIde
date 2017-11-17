package RequestResult;

import Communication.User;

public class LoginRegisterResult {

    private boolean mSuccess;
    private String mMessage;
    private String mAuthToken;
    private User mUser;

    public LoginRegisterResult() {}

    public LoginRegisterResult(boolean success, String message, String authToken, User user) {
        mSuccess = success;
        mMessage = message;
        mAuthToken = authToken;
        mUser = user;
    }

    public LoginRegisterResult(String message) {
        mSuccess = false;
        mMessage = message;
    }

    public LoginRegisterResult(String authToken, User user) {
        mSuccess = true;
        mAuthToken = authToken;
        mUser = user;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getAuthToken() { return mAuthToken; }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
