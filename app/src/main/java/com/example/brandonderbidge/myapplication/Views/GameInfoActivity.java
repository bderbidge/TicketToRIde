package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameInfoPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameInfoView;

import javax.inject.Inject;

import Dagger.MyApplication;

public class GameInfoActivity extends AppCompatActivity implements IGameInfoView {

    @Inject
    IGameInfoPresenter mPresenter;

    Fragment mGameStatusFragment;
    Fragment mHistoryFragment;
    Fragment mChatHistoryFragment;
    Fragment mChatFragment;

    RadioButton mChatButton;
    RadioButton mHistoryButton;

    private FragmentManager mFragmentManager;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameInfoActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        //dont do anything, this will disable the back button from doing anything in this activity
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        mFragmentManager = this.getSupportFragmentManager();

        MyApplication.instance().setIGameInfoView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mPresenter.refreshView();

        mGameStatusFragment = mFragmentManager.findFragmentById(R.id.info_game_status_fragment_container);
        //mHistoryFragment = mFragmentManager.findFragmentById(R.id.info_history_fragment_container);
        //mChatFragment = mFragmentManager.findFragmentById(R.id.info_chat_fragment_container);
        mChatHistoryFragment = mFragmentManager.findFragmentById(R.id.info_chathistory_fragment_container);

        mChatButton = (RadioButton) findViewById(R.id.chat_radio_button);
        mHistoryButton = (RadioButton) findViewById(R.id.history_radio_button);


        if (mGameStatusFragment == null) {
            mGameStatusFragment = GameStatusFragment.newInstance();
            mFragmentManager.beginTransaction().replace(
                    R.id.info_game_status_fragment_container, mGameStatusFragment).commit();
        }
        /*
        if (mHistoryFragment == null) {
            mHistoryFragment = HistoryFragment.newInstance();
            mFragmentManager.beginTransaction().replace(
                    R.id.info_history_fragment_container, mHistoryFragment).commit();
        }
        if (mChatFragment == null) {
            mChatFragment = ChatFragment.newInstance();
            mFragmentManager.beginTransaction().replace(
                    R.id.info_chat_fragment_container, mChatFragment).commit();
        }
        */
        if (mChatHistoryFragment == null) {
            mChatHistoryFragment = ChatFragment.newInstance();
            mFragmentManager.beginTransaction().replace(
                    R.id.info_chathistory_fragment_container, mChatHistoryFragment).commit();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDestroy();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.chat_radio_button:
                //turn on chat fragment;
                mChatHistoryFragment = ChatFragment.newInstance();
                mFragmentManager.beginTransaction().replace(
                        R.id.info_chathistory_fragment_container, mChatHistoryFragment).commit();
                break;
            case R.id.history_radio_button:
                //turn on history fragment
                mChatHistoryFragment = HistoryFragment.newInstance();
                mFragmentManager.beginTransaction().replace(
                        R.id.info_chathistory_fragment_container, mChatHistoryFragment).commit();
                break;
        }
    }

    private void updateHistoryChatFragment() {

    }

    public void displayMessage(String msg) {
        ViewUtil.displayToast(this, msg);
    }

    @Override
    public void endGame() {
        Intent newIntent = EndGameActivity.newIntent(getApplicationContext());
        startActivity(newIntent);
    }
}
