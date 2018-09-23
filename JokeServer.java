/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplicationjokes;

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
        userData = in.readLine();
        System.out.println("Server received:  " + userData);
        String[] userDataArray = userData.split("\\|");
        
        userID = userDataArray[0];
        userName = userDataArray[1].replaceAll("\\s+","");
        
        System.out.println("Server received User ID: " + userID);
        System.out.println("Server received User Name: " + userName);
       
        //CHECKS TO SEE IF CLIENT HAS ALREADY VISITED BY QUERYING THE CLIENT MAP
        //IF AN ENTRY FOR THE UID DOES NOT EXIST, ONE IS CREATED AND GIVEN TWO BLANK
        //ARRAY LISTS TO TRACK THE PROVERBS AND JOKES THAT THE CLIENT HAS SEEN
        
        if(!JokeServer.clientMap.containsKey(userID)){
            
            //IF USER DOES NOT EXIST, TWO ARRAYS (ONE FOR JOKES SEEN AND PROVERBS SEEN) ARE CREATED
            //AND ADDED TO HASH MAP WHERE USER UID IS KEY
            
            System.out.println("User doesn't exist, adding now!");
            
            ArrayList<ArrayList<String>> contentArray = new ArrayList<ArrayList<String>>();
            ArrayList<String> jokeArray = new ArrayList<String>();
            ArrayList<String> proverbArray = new ArrayList<String>();
         
            contentArray.add(jokeArray);
            contentArray.add(proverbArray);
            JokeServer.clientMap.put(userID, contentArray);
            
        }
        System.out.println("Current UserMap is: ");
            for(String key : JokeServer.clientMap.keySet()){
                System.out.println(key +" " + JokeServer.clientMap.get(key));
            }
        sendJoke(userName, userID, out);
        sock.close();
    } catch (Exception x){x.printStackTrace();}
   
}

static void sendJoke (String userName, String clientID, PrintStream out){
    String jokeKey;
    String proverbKey;

    try{
        if (JokeServer.JOKE_MODE == true){
            System.out.println("Joke Mode is " + JokeServer.JOKE_MODE);
            jokeKey = checkClientJokes(clientID);
            out.println(jokeKey +": " + userName + ", " + JokeServer.jokeMap.get(jokeKey));out.flush();
        }
        else{
            System.out.println("Joke Mode is " + JokeServer.JOKE_MODE);
            System.out.println("In Proverbs Search");
            proverbKey = checkClientProverbs(clientID);
            out.println(proverbKey +": " + userName + ", " + JokeServer.proverbMap.get(proverbKey));out.flush();
        }
    }catch(Exception ex){
        out.println("Failed in attempt to look  up ");
    }
}

static String checkClientProverbs(String ClientID){
    String proverbToReturn = "";
    
    //BUG IS IN ARRAY SIZE COUNT
    
    ArrayList<String> proverbsSeen = JokeServer.clientMap.get(ClientID).get(1);
    if (proverbsSeen.size() == 0){
        proverbToReturn = "PA";
        proverbsSeen.add(proverbToReturn);
    }
    else if(proverbsSeen.size() == 4){
        proverbsSeen.clear();
        proverbToReturn = "PA";
        System.out.println("Proverb Cycle Completed");
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
    String jokeToReturn = "";
    
    System.out.println("In Check Client Jokes.");
    
    ArrayList<String> jokesSeen = JokeServer.clientMap.get(ClientID).get(0);
   
    System.out.println("current set of Jokes Seen: " + jokesSeen);
    
    if (jokesSeen.size() == 0){
        jokeToReturn = "JA";
        jokesSeen.add(jokeToReturn);
    }
    else if((jokesSeen.size() == 4)){
        jokesSeen.clear();
        jokeToReturn = "JA";
        System.out.println("Joke Cycle Completed");
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
    

public static class JokeServer {
    
    public static boolean JOKE_MODE = true;

    public static HashMap<String, String> jokeMap = new HashMap<String, String>();
    public static HashMap<String, String> proverbMap = new HashMap<String, String>();
    public static ArrayList<String> jokeChoices = new ArrayList<String>();
    public static ArrayList<String> proverbChoices = new ArrayList<String>();
    
    private static HashMap<String, ArrayList<ArrayList<String>>> clientMap = new HashMap<String, ArrayList<ArrayList<String>>>();
    
    public static void main(String[] args) throws IOException {

       int q_len = 6;
       int port = 4545;
       Socket sock;
       
       AdminLooper AL = new AdminLooper();
       Thread t = new Thread(AL);
       t.start();
    
       ServerSocket servsock = new ServerSocket(port, q_len);
       
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

static class AdminWorker extends Thread{
    Socket AdminSock;
    AdminWorker (Socket s) {AdminSock = s;}}
    
static class AdminLooper implements Runnable {
  public boolean adminControlSwitch = true;
  PrintStream out = null;
  BufferedReader in = null;
  String modeChangeInput;

  public void run(){ // RUNning the Admin listen loop
    int q_len = 6; /* Number of requests for OpSys to queue */
    int port = 5050;  // We are listening at a different port for Admin clients
    Socket sock;
   
    try{
      ServerSocket servsock = new ServerSocket(port, q_len);
      
      while (adminControlSwitch) {
	// wait for the next ADMIN client connection:
	sock = servsock.accept();
	new AdminWorker (sock).start(); 
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintStream(sock.getOutputStream());
        modeChangeInput = in.readLine();
        System.out.println("Received from Admin=" + modeChangeInput);
        System.out.println();
        if(modeChangeInput.equals("1")){
            System.out.println("Changing Mode");
            if(JokeServer.JOKE_MODE==true){
                JokeServer.JOKE_MODE = false;
                out.println("Mode was changed to proverb.");
            }
            else{
                System.out.println("Changing Mode");
                JokeServer.JOKE_MODE = true;
                out.println("Mode was changed to joke.");
            }
        }
      }
    }catch (IOException ioe) {System.out.println(ioe);}
  
  }
}
}




