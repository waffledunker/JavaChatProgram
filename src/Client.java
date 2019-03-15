/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alpalpalapalallapala
 */

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame {
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    
    //constructor
    
    public Client(String host){
        super("Client!");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
        
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(450, 250);
        setVisible(true);
    }
    
    //running program
    public void running(){
        try{
            
            connectToServer();
            setupStreams();
            whileChatting();
            
        }catch(EOFException eof){
            showMessage("\n Client Terminated connection!");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            closeCrap(); //housekeeping stuff
        }
    }
    //connecting to server
    private void connectToServer() throws IOException {
        showMessage("Attempting Connection...");
         //ask user for ip address
        connection = new Socket(InetAddress.getByName(serverIP), 6789); 
        showMessage("\n Connected to :" + connection.getInetAddress().getHostName());
        
    }
    
    //set up streams to send and recieve messages
    
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); //housekeeping
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Your streams are now good to go! \n");
    }
    
    //while chatting with server
    private void whileChatting() throws IOException {
        
        ableToType(true);  //user is able to type now
        do{
            try{
                
                message = (String) input.readObject(); //read object and convert to string
                showMessage("\n" + message);
                
            }catch(ClassNotFoundException cnfe){
                showMessage("\n I don't know that object type.");   
            } 
        }while(!message.equals(" Server - END "));
        
    }
    
    //close the streams and sockets
    private void closeCrap(){
        showMessage("\n Closing...");
        ableToType(false);
        try{
            
            output.close();
            input.close();  //housekeeping
            connection.close();
            
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
    }
    
    //send messages to server
private void sendMessage(String message){
try{
    
    output.writeObject("CLIENT - " + message);
    output.flush();
    // even we sent to message through stream,it wont show until we display
    showMessage("\n CLIENT -" + message);
    

}catch(IOException ioe){
    chatWindow.append("\n Something went wrong really bad!");
}    
}

//change/update chatWindow
private void showMessage(final String message){
    SwingUtilities.invokeLater(
    new Runnable(){
        @Override
        public void run(){
            chatWindow.append(message);
        }
    }
    );
    
}

//gives user permission to type crap into the text box
private void ableToType(final boolean tof){
    SwingUtilities.invokeLater(
    new Runnable(){
        @Override
        public void run(){
            userText.setEditable(tof);
        }
    }
    );
}

}
