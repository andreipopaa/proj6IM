package proj6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class ClientConnection extends Observable { 
    private Socket sock;
    private PrintWriter pout;
    
    public ClientConnection(String server, int port) {
        try {System.out.println("HERE. server: " + server + " port: " + port);
            sock = new Socket(server, port);
            pout = new PrintWriter(sock.getOutputStream(), true);
            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) { 
                            setChanged();
                            notifyObservers(line);
                        }
                    } catch (IOException ex) {
                        System.out.println("**Error while notifying about the message!");
                    }
                }
            };
            receivingThread.start();
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
        }     
    }
 
    public void send(String message) {
        pout.println(message);
        pout.flush();
    }

    /** Close the socket */
    public void close() {
        try {
            sock.close();
        } catch (IOException ex) {
            System.out.println("Cannot close connection");
        }
    }    
}
