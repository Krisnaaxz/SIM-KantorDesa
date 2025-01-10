/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sim.kantordesa;
import sim.kantordesa.auth.login;

/**
 *
 * @author Krisna
 */
public class SIMKantorDesa {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        login LoginFrame = new login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
    }
}
