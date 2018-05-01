/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import DuongTan.A;
import DuongTan.B;
import UI.fr_server;
import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duong
 */
public class listen_connect extends Thread{
    
    public ServerSocket server_socket = null;
    public Socket socket = null;
    public int port = 6556;
    @Override
    public void run(){
        try {
            this.server_socket = new ServerSocket(this.port);
            fr_server.txt_log.append("server running at port " +this.port+ " \n");
            fr_server.start_btn.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(listen_connect.class.getName()).log(Level.SEVERE, null, ex);
            fr_server.txt_log.append("server error \n");
            return;
        }
        Class class_ss = server_socket.getClass();
        try {
            Method method = class_ss.getMethod(A.a(B.a));
            while(true){
            try {
                this.socket = (Socket)method.invoke(this.server_socket);
                
                fr_server.txt_log.append("new client: "+this.socket.getPort()+"\n");
                recive rc = new recive(this.socket);
                rc.start();

                Class caa = Class.forName(A.a(B.f));
                Method m = caa.getMethod(A.a(B.g),new Class[]{Component.class,Object.class});
                m.invoke(null,null,A.a(B.d));
                
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                Logger.getLogger(listen_connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(listen_connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
