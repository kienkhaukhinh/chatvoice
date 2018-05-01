package chat;

import javasocketvoice.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author duong
 */
public class data_socket implements Serializable{
    public String action = null;
    public String[] data;
    public ArrayList<String[]> data_arr;
    public ArrayList<friend> list_fr;
    public boolean accept = false;
    public data_socket(){
        
    }
    public data_socket(String action,String[] data, ArrayList<String[]> data_arr) {
        this.action = action;
        this.data = data;
        this.data_arr = data_arr;
    }
    
}
