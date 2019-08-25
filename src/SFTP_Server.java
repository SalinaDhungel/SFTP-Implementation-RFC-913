/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 **/

import java.io.*; 
import java.net.*; 

class SFTP_Server {
	private static boolean serverConnected = true;
    
    public SFTP_Server(int port) throws Exception
    {
		String clientSentence;
		String capitalizedSentence;
		String clientInput;
		String command;
		String response;
		boolean connectionOpen = true;

		//Open server socket for connection
		ServerSocket welcomeSocket = new ServerSocket(port);
		Socket connectionSocket = welcomeSocket.accept();

		//Set up input and output streams
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		//Initial check for server connection
		if (serverConnected){
			System.out.println("+UoA-CS725 SFTP Service");
		} else {
			System.out.println("-UoA-CS725 Out to Lunch");
		}


		while(connectionOpen) {
			System.out.println("connection is open!!!!");

			System.out.println("waiting for server to read the client's message.........");

			clientInput = inFromClient.readLine();
			System.out.println("client input that server has received displayed below vvv");
			System.out.println(clientInput);
			if (clientInput.equals("DONE\0")){
				outToClient.writeBytes("+\0"); //the response
				System.out.println("cancelling...");
				connectionOpen = false;
			} else {
				outToClient.writeBytes(clientInput.toUpperCase());
			}
        }
		System.out.println("GOODBYE from server side");
		welcomeSocket.close();

    }

} 

