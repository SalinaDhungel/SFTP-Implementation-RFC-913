import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LoginHandler {
    private String currentUser;
    private String currentAccount;
    private String currentPassword;
    private boolean userVerified = false;
    boolean passwordVerified = false;
    boolean accountVerified = false;
    private int lineNumber = 0;


    public int verifyUserID(String userID) throws IOException {
        BufferedReader fileContents = new BufferedReader(new FileReader("login_data.txt"));
        StringBuilder fileText = new StringBuilder();
        String lineFromFile = fileContents.readLine();
        String currentUser;
        int endOfUserID;
        int startOfPassword;

        try {
            //read user list from txt file
            while ((lineFromFile = fileContents.readLine()) != null) {
                lineNumber++;
                endOfUserID = lineFromFile.indexOf("[");
                startOfPassword = lineFromFile.indexOf("]");
                currentUser = lineFromFile.substring(0, endOfUserID);
                //Check if username exists
                if (userID.equals(currentUser+"\0")) {
                    String linkedAccount = lineFromFile.substring(endOfUserID + 1, startOfPassword);
                    String linkedPassword = lineFromFile.substring(startOfPassword + 1);
                    //Check what other credentials the entered username requires to log in
                    if (userID.equals(currentUser + "\0") && linkedAccount.isEmpty() && linkedPassword.isEmpty()) {
                        userVerified = true;
                        return 2; //valid user
                    } else if (userID.equals(currentUser + "\0") && (!linkedAccount.isEmpty() || !linkedPassword.isEmpty())) {
                        userVerified = true;
                        return 1; //user id valid, send account and password
                    } else if (!userID.equals(currentUser + "\0")) {
                        return 0; //invalid user
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("(ArrayIndexOutOfBoundsException occured when trying to access userList");
        }
        return -1;
    }

    public int verifyAcct(String acct_string) throws IOException {
        BufferedReader fileContents = new BufferedReader(new FileReader("login_data.txt"));
        StringBuilder fileText = new StringBuilder();
        String lineFromFile = fileContents.readLine();
        int endOfUserID;
        int startOfPassword;
        int count = 0;

        try {
            while ((lineFromFile = fileContents.readLine()) != null) {
                count++;
                endOfUserID = lineFromFile.indexOf("[");
                startOfPassword = lineFromFile.indexOf("]");
                String currentAccount = lineFromFile.substring(endOfUserID + 1, startOfPassword);
                String linkedPassword = lineFromFile.substring(startOfPassword+1);

                if (lineNumber == count) {
                    currentAccount = lineFromFile.substring(endOfUserID + 1, startOfPassword);
                    linkedPassword = lineFromFile.substring(startOfPassword+1);
                    //Check what other credentials the entered acct requires to log in
                    if (acct_string.equals(currentAccount + "\0") && userVerified && linkedPassword.isEmpty()) {
                        accountVerified = true;
                        return 2; //valid acct
                    } else if ((acct_string.equals(currentAccount + "\0") && userVerified && (!passwordVerified && !linkedPassword.isEmpty()))){
                        accountVerified = true;
                        return 1; //acct valid, send password
                    } else if (!userVerified || (!acct_string.equals(currentAccount + "\0"))){
                        return 0; //acct invalid
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("(ArrayIndexOutOfBoundsException occured when trying to access userList");
        }
        return 1;
    }

    public int verifyPass(String pass_string) throws IOException {
        BufferedReader fileContents = new BufferedReader(new FileReader("login_data.txt"));
        StringBuilder fileText = new StringBuilder();
        String lineFromFile = fileContents.readLine();
        int endOfUserID ;
        int startOfPassword;
        int count = 0;

            try {
                while ((lineFromFile = fileContents.readLine()) != null) {
                    count++;
                    endOfUserID = lineFromFile.indexOf("[");
                    startOfPassword = lineFromFile.indexOf("]");
                    if (lineNumber == count) {
                        currentPassword = lineFromFile.substring(startOfPassword+1);
                        String linkedAccount = lineFromFile.substring(endOfUserID + 1, startOfPassword);
                        if (pass_string.equals(currentPassword+ "\0") && userVerified) {
                            if (accountVerified || linkedAccount.isEmpty()) {
                                passwordVerified = true;
                                return 2; //valid acct
                            } else {
                                return 1; //valid account but need to give acct first
                            }
                        } else if (!pass_string.equals(currentPassword+ "\0") || !userVerified) {
                            return 0 ; //invalid
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("(ArrayIndexOutOfBoundsException occurred when trying to access userList");
            }
        return 1; //ask user to send user or acct before password
    }
}