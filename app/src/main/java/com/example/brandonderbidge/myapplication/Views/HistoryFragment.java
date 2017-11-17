package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IHistoryPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IHistoryView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import Communication.History;
import Dagger.MyApplication;
import Model.GUIFacade;
import Model.ModelRoot;

/**
 */
public class HistoryFragment extends Fragment implements IHistoryView {


    @Inject
    IHistoryPresenter mIHistoryPresenter;

    private RecyclerView mHistoryRecyclerView;
    private HistoryFragment.HistoryAdapter mHistoryAdapter;

    private List<History> mLatestHistory = new ArrayList<History>();

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    public HistoryFragment() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        MyApplication.instance().setIHistoryView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mIHistoryPresenter.refreshView();

        mHistoryRecyclerView = (RecyclerView) v.findViewById(R.id.history_recycler_view);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if (mHistoryAdapter == null) {
            mHistoryAdapter = new HistoryAdapter(mLatestHistory);
            mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        }
        else {
            mHistoryAdapter.setHistory(mLatestHistory);
            mHistoryAdapter.notifyDataSetChanged();
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIHistoryPresenter.onViewDestroy();
    }

    public void updateHistory(List<History> updatedHistory) {
        mLatestHistory = updatedHistory;
        if (mHistoryAdapter != null) {
            mHistoryAdapter.setHistory(mLatestHistory);
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    public void displayMessage(String msg) {
        ViewUtil.displayToast(this.getContext(), msg);
    }

    // recycler view for the available games given by the server
    private class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView mUserNameText;
        private TextView mHistoryText;

        public HistoryHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_history, parent, false));

            mUserNameText = (TextView) itemView.findViewById(R.id.history_username_text);
            mHistoryText = (TextView) itemView.findViewById(R.id.history_message_text);
        }

        public void bind(History history) {

            mUserNameText.setText(history.getUserName());
            int playerColor = ViewUtil.getPlayerColor(getContext(), history.getColor());
            mUserNameText.setTextColor(playerColor);
            mHistoryText.setText(history.getHistMessage());
        }
    }

    //adapter for recycler view
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

        //gamesList map given by the GamesList object
        private List<History> mHistoryList;
        public HistoryAdapter(List<History> latestHistory) { mHistoryList = latestHistory; }

        @Override
        public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            return new HistoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(HistoryHolder holder, int position) {
            holder.bind(mHistoryList.get(position));
        }

        @Override
        public int getItemCount() { return mHistoryList.size(); }

        //allows for changing of the gameslist as it gets updated
        public void setHistory(List<History> history) {
            mHistoryList = history;
        }

    }

}
