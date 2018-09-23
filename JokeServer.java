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
       
        if(!JokeServer.clientMap.containsKey(userID)){
            System.out.println("User doesn't exist, adding now!");
            JokeServer.clientMap.put(userID, new ArrayList<String>());
    
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
    try{
        jokeKey = checkClientJokes(clientID);
        out.println(jokeKey +": " + userName + ", " + JokeServer.jokeMap.get(jokeKey));out.flush();
        
    }catch(Exception ex){
        out.println("Failed in attempt to look  up ");
    }
}

static String checkClientJokes(String ClientID){
    String jokeToReturn = "";
    
    ArrayList<String> jokesSeen = JokeServer.clientMap.get(ClientID);
    if (jokesSeen.size() == 0){
        jokeToReturn = "JA";
        JokeServer.clientMap.get(ClientID).add(jokeToReturn);
    }
    else if((jokesSeen.size() == 4)){
        JokeServer.clientMap.get(ClientID).clear();
        jokeToReturn = "JA";
        JokeServer.clientMap.get(ClientID).add(jokeToReturn);
        
        }
    else{
        for(String joke : JokeServer.jokeChoices){
            if(!jokesSeen.contains(joke)){
                jokeToReturn = joke;
                JokeServer.clientMap.get(ClientID).add(jokeToReturn);
                return jokeToReturn;
            }
        }
    }
    return jokeToReturn;
}
    

public static class JokeServer {
    
    public static boolean JOKE_MODE = false;

    public static HashMap<String, String> jokeMap = new HashMap<String, String>();
    public static HashMap<String, String> proverbMap = new HashMap<String, String>();
    public static ArrayList<String> jokeChoices = new ArrayList<String>();
    
    private static HashMap<String, ArrayList<String>> clientMap = new HashMap<String, ArrayList<String>>();
    
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

  public void run(){ // RUNning the Admin listen loop
    System.out.println("In the admin looper thread");
    
    int q_len = 6; /* Number of requests for OpSys to queue */
    int port = 5050;  // We are listening at a different port for Admin clients
    Socket sock;
   
    try{
      ServerSocket servsock = new ServerSocket(port, q_len);
      //in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      //out = new PrintStream(sock.getOutputStream());
      
      while (adminControlSwitch) {
	// wait for the next ADMIN client connection:
	sock = servsock.accept();
	new AdminWorker (sock).start(); 
      }
    }catch (IOException ioe) {System.out.println(ioe);}
  
  }
}
}




