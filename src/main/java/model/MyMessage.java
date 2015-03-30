/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NadiaCom
 */
public class MyMessage {
   public DataInputStream  streamIn   = null;
   //public InputStream stIn = null;
   
   
  public MyMessage(){
      //String first = "nadia";
      //streamIn = new DataInputStream(new ByteArrayInputStream(first.getBytes(StandardCharsets.UTF_8)));
            
  }
   public void setStream(String msg){
       System.out.println(msg);
        String message = msg;
        streamIn = new DataInputStream(new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)));
    
       /*try {
           System.out.println(streamIn.readLine());
       } catch (IOException ex) {
           Logger.getLogger(MyMessage.class.getName()).log(Level.SEVERE, null, ex);
       }*/
   
   }
     
   public DataInputStream getStream(){
          return streamIn;
    }
    
}


