package proj6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        int port = 6100;
        String host = "127.0.0.1";
        ObjectInputStream in = null;
        Socket sock = null;
        String userString = "";
        
        try 
        {
            sock = new Socket(host, port);
            in = new ObjectInputStream(sock.getInputStream());
            PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
            
            pout.println(userString);
            // feedback messages
            System.out.println("String sent to the server.");
            System.out.println("Waiting for the server to respond...");
                       
            sock.close();
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}
