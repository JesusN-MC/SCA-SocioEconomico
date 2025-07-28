/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author jobno
 */
public class Formulario {
    int idFormulario;
    String fecha;
    int id_datosAlumno;
    int id_condicionesVivienda;
    int id_gastos;
    int id_datosFamiliaresF;
    int id_datosFamiliaresM;

    public Formulario(int idFormulario, int id_datosAlumno, String n) {
        this.idFormulario = idFormulario;
        this.id_datosAlumno = id_datosAlumno;
    }

    public Formulario(String n, int idFormulario, int id_condicionesVivienda) {
        this.idFormulario = idFormulario;
        this.id_condicionesVivienda = id_condicionesVivienda;
    }

    public Formulario(int idFormulario, String n, int id_gastos) {
        this.idFormulario = idFormulario;
        this.id_gastos = id_gastos;
    }

    public Formulario(int idFormulario, int id_datosFamiliaresF , int n) {
        this.idFormulario = idFormulario;
        this.id_datosFamiliaresF = id_datosFamiliaresF;
    }

    public Formulario(int idFormulario, int id_datosFamiliaresM) {
        this.idFormulario = idFormulario;
        this.id_datosFamiliaresM = id_datosFamiliaresM;
    }
    
    

    public Formulario() {
    }

    
    public boolean crearFormulario(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO formulario VALUES (NULL, NOW(), NULL, NULL, NULL, NULL, NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            e.printStackTrace();
            showMessageDialog(null, "Error al crear el Formulario");
            return false;
        }
    }
    
    public void updateDatosAlumno(){
        try{
                //actualizar la id datosAlumno
            Conexion conexion2 = new Conexion();
            Connection con2 = conexion2.con;

            String sql2 = "UPDATE formulario SET id_datosAlumno = ? WHERE idFormulario = ?";
            PreparedStatement ps2 = con2.prepareStatement(sql2);
            ps2.setInt(1, id_datosAlumno);
            ps2.setInt(2, idFormulario);
            
            ps2.executeUpdate();
            
            
        }catch (Exception e){
            showMessageDialog(null, "Error al actualizar id_datosAlumno");
        }
    }
    
    public void updateCondicionesVivienda() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE formulario SET id_condicionesVivienda = ? WHERE idFormulario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_condicionesVivienda);
            ps.setInt(2, idFormulario);

            ps.executeUpdate();

        } catch (Exception e) {
            showMessageDialog(null, "Error al actualizar id_condicionesVivienda");
        }
    }

    public void updateGastos() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE formulario SET id_gastos = ? WHERE idFormulario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_gastos);
            ps.setInt(2, idFormulario);

            ps.executeUpdate();

        } catch (Exception e) {
            showMessageDialog(null, "Error al actualizar id_gastos");
        }
    }

    public void updateDatosFamiliaresF() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE formulario SET id_datosFamiliaresF = ? WHERE idFormulario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_datosFamiliaresF);
            ps.setInt(2, idFormulario);

            ps.executeUpdate();

        } catch (Exception e) {
            showMessageDialog(null, "Error al actualizar id_datosFamiliaresF");
        }
    }

    public void updateDatosFamiliaresM() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE formulario SET id_datosFamiliaresM = ? WHERE idFormulario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_datosFamiliaresM);
            ps.setInt(2, idFormulario);

            ps.executeUpdate();

        } catch (Exception e) {
            showMessageDialog(null, "Error al actualizar id_datosFamiliaresM");
        }
    }

}