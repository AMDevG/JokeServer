import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
        printRemoteAddress(out);
    } catch (Exception x){
        System.out.println("Server read error");
        x.printStackTrace();
    }
    sock.close();
    }catch(IOException ioe){System.out.println(ioe);
    }
}

static void printRemoteAddress (PrintStream out){

    try{
        out.println("Why did chicken cross road");
    }catch(Exception ex){
        out.println("Failed in attempt to look  up ");
    }
    
}

static String toText (byte ip[]) {
  /* toText is used to make the output of getAddress() human readable */
 StringBuffer result = new StringBuffer ();
 for (int i = 0; i < ip.length; ++ i) {
 if (i > 0) result.append (".");
 result.append (0xff & ip[i]);
 }
 return result.toString ();
 }
}

public class JokeServer {

     public static ArrayList<String> jokeList;

    
    /* InetServer's main function creates a new ServerSocket that will accept client connections.
      The server will accept new connections until it is terminated due to the while(true) condition
      which will always remain true. When a new connection is accepted, a new Worker thread is created
      to handle the client requests. The server listens on port 3024 for any incoming connections. */

    public static void main(String[] args) throws IOException {
       int q_len = 6;
       int port = 3024;
       Socket sock;
       ServerSocket servsock = new ServerSocket(port, q_len);

       
       System.out.println("John Berry's Inet Server 1.8 starting up, listening at port 3024. \n");
       while(true){
           sock = servsock.accept();
           new Worker(sock).start();
       }
    }
}
