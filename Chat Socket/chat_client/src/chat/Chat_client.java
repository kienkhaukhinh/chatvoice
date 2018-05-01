/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import UI.fr_result_search;
import UI.fr_search_friend;
import UI.fr_client;
import UI.fr_chat;
import UI.fr_reg;
import UI.fr_Login;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author duong
 */
public class Chat_client {
    public static int socket_port = 6556;
    public static String ip_server = "127.0.0.1";
    public static Socket socket = null;
    public static boolean login = false;
    public static int my_ID;
    public static ImageIcon icon = null;
    public static String full_name = null;
    public static boolean connected = false;
    public static fr_client fr_client = null;
    public static fr_Login fr_login = null;
    public static fr_search_friend fr_search = null;
    public static fr_result_search fr_result_search = null;
    public static fr_reg fr_reg = null;
    public static ArrayList<friend> list_friend = new ArrayList<>();
    public static ArrayList<fr_chat> list_fr_chat = new ArrayList<>();
    public static boolean calling = false;
    
    public static void main(String[] args) {
        Chat_client chat_client = new Chat_client();
        chat_client.init();
    }
    public void init(){
        Chat_client.fr_client = new fr_client();
        Chat_client.fr_login = new fr_Login();
        Chat_client.fr_search = new fr_search_friend();
        Chat_client.fr_result_search = new fr_result_search();
        
        Chat_client.fr_login.setVisible(true);
        try {
            Chat_client.socket = new Socket(Chat_client.ip_server,Chat_client.socket_port);
            Thread receive = new receive(Chat_client.socket);
            receive.start();
            Chat_client.connected = true;
            System.out.println("conected");
        } catch (IOException ex) {
            Logger.getLogger(fr_Login.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "socket error");
        }
        
    }
}
