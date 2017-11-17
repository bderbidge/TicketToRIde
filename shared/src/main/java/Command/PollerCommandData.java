package Command;

/**
 * Created by brandonderbidge on 10/6/17.
 */

public class PollerCommandData extends Command{

    String Authtoken;
    int Version;

    /**
     * required public constructor
     */
    public PollerCommandData(){
        this.setType("PollerCommand");
    }

    /**
     * public constructor for when info is available
     * @param _authtoken the auth token of the polling client
     * @param _version int indicates what stage the game is in
     */
    public PollerCommandData(String _authtoken, int _version) {
        this.setType("PollerCommand");
        Authtoken = _authtoken;
        Version = _version;
    }

    public String getAuthtoken() {
        return Authtoken;
    }

    public void setAuthtoken(String authtoken) {
        Authtoken = authtoken;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }
}
