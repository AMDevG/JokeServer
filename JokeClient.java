import java.io.*;
import java.net.*;

public class JokeClient {
    
    public static void main (String args[]){

        String serverName;
        if(args.length < 1) serverName = "localhost";
        else serverName = args[0];
        System.out.println("John Berry's Inet Client, 1.8.\n");
        System.out.println("Using server: " + serverName + ", Port: 3024");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try{
            String name;
            do{
                // System.out.print
                //         ("Enter a hostname or an IP address, (quit) to end: ");
                // System.out.flush();
                name = in.readLine();
                if(name.indexOf("quit") < 0)
                    getRemoteAddress(serverName);

            } while (true);
            
        } catch (Exception x) {x.printStackTrace();}
    }
    

    static String toText (byte ip[]){
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < ip.length; ++i){
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }
    


static void getRemoteAddress (String serverName){

	/* getRemoteAddress does most of the heavy lifting in communicating with the server.
	It first creates a new socket (sock), passing the name of the server (localhost) and specifying which port
	to connect to (1565) */

    Socket sock;
    BufferedReader fromServer;
    PrintStream toServer;
    String textFromServer;
    
    try{
        sock = new Socket(serverName, 3024);

    /* An attempt to connect to the server is executed when the socket, named sock here, is created.
    	Variables fromServer, toServer, and textFromServer are created to read and write output between
    	client and server */

        fromServer =
                new BufferedReader(new InputStreamReader(sock.getInputStream()));
        //toServer = new PrintStream(sock.getOutputStream());

        //toServer.println(name); toServer.flush();

        for (int i = 1; i<=3; i++){
            textFromServer = fromServer.readLine();
            if (textFromServer != null) System.out.println(textFromServer);
        }

        /* readLine() is called on fromServer which retrieves outputfrom the server.
    	If it is not null, the text will be printed to the client console. (i.e. if the server
    	returns an IP address or an exception, the user will see the output.) and finally the
    	socket is closed */

        sock.close();
    } catch (IOException x){
        System.out.println("Socket error");
        x.printStackTrace();
    }
  }  
}
