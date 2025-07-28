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

    public DatosAlumno(
        int id_datosAlumno,
        String religion,
        String estado,
        String municipio,
        String localidad,
        String colonia,
        String calle,
        String numeroCasa
    ) {
        this.id_datosAlumno = id_datosAlumno;
        this.religion = religion;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
        this.colonia = colonia;
        this.calle = calle;
        this.numeroCasa = numeroCasa;
    }

    public DatosAlumno() {
    }
    
    
    
    public boolean crear(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO datosAlumno VALUES (NULL, '', '', '', '', '', '', '')";
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
            System.out.println("no se encontro la id");
        }
        return id;
    }
    
    public boolean actualizar() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE datosAlumno SET " +
                         "religion = ?, estado = ?, municipio = ?, localidad = ?, colonia = ?, calle = ?, numeroCasa = ? " +
                         "WHERE id_datosAlumno = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, religion);
            ps.setString(2, estado);
            ps.setString(3, municipio);
            ps.setString(4, localidad);
            ps.setString(5, colonia);
            ps.setString(6, calle);
            ps.setString(7, numeroCasa);
            ps.setInt(8, id_datosAlumno);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al actualizar datosAlumno: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
