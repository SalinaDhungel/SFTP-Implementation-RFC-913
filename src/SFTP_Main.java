public class SFTP_Main {

    public static void main(String argv[]) {
        int port = 115;

        //Make a new thread to start the server
        Thread serverThread = new Thread(){
            public void run(){
                System.out.println("SFTP Server Starting...");
                try {
                    SFTP_Server myServer = new SFTP_Server(port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serverThread.start();


        //Start Client
        System.out.println("SFTP Client Starting...");
        try {
            SFTP_Client myClient = new SFTP_Client(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n SFTP Protocol Ended");
    }

}
