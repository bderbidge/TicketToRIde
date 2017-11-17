package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameSelectionPresenter;
import com.example.brandonderbidge.myapplication.Presenters.GameSelectionPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameSelectionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.Player;
import Dagger.MyApplication;
import Dagger.UITestModule;
import Model.GUIFacade;
import Model.GUIFacadeUITest;
import Model.GUIFacade_Phase2;

/**
 * Created by pbstr on 10/6/2017.
 */

public class GameSelectionActivity extends AppCompatActivity implements IGameSelectionView, CreateGameDialogFragment.NoticeDialogListener, JoinGameDialogFragment.NoticeDialogListener {

    //for intents
    private static final String EXTRA_GAMES_LIST = "com.example.brandonderbidge.myapplication.game_state";

    @Inject
    IGameSelectionPresenter mGameSelectionPresenter;
    public static Intent newIntent(Context packageContext, GamesList gamesList) {
        Intent intent = new Intent(packageContext, GameSelectionActivity.class);
        //TODO: figure out how to pass stuff in instead of using the GUI facade in this activity
        //TODO: also, find out if its even necessary if we have the poller, the poller could take care of it
        //intent.putExtra(EXTRA_GAMES_LIST, (Serializable) gamesList);
        return intent;
    }

    private Button mJoinGameButton;
    private Button mCreateGameButton;
    private RecyclerView mAvailableGamesRecyclerView;
    private AvailableGamesAdapter mAdapter;
    private String selectedGameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

         MyApplication.instance().setIGameSelectionView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        //recyclerview for the avaiblable games
        mAvailableGamesRecyclerView = (RecyclerView) findViewById(R.id.open_game_recycler_view);
        mAvailableGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mJoinGameButton = (Button) findViewById(R.id.join_game_button);
        mJoinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no game selected
                if (selectedGameID == null) {
                    DisplayMessage("No game is selected, can't join");
                }
                else { //create the dialog for creating joining a game
                    DialogFragment joinGameFragment = JoinGameDialogFragment.newInstance(mGameSelectionPresenter.getGame(selectedGameID));
                    joinGameFragment.show(getSupportFragmentManager(), "join game");
                }
            }
        });
        mCreateGameButton = (Button) findViewById(R.id.create_game_button);
        mCreateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new CreateGameDialogFragment();
                newFragment.show(getSupportFragmentManager(), "create game");
            }
        });

        if (mAdapter == null) {
            //TODO: I dont want to see GUIFacade from activity, but ran into serializable problems, not going to worry about it till after monday
            //^^^ may not be necessary because of the poller

            mAdapter = new AvailableGamesAdapter(GUIFacade.instance().getGamesList().getOpenGames());
            //mAdapter = new AvailableGamesAdapter(startGamesList.getOpenGames());
            mAvailableGamesRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onBackPressed() {
        //do nothing when back is press so that users cannot go back to login screen
    }


    //to satisfy IGameSelectionView
    public void updateAvailableGames(GamesList currentGames)
    {
        //when presenters are created, they will be updated, and will send in the open games, but this is before the adapter is created
        //this ensures that it will not set the adapter until it has been created
        if (mAdapter != null) {
            mAdapter.setGamesList(currentGames.getOpenGames());
            mAdapter.notifyDataSetChanged();
        }
    }

    //to satisfy IGameSelectionView
    public void startGameLobbyView(GameState gameState)
    {
        Intent gameLobbyIntent = GameLobbyActivity.newIntent(this, gameState);
        startActivity(gameLobbyIntent);
    }

    //to satisfy IGameSelectionView
    public void DisplayMessage(String msg) {
        ViewUtil.displayToast(this, msg );
    }

    public void createGame(String newGameName, int numPlayers) {
        mGameSelectionPresenter.createGame(newGameName, numPlayers);
    }

    public void joinGame(String playerColor) {
        mGameSelectionPresenter.joinGame(selectedGameID, playerColor);
    }

    //ok clicked on join game dialog
    public void onDialogPositiveClick(JoinGameDialogFragment dialog) {
        joinGame(dialog.getSelectedColor());
    }

    //cancel clicked on join game dialog
    public void onDialogNegativeClick(JoinGameDialogFragment dialog) {
        //do nothing, it just cancels when we close the dialog
    }

    //ok clicked on create game dialog
    public void onDialogPositiveClick(CreateGameDialogFragment dialog) {
        String newGameName = dialog.mGameNameEdit.getText().toString();
        int numPlayers = Integer.parseInt(dialog.getCurrentNumPlayers());

        createGame(newGameName, numPlayers);
    }

    //cancel clicked on create game dialog
    public void onDialogNegativeClick(CreateGameDialogFragment dialog) {
        //do nothing, it just cancels when we close the dialog
    }


    // recycler view for the available games given by the server
    private class AvaliableGamesHolder extends RecyclerView.ViewHolder {
        private Game mGame;
        private LinearLayout mGameSelectionLayout;
        private TextView mGameNumber;
        private TextView mGameName;
        private TextView mGamePlayerNames;
        private TextView mPlayersJoined;

        public AvaliableGamesHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_available_game, parent, false));

            mGameSelectionLayout = (LinearLayout) itemView.findViewById(R.id.game_individual_layout);

            mGameNumber = (TextView) itemView.findViewById(R.id.game_number_text);
            mGameName = (TextView) itemView.findViewById(R.id.game_name_text);
            mGamePlayerNames = (TextView) itemView.findViewById(R.id.joined_players_text);
            mPlayersJoined = (TextView) itemView.findViewById(R.id.number_players_text);



        }

        public void bind(Game game) {

            mGame = game;
            mGameNumber.setText(game.getGameID());
            mGameName.setText(game.getGameName());

            GameState gameState = game.getCurrentGameState();

            //create string of the current players
            List<String> currentPlayers = new ArrayList<String>();
            for (Map.Entry<String, Player> entry : game.getCurrentGameState().getCurrentPlayers().entrySet()) {
                currentPlayers.add(entry.getValue().getUsername());
            }
            mGamePlayerNames.setText(currentPlayers.toString());

            //create a string of the total number of players joined
            String joinedPlayers = Integer.toString(gameState.getCurrentNumberOfPlayers()) + "/" + Integer.toString(game.getMaxNumberOfPlayers());
            mPlayersJoined.setText(joinedPlayers);

            //sets the background if a game is selected, if not selected, normal, if selected, a little box is created to show what was selected
            setBackground();

            //on the individual game clicked
            mGameSelectionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedGameID = mGameNumber.getText().toString();
                    selectedGameID = mGame.getGameID();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        //creates a box outline if game was selected
        private void setBackground() {
            if(selectedGameID != null) {
                if (selectedGameID.contains(mGameNumber.getText().toString())) {
                    mGameSelectionLayout.setBackgroundResource(R.drawable.customborder);
                }
                else {
                    mGameSelectionLayout.setBackgroundResource(R.drawable.noborder);
                }
            }

        }
    }

    //adapter for recycler view
    private class AvailableGamesAdapter extends RecyclerView.Adapter<AvaliableGamesHolder> {

        //gamesList map given by the GamesList object
        private Map<String, Game> mGamesList;
        public AvailableGamesAdapter(Map<String, Game> gamesList) { mGamesList = gamesList; }

        @Override
        public AvaliableGamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(GameSelectionActivity.this);
            return new AvaliableGamesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AvaliableGamesHolder holder, int position) {
            //this is the best way I could find to iterate and find the element at position x in a map
            int i = 0;
            for (Map.Entry<String, Game> entry : mGamesList.entrySet()) {

                if (i == position) {
                    holder.bind(entry.getValue());
                    break;
                }
                i++;
            }
        }

        @Override
        public int getItemCount() { return mGamesList.size(); }

        //allows for changing of the gameslist as it gets updated
        public void setGamesList(Map<String, Game> gamesList) {
            mGamesList = gamesList;
        }

    }


}
