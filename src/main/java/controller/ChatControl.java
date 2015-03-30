/*
 * Copyright (c) 2011, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package controller;

import java.io.DataInputStream;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.ChatClient;
import model.MyMessage;


/**
 * Sample custom control hosting a text field and a button.
 */
public class ChatControl extends VBox {
    
    //private DataInputStream  console   = null;
   
    @FXML
    TextArea textarea;
    @FXML
    TextArea chat;
    @FXML
    TextArea connection;

    
    @FXML
    private Button button;
    @FXML
    private Label label;
    
    public MyMessage message = null;
   
    public ChatControl(MyMessage mess) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chatview.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        chat.setEditable(false);
        connection.setEditable(false);
        message = mess;
    }
    
    public String getText() {
        return textProperty().get();
    }
    
    public void setText(String value) {
        textProperty().set(value);
    }
    
    public StringProperty textProperty() {
        return textarea.textProperty();                
    }
    
    public void sendChat() {
       
       message.setStream(textarea.getText()+"\n");
       //System.out.print("oui");
       textarea.clear();   
    }
    
    public void receiveChat(String msg) {
       chat.appendText(msg+"\n");
                   
    }
     public void dispayConnection(String msg) {
       connection.appendText(msg);
                   
    }
     public void sendChattoStream() {
       chat.appendText(textarea.getText()+"\n");
       
                   
    } 
   
    @FXML
    public void handleChat() {
        sendChat();
    }
     @FXML
    public void handleChatReceived(String msg){
        receiveChat(msg);
    }
   @FXML
    public void handleConnection(String msg) {
 dispayConnection(msg+"\n");       
  
    }
   
    
   
}

