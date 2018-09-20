import java.io.*;
import java.net.*;

public class JokeClient {
    
    public static void main (String args[]){

        String serverName;
        if(args.length < 1) serverName = "localhost";
        else serverName = args[0];
        System.out.println("John Berry's Inet Client, 1.8.\n");
        System.out.println("Using server: " + serverName + ", Port: 3024");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try{
            String userName;
            do{
               System.out.print("Please enter your name");
                // System.out.flush();
                userName = in.readLine();
                if(userName.indexOf("quit") < 0)
                    getRemoteAddress(userName, serverName);

            } while (true);
            
        } catch (Exception x) {x.printStackTrace();}
    }
    

    static String toText (byte ip[]){
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < ip.length; ++i){
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }
    


static void getRemoteAddress (String userName, String serverName){

    Socket sock;
    BufferedReader fromServer;
    PrintStream toServer;
    String textFromServer;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    try{
    
        sock = new Socket(serverName, 3024);

        fromServer =
                new BufferedReader(new InputStreamReader(sock.getInputStream()));
        toServer = new PrintStream(sock.getOutputStream());
        toServer.println(userName); toServer.flush();

        for (int i = 1; i<=3; i++){
            textFromServer = fromServer.readLine();
            if (textFromServer != null) System.out.println(textFromServer);
        }

        sock.close();
    } catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
  }  
}
