package Dagger;

import com.example.brandonderbidge.myapplication.Views.IViews.IChatView;
import com.example.brandonderbidge.myapplication.Views.IViews.IEndGameView;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameInfoView;
import com.example.brandonderbidge.myapplication.Views.IViews.IDestinationSelectionView;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameLobbyView;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameSelectionView;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameStatusView;
import com.example.brandonderbidge.myapplication.Views.IViews.IHistoryView;
import com.example.brandonderbidge.myapplication.Views.IViews.ILoginView;
import com.example.brandonderbidge.myapplication.Views.IViews.IMapTempView;
import com.example.brandonderbidge.myapplication.Views.IViews.ITrainCardYardView;

import Model.IGUIFacade;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/23/2017.
 */

public class ModuleBase {

    protected IGUIFacade mIGUIFacade;
    protected ModelSubject mModelSubject;
    protected ILoginView mILoginView;
    protected IGameSelectionView mIGameSelectionView;
    protected IGameLobbyView mIGameLobbyView;
    protected IChatView mIChatView;
    protected IHistoryView mIHistoryView;
    protected IGameInfoView mIGameInfoView;
    protected IDestinationSelectionView mIDestinationSelectionView;
    protected IMapTempView mIMapTempView;
    protected ITrainCardYardView mITrainCardYardView;
    protected IGameStatusView mIGameStatusView;
    protected IEndGameView mIEndGameView;

    public void setILoginView(ILoginView view) {
        mILoginView = view;
    }

    public void setIGameSelectionView(IGameSelectionView view) {
        mIGameSelectionView = view;
    }

    public void setIGameLobbyView(IGameLobbyView view) {
        mIGameLobbyView = view;
    }

    public void setIChatView(IChatView view) {
        mIChatView = view;
    }

    public void setIDestinationSelectionView(IDestinationSelectionView view) {
        mIDestinationSelectionView = view;
    }

    public void setIHistoryView(IHistoryView view) {
        mIHistoryView = view;
    }

    public void setIGameInfoView(IGameInfoView view) {
        mIGameInfoView = view;
    }

    public void setIMapTempView(IMapTempView view) { mIMapTempView = view; }    //temporary

    public void setITrainCardYardView(ITrainCardYardView view) { mITrainCardYardView = view; }

    public void setIGameStatusView(IGameStatusView view) { mIGameStatusView = view; }

    public void setIEndGameView(IEndGameView view) {mIEndGameView = view; }
}
