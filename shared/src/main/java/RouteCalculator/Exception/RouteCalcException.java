package RouteCalculator.Exception;

import RouteCalculator.RouteCalculator;

/**
 * Created by pbstr on 11/8/2017.
 */

public class RouteCalcException extends Exception {

    private String msg;
    public RouteCalcException(String msg){ this.msg = msg;}
    public String getMessage() {
        return msg;
    }
}
