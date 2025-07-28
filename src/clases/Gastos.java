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

    public Gastos(
        int id_gastos,
        String estadoCivil,
        String nombres,
        String ap,
        String am,
        String hijos,
        int cantidadHijos,
        String trabaja,
        String tiempoCasados,
        String ustedTrabaja,
        String donde,
        String horario,
        String costeaGastos,
        String gastosPersonales,
        String beca,
        String cual,
        String preparatoria,
        String localidad,
        String añoEgreso,
        String tiempoLibre,
        String addiciones
    ) {
        this.id_gastos = id_gastos;
        this.estadoCivil = estadoCivil;
        this.nombres = nombres;
        this.ap = ap;
        this.am = am;
        this.hijos = hijos;
        this.cantidadHijos = cantidadHijos;
        this.trabaja = trabaja;
        this.tiempoCasados = tiempoCasados;
        this.ustedTrabaja = ustedTrabaja;
        this.donde = donde;
        this.horario = horario;
        this.costeaGastos = costeaGastos;
        this.gastosPersonales = gastosPersonales;
        this.beca = beca;
        this.cual = cual;
        this.preparatoria = preparatoria;
        this.localidad = localidad;
        this.añoEgreso = añoEgreso;
        this.tiempoLibre = tiempoLibre;
        this.addiciones = addiciones;
    }

    public Gastos() {
    }
    
    
    
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
    
    public boolean actualizar() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE informacionGastosEstudiante SET " +
                         "estadoCivil=?, nombres=?, ap=?, am=?, hijos=?, cantidadHijos=?, trabaja=?, tiempoCasados=?, " +
                         "ustedTrabaja=?, donde=?, horario=?, costeaGastos=?, gastosPersonales=?, beca=?, cual=?, " +
                         "preparatoria=?, localidad=?, añoEgreso=?, tiempoLibre=?, addiciones=? " +
                         "WHERE id_gastos=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, estadoCivil);
            ps.setString(2, nombres);
            ps.setString(3, ap);
            ps.setString(4, am);
            ps.setString(5, hijos);
            ps.setInt(6, cantidadHijos);
            ps.setString(7, trabaja);
            ps.setString(8, tiempoCasados);
            ps.setString(9, ustedTrabaja);
            ps.setString(10, donde);
            ps.setString(11, horario);
            ps.setString(12, costeaGastos);
            ps.setString(13, gastosPersonales);
            ps.setString(14, beca);
            ps.setString(15, cual);
            ps.setString(16, preparatoria);
            ps.setString(17, localidad);
            ps.setString(18, añoEgreso);
            ps.setString(19, tiempoLibre);
            ps.setString(20, addiciones);
            ps.setInt(21, id_gastos);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al actualizar gastos del estudiante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
