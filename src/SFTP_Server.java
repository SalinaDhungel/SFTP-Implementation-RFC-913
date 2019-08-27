/**
 * SFTP Server Implementation : COMPSYS725 - Assignment 1
 * Author: Salina Dhungel | sdhu434
 * University of Auckland 2019
 **/

import java.io.*; 
import java.net.*; 

class SFTP_Server {
	private static boolean serverConnected = true;
	private OutputStream outputStream;
	private String response = null;
	private LoginHandler loginHandler = new LoginHandler();
	private boolean loggedIn = false;
	private int fileType = 1; // 0 = ASCII, 1 = Binary, 2 = Continuous


	public SFTP_Server(int port) throws Exception
    {
		String clientInput;
		String command;
		String args;

		boolean connectionOpen= true;

		//Open server socket for connection
		ServerSocket welcomeSocket = new ServerSocket(port);
		Socket connectionSocket = welcomeSocket.accept();
		welcomeSocket.setReuseAddress(true);

		//Set up input and output streams
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
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
			command = clientInput.substring(0,4);
			//System.out.println("\ncommand is:::::::: " + command);
			args = clientInput.substring(5, clientInput.length());

			if(command.equalsIgnoreCase("DONE")){
				connectionOpen = false;
				response = ("+UoA-CS725 closing connection");
			} else if (command.equalsIgnoreCase("USER")){
				//System.out.println("args are:::: " + args);
				response = USERCommand(args);
			}  else if (command.equalsIgnoreCase("ACCT")){
				//System.out.println("ACCt args are:::: " + args);
				response = ACCTCommand(args);
			}else if (command.equalsIgnoreCase("PASS")){
				response = PASSCommand(args);
			} else if (command.equalsIgnoreCase("TYPE")) {
				response = TYPECommand(args);
			} else if (command.equalsIgnoreCase("LIST")) {
				response = LISTCommand(args);
			}else {
				response = "-Invalid Query";
				//System.out.println("not done or user");
			}

			//SEND RESPONSE BACK TO SERVER
			System.out.println("response from server:: "+ response + "\0");
			if (!response.isEmpty()) {
				outToClient.println(response);
			}
			response = null;
			//System.out.println("hello!");

		}
		//System.out.println("goodbye from server side");
		welcomeSocket.close();
	}

	/*Handle USER command*/
	public String USERCommand(String userID_string) throws Exception {
		int status;
		status = loginHandler.verifyUserID(userID_string);
		if (status == 2){
			loggedIn = true;
			response = "!"+ userID_string+ " logged in";
		} else if (status == 1) {
			response = "+User-id valid, send account and password";
		} else if (status == 0){
			response = "-Invalid user-id, try again";
		}
		return response;
	}

	/*Handle ACCT command*/
	public String ACCTCommand(String acct_string) throws Exception {
		int status;
		status = loginHandler.verifyAcct(acct_string);
		if (status == 2){
			loggedIn = true;
			response = "!"+ acct_string+ " logged in";
		} else if (status == 1) {
			response = "+Account valid, send password";
		} else if (status == 0){
			response = "-Invalid Account, try again";
		}
		return response;
	}

	/*Handle PASS command*/
	public String PASSCommand(String pass_string) throws Exception {
		int status;
		status = loginHandler.verifyPass(pass_string);
		if (status == 2) {
			loggedIn = true;
			response = "!Logged in";
		} else if (status == 1) {
			response = "+Send account";
		} else if (status == 0){
			response = "-Wrong password, try again";
		}
		return response;
	}

	public String TYPECommand(String type_string) throws Exception {
		System.out.println(type_string);
		if (type_string.equalsIgnoreCase("A\0")){
			fileType = 0;

			response = "+Using Ascii mode";
		} else if (type_string.equalsIgnoreCase("B\0")){
			response = "+Using Binary mode";
			fileType = 1;
		} else if (type_string.equalsIgnoreCase("C\0")){
			response = "+Using Continuous mode";
			fileType = 2;
		} else {
			fileType = -1;
			response = "-Type not valid";
		}
		return  response;
	}

	public String LISTCommand(String list_para){
		//check if the user is logged in first!
		if (loggedIn){
			System.out.println(list_para);
		} else {
			response = "-Must be logged in to use this command";
		}
		return response;
	}
}

