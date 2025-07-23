/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto.integrador;

import com.formdev.flatlaf.FlatLightLaf;
import interfaz.Login;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author jobno
 */
public class PROYECTOINTEGRADOR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                Login login = new Login(); // Crear instancia del JFrame
                login.setVisible(true);      // Hacer visible la ventana
                login.setLocationRelativeTo(null); // Centrar en pantalla (opcional)
       
    }
    
}
