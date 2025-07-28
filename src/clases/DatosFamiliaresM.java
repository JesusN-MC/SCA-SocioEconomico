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
    String tieneHermanos;
    String cuantosH;
    String comentarioPersonal;

    public DatosFamiliaresM() {
    }
    
    
    
    public DatosFamiliaresM(
        int id_datosFamiliaresM,
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
        String numeroCasa,
        String tieneHermanos,
        String cuantosH,
        String comentarioPersonal
    ) {
        this.id_datosFamiliaresM = id_datosFamiliaresM;
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
        this.tieneHermanos = tieneHermanos;
        this.cuantosH = cuantosH;
        this.comentarioPersonal = comentarioPersonal;
    }
    

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
    
        public boolean actualizar() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE datosFamiliaresM SET " +
                         "nombres = ?, ap = ?, am = ?, edad = ?, escolaridad = ?, ingresoSemanal = ?, " +
                         "estado = ?, municipio = ?, localidad = ?, colonia = ?, calle = ?, numeroCasa = ?, " +
                         "tieneHermanos = ?, cuantosH = ?, comentarioPersonal = ? " +
                         "WHERE id_datosFamiliaresM = ?";

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
            ps.setString(13, tieneHermanos);
            ps.setString(14, cuantosH);
            ps.setString(15, comentarioPersonal);
            ps.setInt(16, id_datosFamiliaresM); // where

            ps.executeUpdate();
            return true;

        } catch(Exception e) {
            System.out.println("Error al actualizar datos familiares M: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

