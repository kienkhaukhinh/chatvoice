/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import chat.Chat_client;
import chat.data_socket;
import chat.friend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 *
 * @author duong
 */
public class fr_client extends javax.swing.JFrame{

    ObjectOutputStream dout = null;
    ObjectInputStream din = null;
    public fr_client() {
        initComponents();
        this.lb_avatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() ==2){
                    fr_change_info fr = new fr_change_info();
                    fr.lb_avatar.setIcon(Chat_client.icon);
                    fr.txt_name.setText(Chat_client.full_name);
                    fr.setVisible(true);
                }
            }
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width-this.getSize().width-50, 50);
        list_friend.requestFocus();
        txt_status.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                txt_status.setBackground(Color.WHITE);
                txt_status.selectAll();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                txt_status.setBackground(new Color(240,240,240));
                update_status();
                
            }
        });
        txt_status.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                update_status();
                
            }
        });
        cb_choose_status.addActionListener((ActionEvent ae) -> {
            update_online();
        });
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        btn_add_friend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_friend = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        lb_avatar = new javax.swing.JLabel();
        lb_display_name = new javax.swing.JLabel();
        txt_status = new javax.swing.JTextField();
        cb_choose_status = new javax.swing.JComboBox<>();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem2");
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setLocation(new java.awt.Point(0, 0));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 780));

        btn_add_friend.setText("Thêm Bạn");
        btn_add_friend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_friendActionPerformed(evt);
            }
        });

        list_friend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_friendMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list_friend);

        jLabel1.setText("Danh sách bạn");

        lb_avatar.setBorder(BorderFactory.createLineBorder(new Color(0,255,0),2));

        lb_display_name.setBackground(new java.awt.Color(204, 204, 204));
        lb_display_name.setFont(new java.awt.Font("Times New Roman", 2, 20)); // NOI18N
        lb_display_name.setText("Họ tên");
        lb_display_name.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        txt_status.setBackground(new java.awt.Color(240, 240, 240));
        txt_status.setText("Bạn có dự định gì ?");
        txt_status.setBorder(null);
        txt_status.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txt_status_hover(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_status_over(evt);
            }
        });

        cb_choose_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ONLINE", "BUSY" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_display_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_status, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_choose_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(btn_add_friend)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_display_name, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(txt_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cb_choose_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_add_friend)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void update_online(){
        data_socket dtsk = new data_socket();
        String[] data = new String[1];
        data[0] = cb_choose_status.getSelectedItem().toString();
        if(data[0].equalsIgnoreCase("ONLINE")){
            Chat_client.fr_client.lb_avatar.setBorder(BorderFactory.createLineBorder(new Color(0,255,0),2));
        }else if(data[0].equalsIgnoreCase("BUSY")){
            Chat_client.fr_client.lb_avatar.setBorder(BorderFactory.createLineBorder(new Color(240,0,0),2));
        }
        dtsk.action = "update_online";
        dtsk.data = data;
        try {
            ObjectOutputStream douts = new ObjectOutputStream(Chat_client.socket.getOutputStream());
            douts.writeObject(dtsk);
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void update_status(){
        list_friend.requestFocus(true);
        data_socket dtsk = new data_socket();
        dtsk.action = "update_status";
        String[] data = new String[1];
        data[0] = txt_status.getText();
        dtsk.data = data;
        try {
            ObjectOutputStream douts = new ObjectOutputStream(Chat_client.socket.getOutputStream());
            douts.writeObject(dtsk);
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void btn_add_friendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_friendActionPerformed
        Chat_client.fr_search.setVisible(true);
    }//GEN-LAST:event_btn_add_friendActionPerformed

    private void list_friendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_friendMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            this.chat(list_friend.getSelectedIndex());
        }
    }//GEN-LAST:event_list_friendMouseClicked

    private void txt_status_hover(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_status_hover
        txt_status.setBackground(Color.WHITE);
    }//GEN-LAST:event_txt_status_hover

    private void txt_status_over(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_status_over
        txt_status.setBackground(new Color(240,240,240));
    }//GEN-LAST:event_txt_status_over
 
    public void chat(int index){
        friend fr = Chat_client.list_friend.get(index);
        if(!Chat_client.list_fr_chat.isEmpty()){
            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
                if(Chat_client.list_fr_chat.get(i).fr_ID == fr.getID()){
                    Chat_client.list_fr_chat.get(i).setVisible(true);
                    return;
                }
            }
        }
        fr_chat chat = new fr_chat();
        chat.fr_ID = fr.getID();
        chat.setTitle("Nhắn tin với: "+fr.getFull_name()+fr.getID());
        chat.fr_name = fr.getFull_name();
        chat.lb_avartar_me.setIcon(Chat_client.icon);
        chat.icon_fr = new ImageIcon(Chat_client.list_friend.get(index).getImageIcon().getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        chat.lb_avaratar_fr.setIcon(chat.icon_fr);
        if(fr.getonline() == 1 ){
            chat.btn_call.setEnabled(true);
        }else{
            chat.btn_call.setEnabled(false);
        }
        chat.load_history();
        chat.setVisible(true);
        Chat_client.list_fr_chat.add(chat);
    }
    
   public void load_friend(int status){
        try {
            data_socket dtsk = new data_socket();
            dtsk.action = "loadfriend";
            String[] data = new String[1];
            data[0] = String.valueOf(status);
            dtsk.data = data;
            dout = new ObjectOutputStream(Chat_client.socket.getOutputStream());
            dout.writeObject(dtsk);
            
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fr_client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new fr_client().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_friend;
    private javax.swing.JComboBox<String> cb_choose_status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lb_avatar;
    public javax.swing.JLabel lb_display_name;
    public static javax.swing.JList<friend> list_friend;
    public javax.swing.JTextField txt_status;
    // End of variables declaration//GEN-END:variables


}
