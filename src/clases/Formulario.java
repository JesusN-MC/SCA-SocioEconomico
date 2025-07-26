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
    
    
    
    
    
    
    public boolean crearFormulario(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO formulario VALUES (NULL, NOW(), NULL, NULL, NULL, NULL, NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            showMessageDialog(null, "Error al crear el Formulario");
            return false;
        }
    }
}
