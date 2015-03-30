/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import chatserver.ChatServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import chatserver.NiochatServer;
import chatserver.NiochatServer;
import controller.ChatControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.naming.InvalidNameException;
import model.ChatClient;
import model.MultiClient;
import model.MultiClientReceive;
import model.MyMessage;

/**
 *
 * @author NadiaCom
 */
public class Main extends Application {

    MyMessage message = null;
    
    static int port = 80;
    static String address = "localhost";
    static String address_mulicast = "224.2.2.5";
    static int multicast = 0;
    String nickname = "";

    @Override
    public void start(final Stage stage) throws Exception {
        if(multicast==0){
        stage.setTitle("Welcome !");
        }else{
        stage.setTitle("Welcome to the multicast chat !");
        }
        Group root = new Group();
        Color c = Color.web("#A1887F");
        Scene scene = new Scene(root, 500, 260, c);
        //scene.getStylesheets().add("view/TextStyle.css");
        
        Text t = new Text("Choose your nickname :");
        t.setLayoutX(169);
        t.setLayoutY(105);
        t.setFont(Font.font ("Verdana",FontWeight.BOLD, 14));
        t.setFill(Color.WHITE);
        
        final TextField txt = new TextField("Guest");
        txt.setLayoutX(179);
        txt.setLayoutY(130);
        txt.setFont(Font.font ("Verdana", 12));
        
        
        Button btn = new Button();
        btn.setLayoutX(208);
        btn.setLayoutY(190);
        btn.setText("Join the chat ! ");
        btn.getStyleClass().add("bevel-grey");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
        nickname = txt.getText();
        message = new MyMessage();
        ChatControl chatControl = new ChatControl(message);
       
        if(multicast==1){
        stage.setScene(new Scene(chatControl));
        stage.setTitle("Multicast chat");
        stage.setWidth(600);
        stage.setHeight(650);
        
        stage.show();
         
        MultiClient c = new MultiClient(address_mulicast,port,chatControl, message, nickname);
        MultiClientReceive c1 = new MultiClientReceive(address_mulicast,port,chatControl, message);
        }else{
        

        stage.setScene(new Scene(chatControl));
        stage.setTitle("Chat");
        
        stage.setWidth(600);
        stage.setHeight(650);
        

        stage.show();
       
        ChatClient client = new ChatClient(address, port, chatControl, message, nickname);
        
        }
            }
        });
        root.getChildren().add(btn);
        root.getChildren().add(t);   
        root.getChildren().add(txt);   
        stage.setScene(scene);
        stage.show();
        
    }

   public static void main(String[] args) throws IOException, InvalidNameException {
        
        int choix = 0;
        int server = 0;
        int start = 0;
        int help = 0;

        for (int i = 0; i < args.length; i++) {

            if (args[i].equals("-n")) {
                server = 1;
                System.out.println("Option: Nio server");

            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-p")) {
                try {
                    String[] nameSplit = args[i].split("=");
                    try {
                        port = Integer.parseInt(nameSplit[1]);
                        
                        System.out.println("Port number: " + port);
                    } catch (NumberFormatException e) {
                        System.out.println("Port option usage: -p [port number]");
                    }

                    // System.out.println("Port set to " + port);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Port option usage: -p [port number]");
                    throw new InvalidNameException("Port option usage: -p [port number]");

                }
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-a")) {

                try {
                    String[] nameSplit = args[i].split("=");
                    address = nameSplit[1];
                    address_mulicast = nameSplit[1];
                    System.out.println("IP address set to " + address);

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Set IP address option usage: -a==[IP address]");
                    throw new InvalidNameException("Set IP address option usage: -a==[IP address]");
                }

            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                help = 1;
                System.out.print("Usage : java - jar target / multichat -0.0.1 - SNAPSHOT . jar [ OPTION ]...\n"
                        + "\n-a , -- address = ADDR set the IP address\n"
                        + "-d , -- debug display error messages\n"
                        + "-h , -- help display this help and quit\n"
                        + "-m , -- multicast start the client en multicast mode\n"
                        + "-n , -- nio configure the server in NIO mode\n"
                        + "-p , -- port = PORT set the port\n"
                        + "-s , -- server start the server");
            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                start = 1;
                if (server == 1) {
                    NiochatServer c = new NiochatServer(port, address);
                    (new Thread(c)).start();
                } else {
                    ChatServer s = new ChatServer(address, port);
                }
            }
        }

        for (int i = 0; i < args.length; i++) {

            if (args[i].equals("-m")) {
                multicast = 1;
                System.out.println("Multicast Option");
            }
        }
        for (int i = 0; i < args.length; i++) {

            if (start == 0 && help == 0 ) {

                launch(args);
                 
            }
        }
        if (args.length==0){
            
            launch(args);
        }

    }
}
