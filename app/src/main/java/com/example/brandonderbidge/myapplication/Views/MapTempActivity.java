package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandonderbidge.myapplication.BoardDrawer;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IMapTempPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.TouchProcessor;
import com.example.brandonderbidge.myapplication.Views.IViews.IMapTempView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Communication.DestinationCard;
import Communication.Player;
import Communication.Route;
import Dagger.MyApplication;

public class MapTempActivity extends FragmentActivity
        implements IMapTempView, RouteConfirmationDialogFragment.NoticeDialogListener,
        DestinationSelectionDialogFragment.DestinationDialogCloseListener,
        ClaimRouteColorOptionsFragment.ClaimRouteColorOptionsCloseListener {

    @Inject
    IMapTempPresenter mIMapTempPresenter;

    private boolean canUpdate;
    private DialogFragment destinationDialogFragment;
    private DialogFragment claimRouteColorOptionFragment;

    private ImageView map;
    private Bitmap mapBM;
    private BoardDrawer mBoardInitializer;
    private TouchProcessor mTouchProcessor;

    private Button mDrawDestBtn;
    private Button mDrawTrainsBtn;

    private TextView mDestinationDeckText;

    private Button mGameInfoBtn;
    private Button mTrainYardBtn;

    private Button mClaimRouteBtn;
    private TextView mPlayerOne;
    private TextView mPlayerTwo;
    private TextView mPlayerThree;
    private TextView mPlayerFour;
    private TextView mPlayerFive;
    private TextView[] mPlayerTextViews;

    private TextView mRedCards;
    private TextView mOrangeCards;
    private TextView mYellowCards;
    private TextView mGreenCards;
    private TextView mBlueCards;
    private TextView mPurpleCards;
    private TextView mBlackCards;
    private TextView mWhiteCards;
    private TextView mWildCards;
    private TextView[] mTrainCardsTextViews;


    private boolean isDestinationVisibile = true; //will be set to false in the onCreate using updateReyclerView visibility function
    private RelativeLayout mDestinationLayout;
    private Button mShowDestinationsBtn;
    private RecyclerView mUserDestinationsRecyclerView;
    private DestinationAdapter mDestinationAdapter;
    private List<DestinationCard> mPlayerDestinationCards = new ArrayList<>();

    private boolean mMapClickable = false;
    private Route mClickedRoute = null;

    private Map<String, Player> mPlayers = new HashMap<>();
    private Map<String, Integer> mPlayerOrder = new HashMap<>();
    private Map<String, List<Route>> claimedRoutes = new HashMap<>();
    private Player mPlayer;
    private boolean mIsMyTurn = false;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MapTempActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_temp);
        //set this to be true so that all the views can be updated
        canUpdate = true;

        MyApplication.instance().setIMapTempView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mDrawTrainsBtn = (Button) findViewById(R.id.draw_trains);
        mDrawDestBtn = (Button) findViewById(R.id.draw_destinations);
        mClaimRouteBtn = (Button) findViewById(R.id.claim_route);

        mClaimRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapClickable = true;
                enableAllButtons(false);
                displayMessage("Claim a route by clicking on it.");
                map.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    return handleClaim(event);
                }
            });
            }
        });

        mDrawDestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableAllButtons(false);
                mIMapTempPresenter.drawDestinationCards();
            }
        });

        mDestinationDeckText = (TextView) findViewById(R.id.num_dest_cards_deck_text);

        mGameInfoBtn = (Button) findViewById(R.id.to_game_info_button);
        mGameInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameInfoView();
            }
        });
        mDrawTrainsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableAllButtons(false);
                Intent newIntent = TrainCardYardActivity.newIntent(getApplicationContext());
                startActivity(newIntent);
            }
        });

        mTrainYardBtn = (Button) findViewById(R.id.to_trainyard_button);
        mTrainYardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = TrainCardYardActivity.newIntent(getApplicationContext());
                startActivity(newIntent);
            }
        });

        mDestinationLayout = (RelativeLayout) findViewById(R.id.destination_layout);
        mShowDestinationsBtn = (Button) findViewById(R.id.my_destinations_button);
        mShowDestinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDestinationVisiblity();
            }
        });
        mUserDestinationsRecyclerView = (RecyclerView) findViewById(R.id.destinations_recycler_view);
        mUserDestinationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mDestinationAdapter == null) {
            mDestinationAdapter = new DestinationAdapter(mPlayerDestinationCards);
            mUserDestinationsRecyclerView.setAdapter(mDestinationAdapter);
        }
        else {
            mDestinationAdapter.setDestinationCards(mPlayerDestinationCards);
            mDestinationAdapter.notifyDataSetChanged();
        }
        //bring the layout and the view up front to cover the map and cities when it is visiible
        mUserDestinationsRecyclerView.bringToFront();
        mDestinationLayout.bringToFront();

        mPlayerOne = (TextView) findViewById(R.id.player_one);
        mPlayerTwo = (TextView) findViewById(R.id.player_two);
        mPlayerThree = (TextView) findViewById(R.id.player_three);
        mPlayerFour = (TextView) findViewById(R.id.player_four);
        mPlayerFive = (TextView) findViewById(R.id.player_five);
        mPlayerTextViews = new TextView[]{mPlayerOne, mPlayerTwo, mPlayerThree, mPlayerFour,
                mPlayerFive};

        mRedCards = (TextView) findViewById(R.id.red_count);
        mOrangeCards = (TextView) findViewById(R.id.orange_count);
        mYellowCards = (TextView) findViewById(R.id.yellow_count);
        mGreenCards = (TextView) findViewById(R.id.green_count);
        mBlueCards = (TextView) findViewById(R.id.blue_count);
        mPurpleCards = (TextView) findViewById(R.id.purple_count);
        mBlackCards = (TextView) findViewById(R.id.black_count);
        mWhiteCards = (TextView) findViewById(R.id.white_count);
        mWildCards = (TextView) findViewById(R.id.wild_count);
        mTrainCardsTextViews = new TextView[]{mRedCards, mOrangeCards, mYellowCards, mGreenCards,
               mBlueCards, mPurpleCards, mBlackCards, mWhiteCards, mWildCards};

        // Initialize the board
        mBoardInitializer = BoardDrawer.getInstance();
        mBoardInitializer.setDrawingUtil(getApplicationContext());
        mTouchProcessor = new TouchProcessor();
        map = (ImageView) findViewById(R.id.map_img);
        drawBoard();
        drawClaimedRoutes(claimedRoutes);
        updateDestinationVisiblity();

        mIMapTempPresenter.updateUIState();
    }



    private void runUITests() {
        mIMapTempPresenter.runUITests();
    }

    @Override
    protected void onPause() {
        super.onPause();
        canUpdate = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        canUpdate = true;
        mIMapTempPresenter.refreshViews();
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIMapTempPresenter.onViewDestroy(); // detaches presenter from model subject
    }

    private void startGameInfoView() {
        if (canUpdate) {
            Intent gameInfoIntent = GameInfoActivity.newIntent(this);
            startActivity(gameInfoIntent);
        }
    }

    @Override
    public void endGame() {
        Intent newIntent = EndGameActivity.newIntent(getApplicationContext());
        startActivity(newIntent);
    }

    private void updateDestinationVisiblity() {
        if (canUpdate) {
            isDestinationVisibile = !isDestinationVisibile;
            if (isDestinationVisibile) {
                mUserDestinationsRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mUserDestinationsRecyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setPlayer(Player player) {
        mPlayer = player;
    }

    @Override
    public void updateIsMyTurn(boolean isMyTurn) {
        if (canUpdate) {
            mIsMyTurn = isMyTurn;
            // set buttons enabled/disabled
            enableAllButtons(isMyTurn);
        }
    }

    @Override
    public void enableAllButtons(boolean enabled) {
        enableTrainBtn(enabled);
        enableDestBtn(enabled);
        mClaimRouteBtn.setEnabled(enabled);
        mTrainYardBtn.setEnabled(enabled);
        mGameInfoBtn.setEnabled(enabled);
    }

    @Override
    public void enableDestBtn(boolean enabled) {
        if(mIMapTempPresenter.enableDestBtn() && enabled)
            mDrawDestBtn.setEnabled(true);
        else
            mDrawDestBtn.setEnabled(false);
    }

    @Override
    public void enableTrainBtn(boolean enabled) {
        if(mIMapTempPresenter.enableTrainBtn() && enabled)
            mDrawTrainsBtn.setEnabled(true);
        else
            mDrawTrainsBtn.setEnabled(false);
    }

    @Override
    public void enableInfoButtons(boolean enabled) {
        if(!mMapClickable) {
            mTrainYardBtn.setEnabled(enabled);
            mGameInfoBtn.setEnabled(enabled);
        }
    }

    @Override
    public void initializePlayers(List<Player> players) {
        if(canUpdate) {
            if (players != null) {
                for (int i = 0; i < players.size(); i++) {
                    mPlayers.put(players.get(i).getPlayerID(), players.get(i));
                    mPlayerOrder.put(players.get(i).getPlayerID(), i);    // same order as text views v
                    mPlayerTextViews[i].setText(players.get(i).getPlayerID());
                    mPlayerTextViews[i].setTextColor(ViewUtil.getColor(getApplicationContext(),
                            players.get(i).getColor()));
                }
            }
        }
    }

    @Override
    public void updatePlayerWhosTurn(Player player) {
        if (canUpdate) {
            List<Player> players = mIMapTempPresenter.getPlayers();
            initializePlayers(players); // want the other names back to normal

            String playerID = player.getPlayerID();
            int orderNum = mPlayerOrder.get(playerID);
            String text = "***" + playerID + "***";

            mPlayerTextViews[orderNum].setText(text);
            mPlayerTextViews[orderNum].setTextColor(ViewUtil.getColor(getApplicationContext(),
                    player.getColor()));
        }
    }

    @Override
    public void updatePlayerTrainCards(Map<String, Integer> map) {
        if (canUpdate) {
            for (String s : map.keySet()) {
                String color = s.toLowerCase();
                switch (color) {
                    case "red":
                        mTrainCardsTextViews[0].setText(map.get(s).toString());
                        break;
                    case "orange":
                        mTrainCardsTextViews[1].setText(map.get(s).toString());
                        break;
                    case "yellow":
                        mTrainCardsTextViews[2].setText(map.get(s).toString());
                        break;
                    case "green":
                        mTrainCardsTextViews[3].setText(map.get(s).toString());
                        break;
                    case "blue":
                        mTrainCardsTextViews[4].setText(map.get(s).toString());
                        break;
                    case "purple":
                        mTrainCardsTextViews[5].setText(map.get(s).toString());
                        break;
                    case "black":
                        mTrainCardsTextViews[6].setText(map.get(s).toString());
                        break;
                    case "white":
                        mTrainCardsTextViews[7].setText(map.get(s).toString());
                        break;
                    default:
                        mTrainCardsTextViews[8].setText(map.get(s).toString());
                }
            }
        }
    }

    private void drawBoard() {
        if(canUpdate) {
            mapBM = mBoardInitializer.drawBoard();
            map.setImageBitmap(mapBM);
        }
    }

    @Override
    public void updateClaimedRoutes(Map<String, List<Route>> claims) {
        // update all claimed routes
        if (canUpdate) {
            claimedRoutes.clear();
            claimedRoutes = claims;
        }
    }

    @Override
    public void drawClaimedRoutes(Map<String, List<Route>> claims){
        if(canUpdate) {
            mapBM = mBoardInitializer.drawClaimedRoutes(mapBM, claims, mPlayers);
            map.setImageBitmap(mapBM);
        }
        mTouchProcessor.remove(mTouchProcessor.claimsToList(claims), mPlayers.size());
    }

    @Override
    public void updateNumDestDeck(int numInDeck) {
        String numDestCards = Integer.toString(numInDeck);
        mDestinationDeckText.setText(numDestCards);
    }

    @Override
    public void updatePlayerDestinationCards(List<DestinationCard> playerDestinationCards) {
        if (canUpdate) {
            //at the start of the game, the player has no destination cards, so its null.
            // Don't assign the variable to be null just use a non-initialized list
            if (playerDestinationCards != null)
                mPlayerDestinationCards = playerDestinationCards;
            if (mDestinationAdapter != null) {
                mDestinationAdapter.setDestinationCards(mPlayerDestinationCards);
                mDestinationAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void startDestinationSelectionView(int numDestsReqd) {
        if(canUpdate && destinationDialogFragment == null) {
            //set canUpdate to false so that the activity does not do anything with update while
            // the fragment is created
            canUpdate = false;
            destinationDialogFragment = DestinationSelectionDialogFragment.newInstance(numDestsReqd);
            destinationDialogFragment.show(getSupportFragmentManager(), "select destination cards");
            // end turn will happen in DestSelectionPresenter
        }
    }

    @Override
    public void handleDestinationDialogClose(DialogInterface dialog) {
        //when the destination dialog fragment closes, set the activity to be updatable again
        canUpdate = true;
        destinationDialogFragment = null;
    }

    @Override
    public void displayMessage(String msg) {
        ViewUtil.displayToast(this, msg);
    }

    /* ----------------- Defines the recycler view to show the destination cards------------------*/
    private class DestinationHolder extends RecyclerView.ViewHolder {
        private TextView mDestinationDetails;
        public DestinationHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_destination, parent, false));

            mDestinationDetails = (TextView) itemView.findViewById(R.id.destination_details_text);
        }
        public void bind(DestinationCard card) {
            String details = card.getCity1() + "  to " + card.getCity2() + " -- " + card.getPoints()
                    + " points";
            mDestinationDetails.setText(details);
        }
    }

    private class DestinationAdapter extends RecyclerView.Adapter<DestinationHolder> {
        private List<DestinationCard> mDestinationCards;
        public DestinationAdapter(List<DestinationCard> destinationCards) {
            mDestinationCards = destinationCards; }
        @Override
        public DestinationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
            return new DestinationHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(DestinationHolder holder, int position) {
            holder.bind(mDestinationCards.get(position));
        }
        @Override
        public int getItemCount() { return mDestinationCards.size(); }

        public void setDestinationCards(List<DestinationCard> destinationCards) {
            mDestinationCards = destinationCards;
        }
    }

    /* ----------------- Helper methods to touch events (claiming routes) -----------------------*/
    private boolean handleClaim(MotionEvent event) {
        if(mMapClickable && event.getAction() == MotionEvent.ACTION_DOWN) {
            mClickedRoute = mTouchProcessor.processTouch(event, map);
            if (mClickedRoute != null) {
                RouteConfirmationDialogFragment dialogFragment = new RouteConfirmationDialogFragment();
                Bundle bundle = new Bundle();
                String message = "Do you want to claim " + mClickedRoute.getCity1() + " to " +
                        mClickedRoute.getCity2() + "? \nColor: " + mClickedRoute.getColor();
                bundle.putString("message", message);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "claim route");
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Didn't find a route, try again",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }

    public void onDialogPositiveClick(RouteConfirmationDialogFragment dialog) {
        List<String> colorOptions = mIMapTempPresenter.canClaimRoute(mClickedRoute);
        if(colorOptions == null) {
            Toast.makeText(getApplicationContext(), "You don't have enough cards for that route. " +
                        "It is still your turn", Toast.LENGTH_LONG).show();
            enableAllButtons(true);
        }
        else if(colorOptions.size() == 1) {
            mIMapTempPresenter.claimRoute(mClickedRoute, colorOptions.get(0));
            mIMapTempPresenter.endMyTurn();
        }
        else {
            startColorOptionsSelectionFragment(colorOptions);
        }
        mMapClickable = false;
    }

    private void startColorOptionsSelectionFragment(List<String> colorOptions) {
        if(canUpdate && claimRouteColorOptionFragment == null) {
            canUpdate = false;
            claimRouteColorOptionFragment = ClaimRouteColorOptionsFragment.newInstance(colorOptions,
                    mClickedRoute, mIMapTempPresenter);
            claimRouteColorOptionFragment.show(getSupportFragmentManager(), "claimRouteColoOptions");
        }
    }

    @Override
    public void handleClaimRouteColorOptionsClose(DialogInterface dialog) {
        canUpdate = true;
        claimRouteColorOptionFragment = null;
    }

    public void onDialogNegativeClick(RouteConfirmationDialogFragment dialog) {
        // do nothing, let the user take his/her turn again
        Toast.makeText(getApplicationContext(), "It is still your turn.", Toast.LENGTH_LONG).show();
        mMapClickable = false;
        enableAllButtons(true);
    }
}