/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import chat.Chat_client;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import voice.player;
import voice.recorder;

/**
 *
 * @author duong
 */
public class fr_call extends javax.swing.JFrame {
    public int fr_ID;
    
    public int fr_port;
    public InetAddress fr_ip;
    public int my_port;
    public String my_ip;
    
    DatagramSocket datagramSocket;
    
    public fr_chat fr_chat;
    recorder r;
    player p;
    static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    public static AudioFormat getAudioFormat(){
   
        float sampleRate = 8000.0F;

        int sampleSizeInBits = 16;

        int channels = 2;

        boolean signed = true;

        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
    }
    SourceDataLine audio_out;
    TargetDataLine audio_in;
    
    public fr_call() {
        initComponents();  
    }
    public void init_audio(){
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info_in = new DataLine.Info(TargetDataLine.class, format);
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info_in)) {
                System.out.println("Line for in not supported");
                System.exit(0);
            }
            if(!AudioSystem.isLineSupported(info_out)){
                System.out.println("Line for out not supported");
                System.exit(0);
            }
            audio_out = (SourceDataLine)AudioSystem.getLine(info_out);
            audio_out.open(format);
            audio_out.start();
            
            audio_in = (TargetDataLine) AudioSystem.getLine(info_in);
            audio_in.open(format);
            audio_in.start();            
        } catch (LineUnavailableException ex) {
            Logger.getLogger(fr_call.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void init_player(){
        try {
            p = new player();
            datagramSocket = new DatagramSocket(0);
            p.din = datagramSocket;
            p.audio_out = audio_out;
            my_ip = InetAddress.getLocalHost().getHostAddress();
            my_port = datagramSocket.getLocalPort();
            System.out.println("my ip: "+my_ip+"  cổng: "+datagramSocket.getLocalPort());
            p.start();
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(fr_call.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void init_recorder(InetAddress ia,int port){
        System.out.println("call in init recorder:"+ ia.toString()+port);
        r = new recorder();
        r.dout = datagramSocket;
        r.audio_in = audio_in;
        r.fr_ip = ia;
        r.fr_port = port;
        r.start();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_avartar = new javax.swing.JLabel();
        lb_status = new javax.swing.JLabel();
        btn_end_call = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        lb_status.setText("Đang gọi");

        btn_end_call.setText("Kết thúc");
        btn_end_call.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_end_callActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_avartar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_end_call)
                    .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_avartar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_end_call)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_end_callActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_end_callActionPerformed
        Chat_client.calling = false;
        fr_chat.btn_call.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_btn_end_callActionPerformed

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
            java.util.logging.Logger.getLogger(fr_call.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new fr_call().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_end_call;
    public javax.swing.JLabel lb_avartar;
    public javax.swing.JLabel lb_status;
    // End of variables declaration//GEN-END:variables
}
