/*--------------------------------------------------------

1. John Berry / 9/23/2018:

2. Java version 1.8

3. Precise command-line compilation examples / instructions:

> javac JokeServer.java
> javac JokeClient.java
> javac JokeCLientAdmin.java

4.

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin


This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java

5. Notes:

Minor bug in having to hit Enter to start communications, the User is prompted
to enter name, then is returned a joke with their name on each subsequent Enter key.

Minor bug in quitting, need to kill process entirely
----------------------------------------------------------*/
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
        //IF SPECIFIC SERVER IP IS NOT PROVIDED DEFAULT OF LOCALHOST IS USED
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
        System.out.println("Press Enter to get started!");
        //HAVE TO PRESS ENTER TO START LOOP; MINOR BUG

    try{
        do{ 
            userInput = in.readLine(); 
            if(userInput.indexOf("quit") < 0){
                //IF QUIT IS NOT ENTERED A JOKE OR PROVERB WILL BE REQUESTED
                getJoke(serverName);}
        }while (true);  
        
    }catch (Exception x) {x.printStackTrace();}
}
    
public static int generateUID(){
    //FUNCTION GENERATES A UNIQUE IDENTIFIER FOR THE CLIENT TO BE SENT TO SERVER
    //SERVER USES UID TO MAINTAIN STATE OF CLIENT (I.E. WHICH JOKES/PROVERBS CLIENT HAS ALREADY SEEN)
    double min = 1;
    double max = 8000000;
    double doubleID = (Math.random() * ((max - min) + 1)) + min;
    int ID = (int) Math.floor(doubleID);
    return ID;
}

static void getJoke(String serverName){

    //MAKES REQUEST TO SERVER TO GET A JOKE OR PROVERB DEPENDING ON WHICH MODE SERVER IS IN
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
        
        //HAS VISITED CHECKS TO SEE WHETHER THE CLIENT HAS ALREADY PROVIDED A USERNAME
        //IF THEY HAVE VISITED THE CLIENT USERNAME IS STORED AND SENT TO SERVER
        //WITH EACH SUBSEQUENT REQUEST
        if(!hasVisited){
            hasVisited = true;
            System.out.println("Please enter your name");
            userName = in.readLine();
            toServer.println(UID + " | " + userName); toServer.flush();
        }
        else{
            //String entry = in.readLine();
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
    


