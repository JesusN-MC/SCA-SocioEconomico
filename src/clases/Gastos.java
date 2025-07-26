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
public class Gastos {
    int id_gastos;
    String estadoCivil;
    String nombres;
    String ap;
    String am;
    String hijos;
    int cantidadHijos;
    String trabaja;
    String tiempoCasados;
    String ustedTrabaja;
    String donde;
    String horario;
    String costeaGastos;
    String gastosPersonales;
    String beca;
    String cual;
    String preparatoria;
    String localidad;
    String añoEgreso;
    String tiempoLibre;
    String addiciones;

    public boolean crear() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "INSERT INTO informacionGastosEstudiante(id_gastos) VALUES (NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            showMessageDialog(null, "Error al crear Gastos");
            return false;
        }
    }

    public int consultarReciente() {
        int id = 0;
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT id_gastos FROM informacionGastosEstudiante WHERE id_gastos = (SELECT MAX(id_gastos) FROM informacionGastosEstudiante)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id_gastos");
        } catch (Exception e) {
            System.out.println("No se encontró la ID de Gastos");
        }
        return id;
    }
}
