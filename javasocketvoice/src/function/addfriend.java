/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasocketvoice.connectdatabase;
import chat.data_socket;
import javasocketvoice.serverprocess;

/**
 *
 * @author pc
 */
public class addfriend {
    
    //tim ban de add
    public static data_socket search_friend(String key, int ID){
        data_socket dtsk = new data_socket();
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
            PreparedStatement prestmt = connectdatabase.connect.prepareStatement(query);
            prestmt.setString(1, key);
            prestmt.setString(2, "%"+key+"%");
            prestmt.setString(3, String.valueOf(ID));
            prestmt.setString(4, String.valueOf(ID));
            prestmt.setString(5, String.valueOf(ID));
            prestmt.setString(6, String.valueOf(ID));
            System.out.println(prestmt.toString());
            result = prestmt.executeQuery();
            while(result.next()){
                String[] re = new String[2];
                re[0] = result.getString("ID");
                re[1] = result.getString("fullname");
                arr.add(re);
            }
            dtsk.action = "searchfriend";
            dtsk.data_arr = arr;
        } catch (SQLException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtsk;
    }
    
    //add friend
    public static void add_relation(String id,String status, int ID){
        String query = "INSERT INTO `relationship` values(?,?,?)";
        try {
            PreparedStatement preparedStatement = connectdatabase.connect.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(ID));
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
