/**
 * SFTP Client Implementation : COMPSYS725 - Assignment 1
 * Author: Salina Dhungel | sdhu434
 * University of Auckland 2019
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
            //System.out.println("Client Says: " + rawInput + " | command: " + command);

            //send user input data to the server for manipulation
            outToServer.println(rawInput+ "\0");

            if (command.equals("DONE\n")){
                connectionOpen = false;
                //sentence = inFromServer.readLine();
                //System.out.println(sentence);
                clientSocket.close();
            } else if (command.equals("USER")){
                //System.out.println("testing!!!!!!");

            } else if (command.equals("ACCT")) {
               // System.out.println("acct command!");

            } else if (command.equals("PASS")) {
                //System.out.println("pass command!");
            }else  {
                //System.out.println("umm excuse you");
            }

                response = inFromServer.readLine();
                System.out.println("the RESPONSE::::: " + response);


        }
        inFromServer.close();

        //.out.println("client side says tataaaa");
        clientSocket.close();
    }
} 
