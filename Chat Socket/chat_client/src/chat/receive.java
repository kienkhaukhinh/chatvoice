package chat;

import UI.fr_result_search;
import UI.fr_client;
import UI.fr_chat;
import UI.fr_list_model;
import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author duong
 */
public class receive  extends Thread{
    Socket socket = null;
    ObjectInputStream din = null;
    data_socket respon = null;
    Clip clip;
    public receive(Socket sk){
        this.socket = sk;
    }
    @Override
    public void run(){
        try {
            din = new ObjectInputStream(this.socket.getInputStream());
            while(true){
                respon = (data_socket)din.readObject();
                switch(respon.action){
                    case "login"             : this.check_login(); break;
                    case "loadfriend"        : this.load_friend(respon); break;
                    case "searchfriend"      : this.show_result_search(respon.data_arr); break;
                    case "chat"              : this.receive_msg(respon.data); break;
                    case "broadcast_status"  : this.broadcast_status(respon.data); break;
                    case "broadcast_thinking": this.broadcast_thinking(respon.data);break;
                    case "reg"               : this.respon_reg(respon);break;
                    case "load_history"      : this.load_history(respon); break;
                    case "respon_call"       : this.respon_call(respon); break;
                    case "request_call"      : this.request_call(respon); break;
                    default                  : System.out.println("unknow action");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.toString());
            System.out.println("Mất kết nối");
            JOptionPane.showMessageDialog(null, "Mất kết nối máy chủ, trương trình sẽ tự thoát");
            System.exit(0);
        }
    }
    public void request_call(data_socket data){
        System.out.println("đã nhận yêu cầu cuộc gọi");
        if(Chat_client.calling){ // client đang thực hiện cuộc gọi 
            data_socket dtsk = new data_socket();
                dtsk.action = "respon_call";
                String[] data2 = new String[3];
                data2[0] = String.valueOf(Chat_client.my_ID);
                data2[1] = String.valueOf(data.data[0]);
                data2[2] = "Đang bận";
                dtsk.data = data2;
                dtsk.accept = false;
                ObjectOutputStream dout;
            try {
                dout = new ObjectOutputStream(Chat_client.socket.getOutputStream());
                dout.writeObject(dtsk);
                System.out.println("đã gửi phản hồi: bận");
            } catch (IOException ex) {
                Logger.getLogger(fr_chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
            for(fr_chat fr_chat:Chat_client.list_fr_chat){
                if(fr_chat.fr_ID == Integer.valueOf(data.data[0])){
                    fr_chat.showcallnotify();
                    fr_chat.btn_call.setEnabled(false);
                    fr_chat.data_from_server = data.data;
                    return;
                }
            }
        System.out.println("khong thay");
    }
    public void respon_call(data_socket data){
            System.out.println("doi phuong da phan hoi yeu cau");
            System.out.println(Arrays.toString(data.data));
            for (fr_chat list_fr_chat : Chat_client.list_fr_chat) {
                if(list_fr_chat.call.fr_ID == Integer.valueOf(data.data[0])){
                    if(data.accept){
                        try {
                            list_fr_chat.call.init_recorder(InetAddress.getByName(data.data[2]), Integer.valueOf(data.data[3]));
                            list_fr_chat.call.lb_status.setText("Đã kết nối");
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(receive.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        list_fr_chat.call.lb_status.setText(data.data[2]);
                        try {
                            Thread.sleep(1000);
                            list_fr_chat.call.dispose();
                            list_fr_chat.btn_call.setEnabled(true);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(receive.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
        
    }
    public void load_history(data_socket dtsk){
        fr_chat fr_chat = null;
        for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
            if(Chat_client.list_fr_chat.get(i).fr_ID == Integer.parseInt(dtsk.data[0])){
                fr_chat = Chat_client.list_fr_chat.get(i);
                break;
            }
        }
        if(fr_chat == null)
            return;
        for(int i = dtsk.data_arr.size()-1; i >= 0;i--){
            if(dtsk.data_arr.get(i)[0].equalsIgnoreCase(String.valueOf(Chat_client.my_ID))){
                fr_chat.cus_append2(dtsk.data_arr.get(i)[1], 1);
            }else{
                fr_chat.cus_append2(dtsk.data_arr.get(i)[1], 2);
            }
        }
    }
    public void broadcast_status(String[] data){
        int size = Chat_client.list_friend.size();
        int size2 = Chat_client.list_fr_chat.size();
        for(int i = 0; i < size;i++){
            if(Chat_client.list_friend.get(i).getID() == Integer.parseInt(data[0])){
                Chat_client.list_friend.get(i).setonline(Integer.parseInt(data[1]));
                break;
            }
        }
        for(int i = 0; i < size2;i++){
            if(Chat_client.list_fr_chat.get(i).fr_ID == Integer.parseInt(data[0])){
                if(data[1].equalsIgnoreCase("1")){
                    Chat_client.list_fr_chat.get(i).btn_call.setEnabled(true);
                }else{
                    Chat_client.list_fr_chat.get(i).btn_call.setEnabled(false);
                }
                break;
            }
        }
        
        DefaultListModel<friend> defaultListModel = new DefaultListModel<>();
        for(int i = 0; i < size; i++){
            defaultListModel.addElement(Chat_client.list_friend.get(i));
        }
        fr_client.list_friend.setModel(defaultListModel);
        fr_client.list_friend.setCellRenderer(new fr_list_model());
    }
    public void broadcast_thinking(String[] data){
        int size = Chat_client.list_friend.size();
        for(int i = 0; i < size;i++){
            if(Chat_client.list_friend.get(i).getID() == Integer.parseInt(data[0])){
                Chat_client.list_friend.get(i).setStatus(data[1]);
                break;
            }
        }
        DefaultListModel<friend> defaultListModel = new DefaultListModel<>();
        for(int i = 0; i < size; i++){
            defaultListModel.addElement(Chat_client.list_friend.get(i));
        }
        fr_client.list_friend.setModel(defaultListModel);
        fr_client.list_friend.setCellRenderer(new fr_list_model());
    }
    public void receive_msg(String[] data){
        ImageIcon icon = null;
        int online = 0;
        if(!Chat_client.list_fr_chat.isEmpty()){
            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
                if(Chat_client.list_fr_chat.get(i).fr_ID == Integer.valueOf(data[0])){
                    Chat_client.list_fr_chat.get(i).setState(Frame.NORMAL);
                    Chat_client.list_fr_chat.get(i).show();
                    if(data[3].equalsIgnoreCase("buzz")){
                        Chat_client.list_fr_chat.get(i).vibrate();
                        Chat_client.list_fr_chat.get(i).clip = this.playsound("Nudge.wav");
                        Chat_client.list_fr_chat.get(i).clip.start();
                        return;
                    }
                    Chat_client.list_fr_chat.get(i).cus_append2(data[3],2);
                    Chat_client.list_fr_chat.get(i).requestFocus();
                    Chat_client.list_fr_chat.get(i).clip = this.playsound("msg.wav");
                    Chat_client.list_fr_chat.get(i).clip.start();
                    return;
                }
            }
        }
        for(int i = 0; i< Chat_client.list_friend.size();i++){
                if(Chat_client.list_friend.get(i).getID() == Integer.valueOf(data[0])){
                    icon = Chat_client.list_friend.get(i).getImageIcon();
                    online = Chat_client.list_friend.get(i).getonline();
                }
            }
        fr_chat chat = new fr_chat();
        chat.setTitle("Nhắn tin với: "+data[2]);
        chat.fr_name = data[2];
        chat.lb_avartar_me.setIcon(Chat_client.icon);
        chat.lb_avaratar_fr.setIcon(icon);
        chat.icon_fr = icon;
        if(online == 1){
            chat.btn_call.setEnabled(true);
        }
        else{
            chat.btn_call.setEnabled(false);
        }
        chat.fr_ID = Integer.valueOf(data[0]);
        chat.setVisible(true);
        Chat_client.list_fr_chat.add(chat);
        if(data[3].equalsIgnoreCase("buzz")){
            chat.vibrate();
            chat.clip = this.playsound("Nudge.wav");
            chat.clip.start();
            return;
        }
        
        chat.cus_append2(data[3],2);
        chat.clip = this.playsound("msg.wav");
        chat.clip.start();
        
    }    
    public Clip playsound(String path){
        String file_path = "sound/"+path;
        try {
            URL yourFile = getClass().getClassLoader().getResource(file_path);
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            
            stream = AudioSystem.getAudioInputStream(yourFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            return clip;
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Logger.getLogger(fr_chat.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    public void show_result_search(ArrayList<String[]> data){
        if(data.isEmpty()){
            JOptionPane.showMessageDialog(null, "Not found");
            return;
        }
        String[] column = new String[1];
        column[0] = "Kết quả";
        int size = data.size();
        Object[][] data_table = new Object[size][1];
        for(int i = 0; i < size; i++){
            data_table[i][0] = data.get(i)[1];
        }
        Chat_client.fr_result_search.arr = data;
        TableModel tableModel = new DefaultTableModel(data_table, column){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        fr_result_search.table_result.setModel(tableModel);
        Chat_client.fr_search.setVisible(false);
        Chat_client.fr_result_search.setVisible(true);
    }
    public void show_friend(ArrayList<friend> data){
        int size = data.size();
        DefaultListModel<friend> defaultListModel = new DefaultListModel<>();
        for(int i = 0; i < size; i++){
            //Chat_client.list_friend.add(data.get(i));
            defaultListModel.addElement(data.get(i));
        }
        fr_client.list_friend.setModel(defaultListModel);
        fr_client.list_friend.setCellRenderer(new fr_list_model());
    }
    public void check_login(){
        if(respon.data[0].equalsIgnoreCase("true")){
            Chat_client.fr_client.setVisible(true);
            Chat_client.fr_client.setTitle("Demo chat java");
            Chat_client.fr_client.lb_display_name.setText(respon.list_fr.get(0).getFull_name());
            if(respon.list_fr.get(0).getStatus().equalsIgnoreCase("")){
                Chat_client.fr_client.txt_status.setText("Bạn có dự định gì");
            }else{
                Chat_client.fr_client.txt_status.setText(respon.list_fr.get(0).getStatus());
            }
            Chat_client.my_ID = respon.list_fr.get(0).getID();
            Chat_client.fr_client.lb_avatar.setIcon(respon.list_fr.get(0).getImageIcon());
            Chat_client.icon = respon.list_fr.get(0).getImageIcon();
            Chat_client.full_name = respon.list_fr.get(0).getFull_name();
            Chat_client.fr_login.setVisible(false);

        }
        else{
            JOptionPane.showMessageDialog(null, "username or password is not correct");
        }
    }
    public void respon_reg(data_socket respon){
        if(respon.data[0].equalsIgnoreCase("true")){
            JOptionPane.showMessageDialog(null, "Đăng ký thành công");
            Chat_client.fr_reg.setVisible(false);
            Chat_client.fr_login.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Tên người dùng đã tồn tại");
        }
    }
    public void load_friend(data_socket respon){
        Chat_client.list_friend = respon.list_fr;
        this.show_friend(Chat_client.list_friend);
    }
}
