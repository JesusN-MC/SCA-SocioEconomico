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
    
    public DatosFamiliaresF(
        int id_datosFamiliaresF,
        String nombres,
        String ap,
        String am,
        int edad,
        String escolaridad,
        int ingresoSemanal,
        String estado,
        String municipio,
        String localidad,
        String colonia,
        String calle,
        String numeroCasa
    ){
        this.id_datosFamiliaresF = id_datosFamiliaresF;
        this.nombres = nombres;
        this.ap = ap;
        this.am = am;
        this.edad = edad;
        this.escolaridad = escolaridad;
        this.ingresoSemanal = ingresoSemanal;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
        this.colonia = colonia;
        this.calle = calle;
        this.numeroCasa = numeroCasa;
    }

    public DatosFamiliaresF() {
    }
    

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
    
        public boolean actualizar() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE datosFamiliaresF SET " +
                         "nombres = ?, ap = ?, am = ?, edad = ?, escolaridad = ?, ingresoSemanal = ?, " +
                         "estado = ?, municipio = ?, localidad = ?, colonia = ?, calle = ?, numeroCasa = ? " +
                         "WHERE id_datosFamiliaresF = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombres);
            ps.setString(2, ap);
            ps.setString(3, am);
            ps.setInt(4, edad);
            ps.setString(5, escolaridad);
            ps.setInt(6, ingresoSemanal);
            ps.setString(7, estado);
            ps.setString(8, municipio);
            ps.setString(9, localidad);
            ps.setString(10, colonia);
            ps.setString(11, calle);
            ps.setString(12, numeroCasa);
            ps.setInt(13, id_datosFamiliaresF);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al actualizar datosFamiliaresF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
