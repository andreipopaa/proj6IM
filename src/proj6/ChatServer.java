package proj6;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class ChatServer 
{
    private static ServerSocket serverSock = null;
    private static Socket client = null;
    private static final int maxClients = 100;
    private static final ManageConnection[] clients = new ManageConnection[maxClients];
    
    public static final HashMap<String, String> users; 
    static
    {
        users = new HashMap<String, String>();
        users.put("andreipopa", "pwd");
        users.put("dillonhenschen", "password");
        users.put("drg", "drg");
        users.put("test", "test");
    }
    
    public static void main(String[] args)
    {
        int portNumber = 4220;
        try
        {
            serverSock = new ServerSocket(portNumber);
            
            while (true)
            {   
                client = serverSock.accept();
                for (int i = 0; i < maxClients; i++) {
                    if (clients[i] == null) {
                      (clients[i] = new ManageConnection(client, clients)).start();
                      break;
                    }
                }
            }
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}
