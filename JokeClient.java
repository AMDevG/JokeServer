import java.io.*;
import java.net.*;
import java.util.Random;

public class JokeClient {
    private static int UID;
    private static boolean hasVisited;
    private static String userName;

    public static void main (String args[]){

        boolean QUIT = false;
        String serverName;
        Socket IDsock = null;
        PrintStream IDout = null;

        if(args.length < 1){
            serverName = "localhost";
        }else{serverName = args[0];}

        UID = generateUID();

        System.out.println("John Berry's JokeClient, 1.8.\n");
        System.out.println("Using server: " + serverName + ", Port: 3024");
        System.out.println("Client UID " + UID);

        try{
            IDsock = new Socket(serverName, 3024);
            IDout = new PrintStream(IDsock.getOutputStream());
        } catch(IOException x){x.printStackTrace();}

        IDout.println("User ID Received " + UID); 
        IDout.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
    try{
        try{
            do{
                //System.out.println("Has Visited = " + hasVisited);
                if (!hasVisited){
                    hasVisited = true;
                    //System.out.println("In first time visit loop");
                    System.out.println("Please enter your name");
                    userName = in.readLine();
                    IDout.println(userName);
                    IDout.flush();

                    System.out.print("Thank you, "+ userName + "!");
                    getJoke(serverName);}
                    // System.out.flush();
                if(userName.indexOf("quit") < 0){
                    System.out.println("Back into visited");
                    System.out.println("User Input is " + userName);
                    userName = in.readLine();
                    getJoke(serverName);}
                else{QUIT = true;}
            }while (!QUIT);  
        }catch (Exception x) {x.printStackTrace();}
    }catch (Exception ex) {ex.printStackTrace();}
}


    
public static int generateUID(){
    double min = 1;
    double max = 8000000;
    double doubleID = (Math.random() * ((max - min) + 1)) + min;
    int ID = (int) Math.floor(doubleID);
    return ID;
}

static void getJoke(String serverName){

    System.out.println("In get joke");

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
        //toServer.println(UID); toServer.flush();
        toServer.println("Client UID " + UID); toServer.flush();

        for (int i = 1; i<=3; i++){
            System.out.println("In for loop from server");
            textFromServer = fromServer.readLine();
            if (textFromServer != null) System.out.println(textFromServer);
        }

        //sock.close();
    } catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
}
}
    


