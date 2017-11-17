package Communication;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import Handler.CommandHandler;
import Handler.LoginHandler;
import Handler.RegisterHandler;

/**
 * Created by autumnchapman on 9/27/17.
 */

public class  ServerCommunicator {

    private static final int NUM = 12;

    private HttpServer server;

    /**
     * runs the server with the hardcoded port number
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("IP Address: "+ InetAddress.getLocalHost().getHostAddress());
        //String portNumber = args[0];//8080
        String portNumber = "8080";
        new ServerCommunicator().run(portNumber);


    }

    /**
     * Creates contexts based on the request coming in
     * @param portNumber String
     */
    private void run(String portNumber){

        //System.out.println("IP Address: "+ InetAddress.getLocalHost().getHostAddress());

        System.out.println("Initializing HTTP Server");

        try{
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)), NUM);
        }
        catch(IOException e){
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);

        System.out.println("Creating contexts");
        
        server.createContext("/login", new LoginHandler());
        server.createContext("/register", new RegisterHandler());
        server.createContext("/command", new CommandHandler());

        System.out.println("Starting server");
        server.start();
        System.out.println("Server started ");

        //server.setExecutor(null);
    }


}

