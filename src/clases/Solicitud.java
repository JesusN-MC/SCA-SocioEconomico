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
public class Solicitud {
    int idSolicitud;
    String idAlumno, fecha, estatus, motivo, telefonoSolicitud, canaliza, familiar, telefonoFamiliar, tipo, argumentacion;

    public Solicitud(int idSolicitud, String idAlumno, String fecha, String estatus, String motivo, String telefonoSolicitud, String canaliza, String familiar, String telefonoFamiliar, String tipo, String argumentacion) {
        this.idSolicitud = idSolicitud;
        this.idAlumno = idAlumno;
        this.fecha = fecha;
        this.estatus = estatus;
        this.motivo = motivo;
        this.telefonoSolicitud = telefonoSolicitud;
        this.canaliza = canaliza;
        this.familiar = familiar;
        this.telefonoFamiliar = telefonoFamiliar;
        this.tipo = tipo;
        this.argumentacion = argumentacion;
    }


    public Solicitud(int idSolicitud, String idAlumno, String fecha, String estatus, String motivo) {
        this.idSolicitud = idSolicitud;
        this.idAlumno = idAlumno;
        this.fecha = fecha;
        this.estatus = estatus;
        this.motivo = motivo;
    }
    
    public Solicitud(String idAlumno, String motivo, String telefonoSolicitud, String canaliza, String familiar, String telefonoFamiliar, String tipo, String argumentacion) {
        this.idAlumno = idAlumno;
        this.motivo = motivo;
        this.telefonoSolicitud = telefonoSolicitud;
        this.canaliza = canaliza;
        this.familiar = familiar;
        this.telefonoFamiliar = telefonoFamiliar;
        this.tipo = tipo;
        this.argumentacion = argumentacion;
    }
    
    

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getTelefonoSolicitud() {
        return telefonoSolicitud;
    }

    public String getCanaliza() {
        return canaliza;
    }

    public String getFamiliar() {
        return familiar;
    }

    public String getTelefonoFamiliar() {
        return telefonoFamiliar;
    }

    public String getTipo() {
        return tipo;
    }

    public String getArgumentacion() {
        return argumentacion;
    }
    
    public boolean Guardar(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO solicitud VALUES (NULL, ?, NOW(),	0, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idAlumno);
            ps.setString(2, motivo);
            ps.setString(3, telefonoSolicitud);
            ps.setString(4, canaliza);
            ps.setString(5, familiar);
            ps.setString(6, telefonoFamiliar);
            ps.setString(7, tipo);
            ps.setString(8, argumentacion);
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos en solicitud");
            return false;
        }
    }
}