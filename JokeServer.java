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
        userID = in.readLine();
        System.out.println("Server got user ID " + userID);
        printRemoteAddress(userID, out);
    } catch (Exception x){
        System.out.println("Server read error");
        x.printStackTrace();
    }
    sock.close();
    }catch(IOException ioe){System.out.println(ioe);
    }
}

static void printRemoteAddress (String userID, PrintStream out){

    try{
        out.println(userID + ", " + "Why did chicken cross road");
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

    private static HashMap<String, String> jokeMap = new HashMap<String, String>();
    private static HashMap<String, String> clientMap = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {
       int q_len = 6;
       int port = 3024;
       Socket sock;
       ServerSocket servsock = new ServerSocket(port, q_len);
       //ClientDB clientDB = clientDB.getInstance();
        jokeMap.put("A", "Why did the chicken cross the road?");
        jokeMap.put("B", "Why did the chicken cross the alley?");
        jokeMap.put("C", "Why did the chicken cross the SIDEWALK?");
        jokeMap.put("D", "Why did the chicken cross the GUTTER?");
       
       System.out.println("John Berry's JokeServer 1.8 starting up, listening at port 3024. \n");
       while(true){
           sock = servsock.accept();
           new Worker(sock).start();
       }
    }
}

/*public final class ClientDB{

  private static Map<Double, String> clientMap = new HashMap<Double, String>();

  private static ClientDB instance;

    private ClientDB(){}

    public static CLientDB getInstance(){

      if(instance == null){
        instance = new ClientDB();
        jokeMap.put("A", "Why did the chicken cross the road?");
        jokeMap.put("B", "Why did the chicken cross the alley?");
        jokeMap.put("C", "Why did the chicken cross the SIDEWALK?");
        jokeMap.put("D", "Why did the chicken cross the GUTTER?");
        System.out.println("Created Hashmap of jokes");
        return instance;
      }
      return instance;
    }*/


