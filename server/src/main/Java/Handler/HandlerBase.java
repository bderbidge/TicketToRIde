package Handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class HandlerBase implements HttpHandler {

    /**
     * interprets the HttpExchange executes it in the proper handler and returns the http response body to the client
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            //if (exchange.getRequestMethod().toLowerCase().equals("post")){
            InputStream reqBody = exchange.getRequestBody();
            String reqData = readString(reqBody);

            //
            //

            String jsontoRet  = Execute(reqData);

            //
            //

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStream respBody = exchange.getResponseBody();

            writeString(jsontoRet, respBody);


            success = true;

            //}
            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (IOException e){

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }

    }

    /**
     * This function is overridden in each Handler
     * @param reqData String
     * @return String of Json
     */
    protected String Execute(String reqData){

        return null;
    }

    /**
     * reads the request body that comes from the HTTP exchange
     * @param is InputStream
     * @return String
     * @throws IOException
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }


    /**
     * writes to the HTTP response body
     * @param str String to write
     * @param os OutputStream to write to
     * @throws IOException
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
