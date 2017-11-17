package com.example.brandonderbidge.myapplication.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brandonderbidge.myapplication.Presenters.ChatPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IChatPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IChatView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Communication.Chat;
import Communication.Game;
import Communication.GameState;
import Communication.Player;
import Dagger.MyApplication;
import Dagger.UITestModule;
import Model.GUIFacade;
import Model.GUIFacadeUITest;
import Model.ModelRoot;

/**
 * Created by pbstr on 10/16/2017.
 */

public class ChatFragment extends Fragment implements IChatView {


    private static final String ARG_PRESENTER = "com.example.brandonderbidge.myapplication.chatpresenter";
    @Inject
    IChatPresenter mChatPresenter;

    private EditText mChatEditText;
    private Button mSendChatButton;

    private RecyclerView mChatRecyclerView;
    private ChatAdapter mChatAdapter;

    private List<Chat> mChat = new ArrayList<Chat>();

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        //args.putSerializable(ARG_PRESENTER, presenter);

        ChatFragment fragment = new ChatFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        MyApplication.instance().setIChatView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        mChatPresenter.refreshView();

        mChatEditText = (EditText) v.findViewById(R.id.chat_edit_text);
        mSendChatButton = (Button) v.findViewById(R.id.send_chat_button);
        mSendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });

        mChatRecyclerView = (RecyclerView) v.findViewById(R.id.chat_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mChatAdapter == null) {
            mChatAdapter = new ChatAdapter(mChat);
            mChatRecyclerView.setAdapter(mChatAdapter);
        }
        else {
            mChatAdapter.setChat(mChat);
            mChatAdapter.notifyDataSetChanged();
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mChatPresenter.onViewDestroy();
    }

    private void sendChat() {
        String msg = mChatEditText.getText().toString();
        mChatEditText.setText("");
        mChatEditText.setHint("Enter Chat Here");
        mChatPresenter.postChat(msg);
    }

    public void updateChat(List<Chat> allChat) {
        mChat = allChat;
        if (mChatAdapter != null) {
            mChatAdapter.setChat(allChat);
            mChatAdapter.notifyDataSetChanged();
        }
    }

    public void displayMessage(String msg) {
        ViewUtil.displayToast(this.getContext(), msg);
    }

    // recycler view for the available games given by the server
    private class ChatHolder extends RecyclerView.ViewHolder {
        private LinearLayout mGameSelectionLayout;
        private TextView mUserNameText;
        private TextView mChatText;

        public ChatHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_chat, parent, false));

            mGameSelectionLayout = (LinearLayout) itemView.findViewById(R.id.game_individual_layout);

            mUserNameText = (TextView) itemView.findViewById(R.id.chat_username_text);
            mChatText = (TextView) itemView.findViewById(R.id.chat_message_text);
        }

        public void bind(Chat chat) {

            mUserNameText.setText(chat.getUserName());
            int playerColor = ViewUtil.getPlayerColor(getContext(), chat.getColor());
            mUserNameText.setTextColor(playerColor);
            mChatText.setText(chat.getChatMessage());
        }
    }

    //adapter for recycler view
    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

        //gamesList map given by the GamesList object
        private List<Chat> mAllChat;
        public ChatAdapter(List<Chat> allChat) { mAllChat = allChat; }

        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            return new ChatHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ChatHolder holder, int position) {
            holder.bind(mAllChat.get(position));
        }

        @Override
        public int getItemCount() { return mAllChat.size(); }

        //allows for changing of the gameslist as it gets updated
        public void setChat(List<Chat> chat) {
            mAllChat = chat;
        }

    }

}
