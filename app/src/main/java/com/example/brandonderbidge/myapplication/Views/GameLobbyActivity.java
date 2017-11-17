package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.GameLobbyPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameLobbyPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameLobbyView;

import java.util.Map;

import javax.inject.Inject;

import Communication.GameState;
import Communication.Player;
import Dagger.MyApplication;
import Dagger.UITestModule;
import Model.GUIFacade;
import Model.GUIFacadeUITest;
import Model.GUIFacade_Phase2;
import Model.ModelRoot;

/**
 * Created by pbstr on 10/6/2017.
 */

public class GameLobbyActivity extends AppCompatActivity implements IGameLobbyView {

    //GameLobbyPresenter, implmention will be passed in via the constructor
    @Inject
    IGameLobbyPresenter mPresenter;

    //used for making intents
    private static final String EXTRA_GAME_STATE = "com.example.brandonderbidge.myapplication.game_state";

    public static Intent newIntent(Context packageContext, GameState gameState) {
        Intent intent = new Intent(packageContext, GameLobbyActivity.class);
        //TODO: I cant serialize this, figure out how, but after Monday
        //intent.putExtra(EXTRA_GAME_STATE, (Serializable) gameState);
        return intent;
    }

    private RecyclerView mCurrentPlayersRecyclerView;
    private CurrentPlayersAdapter mAdapter;
    private Button mStartGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        MyApplication.instance().setIGameLobbyView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        //TODO: I want to use serialzable, but ran into problems, fix after Monday
        //GameState startGameState = (GameState) getIntent().getSerializableExtra(EXTRA_GAME_STATE);

        //get all views from activity
        mCurrentPlayersRecyclerView = (RecyclerView) findViewById(R.id.current_player_recycler_view);
        mCurrentPlayersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStartGameButton = (Button) findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startGame();
            }
        });

        //initialize the recyclerview with the current players in the current gameState
        if (mAdapter == null) {
            //TODO: use serialzabe instead of accessing GUI facade, that way the view has no logic of the model
            mAdapter = new CurrentPlayersAdapter(GUIFacade.instance().getGameState(ModelRoot.instance().getCurrentGameID()).getCurrentPlayers());
            mCurrentPlayersRecyclerView.setAdapter(mAdapter);
        }
        else {
            //TODO: don't use GUIFacade, views should have no knowledge of the facade/model
            mAdapter.setPlayerList(GUIFacade.instance().getGameState(ModelRoot.instance().getCurrentGameID()).getCurrentPlayers());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing when back is press so that users cannot go back to gameSelection screen
    }

    //implementation of the IGameLobbyView
    public void updatePlayerList(GameState gameState) {
        //when presenters are created, they will be updated, and will set the gamestate on the adapter, but it has not created yet
        //this ensures that it will not set the adapter until it has been created
        if (mAdapter != null) {
            mAdapter.setPlayerList(gameState.getCurrentPlayers());
            mAdapter.notifyDataSetChanged();
        }
    }
    //implementation of the IGameLobbyView
    public void startGameView() {
        //Intent newIntent = MapActivity.newIntent(this);
        Intent newIntent = MapTempActivity.newIntent(this);     // temporary
        startActivity(newIntent);
    }

    //implementation of the IGameLobbyView
    public void DisplayMessage(String msg) {
        ViewUtil.displayToast(this, msg);
    }

    //Holder for the current players recycler view, uses list_item_current_player xml
    private class CurrentPlayersHolder extends RecyclerView.ViewHolder {

        private TextView mPlayerName;

        private String playerName;
        public CurrentPlayersHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_current_player, parent, false));
            mPlayerName = (TextView) itemView.findViewById(R.id.player_text);
        }

        public void bind(Player player) {
            playerName = player.getUsername();
            String playerColor = player.getColor();

            int color = ViewUtil.getPlayerColor(getBaseContext() , playerColor);

            //sets the views text to be the playerName given with the color given in the player object
            mPlayerName.setText(playerName);
            mPlayerName.setTextColor(color);
        }
    }

    //adapter for the CurrentPlayersHolder
    private class CurrentPlayersAdapter extends RecyclerView.Adapter<CurrentPlayersHolder> {
        //list of players given by model
        private Map<String, Player> mPlayerList;

        public CurrentPlayersAdapter(Map<String, Player> playerList) { mPlayerList = playerList; }

        @Override
        public CurrentPlayersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(GameLobbyActivity.this);
            return new CurrentPlayersHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CurrentPlayersHolder holder, int position) {
            //this is the best way I could find to display the data in a recyclerview from a map
            int i = 0;
            for (Map.Entry<String, Player> entry : mPlayerList.entrySet()) {

                if (i == position) {
                    holder.bind(entry.getValue());
                    break;
                }
                i++;
            }
        }

        @Override
        public int getItemCount() { return mPlayerList.size(); }

        //used to update the playerList map before updating the recyclerview
        public void setPlayerList(Map<String, Player> playerList) {
            mPlayerList = playerList;
        }

    }
}
