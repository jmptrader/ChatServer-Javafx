/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import controller.ChatControl;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.naming.InvalidNameException;
import model.MultiClient;
import model.MultiClientReceive;
import model.MyMessage;

/**
 *
 * @author NadiaCom
 */
public class Test extends Application{
     MyMessage message = null;
    static int port = 80;
    static String address = "224.0.0.1";
  
    String nickname = "";
    
    @Override
    public void start(Stage stage) throws Exception {
        nickname = "Nadia";
        message = new MyMessage();
        ChatControl chatControl = new ChatControl(message);
        stage.setScene(new Scene(chatControl));
        stage.setTitle("Multicast chat");
        stage.setWidth(600);
        stage.setHeight(650);
        
        stage.show();
         
        MultiClient c = new MultiClient(address,port,chatControl, message, nickname);
        MultiClientReceive c1 = new MultiClientReceive(address,port,chatControl, message);
    }
    
    
    /* public static void main(String[] args) throws IOException, InvalidNameException {
         launch(args);}*/
    
}
