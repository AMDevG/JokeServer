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

Changes server mode on enter key; minor but in quitting server, need to kill process
Sends a "1" to JokeServer to flip modes
----------------------------------------------------------*/
import java.io.*;
import java.net.*;

public class JokeClientAdmin {
    public static String userInput;
    
      public static void main (String args[]){
        //IF NO SERVER IP IS PROVIDED, ADMIN CONNECTS TO LOCALHOST AT PORT 5050
        String serverName;
        if(args.length < 1){
            serverName = "localhost";        
        }else{serverName = args[0];
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
        System.out.println("John Berry's JokeAdminClient, 1.8.\n");
        System.out.println("Using server: " + serverName + ", Port: 5050");
        System.out.println("Press Enter to Change Server Modes");

    try{
        do{ 
            userInput = in.readLine(); 
            if(userInput.indexOf("quit") < 0){
            switchModes(serverName);    
            }
        }while (true);  
        
    }catch (Exception x) {x.printStackTrace();}
      }
static void switchModes(String serverName){
    //IF ENTER IS PUSHED, CLIENTADMIN WILL SEND MESSAGE TO JOKESERVER TOGGLING BETWEEN PROVERB
    //AND JOKE MODES BY SENDING INT 1
    try{
        Socket sock = new Socket(serverName, 5050);
        PrintStream toServer = new PrintStream(sock.getOutputStream());
        System.out.println("Changing JokeServer Mode");
        toServer.println(1);
    }catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
}
}