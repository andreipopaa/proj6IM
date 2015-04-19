package proj6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageConnection extends Thread {
    private String username;
    private Socket sock;
    private String msgTokens[];
    private String usernames[];
    private PrintWriter pout;
    private BufferedReader in;
    private final ManageConnection[] clients;
    private int maxClients;

    public ManageConnection(Socket sock, ManageConnection[] clients)
    {
        this.sock = sock;
        this.clients = clients;
        maxClients = clients.length;
        
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
            pout = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            
            String response = "";
            String message;
            
            while((message = in.readLine()) != null){  
                if(message != null){
                    // feedback messages
                    System.out.println("Server received the following String: " + message);
                    System.out.println("Server managing your request...");  
                    
                    StringTokenizer st = new StringTokenizer(message, " ");
                    int j = 0;
                    while (st.hasMoreElements() && j < 3) {
                        String tk = (String)st.nextElement();
                        msgTokens[j] = tk;
                        j++;
                    }

                    // If a login message
                    if (msgTokens[0].equals("1")) {
                        boolean correct = checkLogin();
                        if (correct == true) {
                            System.out.println("Valid credentials: " + correct);
                        } else {
                            pout.println("false");
                        }                    
                    } else if(msgTokens[0].equals("2")) {
                        String usrName = msgTokens[1];
                        // notify everyone
                        synchronized (this) {
                            for (int i = 0; i < maxClients; i++) {
                              if (clients[i] != null && clients[i] != this && clients[i].username != null) {
                                clients[i].pout.println("5 " + username);
                              }
                            }
                        }
                        // forget current thread
                        synchronized (this) {
                            for (int i = 0; i < maxClients; i++) {
                              if (clients[i] == this) {
                                clients[i] = null;
                              }
                            }
                        }
                        in.close();
                        pout.close();
                        sock.close();
                        System.out.println("User Logged Off: " + usrName);
                    } else if(msgTokens[0].equals("3")){
                        synchronized (this) {
                            for (int i = 0; i < maxClients; i++) {
                              if (clients[i] != null && (clients[i].username).equals(msgTokens[2]) ) {
                                  System.out.println("Message: " + message + ". Sent to: " + clients[i].username);
                                  clients[i].pout.println(message);
                              }
                            }
                        }
                    } else {
                        System.out.println("Something went wrong.");
                    } 
                }
            } 
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
                //loop to notify buddy on
                synchronized (this) {    
                    username = usrName;
                    
                    String online = username + " ";
                    
                    for (int i = 0; i < maxClients; i++) {
                        if (clients[i] != null && clients[i] != this) {
                            online += clients[i].username + " ";
                            clients[i].pout.println("4 " + username);
                        }
                    }
                    System.out.println("Message sent to client:" + online);
                    pout.println(online);
                  }
                return true;
            }
            else {
                for (int i = 0; i < maxClients; i++) {
                        if (clients[i] == this) {
                            clients[i] = null;
                        }
                }
            }
        }
        
        return false;     
    }
}
