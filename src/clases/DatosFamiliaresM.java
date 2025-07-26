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
public class DatosFamiliaresM {
    int id_datosFamiliaresM;
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
            String sql = "INSERT INTO datosFamiliaresM(id_datosFamiliaresM) VALUES (NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            showMessageDialog(null, "Error al crear DatosFamiliaresM");
            return false;
        }
    }

    public int consultarReciente() {
        int id = 0;
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT id_datosFamiliaresM FROM datosFamiliaresM WHERE id_datosFamiliaresM = (SELECT MAX(id_datosFamiliaresM) FROM datosFamiliaresM)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id_datosFamiliaresM");
        } catch (Exception e) {
            System.out.println("No se encontró la ID de DatosFamiliaresM");
        }
        return id;
    }
}

