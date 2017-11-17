package com.example.brandonderbidge.myapplication.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ILoginPresenter;
import com.example.brandonderbidge.myapplication.Presenters.LoginPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.ILoginView;

import javax.inject.Inject;

import Communication.GamesList;
import Dagger.MyApplication;
import Dagger.UITestModule;
import Model.GUIFacade;
import Model.GUIFacadeUITest;
import Model.GUIFacade_Phase2;

/**
 * Created by pbstr on 9/28/2017.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView {

    //uses interace to be able to test/create different implementations if needed
    @Inject
    ILoginPresenter mLoginPresenter;

    //all widgets
    private EditText mServerIPEditText;
    private EditText mServerPortEditText;
    private EditText mLoginUserNameEditText;
    private EditText mLoginPasswordEditText;
    private EditText mRegisterUserNameEditText;
    private EditText mRegisterPasswordEditText;
    private EditText mRegisterPasswordConfirmEditText;
    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication.instance().setILoginView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        //get all the widgets references
        mServerIPEditText = (EditText) findViewById(R.id.ip_edit);
        mServerPortEditText = (EditText) findViewById(R.id.port_edit);
        mLoginUserNameEditText = (EditText) findViewById(R.id.login_username_edit);
        mLoginPasswordEditText = (EditText) findViewById(R.id.login_password_edit);
        mRegisterUserNameEditText = (EditText) findViewById(R.id.register_username_edit);
        mRegisterPasswordEditText = (EditText) findViewById(R.id.register_password_edit);
        mRegisterPasswordConfirmEditText = (EditText) findViewById(R.id.register_password_confirm_edit);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setServerInfo();
                mLoginPresenter.login(); //call the presenters login when login button is pressed
            }
        });
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setServerInfo();
                mLoginPresenter.register(); // call the presenters register when register button is pressed
            }
        });

    }

    private void setServerInfo() {
        String serverIP = mServerIPEditText.getText().toString();
        String serverPort = mServerPortEditText.getText().toString();

        mLoginPresenter.setServerIPPort(serverIP, serverPort);
    }

    //presenter will call these methods to get info to login/register
    public String getLoginUserName(){
        return mLoginUserNameEditText.getText().toString();
    }
    public String getLoginPassword() {
        return mLoginPasswordEditText.getText().toString();
    }
    public String getRegisterUserName() {
        return mRegisterUserNameEditText.getText().toString();
    }
    public String getRegisterPassword() {
        return mRegisterPasswordEditText.getText().toString();
    }
    public String getRegisterPasswordConfirm() {
        return mRegisterPasswordConfirmEditText.getText().toString();
    }
    //presenter calls this to start the GameSelectionActivity
    public void StartJoinGameView(GamesList gamesList) {
        Intent intent = GameSelectionActivity.newIntent(this, gamesList);
        startActivity(intent);
    }

    //displays a Toast to the users, given by the presenter if needed
    public void DisplayMessage(String msg) {
        ViewUtil.displayToast(this, msg);
    }
}
