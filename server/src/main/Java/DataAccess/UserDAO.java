package DataAccess;

/**
 * Created by brandonderbidge on 9/28/17.
 */


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import Communication.DestinationCard;
import Communication.Game;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import RequestResult.LoginRegisterResult;

import RouteCalculator.Exception.RouteCalcException;
import RouteCalculator.RouteCalculator;
import RouteCalculator.Util;


public class UserDAO {

    private static long authTokenExpire;
    private ServerModelRoot db;

    public static void SetAuthTokenExpire(long authTokenTimeout){
        authTokenExpire = authTokenTimeout * 1000;
    }

    public UserDAO(ServerModelRoot db) {
        this.db = db;
    }


    /**
     * logs in a user
     * @param username, password
     * @return LoginRegisterResult
     */

    public LoginRegisterResult login(String username, String password) {

        Map<String, User> registeredUsers = db.getRegisteredUsers();
        Map<String, String> authtokenUsers = db.getAuthtokenUsers();
        Map<String, String> userAuthtokens = db.getUserAuthtokens();

        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        String sql = "SELECT authtoken FROM [User] Where username = ? AND password = ?";

        LoginRegisterResult loginRegisterResult;

        //check if the user is in the map via its username.
        User user = registeredUsers.get(username);

        if(user.getUsername().equals(username)
                && user.getPassword().equals(password))
        {


            String authToken = UUID.randomUUID().toString();

            loginRegisterResult = new LoginRegisterResult(authToken, user);

            //If so then we get the old authtoken
            String auth = userAuthtokens.get(username);

            //remove old auth from both maps
            authtokenUsers.remove(auth);
            userAuthtokens.remove(username);


            //put new auth into the map
            authtokenUsers.put(authToken, username);
            userAuthtokens.put(username, authToken);


        }
        else {

            System.out.println("Log in Failed");
            loginRegisterResult = new LoginRegisterResult("Log in failed");
        }

        //db.setRegisteredUsers(registeredUsers);
        //db.setAuthtokenUsers(authtokenUsers);
        //db.setUserAuthtokens(userAuthtokens);
        return loginRegisterResult;
    }

    /**
     * Creates a new User in the database
     * @param username, password
     * @return LoginRegisterResult
     */

    public LoginRegisterResult register(String username, String password) {

        Map<String, User> registeredUsers = db.getRegisteredUsers();
        Map<String, String> authtokenUsers = db.getAuthtokenUsers();
        Map<String, String> userAuthtokens = db.getUserAuthtokens();

        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        String sql = "insert into User (username, password, ) values (?, ?)";

        LoginRegisterResult loginRegisterResult;


        //check if the User has been registered yet
        if(registeredUsers.get(username) == null)
        {
            //If the user has not been registered then place the user in the map
            // with an authtoken
            User user = new User(username, password);
            String authToken = UUID.randomUUID().toString();

            loginRegisterResult = new LoginRegisterResult(authToken, user);
            registeredUsers.put(username, user);

            authtokenUsers.put(authToken, username);
            userAuthtokens.put(username,authToken);
        }
        else {
            loginRegisterResult = new LoginRegisterResult("Username already in use");
        }

        //db.setRegisteredUsers(registeredUsers);
        //db.setAuthtokenUsers(authtokenUsers);
        //db.setUserAuthtokens(userAuthtokens);

        return loginRegisterResult;

    }

    public void drawTCard(String userName, TrainCard tc){
        User user = db.getUser(userName);
        user.addTrainCard(tc);
    }

    public void drawDCards(String userName, List<DestinationCard> dc){
        Map<String, User> registeredUsers = db.getRegisteredUsers();
        User use = registeredUsers.get(userName);
        List<DestinationCard> myCards = use.getDestCards();
        for (DestinationCard card: dc) {
            myCards.add(card);
        }
        use.setDestinationCards(myCards);
    }

    public void returnDCards(String userName, List<DestinationCard> dc){
        Map<String, User> registeredUsers = db.getRegisteredUsers();
        User use = registeredUsers.get(userName);
        if(dc != null){
            for (DestinationCard card: dc) {
                use.removeDcard(card);
            }
        }
        registeredUsers.put(userName, use);
        //db.setRegisteredUsers(registeredUsers);
    }

    public List<TrainCard> claimRoute(String userName, Route r, String cColor){//claiming Color in cases where the route it gray

        Map<String, User> registeredUsers = db.getRegisteredUsers();
        User use = registeredUsers.get(userName);
        String color = r.getColor();
        if(cColor != null){
            color = cColor;
        }

        int numLeft = r.getLength();
        List<TrainCard> toRet = new ArrayList<>();
        while(numLeft > 0)
        {
            if(use.getMyTrainCardCounts().get(color) > 0) {
                use.removeTrainCard(color);
                TrainCard card = new TrainCard(color);
                toRet.add(card);
                numLeft--;
            }
            else {
                use.removeTrainCard("wild");
                TrainCard card = new TrainCard("wild");
                toRet.add(card);
                numLeft--;
            }
        }
        return toRet;

    }

    public String getUserFromAuth(String auth){
        Map<String, String> authtokenUsers = db.getAuthtokenUsers();
        String userName = authtokenUsers.get(auth);
        return userName;
    }

    //calculates the points gained from completing or not completing destination cards
    public void endGamePoints(String gameID){
        Util util = new Util();
        RouteCalculator rc = new RouteCalculator(util.getAllCities());
        Map<String, List<User>> gameToUsers = db.getGameToUsers();
        List<User> users = gameToUsers.get(gameID);
        for (User user: users) {
            int pointForDestCardP = 0;
            int pointForDestCardN = 0;
            Player player = db.getPlayerFromUserName(user.getUsername(), gameID);
            for (DestinationCard dCard:user.getDestCards()) {
                try {
                    int points = rc.calculatePointsForDest(dCard, new ArrayList(player.getRoutes()));
                    if(points > 0){
                        pointForDestCardP = pointForDestCardP + points;
                    }
                    if(points < 0){
                        pointForDestCardN = pointForDestCardN + points;
                    }
                } catch (RouteCalcException e) {
                    e.printStackTrace();
                }
            }
            player.addNegDestPoints(pointForDestCardN);
            player.addDestPoints(pointForDestCardP);

        }

    }

}
