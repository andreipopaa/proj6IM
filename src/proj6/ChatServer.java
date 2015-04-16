package proj6;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer 
{
    public static Socket ConnectionArray[];
    
    public static boolean online[];
    
    public static final HashMap<String, String> users;
    
    static
    {
        users = new HashMap<String, String>();
        users.put("andreipopa", "pwd");
        users.put("dillonhenschen", "password");
        users.put("drg", "drg");
    }
    
    public static void main(String[] args)
    {
        int size = users.size();
        online = new boolean[size];
        ConnectionArray = new Socket[size];
        for(int i = 0; i < ConnectionArray.length; i++){
            ConnectionArray[i] = null;
        }
        
        try
        {
            ServerSocket sock = new ServerSocket(4220);
            
            while (true)
            {   
                Socket client = sock.accept();
                Thread thrd = new Thread(new ManageConnection(client));
                
                thrd.start();

                try {
                    thrd.join();                                       
                }
                catch (InterruptedException ie) {} 
                
                //client.close();
            }
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}
