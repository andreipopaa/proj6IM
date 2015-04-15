package proj6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ManageConnection implements Runnable{
    private Socket sock;
    private String msgTokens[];
    private boolean online[];
    private static final HashMap<String, String> users;
    static
    {
        users = new HashMap<String, String>();
        users.put("andreipopa", "pwd");
        users.put("dillonhenschen", "password");
        users.put("drg", "drg");
    }

    public ManageConnection(Socket sock)
    {
        this.sock = sock;
        online = new boolean[users.size()];
        msgTokens = new String[3];
    }

    @Override
    public void run() {
        // create Message object using the String that was received from the client 
        try { 
            int i = 0;
            PrintWriter pout;
            pout = new PrintWriter(sock.getOutputStream(), true);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            String message = in.readLine();
            // feedback messages
            System.out.println("Server received the following String: " + message);
            System.out.println("Server managing your request...");  
            
            StringTokenizer st = new StringTokenizer(message, " ");
            
            while (st.hasMoreElements()) {
                msgTokens[i] = (String)st.nextElement();
                i++;
            }
            
            if (msgTokens[0].equals("login")) {
                System.out.println("Required password: " + users.get(msgTokens[1]) + 
                                   "\nReceived password: " + msgTokens[2] + "."); 
                boolean correct = false;
                if (users.get(msgTokens[1]).equals(msgTokens[2])) {
                    correct = true;
                }
                pout.println(correct);
                System.out.println("Response sent: " + correct);
            } else {
                System.out.println("Something went wrong.");
            }
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}
