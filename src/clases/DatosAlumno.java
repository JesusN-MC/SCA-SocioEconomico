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
public class DatosAlumno {
    int id_datosAlumno; 
    String religion; 
    String estado; 
    String municipio;
    String localidad; 
    String colonia;
    String calle; 
    String numeroCasa; 
    String telefono;
    String familiar;
    String contactoFamiliar;
    
    public boolean crear(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO datosAlumno VALUES (NULL, '', '', '', '', '', '', '', '', '', '')";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            showMessageDialog(null, "Error al crear DatosAlumno");
            return false;
        }
    }
    
    public int consultarReciente(){
        int id = 0;
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT id_datosAlumno FROM datosAlumno WHERE id_datosAlumno = (SELECT MAX(id_datosAlumno) FROM datosAlumno)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet datos = ps.executeQuery();
            if(datos.next()){
                id = datos.getInt("id_datosAlumno");
            }
        }catch(Exception e){
            
        }
        return id;
    }
}
