/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasocketvoice.connectdatabase;
import chat.data_socket;
import chat.friend;
import javasocketvoice.serverprocess;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javasocketvoice.Javasocketvoice;

/**
 *
 * @author pc
 */
public class login extends serverfunction{
    public static data_socket login(String name,String pass, int port){
        String query = "SELECT `ID`,`fullname`,`status` FROM `info_user` WHERE `user_name`= ? AND `pass` = ?";
        ResultSet result = null;
        String[] data = new String[1];
        data_socket dtsk = new data_socket();
        try {
            PreparedStatement prepstmt = connectdatabase.connect.prepareStatement(query);
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
                append_txt("client: "+ port + " Login");
            }else{
                append_txt("client: "+ port +" try to login");
                data[0] = "false";
            }
            dtsk.data = data;
            return dtsk;
        } catch (SQLException | IOException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtsk;
    }
    
    public static data_socket loadfriend(int ID,int status){
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
            PreparedStatement prepstmt = connectdatabase.connect.prepareStatement(query);
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
            PreparedStatement prepstmt2 = connectdatabase.connect.prepareStatement(query2);
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
                for(int i = 0; i< Javasocketvoice.arr_client.size();i++){
                    if(Javasocketvoice.arr_client.get(i).ID == result2.getInt("ID")){
                        int_statu = Javasocketvoice.arr_client.get(i).int_status;
                        break;
                    }
                }
                ImageIcon icon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                friend me = new friend(result2.getInt("ID"), result2.getString("fullname"),int_statu,icon,result2.getString("status"));
                respon.add(me);
            }
            
            dtsk.list_fr = respon;
            
        } catch (SQLException | IOException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtsk;
    }
}
