/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voice;

import chat.Chat_client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author duong
 */
public class recorder extends Thread{
        public TargetDataLine audio_in = null;
        public DatagramSocket dout;
        byte byte_buff[] = new byte[512];
        public InetAddress fr_ip;
        public int fr_port;
        @Override
        public void run(){
                Long pack = 0l;
                while(Chat_client.calling){
                    try {
                        int read = audio_in.read(byte_buff, 0, byte_buff.length);
                        //audio_out.write(byte_buff, 0, byte_buff.length);
                        //System.out.println("sending");
                        DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, fr_ip, fr_port);
                        System.out.println("send: #"+ pack++);
                        //Thread.sleep(1000);
                        dout.send(data);
                    } catch (IOException ex) {
                        Logger.getLogger(recorder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
                System.out.println("call in recorder: recorder is stop");
                audio_in.drain();
                audio_in.close();
                
                System.out.println("call in recorder: audio is drain and close");
            
        }
}
