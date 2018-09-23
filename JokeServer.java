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

/* Worker class extends the Thread class making it a subclass so that it can make use of Java's
  threading capabilities. Threading is necessary to the server's functionality so that the server can 
  accept multiple client requests simultaneously (or almost simultaneously)*/

public void run(){

    PrintStream out = null;
    BufferedReader in = null;
    
    try{
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintStream(sock.getOutputStream());
    try{
        String userID;
        String userName;
        userID = in.readLine();
        
        if(JokeServer.clientMap.containsKey(userID)){
            checkClientJokes(userID);
        }else{
            System.out.println("User doesn't exist, adding now!");
            JokeServer.clientMap.put(userID, new ArrayList<String>());
            //System.out.println("User: " + userID + "has seen jokes: " + checkClientJokes(userID));
        }
        
        System.out.println("Connection accepted from " + userID);
        //System.out.println("Here is a proverb " + JokeServer.proverbMap.get("PA"));
        userName = in.readLine();
        //System.out.println("Server got user " + userName);
        //out.println("About to send you a joke!");
        sendJoke(userName, userID, out);
    } catch (Exception x){
        System.out.println("Server read error");
        x.printStackTrace();
    }
    sock.close();
    }catch(IOException ioe){System.out.println(ioe);
    }
}

static void sendJoke (String userName, String clientID, PrintStream out){
    String jokeKey;
    try{
        jokeKey = checkClientJokes(clientID);
        System.out.println("Checked Jokes Jokey Key is: " + jokeKey);
        System.out.println("Current Client Map is " + JokeServer.clientMap.get(clientID));
        //System.out.println("Checking client jokes return value is: " + checkClientJokes(clientID));
        out.println(userName + ", " + JokeServer.jokeMap.get(jokeKey));
        
    }catch(Exception ex){
        out.println("Failed in attempt to look  up ");
    }
}

static String checkClientJokes(String ClientID){
    String jokeToReturn = "";
    
    ArrayList<String> jokesSeen = JokeServer.clientMap.get(ClientID);
    if (jokesSeen.size() == 0){
        jokeToReturn = "JA";
        //ADDS JOKE TO CLIENT ARRAY LIST
        JokeServer.clientMap.get(ClientID).add(jokeToReturn);
    }
    else{
        for(String joke : JokeServer.jokeChoices){
            if(!jokesSeen.contains(joke)){
                jokeToReturn = joke;
                return jokeToReturn;
            }
        }
    }
    return jokeToReturn;
}
    
    








public static class JokeServer {

    public static HashMap<String, String> jokeMap = new HashMap<String, String>();
    public static HashMap<String, String> proverbMap = new HashMap<String, String>();
    public static ArrayList<String> jokeChoices = new ArrayList<String>();
    
    private static HashMap<String, ArrayList<String>> clientMap = new HashMap<String, ArrayList<String>>();
    
    public static void main(String[] args) throws IOException {
        
       System.out.println("In Main of Server");
       int q_len = 6;
       int port = 4545;
       Socket sock;
       ServerSocket servsock = new ServerSocket(port, q_len);
       
          jokeMap.put("JA", "Why did the chicken cross the road?");
          jokeMap.put("JB", "Why did the chicken cross the alley?");
          jokeMap.put("JC", "Why did the chicken cross the SIDEWALK?");
          jokeMap.put("JD", "Why did the chicken cross the GUTTER?");

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

   // private JokeServer(){};

    /*public static JokeServer getInstance(){
      if(instance == null){
        instance = new JokeServer();
        return instance;
      }
      return instance;
    }*/
}
}