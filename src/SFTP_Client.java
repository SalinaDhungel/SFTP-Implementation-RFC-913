/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 **/

import java.io.*; 
import java.net.*; 
class SFTP_Client {
    
    public SFTP_Client(int port) throws Exception
    { 
        String sentence; 
        String response;
        String command;
        boolean connectionOpen = true;


        //Initialise socket
        Socket clientSocket = new Socket("localhost", port);

        //Set up input and output streams
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        while(connectionOpen) {
            //extract command from the user input and send to server
            command = inFromUser.readLine().substring(0,4).toUpperCase();
            System.out.println("the client: " + command);
            outToServer.writeBytes(command + '\0');

            //receive the response from the server
            response = inFromServer.readLine();
            System.out.println("FROM SERVER: the response is..." + response);

            if (command.equals("DONE")) {
                connectionOpen = false;
                System.out.println("the command was... " + command);
            } else {
                System.out.println("the else statement");
            }
        }

        System.out.println("client side says TATA!!");
        clientSocket.close();
    }
} 
