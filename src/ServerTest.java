/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alpalpalapalallapala
 */
//testing server.java
import javax.swing.JFrame;
public class ServerTest {
    public static void main(String[] args){
        Server zco = new Server();
        zco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        zco.running();
    }
}
