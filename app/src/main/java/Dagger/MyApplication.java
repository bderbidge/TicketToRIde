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
import com.example.brandonderbidge.myapplication.Views.IViews.IMapTempView; //temporary
import com.example.brandonderbidge.myapplication.Views.IViews.ITrainCardYardView;

/**
 * Created by pbstr on 10/21/2017.
 */


/**
 *  The purpose of this class is to create the right factory for Dependency Injection using Dagger 2.
 *  Application is the Android "global" class, so this class can be accessed globally
 */
public class MyApplication {

    private static MyApplication mInstance = new MyApplication();

    private MyApplication() {
    }

    public static MyApplication instance() {

        return mInstance;
    }

    private BasicInjectionComponent mBasicInjectionComponent;


    public void setILoginView(ILoginView view) {
        UITestModule.instance().setILoginView(view);
        ActualModule.instance().setILoginView(view);
    }

    public void setIGameSelectionView(IGameSelectionView view) {
        UITestModule.instance().setIGameSelectionView(view);
        ActualModule.instance().setIGameSelectionView(view);
    }

    public void setIGameLobbyView(IGameLobbyView view) {
        UITestModule.instance().setIGameLobbyView(view);
        ActualModule.instance().setIGameLobbyView(view);
    }

    public void setIChatView(IChatView view) {
        UITestModule.instance().setIChatView(view);
        ActualModule.instance().setIChatView(view);
    }

    public void setIHistoryView(IHistoryView view) {
        UITestModule.instance().setIHistoryView(view);
        ActualModule.instance().setIHistoryView(view);
    }

    public void setIGameInfoView(IGameInfoView view) {
        UITestModule.instance().setIGameInfoView(view);
        ActualModule.instance().setIGameInfoView(view);
    }

    public void setIDestinationSelectionView(IDestinationSelectionView view) {
        UITestModule.instance().setIDestinationSelectionView(view);
        ActualModule.instance().setIDestinationSelectionView(view);
    }

    public void setIGameStatusView(IGameStatusView view) {
        UITestModule.instance().setIGameStatusView(view);
        ActualModule.instance().setIGameStatusView(view);
    }

    public void setIMapTempView(IMapTempView view) {
        UITestModule.instance().setIMapTempView(view);
        ActualModule.instance().setIMapTempView(view);
    }

    public void setITrainCardYardView(ITrainCardYardView view) {
        UITestModule.instance().setITrainCardYardView(view);
        ActualModule.instance().setITrainCardYardView(view);
    }

    public void setIEndGameView(IEndGameView view) {
        UITestModule.instance().setIEndGameView(view);
        ActualModule.instance().setIEndGameView(view);
    }

    public BasicInjectionComponent getBasicInjectionComponent() {
        if (mBasicInjectionComponent == null) {
            //mBasicInjectionComponent = DaggerBasicInjectionComponent.builder().uITestModule(UITestModule.instance()).build();
            mBasicInjectionComponent = DaggerBasicInjectionComponent.builder().actualModule(ActualModule.instance()).build();
        }
        return mBasicInjectionComponent;
    }
}
