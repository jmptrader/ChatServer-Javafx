package model;

import controller.ChatControl;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class MultiClientReceive implements Runnable {

    private BufferedReader br = null;
    private MulticastSocket chat = null;
    private InetAddress group = null;
    private Thread thread = null;
    private MyMessage mess = null;
    private DataInputStream console = null;
    int port = 0;
    private String address = "";
    private ChatControl chatControl = null;

    public MultiClientReceive(String addr, int the_port, ChatControl chatControl1, MyMessage message) {
        this.address = addr;
        this.chatControl = chatControl1;
        this.mess = message;
        this.port = the_port;
        try {
            chat = new MulticastSocket(port);
            group = InetAddress.getByName(addr);
            chat.joinGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            start();
        } catch (IOException ex) {
            Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void run() {
        while (thread != null) {
            try {

                try {

                    byte buf[] = new byte[1024];
                    DatagramPacket data = new DatagramPacket(buf, buf.length);
                    //Separate thread that receives data
                    chat.receive(data);
                    String msg1 = new String(data.getData()).trim();
                    System.out.print(msg1+"voilà");
                    //And display messages
                    displayMessage(msg1);
                } catch (NullPointerException e) {
                }

            } catch (IOException ioe) {
                Logger.getLogger(MultiClient.class.getName()).log(Level.SEVERE, null, ioe);
                displayConnectionMessage("Sending error: " + ioe.getMessage());
                System.out.println("Sending error: " + ioe.getMessage());
                stop();
            }

        }
    }

    public void handle(String msg) {
        if (msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit ...");
            displayConnectionMessage("Good bye. Press RETURN to exit ...");

            stop();
        } else {
            displayMessage(msg);
            System.out.println(msg);
        }
    }

    public void displayConnectionMessage(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here  
                chatControl.handleConnection(msg);
            }
        });
    }

    public void start() throws IOException {
        console = mess.getStream();
        
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
         try
      {  if (console   != null)  console.close();
        
         if (chat    != null)  chat.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ...");
          displayConnectionMessage("Error closing ...");
      }
   
    }

    public void displayMessage(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here 
                chatControl.handleChatReceived(msg);
            }
        });
    }

}
