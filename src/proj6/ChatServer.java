package proj6;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class ChatServer 
{    
    public static void main(String[] args)
    {
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
                
                client.close();
            }
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}
