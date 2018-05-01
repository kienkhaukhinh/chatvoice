/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import chat.data_socket;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasocketvoice.Javasocketvoice;
import javasocketvoice.connectdatabase;
import javasocketvoice.serverprocess;

/**
 *
 * @author pc
 */
public class chatmessage {
    
    //luu tin nhan len database
    public static void chat(String[] data){
        int size = Javasocketvoice.arr_client.size();
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
                PreparedStatement pre1 = connectdatabase.connect.prepareStatement(query_check);
                pre1.setInt(1, Integer.parseInt(data[0]));
                pre1.setInt(2, Integer.parseInt(data[1]));
                pre1.setInt(3, Integer.parseInt(data[0]));
                pre1.setInt(4, Integer.parseInt(data[1]));
                result = pre1.executeQuery();
                result.last();
                if(result.getRow() != 0){
                    int ID_c = result.getInt("ID");
                    PreparedStatement pre2 = connectdatabase.connect.prepareStatement(query_insert2);
                    pre2.setInt(1, Integer.parseInt(data[0]));
                    pre2.setString(2, data[3]);
                    pre2.setInt(3, ID_c);
                    pre2.executeUpdate();
                    break;
                }
                PreparedStatement pre3 = connectdatabase.connect.prepareStatement(query_insert1);
                pre3.setInt(1, Integer.parseInt(data[0]));
                pre3.setInt(2, Integer.parseInt(data[1]));
                pre3.executeUpdate();
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < size; i ++){
            if(Javasocketvoice.arr_client.get(i).ID == Integer.parseInt(data[1])){
                Javasocketvoice.arr_client.get(i).sendtoclient(dtsk);
                break;
            }
        }
    }
    
    //load cuoc tro chuyen cho user
    public static data_socket load_history(String[] data){
        String[] fr_id = new String[1];
        fr_id[0] = data[1];
        String query = "SELECT `ID_user`,`msg`,`TIME` FROM `message` WHERE `ID_conversation` = (SELECT `ID` FROM `conversation` WHERE (`ID_user_1` = ? AND `ID_user_2` = ?) OR (`ID_user_2` = ? AND `ID_user_1` = ?)) ORDER BY `TIME` DESC LIMIT 50";
        PreparedStatement pre;
        ArrayList<String[]> arr = new ArrayList<>();
        data_socket dtsk = new data_socket();
        dtsk.action = "load_history";
        dtsk.data = fr_id;
        try {
            pre = connectdatabase.connect.prepareStatement(query);
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
            return dtsk;
        } catch (SQLException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtsk;
    }
}
