package Dagger;

import com.example.brandonderbidge.myapplication.Presenters.LoginPresenter;
import com.example.brandonderbidge.myapplication.Views.ChatFragment;
import com.example.brandonderbidge.myapplication.Views.EndGameActivity;
import com.example.brandonderbidge.myapplication.Views.GameInfoActivity;
import com.example.brandonderbidge.myapplication.Views.DestinationSelectionDialogFragment;
import com.example.brandonderbidge.myapplication.Views.GameLobbyActivity;
import com.example.brandonderbidge.myapplication.Views.GameSelectionActivity;
import com.example.brandonderbidge.myapplication.Views.GameStatusFragment;
import com.example.brandonderbidge.myapplication.Views.HistoryFragment;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameLobbyView;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameSelectionView;
import com.example.brandonderbidge.myapplication.Views.IViews.ILoginView;
import com.example.brandonderbidge.myapplication.Views.IViews.IMapTempView;
import com.example.brandonderbidge.myapplication.Views.LoginActivity;
import com.example.brandonderbidge.myapplication.Views.MapActivity;
import com.example.brandonderbidge.myapplication.Views.MapTempActivity;
import com.example.brandonderbidge.myapplication.Views.TrainCardYardActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pbstr on 10/21/2017.
 */

@Singleton
//@Component( modules = {UITestModule.class})
@Component( modules = {ActualModule.class})
public interface BasicInjectionComponent {

    //need to name the activities so that dagger can inject the dependencies in those activities/fragments
    void inject(LoginActivity loginActivity);
    void inject(GameSelectionActivity gameSelectionActivity);
    void inject(GameLobbyActivity gameLobbyActivity);
    void inject(ChatFragment chatFragment);
    void inject(HistoryFragment historyFragment);
    void inject(MapActivity mapActivity);
    void inject(GameInfoActivity gameInfoActivity);
    void inject(DestinationSelectionDialogFragment destinationSelectionDialogFragment);
    void inject(MapTempActivity mapTempActivity);
    void inject(TrainCardYardActivity trainCardYardActivity);
    void inject(GameStatusFragment gameStatusFragment);
    void inject(EndGameActivity endGameActivity);

    void inject(IGameSelectionView gameSelectionView);

    void inject(IGameLobbyView gameLobbyView);
}
