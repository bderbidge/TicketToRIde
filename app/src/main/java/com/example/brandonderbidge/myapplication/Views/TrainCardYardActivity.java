package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.ITrainCardYardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import Communication.TrainCard;
import Dagger.MyApplication;

/**
 * Created by emilyprigmore on 10/30/17.
 */

public class TrainCardYardActivity extends Activity implements ITrainCardYardView {

    @Inject
    ITrainCardYardPresenter mITrainCardYardPresenter;

    private TextView mCardOne;
    private TextView mCardTwo;
    private TextView mCardThree;
    private TextView mCardFour;
    private TextView mCardFive;
    private TextView[] mTrainCardTextViews;
    private TextView mDeck;

    private List<TrainCard> mFaceUpCards;
    private boolean mCanGoBack;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TrainCardYardActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_cards);

        MyApplication.instance().setITrainCardYardView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        // wire up the cards and their click listeners
        mCardOne = (TextView) findViewById(R.id.train_card_one);
        mCardOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawCard(mFaceUpCards.get(0));
            }
        });
        mCardTwo = (TextView) findViewById(R.id.train_card_two);
        mCardTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawCard(mFaceUpCards.get(1));
            }
        });
        mCardThree = (TextView) findViewById(R.id.train_card_three);
        mCardThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawCard(mFaceUpCards.get(2));
            }
        });
        mCardFour = (TextView) findViewById(R.id.train_card_four);
        mCardFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawCard(mFaceUpCards.get(3));
            }
        });
        mCardFive = (TextView) findViewById(R.id.train_card_five);
        mCardFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawCard(mFaceUpCards.get(4));
            }
        });
        mDeck = (TextView) findViewById(R.id.train_deck);
        mDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITrainCardYardPresenter.drawFaceDownCard();
            }
        });
        mTrainCardTextViews = new TextView[] {mCardOne, mCardTwo, mCardThree, mCardFour, mCardFive};

        mITrainCardYardPresenter.updateUIState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mITrainCardYardPresenter.onViewDestroy(); // will call detach on the presenter
    }

    @Override
    public void onBackPressed() {
        if (mITrainCardYardPresenter.onBackPressed()) { // user can go back if not their turn
            super.onBackPressed();
        }
        /*if(mCanGoBack)
            super.onBackPressed();*/
    }

    @Override
    public void updateCards(TrainCard[] faceUp, int cardsInDeck) {
        mFaceUpCards = Arrays.asList(faceUp);
        for(int i = 0; i < faceUp.length; i++) {
            int color = ViewUtil.getTrainColor(this, faceUp[i].getColor());
            mTrainCardTextViews[i].setBackgroundResource(color);
        }
        mDeck.setText(String.valueOf(cardsInDeck));
    }

    @Override
    public void endGame() {
        Intent newIntent = EndGameActivity.newIntent(getApplicationContext());
        startActivity(newIntent);
    }

    @Override
    public void setCanGoBack(boolean canGoBack) {
        mCanGoBack = canGoBack;
    }

    @Override
    public void destroyView() {
        //onBackPressed(); //will go back to the map view
        super.onBackPressed();
    }
    @Override
    public void displayMessage(String msg) {
        ViewUtil.displayToast(this, msg);
    }


}
