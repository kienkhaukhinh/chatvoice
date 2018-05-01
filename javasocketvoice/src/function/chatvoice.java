/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import javasocketvoice.Javasocketvoice;
import javasocketvoice.client;
import chat.data_socket;

/**
 *
 * @author pc
 */
public class chatvoice {
    public static void respon_call(data_socket data){
        for(client client: Javasocketvoice.arr_client){
            if(client.ID == Integer.valueOf(data.data[1])){
                client.sendtoclient(data);
            }
        }
    }
    public static void request_call(data_socket data){
        for(client client: Javasocketvoice.arr_client){
            if(client.ID == Integer.valueOf(data.data[1])){ // tìm id người nhận
                client.sendtoclient(data);
                break;
            }
        }
    }
}
