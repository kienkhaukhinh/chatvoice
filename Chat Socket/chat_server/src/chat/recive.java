/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import UI.fr_server;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author duong
 */
public class recive extends Thread{
    private Socket socket = null;
    private int ID;
    private int int_status;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    ArrayList<friend> arr_fr = new ArrayList<>();
    Boolean is_running = true;
    public recive(Socket socket){
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run(){
        data_socket msg = null;
        try {
            while(is_running){
                    in  = new ObjectInputStream(this.socket.getInputStream());
                    msg = (data_socket)in.readObject();
                    System.out.println(msg.action);
                    switch(msg.action){
                        case "login":             this.login(msg.data[0],msg.data[1]); break;
                        case "logout":            this.user_disconnect(); break;
                        case "loadfriend":        this.loadfriend(this.ID,Integer.parseInt(msg.data[0])); break;
                        case "search":            this.search_friend(msg.data[0]); break;
                        case "chat":              this.chat(msg.data); break;
                        case "loadd_relationgin": this.add_relation(msg.data[0],msg.data[1]); break;
                        case "reg":               this.reg(msg.data[0], msg.data[1], msg.data[2]); break;
                        case "update_info":       this.update_info(msg.list_fr.get(0)); break;
                        case "update_status":     this.update_status(msg.data);break;
                        case "update_online":     this.update_online(msg.data); break;
                        case "load_history":      this.load_history(msg.data); break;
                        case "request_call":      this.request_call(msg); break;
                        case "respon_call":       this.respon_call(msg); break;
                        default: System.out.println("unknow action");
                    }
            }
        } catch (IOException | ClassNotFoundException ex) {
            this.user_disconnect();
            System.out.println("user disconnect 1");
        } finally {
            try {
                in.close();
                this.user_disconnect();
                System.out.println("user disconnect 2");
            } catch (IOException ex) {
                Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void respon_call(data_socket data){
        for(client client: Chat_server.arr_client){
            if(client.ID == Integer.valueOf(data.data[1])){
                try {
                    client.dout.writeObject(data);
                } catch (IOException ex) {
                    Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public void request_call(data_socket data){
        for(client client: Chat_server.arr_client){
            if(client.ID == Integer.valueOf(data.data[1])){ // tìm id người nhận
                try {
                    client.dout.writeObject(data);
                } catch (IOException ex) {
                    Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }
    public void load_history(String[] data){
        String[] fr_id = new String[1];
        fr_id[0] = data[1];
        String query = "SELECT `ID_user`,`msg`,`TIME` FROM `message` WHERE `ID_conversation` = (SELECT `ID` FROM `conversation` WHERE (`ID_user_1` = ? AND `ID_user_2` = ?) OR (`ID_user_2` = ? AND `ID_user_1` = ?)) ORDER BY `TIME` DESC LIMIT 50";
        PreparedStatement pre;
        ArrayList<String[]> arr = new ArrayList<>();
        data_socket dtsk = new data_socket();
        dtsk.action = "load_history";
        dtsk.data = fr_id;
        try {
            pre = Chat_server.connect.prepareStatement(query);
            pre.setString(1, data[0]);
            pre.setString(2, data[1]);
            pre.setString(3, data[0]);
            pre.setString(4, data[1]);
            ResultSet re = pre.executeQuery();
            while(re.next()){
                String[] data_re = new String[3];
                data_re[0] = re.getString("ID_user");
                data_re[1] = re.getString("msg");
                data_re[2] = re.getString("TIME");
                arr.add(data_re);
            }
            dtsk.data_arr = arr;
            out.writeObject(dtsk);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void update_status(String[] data){
        String query = "UPDATE `info_user` SET `status` = ? WHERE `ID` = ?";
        try {
            PreparedStatement pre = Chat_server.connect.prepareStatement(query);
            pre.setString(1, data[0]);
            pre.setInt(2, this.ID);
            pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.broadcast_thinking(data[0]);
    }
    public void update_online(String[] data){ // trang thai online len server
        int int_online;
        if(data[0].equalsIgnoreCase("ONLINE")){
            int_online = 1;
        }else{
            int_online = 3;
        }
        this.int_status = int_online;
        for(int i = 0; i < Chat_server.arr_client.size() ; i++){
            if(Chat_server.arr_client.get(i).ID == this.ID){
                Chat_server.arr_client.get(i).int_status = int_online;
                break;
            }
        }
        this.broadcast_status();
    }
    public void broadcast_status(){ // cap nhat lai trang thai toi ban be online, busy . . .
        data_socket dtsk = new data_socket();
        dtsk.action = "broadcast_status";
        String[] data  = new String[2];
        data[0] = String.valueOf(this.ID);
        data[1] = String.valueOf(this.int_status);
        dtsk.data = data;
        for(int i = 0; i < this.arr_fr.size();i++){
            for(int j = 0; j < Chat_server.arr_client.size();j++){
                if(this.arr_fr.get(i).getID() == Chat_server.arr_client.get(j).ID){
                    try {
                        Chat_server.arr_client.get(j).dout.writeObject(dtsk);
                    } catch (IOException ex) {
                        Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }
    public void broadcast_thinking(String data){ // cap nhat dong trang thai, "ban dang nghi gi" toi ban be
        data_socket dtsk = new data_socket();
        dtsk.action = "broadcast_thinking";
        String[] data_to_send  = new String[2];
        data_to_send[0] = String.valueOf(this.ID);
        data_to_send[1] = data;
        dtsk.data = data_to_send;
        for(int i = 0; i < this.arr_fr.size();i++){
            for(int j = 0; j < Chat_server.arr_client.size();j++){
                if(this.arr_fr.get(i).getID() == Chat_server.arr_client.get(j).ID){
                    try {
                        Chat_server.arr_client.get(j).dout.writeObject(dtsk);
                    } catch (IOException ex) {
                        Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }
    public void update_info(friend e){
        String query = "UPDATE `info_user` SET `fullname` = ?, `pass` = ? WHERE `ID` = ?";
        PreparedStatement pre;
        try {
            pre = Chat_server.connect.prepareStatement(query);
            pre.setString(1, e.getFull_name());
            pre.setString(2, e.getPass());
            pre.setInt(3, this.ID);
            pre.executeUpdate();
            if(e.getImageIcon() != null){
                File output_image = new File("D:\\image/"+this.ID+".png");
                BufferedImage bi = new BufferedImage(e.getImageIcon().getIconWidth(), e.getImageIcon().getIconHeight(), BufferedImage.TYPE_INT_BGR);
                Graphics g = bi.getGraphics();
                g.drawImage(e.getImageIcon().getImage(), 0, 0, null);
                ImageIO.write(bi, "png", output_image);
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void chat(String[] data){
        int size = Chat_server.arr_client.size();
        data_socket dtsk = new data_socket();
        dtsk.action = "chat";
        dtsk.data = data;
        String query_check   = "SELECT `ID` FROM `conversation` WHERE (`ID_user_1` = ? AND `ID_user_2` = ?) OR (`ID_user_2` = ? AND `ID_user_1` = ?)";
        String query_insert1 = "INSERT INTO `conversation` VALUES(NULL,?,?)";
        String query_insert2 = "INSERT INTO `message` (`ID_user`, `msg`, `id_conversation`, `TIME`) VALUES (?, ?, ?, CURRENT_TIMESTAMP);";
        try {
            int i = 0;
            while(i<=1){
                ResultSet result;
                PreparedStatement pre1 = Chat_server.connect.prepareStatement(query_check);
                pre1.setInt(1, Integer.parseInt(data[0]));
                pre1.setInt(2, Integer.parseInt(data[1]));
                pre1.setInt(3, Integer.parseInt(data[0]));
                pre1.setInt(4, Integer.parseInt(data[1]));
                result = pre1.executeQuery();
                result.last();
                if(result.getRow() != 0){
                    int ID_c = result.getInt("ID");
                    PreparedStatement pre2 = Chat_server.connect.prepareStatement(query_insert2);
                    pre2.setInt(1, Integer.parseInt(data[0]));
                    pre2.setString(2, data[3]);
                    pre2.setInt(3, ID_c);
                    pre2.executeUpdate();
                    break;
                }
                PreparedStatement pre3 = Chat_server.connect.prepareStatement(query_insert1);
                pre3.setInt(1, Integer.parseInt(data[0]));
                pre3.setInt(2, Integer.parseInt(data[1]));
                pre3.executeUpdate();
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < size; i ++){
            if(Chat_server.arr_client.get(i).ID == Integer.parseInt(data[1])){
                try {
                    Chat_server.arr_client.get(i).dout.writeObject(dtsk);
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public void search_friend(String key){
        ArrayList<String[]> arr = new ArrayList<>();
        String query = "SELECT * "
                + "FROM `info_user` "
                + "WHERE `ID` LIKE ? "
                + "OR `fullname` LIKE ? "
                + "AND `ID` NOT IN ("
                + "SELECT `id_user1` "
                + "FROM `relationship` "
                + "WHERE `id_user1` = ? OR `id_user2` = ?) "
                + "AND `ID` NOT IN ("
                + "SELECT `id_user2` "
                + "FROM `relationship` "
                + "WHERE `id_user1` = ? OR `id_user2` = ?) "
                + "ORDER BY `fullname` "
                + "LIMIT 10";
        ResultSet result = null;
        try {
            PreparedStatement prestmt = Chat_server.connect.prepareStatement(query);
            prestmt.setString(1, key);
            prestmt.setString(2, "%"+key+"%");
            prestmt.setString(3, String.valueOf(this.ID));
            prestmt.setString(4, String.valueOf(this.ID));
            prestmt.setString(5, String.valueOf(this.ID));
            prestmt.setString(6, String.valueOf(this.ID));
            System.out.println(prestmt.toString());
            result = prestmt.executeQuery();
            while(result.next()){
                String[] re = new String[2];
                re[0] = result.getString("ID");
                re[1] = result.getString("fullname");
                arr.add(re);
            }
            data_socket dtsk = new data_socket();
            dtsk.action = "searchfriend";
            dtsk.data_arr = arr;
            out.writeObject(dtsk);
            out.flush();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadfriend(int ID,int status){
        String query = "SELECT * FROM `relationship` WHERE `ID_user1` = ? OR `ID_user2` = ? AND status = ?";
        String query2 = "SELECT `ID`,`fullname`,`status` FROM `info_user` WHERE `ID` ";
        ArrayList<Integer> data_bind = new ArrayList<>();
        String string_in_clause = "IN (";
        ResultSet result = null;
        ResultSet result2 = null;
        ArrayList<friend> respon = new ArrayList<>();
        data_socket dtsk = new data_socket();
        dtsk.action = "loadfriend";
        try {
            PreparedStatement prepstmt = Chat_server.connect.prepareStatement(query);
            prepstmt.setInt(1, ID);
            prepstmt.setInt(2, ID);
            prepstmt.setInt(3, status);
            result = prepstmt.executeQuery();
            while(result.next()){
                if(result.getInt("id_user1") == ID){
                    data_bind.add(result.getInt("id_user2"));
                }else{
                    data_bind.add(result.getInt("id_user1"));
                }
            }
            int size = data_bind.size();
            for(int i = 0; i < size; i++){
                string_in_clause+=data_bind.get(i);
                if(i<size-1)
                    string_in_clause+=",";
                else{
                    string_in_clause+=")";
                }
            }
            query2 += string_in_clause;
            PreparedStatement prepstmt2 = Chat_server.connect.prepareStatement(query2);
            result2 = prepstmt2.executeQuery();
            while(result2.next()){
                BufferedImage img = null;
                try {
                    File file = new File("D:\\image/"+result2.getString("ID")+".png");
                    img = ImageIO.read(file);
                } catch (IOException e) {
                   File file = new File("D:\\image/default.png");
                    img = ImageIO.read(file);
                }
                /*
                1 = online;
                2 = offline;
                3 = busy;
                */
                int int_statu = 2;
                for(int i = 0; i< Chat_server.arr_client.size();i++){
                    if(Chat_server.arr_client.get(i).ID == result2.getInt("ID")){
                        int_statu = Chat_server.arr_client.get(i).int_status;
                        break;
                    }
                }
                ImageIcon icon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                friend me = new friend(result2.getInt("ID"), result2.getString("fullname"),int_statu,icon,result2.getString("status"));
                respon.add(me);
            }
            this.arr_fr = respon;
            dtsk.list_fr = respon;
            out.writeObject(dtsk);
            out.flush();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void add_relation(String id,String status){
        String query = "INSERT INTO `relationship` values(?,?,?)";
        try {
            PreparedStatement preparedStatement = Chat_server.connect.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(this.ID));
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean login(String name,String pass){
        String query = "SELECT `ID`,`fullname`,`status` FROM `info_user` WHERE `user_name`= ? AND `pass` = ?";
        ResultSet result = null;
        String[] data = new String[1];
        data_socket dtsk = new data_socket();
        try {
            PreparedStatement prepstmt = Chat_server.connect.prepareStatement(query);
            prepstmt.setString(1, name);
            prepstmt.setString(2, pass);
            result = prepstmt.executeQuery();
            result.last();
            dtsk.action = "login";
            if(result.getRow() != 0){
                data[0] = "true";
                BufferedImage img = null;
                try {
                    File file = new File("D:\\image/"+result.getInt("ID")+".png");
                    img = ImageIO.read(file);
                } catch (IOException e) {
                   File file = new File("D:\\image/default.png");
                    img = ImageIO.read(file);
                }
                ImageIcon icon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                friend me = new friend(result.getInt("ID"), result.getString("fullname"),1,icon,result.getString("status"));
                ArrayList<friend> ar = new ArrayList<>();
                ar.add(me);
                dtsk.list_fr = ar;
                this.user_login(result.getInt("ID"));
                this.ID = result.getInt("ID");
                this.int_status = 1;
                this.loadfriend(this.ID, 2); // 2 for freind
                this.broadcast_status();
                append_txt("client: "+this.socket.getPort()+ " Login");
            }else{
                append_txt("client: "+this.socket.getPort()+" try to login");
                data[0] = "false";
            }
            dtsk.data = data;
            out.writeObject(dtsk);
            out.flush();
            return true;
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public void reg(String username,String full_name,String pass){
        String[] data = new String[1];
        data_socket dtsk = new data_socket();
        dtsk.action = "reg";
        String query_check = "SELECT `user_name` FROM `info_user` WHERE `user_name` = ?";
        String query = "INSERT INTO `info_user` (`ID`, `user_name`, `pass`, `fullname`) VALUES (NULL, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement1 = Chat_server.connect.prepareStatement(query_check);
            preparedStatement1.setString(1, username);
            System.out.println(preparedStatement1.toString());
            ResultSet rs = preparedStatement1.executeQuery();
            rs.last();
            if(rs.getRow() != 0){
                //user_name exist;
                data[0] = "false";
                dtsk.data = data;
                out.writeObject(dtsk);
                return;
            }
            PreparedStatement preparedStatement2 = Chat_server.connect.prepareStatement(query);
            preparedStatement2.setString(1, username);
            preparedStatement2.setString(2, pass);
            preparedStatement2.setString(3, full_name);
            preparedStatement2.executeUpdate();
            data[0] = "true";
            dtsk.data = data;
            out.writeObject(dtsk);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void user_login(int ID){
        int size = Chat_server.arr_client.size();
        for(int i = 0; i < size ; i++){
            if(Chat_server.arr_client.get(i).ID == ID){
                Chat_server.arr_client.get(i).dout = out;
                // trường hợp đăng nhập ở nhiều máy
                return;
            }
        }
        this.int_status = 1; // 1 = online
        this.broadcast_status();
        client cl = new client(socket, ID);
        cl.dout = out;
        cl.int_status = 1;
        Chat_server.arr_client.add(cl);
    }
    public void user_disconnect(){
        this.int_status = 2; // 2 = offline
        this.broadcast_status(); // update to friend
        for(int i = 0; i< Chat_server.arr_client.size();i++){
            if(Chat_server.arr_client.get(i).ID == this.ID){
                Chat_server.arr_client.remove(i); // remove form online list
            }
        }
        this.is_running = false; // terminal thread
    }
    public void append_txt(String msg){
        fr_server.txt_log.append(msg+"\n");
        fr_server.txt_log.setCaretPosition(fr_server.txt_log.getDocument().getLength());
    }
   
}
