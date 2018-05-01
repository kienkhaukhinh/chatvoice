/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasocketvoice;

import chat.friend;
import function.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import chat.data_socket;

/**
 *
 * @author pc
 */
public class serverprocess extends Thread{
    private Socket socket = null;
    private int ID;
    private int int_status;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    ArrayList<friend> arr_fr = new ArrayList<>();
    Boolean is_running = true;
    public serverprocess(Socket socket){
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run(){
        data_socket msg = null;
        data_socket data = new data_socket();
        data_socket dtsk = new data_socket();
        try {
            while(is_running){
                    in  = new ObjectInputStream(this.socket.getInputStream());
                    msg = (data_socket) in.readObject();
                    System.out.println(msg.action);
                    switch(msg.action){
                        case "login":              
                        {
                            dtsk = login.login(msg.data[0], msg.data[1], this.socket.getPort());
                            this.user_login(dtsk.list_fr.get(0).getID());
                            this.ID = dtsk.list_fr.get(0).getID();
                            this.int_status = 1;
                            
                            
                            data = login.loadfriend(this.ID, 2); // 2 for friend
                            this.arr_fr = data.list_fr;
                            
                            if(data.action != null) this.sendtoclient(data);
                            System.out.println("hello");
                            this.broadcast_status();
                            data.action = null;
                            if(dtsk.action != null) this.sendtoclient(dtsk);
                            break;
                        } 
                        case "logout":            this.user_disconnect(); break;
                        case "loadfriend":        data = login.loadfriend(this.ID,Integer.parseInt(msg.data[0])); break;
                        
                        case "search":            data = addfriend.search_friend(msg.data[0], this.ID); break;
                        case "chat":              chatmessage.chat(msg.data); break;
                        case "add_relation":      addfriend.add_relation(msg.data[0],msg.data[1], this.ID); break;
                        case "reg":               data = register.reg(msg.data[0], msg.data[1], msg.data[2]); break;
                        case "update_info":       updateuser.update_info(msg.list_fr.get(0), this.ID); break;
                        case "update_status":     
                        {
                            updateuser.update_status(msg.data, this.ID);
                            this.broadcast_thinking(msg.data[0]);
                            break;
                        }
                        case "update_online":     this.update_online(msg.data); break;
                        case "load_history":      data = chatmessage.load_history(msg.data); break;
                        case "request_call":      chatvoice.request_call(msg); break;
                        case "respon_call":       chatvoice.respon_call(msg); break;
                        default: System.out.println("unknow action");
                    }
                    if(data.action != null) this.sendtoclient(data);
            }
        } catch (IOException | ClassNotFoundException ex) {
            this.user_disconnect();
            System.out.println("user disconnect 1");
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                this.user_disconnect();
                System.out.println("user disconnect 2");
            } catch (IOException ex) {
                Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //gui du lieu toi client
    private void sendtoclient(data_socket dtsk){
        try {
            out.writeObject(dtsk);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //update trang thai online cho tat ca friend dang online
    public void update_online(String[] data){ // trang thai online len server
        int int_online;
        if(data[0].equalsIgnoreCase("ONLINE")){
            int_online = 1;
        }else{
            int_online = 3;
        }
        this.int_status = int_online;
        for(int i = 0; i < Javasocketvoice.arr_client.size() ; i++){
            if(Javasocketvoice.arr_client.get(i).ID == this.ID){
                Javasocketvoice.arr_client.get(i).int_status = int_online;
                break;
            }
        }
        this.broadcast_status();
    }
    
    //gui cho tat ca user dang online ban dang suy nghi gi
    public void broadcast_thinking(String data){ // cap nhat dong trang thai, "ban dang nghi gi" toi ban be
        data_socket dtsk = new data_socket();
        dtsk.action = "broadcast_thinking";
        String[] data_to_send  = new String[2];
        data_to_send[0] = String.valueOf(this.ID);
        data_to_send[1] = data;
        dtsk.data = data_to_send;
        for(int i = 0; i < this.arr_fr.size();i++){
            for(int j = 0; j < Javasocketvoice.arr_client.size();j++){
                if(this.arr_fr.get(i).getID() == Javasocketvoice.arr_client.get(j).ID){
                    try {
                        Javasocketvoice.arr_client.get(j).dout.writeObject(dtsk);
                    } catch (IOException ex) {
                        Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }
    
    public void broadcast_status(){ // cap nhat lai trang thai toi ban be online, busy . . .
        data_socket dtsk = new data_socket();
        dtsk.action = "broadcast_status";
        String[] data  = new String[2];
        data[0] = String.valueOf(this.ID);
        data[1] = String.valueOf(this.int_status);
        dtsk.data = data;
        if(this.arr_fr != null) 
        {
        for(int i = 0; i < this.arr_fr.size();i++){
            for(int j = 0; j < Javasocketvoice.arr_client.size();j++){
                if(this.arr_fr.get(i).getID() == Javasocketvoice.arr_client.get(j).ID){
                    try {
                        Javasocketvoice.arr_client.get(j).dout.writeObject(dtsk);
                    } catch (IOException ex) {
                        Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }}
    }
    
    public void user_login(int ID){
        int size = Javasocketvoice.arr_client.size();
        for(int i = 0; i < size ; i++){
            if(Javasocketvoice.arr_client.get(i).ID == ID){
                Javasocketvoice.arr_client.get(i).dout = out;
                // trường hợp đăng nhập ở nhiều máy
                return;
            }
        }
        this.int_status = 1; // 1 = online
        this.broadcast_status();
        client cl = new client(socket, ID);
        cl.dout = out;
        cl.int_status = 1;
        Javasocketvoice.arr_client.add(cl);
    }
    
    public void user_disconnect(){
        this.int_status = 2; // 2 = offline
        this.broadcast_status(); // update to friend
        for(int i = 0; i< Javasocketvoice.arr_client.size();i++){
            if(Javasocketvoice.arr_client.get(i).ID == this.ID){
                Javasocketvoice.arr_client.remove(i); // remove form online list
            }
        }
        this.is_running = false; // terminal thread
    }
    
}
