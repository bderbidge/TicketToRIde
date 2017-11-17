package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IEndGamePresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IEndGameView;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import Communication.Player;
import Dagger.MyApplication;

/**
 * Created by emilyprigmore on 11/8/17.
 */

public class EndGameActivity extends Activity implements IEndGameView {

    @Inject
    IEndGamePresenter mPresenter;

    RecyclerView mRecyclerView;
    EndGameActivity.StatusAdapter mAdapter;

    List<String> mPlayersLongestRoad;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, EndGameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        MyApplication.instance().setIEndGameView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mPresenter.updateUI();
    }

    public void updatePlayersLongestRoad(List<String> playerNames) {
        mPlayersLongestRoad = playerNames;
    }

    public void displayPlayerInfo(List<Player> players) {
        mRecyclerView = (RecyclerView) findViewById(R.id.over_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mAdapter == null) {
            mAdapter = new EndGameActivity.StatusAdapter(players);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayerList(players);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        // don't let them go back to the game
    }

    private class StatusHolder extends RecyclerView.ViewHolder {
        private TextView mUserNameText;
        private TextView mRoutePoints;
        private TextView mDestMade;
        private TextView mDestUnmet;
        private TextView mLongestRoad;
        private TextView mTotalPoints;

        public StatusHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_over, parent, false));

            mUserNameText = (TextView) itemView.findViewById(R.id.over_item_player_name);
            mRoutePoints = (TextView) itemView.findViewById(R.id.over_item_route_points);
            mDestMade = (TextView) itemView.findViewById(R.id.over_item_dest_made);
            mDestUnmet = (TextView) itemView.findViewById(R.id.over_item_dest_unmet);
            mLongestRoad = (TextView) itemView.findViewById(R.id.over_item_longest_road);
            mTotalPoints = (TextView) itemView.findViewById(R.id.over_item_total);
        }

        public void bind(Player player) {
            // setting a flag for who has the longest route
            if(mPlayersLongestRoad != null) {
                for (String s : mPlayersLongestRoad) {
                    if (player.getUsername().equals(s)) {
                        mLongestRoad.setText(R.string.longest_road_points);
                    }
                    else {
                        mLongestRoad.setText("--");
                    }
                }
            }
            mUserNameText.setText(player.getUsername());
            int playerColor = ViewUtil.getPlayerColor(getApplicationContext(), player.getColor());
            mUserNameText.setTextColor(playerColor);
            mRoutePoints.setText(String.valueOf(player.getRoutePoints()));
            mDestMade.setText(String.valueOf(player.getDestPoints()));
            mDestUnmet.setText(String.valueOf(player.getNegDestPoints()));
            mTotalPoints.setText(String.valueOf(player.getTotalPoints()));
        }
    }

    //adapter for recycler view
    private class StatusAdapter extends RecyclerView.Adapter<EndGameActivity.StatusHolder> {

        private List<Player> mPlayerList;
        public StatusAdapter(List<Player> playerList) { mPlayerList = playerList; }

        @Override
        public EndGameActivity.StatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new EndGameActivity.StatusHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EndGameActivity.StatusHolder holder, int position) {
            holder.bind(mPlayerList.get(position));
        }

        @Override
        public int getItemCount() {
            return mPlayerList.size();
        }

        //allows for changing of the gameslist as it gets updated
        public void setPlayerList(List<Player> playerList) {
            mPlayerList = playerList;
        }
    }
}
