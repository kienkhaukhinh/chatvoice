/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import chat.friend;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasocketvoice.connectdatabase;
import javasocketvoice.serverprocess;
import javax.imageio.ImageIO;

/**
 *
 * @author pc
 */
public class updateuser {
    public static void update_status(String[] data, int ID){
        String query = "UPDATE `info_user` SET `status` = ? WHERE `ID` = ?";
        try {
            PreparedStatement pre = connectdatabase.connect.prepareStatement(query);
            pre.setString(1, data[0]);
            pre.setInt(2, ID);
            pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void update_info(friend e, int ID){
        String query = "UPDATE `info_user` SET `fullname` = ?, `pass` = ? WHERE `ID` = ?";
        PreparedStatement pre;
        try {
            pre = connectdatabase.connect.prepareStatement(query);
            pre.setString(1, e.getFull_name());
            pre.setString(2, e.getPass());
            pre.setInt(3, ID);
            pre.executeUpdate();
            if(e.getImageIcon() != null){
                File output_image = new File("D:\\image/"+ID+".png");
                BufferedImage bi = new BufferedImage(e.getImageIcon().getIconWidth(), e.getImageIcon().getIconHeight(), BufferedImage.TYPE_INT_BGR);
                Graphics g = bi.getGraphics();
                g.drawImage(e.getImageIcon().getImage(), 0, 0, null);
                ImageIO.write(bi, "png", output_image);
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(serverprocess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
