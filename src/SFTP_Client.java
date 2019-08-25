/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 **/

import java.io.*; 
import java.net.*; 
class SFTP_Client {
    private OutputStream outputStream;
    
    public SFTP_Client(int port) throws Exception
    {
        String sentence;
        String modifiedSentence;
        String rawInput;
        String response;
        String command;
        boolean connectionOpen = true;

        //Initialise socket
        Socket clientSocket = new Socket("localhost", port);
        clientSocket.setReuseAddress(true);


        //Set up input and output streams
        //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
        outputStream = clientSocket.getOutputStream();
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


        while (connectionOpen) {

            //extract command from the user input and send to server
            System.out.print("TYPE COMMAND TO SEND TO SERVER: ");
            rawInput = inFromUser.readLine();
            command = rawInput.substring(0,4).toUpperCase();

            //Echo user input for testing
            System.out.println("Client Says: " + rawInput + " | command: " + command);

            //send user input data to the server for manipulation
            outToServer.println(rawInput + '\n');
            //response = inFromServer.readLine();
            //System.out.println("FROM SERVER: " + response);

            if (command.equals("DONE")){
                connectionOpen = false;
                sentence = inFromServer.readLine();
                System.out.println(sentence);
                clientSocket.close();
            } else {
                System.out.println("umm excuse you");
            }

        }
        //.out.println("client side says tataaaa");
        clientSocket.close();
    }
} 
