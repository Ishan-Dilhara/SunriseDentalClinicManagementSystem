/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sunrisedentalclinicmanagementsystem;

import com.formdev.flatlaf.FlatLightLaf;
import sunrise.dental.ui.LoginFrame;

/**
 *
 * @author HP
 */
public class SunriseDentalClinicManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
}
