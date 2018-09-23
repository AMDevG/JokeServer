/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplicationjokes;

/**
 *
 * @author johnberry
 */
import java.io.*;
import java.net.*;

public class JokeClientAdmin {
    public static String userInput;
    
      public static void main (String args[]){
        String serverName;
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
        System.out.println("John Berry's JokeClient, 1.8.\n");
        System.out.println("Using server: " + ", Port: 5050");
        System.out.println("Press Enter to get started!");

    try{
        do{ 
            userInput = in.readLine(); 
            if(userInput.indexOf("quit") < 0){
            switchModes();    
            }
        }while (true);  
        
    }catch (Exception x) {x.printStackTrace();}
      }
static void switchModes(){
    
    try{
        Socket sock = new Socket("localhost", 5050);
        PrintStream toServer = new PrintStream(sock.getOutputStream());
        toServer.println("CHANGE");
    }catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
}
}




