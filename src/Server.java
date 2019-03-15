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
import javax.swing.*;

public class Server  extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    
    //constructor
    public Server(){
        super("Alpcan's Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent event){
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
       add(userText, BorderLayout.NORTH) ;
       chatWindow = new JTextArea();
       add(new JScrollPane(chatWindow));
       setSize(450,250);
       setVisible(true);
    }
    //set up and run server
    
    public void running(){
        try{
            
            server = new ServerSocket(6789, 100); //port and backlog(queue length)
            while(true){
                try{    //connect and have conversation
                    
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                    
                }catch(EOFException iofe){
                    
                    showMessage("\n Server ended the connection!");
                    
                }finally{
                    
                    closeCrap();    //housekeeping stuff
                    
                }
            }
        }catch(IOException iox){
            
            iox.printStackTrace();
        }
    }
    // wait for connection,then display connection info
    
    private void waitForConnection() throws IOException {
        showMessage(" Waiting for someone to connect... \n");
        connection = server.accept();  // server assigns connection to socket
        showMessage(" Now connected to " + connection.getInetAddress().getHostName());
    }
    
    // get stream to send and recieve data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream are now setup! \n");
        
    }
    
    //during the chat conversation
    
    private void whileChatting() throws IOException {
        String message = " You are now connected!";
        sendMessage(message);
        ableToType(true);
        
        do{
                //have a conversation
                try{
                    
                    message = (String) input.readObject();
                    showMessage("\n" + message);
                    
                }catch(ClassNotFoundException cnfe){
                    showMessage(" \n ERROR! \n");
                }
                
        }while(!message.equals("CLIENT - END"));
    }
    
    // close streams and sockets after you are done chatting
    private void closeCrap(){
        showMessage("\n\n Closing connections...\n");
        ableToType(false);
        try{
            
            output.close();
            input.close();
            connection.close();
            
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    //send a message to client 
    private void sendMessage(String message){
        
        try{
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nServer - " + message);
            
        }catch(IOException ioe){
            chatWindow.append("\n Error: Can't sent message");
        }
    }
    
    //updates chatWindow
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
        new Runnable(){  //invoke thread to update text
            @Override
            public void run(){
                chatWindow.append(text);
            }
        }
        );
    }
    
    // let the user type stuff into their box
    
    private void ableToType(final boolean tof) {
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
