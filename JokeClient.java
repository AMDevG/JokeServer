import java.io.*;
import java.net.*;
import java.util.Random;

public class JokeClient {
    private static int UID;
    private static boolean hasVisited;
    public static String userName;
    public static String userInput;
    private static Socket IDsock;

    public static void main (String args[]){
        String serverName;
        IDsock = null;
        if(args.length < 1){
            serverName = "localhost";        
        }else{serverName = args[0];
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        UID = generateUID();
        System.out.println("John Berry's JokeClient, 1.8.\n");
        System.out.println("Using server: " + serverName + ", Port: 4545");
        System.out.println("Client UID " + UID);
        System.out.println("Press Enter to get started!");

    try{
        do{ 
            userInput = in.readLine(); 
            if(userInput.indexOf("quit") < 0){
                getJoke(serverName);}
        }while (true);  
        
    }catch (Exception x) {x.printStackTrace();}
}
    
public static int generateUID(){
    double min = 1;
    double max = 8000000;
    double doubleID = (Math.random() * ((max - min) + 1)) + min;
    int ID = (int) Math.floor(doubleID);
    return ID;
}

static void getJoke(String serverName){

    Socket IDsock;
    BufferedReader fromServer;
    PrintStream toServer;
    String textFromServer;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    try{

        IDsock = new Socket(serverName, 4545);
        fromServer =
                new BufferedReader(new InputStreamReader(IDsock.getInputStream()));
        toServer = new PrintStream(IDsock.getOutputStream());
        
        if(!hasVisited){
            hasVisited = true;
            System.out.println("Please enter your name");
            userName = in.readLine();
            toServer.println(UID + " | " + userName); toServer.flush();
        }
        else{
            String entry = in.readLine();
            toServer.println(UID + " | " + userName); toServer.flush();
        }

        for (int i = 1; i<=3; i++){
            textFromServer = fromServer.readLine();
            if (textFromServer != null) System.out.println(textFromServer);
        }

        IDsock.close();
    } catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
}
}
    


