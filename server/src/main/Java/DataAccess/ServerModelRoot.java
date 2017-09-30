package DataAccess;

/**
 * Created by brandonderbidge on 9/28/17.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Model.User;
import RequestResult.LoginRequest;
import RequestResult.RegisterRequest;
import RequestResult.Result;


public class ServerModelRoot {


    private UserDAO userTable = new UserDAO(this);
    private GameDAO gameTable = new GameDAO(this);


    public static void SetAuthTokenExpire(long authTokenExpire){
        UserDAO.SetAuthTokenExpire(authTokenExpire);
    }

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection connection;

    public ServerModelRoot() {
    }

    /**
     * Creates a database connection and then returns if
     * the connection was successful
     *
     */

    public void openConnection() {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:FamilyMapServer.sqlite";

            // Open a database connection
            connection = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            connection.setAutoCommit(false);

        }
        catch (SQLException e) {

        }
    }

    /**
     * Closes the database connection and returns a boolean
     * if the connection was successfully closed.
     *
     */
    public void closeConnection(boolean commit)  {
        try {
            if (commit) {
                connection.commit();
            }
            else {
                connection.rollback();
            }

            connection.close();
            connection = null;
        }
        catch (SQLException e) {

        }
    }

    public boolean createTables(){

        boolean success = false;

        try {
            Statement stmt = null;


            try {
                stmt = connection.createStatement();

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS User " +
                        "( authtoken text UNIQUE," +
                        " username text NOT NULL UNIQUE," +
                        " date datetime, "+
                        " password text NOT NULL, PRIMARY KEY(username))" );

                success = true;
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e) {

        }

        return success;
    }

    public Result register(User user)  {


        boolean success = false;
        Result result = new Result();

        try {

            result = userTable.register(user);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }

        return result;
    }

    public Result login(User user){

        Result result = new Result();

        try {

          userTable.login(user);

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return result;
    }

    public Game createGame(Player player){

        gameTable.addGame();

        return null;
    }


}
