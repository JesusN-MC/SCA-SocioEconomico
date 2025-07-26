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
public class CondicionesVivienda {
    int id_condicionesVivienda;
    String material;
    String tejas;
    String palma;
    String carton;
    String otro;
    int numeroPlantas;
    String laCasaEs;
    String piezas;
    String cuartosDormir;
    String cuartoPropio;
    String compartidoCon;
    String patioCochera;
    int cantidadIntegrantes;
    String informacionTraslado;
    String espacioTareas;
    String enfermedades;
    String agua;
    String luz;
    String cable;
    String estufa;
    String refrigerador;
    String television;
    String lavadora;
    String computadora;
    String microondas;
    String comedor;
    String sala;
    String celular;
    String minisplit;
    String abanico;
    String piso;
    String observaciones;
    String informacionExtra;

    public boolean crear() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "INSERT INTO condicionesVivienda(id_condicionesVivienda) VALUES (NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            showMessageDialog(null, "Error al crear CondicionesVivienda");
            return false;
        }
    }

    public int consultarReciente() {
        int id = 0;
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT id_condicionesVivienda FROM condicionesVivienda WHERE id_condicionesVivienda = (SELECT MAX(id_condicionesVivienda) FROM condicionesVivienda)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id_condicionesVivienda");
        } catch (Exception e) {
            System.out.println("No se encontró la ID de CondicionesVivienda");
        }
        return id;
    }
}
