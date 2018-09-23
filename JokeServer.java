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

- UID generator is suedo random choosing a number between 1 and 8,000,000 

----------------------------------------------------------*/


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

class Worker extends Thread{
    Socket sock;
    Worker (Socket s) {sock = s;}

public void run(){

    PrintStream out = null;
    BufferedReader in = null;
    String userName;
    String userID;
    String userData;
    
    try{
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintStream(sock.getOutputStream());
        }catch(IOException ioe){System.out.println(ioe);}
   try{
       //READS IN CLIENT UID AND USERNAME, SPLITS AROUND CHARACTER"|"
       //SAVES USERID (UID RANDOM NUM BETWEEN 1 AND 8000000
        userData = in.readLine();
        String[] userDataArray = userData.split("\\|");
        
        userID = userDataArray[0];
        userName = userDataArray[1].replaceAll("\\s+","");
       
        //CHECKS TO SEE IF CLIENT HAS ALREADY VISITED BY QUERYING THE CLIENT MAP
        //IF AN ENTRY FOR THE UID DOES NOT EXIST, ONE IS CREATED AND GIVEN TWO BLANK
        //ARRAY LISTS TO TRACK THE PROVERBS AND JOKES THAT THE CLIENT HAS SEEN
        
        if(!JokeServer.clientMap.containsKey(userID)){
            
            //IF USER DOES NOT EXIST, TWO ARRAYS (ONE FOR JOKES SEEN AND PROVERBS SEEN) ARE CREATED
            //AND ADDED TO HASH MAP WHERE USER UID IS KEY
            
            ArrayList<ArrayList<String>> contentArray = new ArrayList<ArrayList<String>>();
            ArrayList<String> jokeArray = new ArrayList<String>();
            ArrayList<String> proverbArray = new ArrayList<String>();
         
            //CONTENT ARRAY HOLDS TWO ARRAYS; ONE FOR JOKES CLIENT HAS SEEN AND ONE FOR PROVERBS
            contentArray.add(jokeArray);
            contentArray.add(proverbArray);
            JokeServer.clientMap.put(userID, contentArray);
        }
        //SEND JOKE IS CALLED, EITHER A PROVERB OR A JOKE WILL BE SENT DEPENDING ON SERVER MODE
        sendJoke(userName, userID, out);
        sock.close();
    } catch (Exception x){x.printStackTrace();}
   
}

static void sendJoke (String userName, String clientID, PrintStream out){
    String jokeKey;
    String proverbKey;
//SEND JOKE CHECKS TO SEE WHICH MODE SERVER IS CURRENTLY IN
    try{
        if (JokeServer.JOKE_MODE == true){
            //IF JOKE MODE IS ACTIVE, CHECKCLIENTJOKES IS CALLED, PASSING THE CLIENT UID
            //TO CHECK WHICH JOKES THE USER HAS SEEN
            //JOKEKEY VARIABLE IS A JOKE NOT SEEN BEFORE AND IS SENT BACK TO CIENT
            jokeKey = checkClientJokes(clientID);
            out.println(jokeKey +": " + userName + ", " + JokeServer.jokeMap.get(jokeKey));out.flush();
        }
        else{
            //SIMILARYLY IF JOKE MODE IS FALSE, CHECKCLIENTPROVERBS IS CALLED
            //CHECKS WHICH PROVERBS CLIENT HAS NOT SEEN
            //PROVERBKEY IS RETURNED AND ACCESSES THE PROVERBS HASHMAP, RETURNING APPROPRIATE PROVERB
            //TO CLIENT
            proverbKey = checkClientProverbs(clientID);
            out.println(proverbKey +": " + userName + ", " + JokeServer.proverbMap.get(proverbKey));out.flush();
        }
    }catch(Exception ex){
        out.println("Failure");
    }
}

static String checkClientProverbs(String ClientID){
    String proverbToReturn = "";
    
    //THIS METHOD CHECKS TO SEE IF THE CLIENT
    //HAS SEEN CERTAIN PROVERBS, IF CLIENT HAS SEEN NO PROVERBS, PA IS SENT
    //IF CLIENT HAS SEEN ALL PROVERBS, PROVERBSSEEN IS CLEARED AND PROVERBCYCLE COMPLETE
    //IS PRINTED TO CONSOLE
    
    ArrayList<String> proverbsSeen = JokeServer.clientMap.get(ClientID).get(1);
    if (proverbsSeen.size() == 0){
        proverbToReturn = "PA";
        proverbsSeen.add(proverbToReturn);
    }
    else if(proverbsSeen.size() == 4){
        proverbsSeen.clear();
        proverbToReturn = "PA";
        System.out.println("Proverb Cycle Completed for Client: " + ClientID);
        proverbsSeen.add(proverbToReturn);
        }
    else{
        for(String proverb : JokeServer.proverbChoices){
            if(!proverbsSeen.contains(proverb)){
                proverbToReturn = proverb;
                proverbsSeen.add(proverbToReturn);
                return proverbToReturn;
            }
        }
    }
    return proverbToReturn;
}

static String checkClientJokes(String ClientID){
    //THIS METHOD CHECKS TO SEE IF THE CLIENT
    //HAS SEEN CERTAIN JOKES, IF CLIENT HAS SEEN NO JOKES, JA IS SENT
    //IF CLIENT HAS SEEN ALL JOKES, JOKESSEEN IS CLEARED AND JOKECYCLE COMPLETE
    //IS PRINTED TO CONSOLE
    
    String jokeToReturn = ""; 
    ArrayList<String> jokesSeen = JokeServer.clientMap.get(ClientID).get(0);
    
    if (jokesSeen.size() == 0){
        jokeToReturn = "JA";
        jokesSeen.add(jokeToReturn);
    }
    else if((jokesSeen.size() == 4)){
        jokesSeen.clear();
        jokeToReturn = "JA";
        System.out.println("Joke Cycle Completed for client: " + ClientID);
        jokesSeen.add(jokeToReturn);
        
        }
    else{
        for(String joke : JokeServer.jokeChoices){
            if(!jokesSeen.contains(joke)){
                jokeToReturn = joke;
                jokesSeen.add(jokeToReturn);
                return jokeToReturn;
            }
        }
    }
    return jokeToReturn;
}
}
  
public class JokeServer {
    //VARIABLE JOKE_MODE IS TOGGLED BY CLIENTADMIN SERVER; AUTOMATICALLY SET TO TRUE
    public static boolean JOKE_MODE = true;

    public static HashMap<String, String> jokeMap = new HashMap<String, String>();
    public static HashMap<String, String> proverbMap = new HashMap<String, String>();
    public static ArrayList<String> jokeChoices = new ArrayList<String>();
    public static ArrayList<String> proverbChoices = new ArrayList<String>();
    public static HashMap<String, ArrayList<ArrayList<String>>> clientMap = new HashMap<String, ArrayList<ArrayList<String>>>();
    
    public static void main(String[] args) throws IOException {

       int q_len = 6;
       int port = 4545;
       Socket sock;
       
       //NEW ADMIN LOOPER IS STARTED IN DIFFERENT THREAD LISTENING FOR ADMIN CONNECTION
       AdminLooper AL = new AdminLooper();
       Thread t = new Thread(AL);
       t.start();
    
       ServerSocket servsock = new ServerSocket(port, q_len);
       //JOKES AND PROVERBS ARE POPULATED ON STARTUP; CHOICES ARE ALSO POPULATED IN 
       //ARRAYLISTS TO REFERENCE AGAINST JOKES AND PROVERBS SEEN BY CLIENT
       
          jokeMap.put("JA", "Why did the chicken cross the road? To get to the other side");
          jokeMap.put("JB", "What is red and smells like paint? Red paint.");
          jokeMap.put("JC", "How much does a pound of feathers weigh? A pound.");
          jokeMap.put("JD", "What is brown and sticky? A stick.");

          proverbMap.put("PA", "The early bird catches the worm.");
          proverbMap.put("PB", "When in Rome, do as the Romans.");
          proverbMap.put("PC", "When the going gets tough, the tough get going.");
          proverbMap.put("PD", "Tactics without strategy is the noise before defeat.");
          
          jokeChoices.add("JA");
          jokeChoices.add("JB");
          jokeChoices.add("JC");
          jokeChoices.add("JD");
          
          proverbChoices.add("PA");
          proverbChoices.add("PB");
          proverbChoices.add("PC");
          proverbChoices.add("PD");
          
          System.out.println("John Berry's JokeServer 1.8 starting up, listening at port 4545. \n");
       
       while(true){
           sock = servsock.accept();
           new Worker(sock).start();
       }
    }
}

 class AdminWorker extends Thread{
     //ADMINWORKER IS NEW THREAD LISTENING FOR INPUT FROM CLIENTADMIN SERVER
     //CHANGES BETWEEN JOKE AND PROVERB MODE
     
    Socket AdminSock;
    AdminWorker (Socket s) {AdminSock = s;}}
    
 class AdminLooper implements Runnable {
  public boolean adminControlSwitch = true;
  PrintStream out = null;
  BufferedReader in = null;
  String modeChangeInput;

  public void run(){
    int q_len = 6;
    int port = 5050;
    Socket sock;
   
    try{
      ServerSocket servsock = new ServerSocket(port, q_len);
      
      while (adminControlSwitch) {
	//STARTS LISTENING AT PORT 5050 FOR A NEW ADMIN CONNECTION 
	sock = servsock.accept();
	new AdminWorker (sock).start(); 
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintStream(sock.getOutputStream());
        modeChangeInput = in.readLine();
        //MODECHANGEINPUT LISTENS FOR A 1 FROM THE CLIENTADMINSERVER
        //IF ONE IS RECEIVED, JOKESERVER CHANGES BETWEEN PROVERB AND JOKE MODE
        if(modeChangeInput.equals("1")){
            if(JokeServer.JOKE_MODE==true){
                JokeServer.JOKE_MODE = false;
                out.println("Mode was changed to proverb.");
            }
            else{
                JokeServer.JOKE_MODE = true;
                out.println("Mode was changed to joke.");
            }
        }
      }
    }catch (IOException ioe) {System.out.println(ioe);}
  
  }
}





