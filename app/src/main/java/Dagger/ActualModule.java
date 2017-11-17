package Dagger;

import com.example.brandonderbidge.myapplication.Presenters.ChatPresenter;
import com.example.brandonderbidge.myapplication.Presenters.EndGamePresenter;
import com.example.brandonderbidge.myapplication.Presenters.GameInfoPresenter;
import com.example.brandonderbidge.myapplication.Presenters.DestinationSelectionPresenter;
import com.example.brandonderbidge.myapplication.Presenters.GameLobbyPresenter;
import com.example.brandonderbidge.myapplication.Presenters.GameSelectionPresenter;
import com.example.brandonderbidge.myapplication.Presenters.GameStatusPresenter;
import com.example.brandonderbidge.myapplication.Presenters.HistoryPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IEndGamePresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameStatusPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IChatPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameInfoPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IDestinationSelectionPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameLobbyPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameSelectionPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IHistoryPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ILoginPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IMapTempPresenter;
import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.LoginPresenter;
import com.example.brandonderbidge.myapplication.Presenters.MapTempPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardPresenter;

import javax.inject.Singleton;

import Model.GUIFacade;
import Model.GUIFacade_Phase2;
import Model.IGUIFacade;
import Model.ModelRoot;
import dagger.Module;
import dagger.Provides;

/**
 * Created by pbstr on 10/21/2017.
 */

@Module
public class ActualModule extends ModuleBase {

    public static ActualModule instance;

    public static ActualModule instance() {
        if (instance == null) {
            instance = new ActualModule();
        }
        return instance;
    }

    public ActualModule() {
        //do nothing for now, testing
        //mIGUIFacade = GUIFacade_Phase2.instance();
        mIGUIFacade = GUIFacade.instance();
        mModelSubject = ModelRoot.instance();
    }

    //10/31/2017 Peter Strein note- the way I was doing fragments/activities, I have to remove singleton so I call the constructors every time I start a new activty/fragment so I hook up to the model correctly

    @Provides
    public ILoginPresenter provideILoginPresenter() {
        return new LoginPresenter(mILoginView, mIGUIFacade);
    }

    @Provides
    public IGameSelectionPresenter provideIGameSelectionPresenter() {
        return new GameSelectionPresenter(mIGameSelectionView, mIGUIFacade);
    }

    @Provides
    public IGameLobbyPresenter provideIGameLobbyPresenter() {
        return new GameLobbyPresenter(mIGameLobbyView, mIGUIFacade);
    }

    @Provides
    public IChatPresenter provideIChatPresenter() {
        return new ChatPresenter(mIChatView, mIGUIFacade, mModelSubject);
    }

    @Provides
    public IHistoryPresenter provideIHistoryPresenter() {
        return new HistoryPresenter(mIHistoryView, mIGUIFacade, mModelSubject);
    }

    @Provides
    public IGameInfoPresenter provideIGameInfoPresenter() {
        return new GameInfoPresenter(mIGameInfoView, mIGUIFacade, mModelSubject);
    }

    @Provides
    public IDestinationSelectionPresenter provideIDestinationSelectionPresenter() {
        return new DestinationSelectionPresenter(mIDestinationSelectionView, mIGUIFacade, mModelSubject);
    }

    @Provides
    public IGameStatusPresenter provideIGameStatusPresenter() {
        return new GameStatusPresenter(mIGameStatusView, mIGUIFacade, mModelSubject);
    }

    @Singleton
    @Provides
    public IGUIFacade provideIGUIFacade() {
        return mIGUIFacade;
    }

    @Provides
    public IMapTempPresenter provideIMapTempPresenter() {
        return new MapTempPresenter(mIMapTempView, mIGUIFacade);
    }

    @Provides
    public ITrainCardYardPresenter provideITrainCardYardPresenter() {
        return new TrainCardYardPresenter(mITrainCardYardView, mIGUIFacade);
    }

    @Provides
    public IEndGamePresenter provideIEndGamePresenter() {
        return new EndGamePresenter(mIEndGameView, mIGUIFacade);
    }
}

