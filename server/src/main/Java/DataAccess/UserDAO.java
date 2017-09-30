package DataAccess;

/**
 * Created by brandonderbidge on 9/28/17.
 */


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import Model.User;
import RequestResult.LoginRequest;
import RequestResult.RegisterRequest;
import RequestResult.Result;


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
     * Returns a specific AuthToken
     * @param user
     * @return User object
     */


    public Result login(User user) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        String sql = "SELECT authtoken FROM [User] Where username = ? AND password = ?";
        Result result = new Result();

        try
        {
            updateAuthToken(user);
            stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            keyRS = stmt.executeQuery();
            if(keyRS != null)
            {
                result.setAuthToken(keyRS.getString(1));
                result.isSuccess();

            }
            else
            {
                throw new SQLException();
            }

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Log in Failed");
            result.setSuccess(false);
            result.setMessage("Log in Fail");
        }
        finally
        {
            if(stmt != null)
                stmt.close();
            if (keyRS != null)
                keyRS.close();
        }


        return result;
    }

    /**
     * Creates a new User in the database
     * @param user object
     * @return boolean
     */

    public Result register(User user) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        String sql = "insert into User (username, password, ) values (?, ?)";
        Result result = new Result();

        try
        {
            stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());


            if(stmt.executeUpdate() == 1)
            {
                result.isSuccess();
                updateAuthToken(user);
            }
            else
            {
                throw new SQLException();
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            result.setSuccess(false);
            result.setMessage("Register Failed");
        }
        finally
        {
            if(stmt != null)
                stmt.close();
            if (keyRS != null)
                keyRS.close();
        }

        return result;


    }


    public void updateAuthToken(User user) throws SQLException {


        PreparedStatement stmt = null;
        ResultSet keyRS = null;
        long time = System.currentTimeMillis();
        String authToken = UUID.randomUUID().toString();

        try
        {
            String sql = "UPDATE User SET authtoken = ?, date = ? WHERE username = ?";
            stmt = db.getConnection().prepareStatement(sql);

            stmt.setString(1, authToken);
            stmt.setLong(2, time);
            stmt.setString(3, user.getUsername());
            stmt.executeUpdate();
        }
        finally
        {
            if(stmt != null)
                stmt.close();
            if (keyRS != null)
                keyRS.close();
        }



    }

}
