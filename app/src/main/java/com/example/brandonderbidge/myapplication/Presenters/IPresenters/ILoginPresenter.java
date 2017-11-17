package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

/**
 * Created by pbstr on 9/28/2017.
 */

public interface ILoginPresenter {

    /**
     * Sets the servers IP and port to connect to the right server
     * @param serverIP
     * @param serverPort
     */
    void setServerIPPort(String serverIP, String serverPort);
    /**
     * notifies the presenter that login has been pressed, the login interface must ask the
     * views for the username and password
     */
    void login();
    /**
     * notifies the presenter that register has been pressed, the login interface must get the
     * register username, password, and confirm password
     */
    void register();
}
