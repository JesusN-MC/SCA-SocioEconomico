/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static javax.swing.JOptionPane.showMessageDialog;
/**
 *
 * @author adhar
 */
public class DatosHermanos {
    int idDatosHermanos;
    String tieneHermanos;
    String cuantosH;
    String comentarioPersonal;
    
    public boolean crear() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "INSERT INTO datosHermanos(idDatosHermanos) VALUES (NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            showMessageDialog(null, "Error al crear Datos de Hermanos");
            return false;
        }
    }
    
    public int consultarReciente() {
        int id = 0;
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT idDatosHermanos FROM datosHermanos WHERE idDatosHermanos = (SELECT MAX(idDatosHermanos) FROM datosHermanos)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("idDatosHermanos");
        } catch (Exception e) {
            System.out.println("No se encontró la ID de Datos de Hermanos");
        }
        return id;
    }
 
}

