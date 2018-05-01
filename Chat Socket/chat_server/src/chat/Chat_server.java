package chat;

import UI.fr_server;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author duong
 */
public class Chat_server {

    ServerSocket serversocket = null;
    public static ArrayList<client> arr_client = new ArrayList<>();
    public static Connection connect = null; 
    public static fr_server fr_server = null;
    private static final String hostname = "localhost";
    private static final String databasename = "testjava";
    private static final String databaseusername = "root";
    private static final String databasepassword = "";
    public static void main(String[] args) {
        try{  
            Class.forName("com.mysql.jdbc.Driver");
            Chat_server.connect = DriverManager.getConnection("jdbc:mysql://"+ hostname +":3306/"+ databasename +"?useUnicode=true&characterEncoding=UTF-8",databaseusername,databasepassword);
                System.out.println("connected mysql");
            }catch(ClassNotFoundException | SQLException e){ 
                JOptionPane.showMessageDialog(null, "database notfound");
                return;
        }
        fr_server = new fr_server();
        fr_server.setVisible(true);
    }
}
