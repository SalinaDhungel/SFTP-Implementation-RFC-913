/**
 * SFTP Server Implementation : COMPSYS725 - Assignment 1
 * Author: Salina Dhungel | sdhu434
 * University of Auckland 2019
 **/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

class SFTP_Server {
	private static boolean serverConnected = true;
	private OutputStream outputStream;
	private String response = null;
	private LoginHandler loginHandler = new LoginHandler();
	private boolean loggedIn = false;
	private int fileType = 1; // 0 = ASCII, 1 = Binary, 2 = Continuous
	String currentDirectory = System.getProperty("user.dir");
	private int loginStatus = 0; // 0 = not logged in, 1 = logged in, 2 = userVerified, 3 = accountVerified, 4 = passwordVerified
	private String inputDir = null;
	private String newFileName = null;

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
			}else if (command.equalsIgnoreCase("CDIR")) {
				response = CDIRCommand(args);
			}else if (command.equalsIgnoreCase("KILL")) {
				response = KILLCommand(args);
			}else if (command.equalsIgnoreCase("NAME")) {
				response = NAMECommand(args);
			}else if (command.equalsIgnoreCase("TOBE")) {
				response = TOBECommand(args);
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
			loginStatus = 1;
			response = "!"+ userID_string+ " logged in";
		} else if (status == 1) {
			loginStatus = 2;
			response = "+User-id valid, send account and password";
		} else if (status == 0){
			loginStatus = 0;
			response = "-Invalid user-id, try again";
		}
		return response;
	}

	/*Handle ACCT command*/
	public String ACCTCommand(String acct_string) throws Exception {
		int status;

		status = loginHandler.verifyAcct(acct_string);
		if (status == 2){
			loginStatus = 1;
			response = "!"+ acct_string+ " logged in";
		} else if (status == 1) {
			loginStatus = 3;
			response = "+Account valid, send password";
		} else if (status == 0){
			response = "-Invalid Account, try again";
		}
		if (loginStatus == 1){
			if (inputDir != null){
				System.out.println("Inside here!!");
				response = CDIRCommand(inputDir);
				inputDir = null;
			}
		}

		return response;
	}

	/*Handle PASS command*/
	public String PASSCommand(String pass_string) throws Exception {
		int status;
		status = loginHandler.verifyPass(pass_string);
		if (status == 2) {
			loginStatus = 1;
			response = "!Logged in";
		} else if (status == 1) {
			loginStatus = 4;
			response = "+Send account";
		} else if (status == 0){
			loginStatus = 0;
			response = "-Wrong password, try again";
		}
		if (inputDir != null){
			String response = CDIRCommand(inputDir);
			inputDir = null;
		}

		if (loginStatus == 1){
			if (inputDir != null){
				System.out.println("Inside here!!");
				response = CDIRCommand(inputDir);
				inputDir = null;
			}
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

	public String LISTCommand(String list_args) {
		String dirPath;
		String listType = "";
		String listOut = "";

		//if not logged in
		if (loginStatus == 0){
			return "-Please log in to use this command";
		}

		try {
			if (list_args.length() > 2) { //if there is a directory provided
				System.out.println("oh ok:: "+ currentDirectory);
				listType = list_args.substring(0, 1);
				dirPath = list_args.substring(2);

			} else {
				System.out.println("bro:: "+ currentDirectory);
				dirPath = currentDirectory;
				listType = list_args;
			}

			//get all the contents of the directory
			File[] files = new File(dirPath).listFiles();

			for (File file : files) {
				listOut = listOut + "\r\n" + file.getName();
				//System.out.println(listOut);
				if (listType.equalsIgnoreCase("F\0")) {
					response = listOut;
				} else if (listType.equalsIgnoreCase("V\0")) {
					//return getList(dirPath, true);
					if (file.isFile()) {
						listOut = listOut + " : File";
					} else {
						listOut = listOut + " : Folder";
					}
					listOut = listOut + " -Size: " + file.length() + "B  -Last Modified: " + new Date(file.lastModified()); // Append size and date
					listOut = listOut + "\r\n";
					response =listOut;
				}

				else {
					response = "-You must be logged in to use this command"; //TODO: change this condition!!
				}
			}
		} catch (Exception e) {
			response =  "-Format or directory not valid";
		}
		return response;
	}

	public String CDIRCommand(String dir){
		inputDir = dir;
		if (loginStatus == 0){
			response = "-Can't connect to directory because: User details must be provided";
		} else if (loginStatus == 1){
			currentDirectory = dir;
			response = "+!Changed working dir to " + dir;
		} else {
			response = "+directory ok, send account/password";
		}

		return response;
	}

	public String KILLCommand(String filespec){
		if (loginStatus == 1) {
			File dir = new File(currentDirectory + "/" + filespec);
			response = String.format("+" + filespec + "s deleted");
			if (dir.exists()) {
				if (dir.delete()) {
					response = "+" + filespec + "deleted";
				} else {
					response = "-Not deleted, error unknown";
				}
			} else {
				response = "-Not deleted because file does not exist";
			}
		} else {
			response = "-Not deleted because the user must be logged in";
		}
	return response;
	}

	public String NAMECommand(String oldfilespec){
		if (loginStatus == 1){
			File dir = new File(currentDirectory + "/" + oldfilespec);
			if (dir.exists()) { //
				newFileName = oldfilespec;
				response = "+File exists";

			} else {
				newFileName = null;
				response = "-Can't find " + oldfilespec;
			}
		} else {
			response = "-User must be logged in to use this command";
		}
		return response;
	}

	public String TOBECommand(String newFileSpec) {
		if (newFileName.isEmpty()){
			response = "-File was not renamed because NAME cmd not called";
		}
		File dir = new File(currentDirectory + "/" + newFileName); // Local files only. Absolute paths don't work
		File newDir = new File(currentDirectory + "/" + newFileSpec);
		if (dir.renameTo(newDir)) {
			response = "+" + newFileName + " renamed to " + newFileSpec;
		} else {
			response = "-File wasn't renamed, unknown error occurred";
		}
		return response;
	}
}

