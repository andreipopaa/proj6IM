package proj6;

import java.net.*;
import java.io.*;

public class ChatServer 
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket sock = new ServerSocket(6100);
            
            while (true)
            {   
                Socket client = sock.accept();
                Thread thrd = new Thread();
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
