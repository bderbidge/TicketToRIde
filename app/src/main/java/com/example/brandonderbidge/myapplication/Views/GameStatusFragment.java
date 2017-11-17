package com.example.brandonderbidge.myapplication.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameStatusPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameStatusView;

import java.util.List;

import javax.inject.Inject;

import Communication.Player;
import Dagger.MyApplication;

public class GameStatusFragment extends Fragment implements IGameStatusView {
    @Inject
    IGameStatusPresenter mIGameStatusPresenter;


    RecyclerView mGameStatusRecyclerView;
    GameStatusFragment.StatusAdapter mGameStatusAdapter;

    private List<Player> mPlayerList;
    private List<String> mLongestRouteNames;

    public static GameStatusFragment newInstance() {
        Bundle args = new Bundle();
        GameStatusFragment fragment = new GameStatusFragment();
        return fragment;
    }

    public GameStatusFragment() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_status, container, false);

        MyApplication.instance().setIGameStatusView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mIGameStatusPresenter.refreshView();

        mGameStatusRecyclerView = (RecyclerView) v.findViewById(R.id.status_recycler_view);
        mGameStatusRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mGameStatusAdapter == null) {
            mGameStatusAdapter = new StatusAdapter(mPlayerList);
            mGameStatusRecyclerView.setAdapter(mGameStatusAdapter);
        } else {
            mGameStatusAdapter.setPlayerList(mPlayerList);
            mGameStatusAdapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIGameStatusPresenter.onViewDestroy();
    }

    public void updateGameStatus(List<Player> playerList) {
        mPlayerList = playerList;
        if (mGameStatusAdapter != null) {
            mGameStatusAdapter.setPlayerList(playerList);
            mGameStatusAdapter.notifyDataSetChanged();
        }
    }

    public void updatePlayerWithLongestRoute(List<String> playerNames) {
        mLongestRouteNames = playerNames;
    }

    private void updateGameStatusRecyclerView() {
        if(mPlayerList != null) {
            if (mGameStatusAdapter == null) {
                mGameStatusAdapter = new StatusAdapter(mPlayerList);
                mGameStatusRecyclerView.setAdapter(mGameStatusAdapter);
            } else {
                mGameStatusAdapter.setPlayerList(mPlayerList);
                mGameStatusAdapter.notifyDataSetChanged();
            }
        }
    }

    public void displayMessage(String msg) {
        ViewUtil.displayToast(this.getContext(), msg);
    }

    private class StatusHolder extends RecyclerView.ViewHolder {
        private TextView mUserNameText;
        private TextView mNumTrainCardsText;
        private TextView mNumDestCardsText;
        private TextView mNumTrainsLeftText;
        private TextView mNumPointsText;

        public StatusHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_game_status, parent, false));

            mUserNameText = (TextView) itemView.findViewById(R.id.status_player_name_text);
            mNumTrainCardsText = (TextView) itemView.findViewById(R.id.status_num_traincards_text);
            mNumDestCardsText = (TextView) itemView.findViewById(R.id.status_num_destcards_text);
            mNumTrainsLeftText = (TextView) itemView.findViewById(R.id.status_num_trains_text);
            mNumPointsText = (TextView) itemView.findViewById(R.id.status_num_points_text);

        }

        public void bind(Player player) {

            // setting a flag for who has the longest route
            mUserNameText.setText(player.getUsername());
            if(mLongestRouteNames != null) {
                for (String s : mLongestRouteNames) {
                    if (player.getUsername().equals(s)) {
                        mUserNameText.setText(player.getUsername() + " LR ");
                    }
                }
            }
            int playerColor = ViewUtil.getPlayerColor(getContext(), player.getColor());
            mUserNameText.setTextColor(playerColor);
            //need to cast to integers so that it can be set in the text views properly
            String numCards = Integer.toString(player.getNumTrainCards());
            String numDestCards = Integer.toString(player.getNumDestCards());
            String numTrainsLeft = Integer.toString(player.getTrains());
            String numPoints = Integer.toString(player.getTotalPoints());
            mNumTrainCardsText.setText(numCards);
            mNumDestCardsText.setText(numDestCards);
            mNumTrainsLeftText.setText(numTrainsLeft);
            mNumPointsText.setText(numPoints);
        }
    }

    //adapter for recycler view
    private class StatusAdapter extends RecyclerView.Adapter<StatusHolder> {

        //gamesList map given by the GamesList object
        private List<Player> mPlayerList;
        public StatusAdapter(List<Player> playerList) { mPlayerList = playerList; }

        @Override
        public StatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            return new StatusHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StatusHolder holder, int position) {
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
