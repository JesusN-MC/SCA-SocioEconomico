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
 * @author jobno
 */
public class DatosFamiliaresF {
    int id_datosFamiliaresF;
    String nombres;
    String ap;
    String am;
    int edad;
    String escolaridad;
    int ingresoSemanal;
    String estado;
    String municipio;
    String localidad;
    String colonia;
    String calle;
    String numeroCasa;

    public boolean crear() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "INSERT INTO datosFamiliaresF(id_datosFamiliaresF) VALUES (NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            showMessageDialog(null, "Error al crear DatosFamiliaresF");
            return false;
        }
    }

    public int consultarReciente() {
        int id = 0;
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT id_datosFamiliaresF FROM datosFamiliaresF WHERE id_datosFamiliaresF = (SELECT MAX(id_datosFamiliaresF) FROM datosFamiliaresF)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id_datosFamiliaresF");
        } catch (Exception e) {
            System.out.println("No se encontró la ID de DatosFamiliaresF");
        }
        return id;
    }
}
