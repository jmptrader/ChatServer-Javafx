package model;

import controller.ChatControl;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class MultiClient implements Runnable {

    private MulticastSocket chat = null;
    private InetAddress group = null;
    private Thread thread = null;
    private MyMessage mess = null;
    private DataInputStream console = null;
    private int port = 0;
    private String address = "";
    private ChatControl chatControl = null;
    private String nickname = "";

    public MultiClient(String addr, int the_port, ChatControl chatControl1, MyMessage message, String nick) {
        this.nickname = nick;
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

                    console = mess.getStream();
                    String mymsg = console.readLine();
                    
                        if (mymsg != null && !mymsg.isEmpty()) {
                        String the_msg = nickname +" : " + mymsg;
                        //Separate thread that sends data
                        DatagramPacket data1 = new DatagramPacket(the_msg.getBytes(), 0, the_msg.length(), group, port);
                        chat.send(data1);
                        }
                   
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
        displayMessage("***  Welcome ! Let write your message to the community of port " + port + "  ***");

        console = mess.getStream();
        //console   = new DataInputStream(System.in);

        if (thread == null) {
            //client = new MultiClientThread(this, port, address);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (console != null) {
                console.close();
            }

            if (chat != null) {
                chat.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
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
