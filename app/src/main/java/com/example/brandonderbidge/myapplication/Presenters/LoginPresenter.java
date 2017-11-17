package com.example.brandonderbidge.myapplication.Presenters;

import android.widget.Toast;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ILoginPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.ILoginView;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import Dagger.BasicInjectionComponent;
import Dagger.UITestModule;
import Model.Exceptions.InvalidOperationException;
import Model.Exceptions.PasswordMismatchException;
import Model.GUIFacade;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by pbstr on 9/28/2017.
 */

public class LoginPresenter implements ILoginPresenter, Observer {

    private ILoginView mView;
    private ModelSubject mModelSubject; // TODO: make this an interface
    private IGUIFacade mGUIFacade;

    @Inject
    public LoginPresenter(ILoginView view, IGUIFacade facade) {
        mView = view;
        mGUIFacade = facade;
        mModelSubject = ModelRoot.instance(); //TODO: make this an interface and have it passed in the constructor
        mModelSubject.attach(this);



        //this will start skipping screens if user is already logged in
        update(mModelSubject, "update");
    }

    public void setServerIPPort(String serverIP, String serverPort) {
        mGUIFacade.setServerInfo(serverIP, serverPort);
    }

    public void login() {

        //gets the userName and password from the views
        String userName = mView.getLoginUserName();
        String passWord = mView.getLoginPassword();

        try {
            //sends login information to the models for verification and logging in
            mGUIFacade.login(userName, passWord);
        }
        catch (InvalidOperationException ex) { //this exception will be thrown by the facade if an error occurs like bad password
            ex.printStackTrace();
            mView.DisplayMessage("Caught an InvalidOperationException");
        }

    }



    public void register() {
        //gets register info from the views
        String registerUserName = mView.getRegisterUserName();
        String registerPassword = mView.getRegisterPassword();
        String registerPasswordConfirm = mView.getRegisterPasswordConfirm();

        try {
            //sends info to the facade to give the information to the model
            GUIFacade.instance().register(registerUserName, registerPassword, registerPasswordConfirm);
        }
        catch (InvalidOperationException ex) { //happens if some bad password/login given
            ex.printStackTrace();
            mView.DisplayMessage("Caught an InvalidOperationException while registering");
        }
        catch (PasswordMismatchException ex) { //happens if the passwords are matching
            ex.printStackTrace();
            mView.DisplayMessage("The passwords do not match");
        }

    }

    public void update(Observable observable, Object object) {
        String msg = (String) object;
        if (msg.contains("update")) {
            if (GUIFacade.instance().isLoggedIn()) {
                mModelSubject.detach(this);
                mView.StartJoinGameView(GUIFacade.instance().getGamesList());
            }
        } else {
            mView.DisplayMessage(msg);
        }

    }

}
