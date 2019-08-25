/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 **/

import java.io.*; 
import java.net.*; 

class SFTP_Server {
	private static boolean serverConnected = true;
	private OutputStream outputStream;
    
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
		welcomeSocket.setReuseAddress(true);

		//Set up input and output streams
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
		outputStream = connectionSocket.getOutputStream();

		//Initial check for server connection
		if (serverConnected){
			System.out.println("+UoA-CS725 SFTP Service");
		} else {
			System.out.println("-UoA-CS725 Out to Lunch");
		}


		while(connectionOpen) {

			//extract the command from raw client input
			clientInput = inFromClient.readLine();
			command = clientInput.toUpperCase().substring(0,4) + '\n';
			//outToClient.println(command);

			if(command.equals("DONE\n")){
				connectionOpen = false;
				outToClient.println(command);
			} else {
				System.out.println("shouldnt be here...");
			}

		}
		System.out.println("goodbye from server side");
		welcomeSocket.close();
	}

} 

