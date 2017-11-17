package com.example.brandonderbidge.myapplication.Presenters;

import android.graphics.Color;
import android.graphics.PorterDuff;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameInfoPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameInfoView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Communication.Chat;
import Communication.History;
import Communication.Player;
import Communication.User;
import Model.CommandFacade;
import Model.GUIFacade_Phase2;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/28/2017.
 */

public class GameInfoPresenter implements Observer, IGameInfoPresenter{

    private IGameInfoView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface
    private int counter = 0;

    public GameInfoPresenter(IGameInfoView view, IGUIFacade GUIFacade, ModelSubject subject) {
        mView = view;
        mGUIFacade = GUIFacade;
        mModelSubject = subject;
        mModelSubject.attach(this);
    }

    public void update() {
        if(mGUIFacade.isGameOver()) {
            mView.endGame();
        }
        if(mGUIFacade.isFinalRound()) {
            mView.displayMessage("THIS IS THE FINAL ROUND!");
        }
    }

    public void updateUIButtonClicked() {
        runChatHistoryTest();
        runGameStatusTest();
    }
    private void runChatHistoryTest() {
        mView.displayMessage("Adding Fake History");
        CommandFacade.instance().addHistory(new History("The Battle of Waterloo occurred in 7815", "Opponent", "green"));
        CommandFacade.instance().addHistory(new History("The Archduke of South Belgium became Supreme Chancellor in 1808", "Opponent", "green"));
        mView.displayMessage("Adding Fake Chat");
        String un = ModelRoot.instance().getUsername();
        CommandFacade.instance().addChat(new Chat("It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.", un ,GUIFacade_Phase2.instance().getPlayerColor(un)));
        CommandFacade.instance().addChat(new Chat("Or a stiff drink.", "Opponent", "green"));
        CommandFacade.instance().addChat(new Chat("Quiet Mary", un, GUIFacade_Phase2.instance().getPlayerColor(un)));
    }

    private void runGameStatusTest() {
        List<Player> players = mGUIFacade.getPlayers();
        for(Player p : players) {
            p.setNumTrainCards(p.getNumTrainCards() - 5);
            p.setTrains(p.getTrains() - 5);
            p.addTotalPoints(5);
        }
        mModelSubject.notifyObservers();
    }

    public void refreshView() {
        //does nothing for now
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    public void update(Observable observable, Object object) {
        String msg = (String) object;
        if (msg.contains("update")) {
            update();
        } else { //if no update message, then an error was given and must be displayed
            mView.displayMessage(msg);
        }

    }
}
