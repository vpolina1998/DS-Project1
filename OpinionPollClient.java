import java.net.*;
import java.io.*;

/**
 * This module contains the presentation logic of an Echo Client.
 * @author M. L. Liu
 */
public class OpinionPollClient {
   static final String endMessage = ".";
   static final String option = "Choose an option";
   static final int MAX_LEN = 100; // Define MAX_LEN constant here
   private DatagramSocket socket;

   OpinionPollClient(String hostName, String portNum) throws SocketException {
      this.socket = new DatagramSocket();
   }

   public String getEcho(String message) throws IOException {
      byte[] sendBuffer = message.getBytes();
      InetAddress serverAddress = InetAddress.getByName("localhost"); // Adjust this if necessary
      int serverPort = 30000; // Adjust this to the appropriate server port
      DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
      socket.send(sendPacket);

      byte[] receiveBuffer = new byte[MAX_LEN];
      DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
      socket.receive(receivePacket);

      return new String(receivePacket.getData(), 0, receivePacket.getLength());
   }

   public void done() {
      socket.close();
   }

   public static void main(String[] args) {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      try {
         System.out.println("Welcome to the Echo client.\n" +
                            "What is the name of the server host?");
         String hostName = br.readLine();
         if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost";  // use the default host name
         System.out.println("What is the port number of the server host?");
         String portNum = br.readLine();
         if (portNum.length() == 0)
            portNum = "10000";          // default port number
         OpinionPollClient client = new OpinionPollClient(hostName, portNum);
         boolean done = false;
         String message, echo;
         while (!done) {
            System.out.println("Type 'Choose an option' to choose or 'Check the results' to check, or a single period to quit.");
            message = br.readLine();
            if (message.trim().equals(endMessage)) {
               done = true;
               client.done();
            } else {
               if (message.trim().equals(option)) {
                  System.out.println("(1) yesCount, (2) noCount, or (3) dontCareCount");
                  message = br.readLine();
               }
               echo = client.getEcho(message);
               System.out.println(echo);
            }
         } // end while
      } // end try  
      catch (Exception ex) {
         ex.printStackTrace();
      } // end catch
   } // end main
} // end class