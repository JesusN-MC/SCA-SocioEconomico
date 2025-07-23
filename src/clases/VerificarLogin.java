/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author jobno
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VerificarLogin {
    
    
    public boolean VerificarDatos(String usuario, String contra) {
        Conexion conexion = new Conexion();
        Connection con = conexion.con;

        try {
            String sql = "SELECT * FROM usuario WHERE user = ? AND clave = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contra);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // Si hay resultado, usuario y contra son correctos

        } catch (Exception e) {
            System.out.println("Error al verificar login: " + e);
            return false;
        }
    }
}

