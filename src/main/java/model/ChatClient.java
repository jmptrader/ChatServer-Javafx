package model;

import controller.ChatControl;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import javafx.application.Platform;

public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;

    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    private ChatControl chatControl = null;
    public MyMessage mess = null;
    public String nickname = null;

    public ChatClient(String serverName, int serverPort, ChatControl chatControl1, MyMessage message, String nick) {
        this.nickname = nick;
        this.mess = message;
        this.chatControl = chatControl1;
        chatControl.handleConnection("Establishing connection. Please wait ...");

        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            chatControl.handleConnection("Connected: " + socket);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            chatControl.handleConnection("Host unknown: " + uhe.getMessage());
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            chatControl.handleConnection("Unexpected exception: " + ioe.getMessage());
            System.out.println("Unexpected exception: " + ioe.getMessage());

        }

    }

    public void run() {
        while (thread != null) {

            try {
                try {
                    console = mess.getStream();
                    String mymsg = console.readLine();
                    if (mymsg != null && !mymsg.isEmpty()) {
                        streamOut.writeUTF(nickname + " : " + mymsg);

                        streamOut.flush();
                    }

                } catch (NullPointerException e) {
                }
            } catch (IOException ioe) {
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

    public void start() throws IOException {

        console = mess.getStream();
        //console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null) {
            client = new ChatClientThread(this, socket);
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
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
            displayConnectionMessage("Error closing ...");
        }
        client.close();
        client.stop();
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

    public void displayConnectionMessage(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Update UI here  
                chatControl.handleConnection(msg);
            }
        });
    }
}
