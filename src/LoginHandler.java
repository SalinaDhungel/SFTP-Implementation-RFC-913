import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LoginHandler {
    private String currentUser;
    private String currentAccount;
    private String currentPassword;
    private boolean userVerified = false;
    private int lineNumber = 0;


    public LoginHandler() throws Exception {

        int userVerified = 0;
        int passwordVerified = 0;
        int accountVerified = 0;
    }

    public int verifyUserID(String userID) throws IOException {
        BufferedReader fileContents = new BufferedReader(new FileReader("login_data.txt"));
        StringBuilder fileText = new StringBuilder();
        String lineFromFile = fileContents.readLine();
        String currentUser;
        int endOfUserID;
        int startOfPassword;

        try {
            while ((lineFromFile = fileContents.readLine()) != null) {
                lineNumber++;
                endOfUserID = lineFromFile.indexOf("[");
                startOfPassword = lineFromFile.indexOf("]");

                currentUser = lineFromFile.substring(0, endOfUserID);
                System.out.println("--> currentuser: " + currentUser + "--> userID: " + userID + "line number: " + lineNumber);

                if (userID.equals(currentUser + "\0")) {
                    return 2; //valid user
                } else {
                    System.out.println("hlelo");
                    return 0; //invalid user
                }
                //currentAccount = lineFromFile.substring(endOfUserID+1, startOfPassword);
                //currentPassword = lineFromFile.substring(startOfPassword+1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("(ArrayIndexOutOfBoundsException occured when trying to access userList");
        }
        return 2;
    }

    public int verifyAcct(String acct_string) throws IOException {
        BufferedReader fileContents = new BufferedReader(new FileReader("login_data.txt"));
        StringBuilder fileText = new StringBuilder();
        String lineFromFile = fileContents.readLine();
        String currentAccount = null;
        int endOfUserID = lineFromFile.indexOf("[");
        int startOfPassword = lineFromFile.indexOf("]");
        int count = 0;
        try {
            while ((lineFromFile = fileContents.readLine()) != null) {
                count++;
                if (lineNumber == count) {
                    currentAccount = lineFromFile.substring(endOfUserID + 1, startOfPassword);
                    System.out.println("current account = " + currentAccount);
                    if (currentAccount.equals(acct_string + "\0")) {
                        return 2; //valid acct
                    } else {
                        System.out.println("account wrong");
                        return 0; //invalid acct
                    }
                }
                //currentPassword = lineFromFile.substring(startOfPassword+1);
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
        int endOfUserID = lineFromFile.indexOf("[");
        int startOfPassword = lineFromFile.indexOf("]");
        int count = 0;
            try {
                while ((lineFromFile = fileContents.readLine()) != null) {
                    count++;
                    if (lineNumber == count) {
                        currentPassword = lineFromFile.substring(startOfPassword+1);
                        System.out.println("current password = " + currentAccount);
                        if (currentPassword.equals(pass_string + "\0") && (currentUser != null || currentAccount != null)) {
                            return 2; //valid acct
                        } else if (currentPassword.equals(pass_string + "\0") && (currentUser == null || currentAccount == null)) {
                            System.out.println("account wrong");
                            return 1; //valid account but need to give acct first
                        } else {
                            return 0 ; //invalid
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("(ArrayIndexOutOfBoundsException occured when trying to access userList");
            }
        return 1;
    }
}