package proj6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

public class ManageConnection implements Runnable{
    private Socket sock;
    private String msgTokens[];
    private String usernames[];

    public ManageConnection(Socket sock)
    {
        this.sock = sock;
        msgTokens = new String[3];
        Set<String> usrN = ChatServer.users.keySet();
        usernames = new String[usrN.size()];
        int i = 0;
        for (String s : usrN) {
            usernames[i] = s;
            i++;
        }
    }

    @Override
    public void run() {
        // create Message object using the String that was received from the client 
        try { 
            
            PrintWriter pout;
            pout = new PrintWriter(sock.getOutputStream(), true);
            String response = "";
            
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            boolean keepRunning = true;
            //while(keepRunning){
                String message = in.readLine();
                // feedback messages
                if(message != null){
                    System.out.println("Server received the following String: " + message);
                    System.out.println("Server managing your request...");  

                    StringTokenizer st = new StringTokenizer(message, " ");
                    int j = 0;
                    while (st.hasMoreElements()) {
                        msgTokens[j] = (String)st.nextElement();
                        j++;
                    }

                    // If a login message
                    if (msgTokens[0].equals("1")) {
                        // move to another function
                        boolean correct = checkLogin();
                        if (correct == true) {
                            for (int i = 0; i < ChatServer.online.length; i++) {
                                if (ChatServer.online[i] == true) {
                                    response += usernames[i] + " ";
                                }
                            }
                        }
                        else {response += correct;}
                        pout.println(response);
                        System.out.println("Response sent: " + correct);
                    } else if(msgTokens[0].equals("2")) {
                        String usrName = msgTokens[1];
                        int k = 0;
                        for (String s : usernames) {
                            if (s.equals(usrName)) {
                                ChatServer.online[k] = false;
                                response = "LOGGEDOFF";
                                pout.println(response);
                                System.out.println("User Logged Off: " + usrName);
                                Socket tempSock = ChatServer.ConnectionArray[k];
                                ChatServer.ConnectionArray[k] = null;
                                //keepRunning = false; 
                            }
                            k++;
                        }
                        response = "LOGGEDOFF";
                        pout.println(response);
                        System.out.println("User Logged Off: " + usrName);
                    } else {
                        System.out.println("Something went wrong.");
                    }       
                }
            //}
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
    
    private boolean checkLogin()
    {
        String usrName = msgTokens[1];
        String pwd = msgTokens[2];
        if (ChatServer.users.containsKey(usrName)) {
            System.out.println("Required password: " + ChatServer.users.get(usrName) + 
                                   "\nReceived password: " + pwd); 
            if (ChatServer.users.get(usrName).equals(pwd)) {
                int i = 0;
                for (String s : usernames) {
                    if (s.equals(usrName)) {
                        ChatServer.online[i] = true;
                        ChatServer.ConnectionArray[i] = this.sock;
                    }
                    i++;
                }
                return true;
            }
        }
        
        return false;     
    }
}
