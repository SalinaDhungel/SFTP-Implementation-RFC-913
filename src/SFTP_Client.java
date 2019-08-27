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
            System.out.print("\nType command : ");
            rawInput = inFromUser.readLine();
            command = rawInput.substring(0,4).toUpperCase();

            //send user input data to the server for manipulation
            outToServer.println(rawInput+ "\0");

            //Read response from server
            response = inFromServer.readLine();
            while (inFromServer.ready()){
                response = response + "\n" +  inFromServer.readLine();
            }
            System.out.println("Response from Server: " + response);

            if (command.equals("DONE\n")){
                connectionOpen = false;
                clientSocket.close();
            }
        }
        inFromServer.close();
        clientSocket.close();
    }
}
