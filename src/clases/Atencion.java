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
 * @author Lizbetg P. Guillén
 */
public class Atencion {
    int idAtencion;
    String fecha , canalizacion, estatus, estatusNotificado, idFormulario, idSolicitud, resumenAtencion;

    public Atencion(int idAtencion, String fecha, String canalizacion, String estatus, String estatusNotificado, String idFormulario, String idSolicitud, String resumenAtencion) {
        this.idAtencion = idAtencion;
        this.fecha = fecha;
        this.canalizacion = canalizacion;
        this.estatus = estatus;
        this.estatusNotificado = estatusNotificado;
        this.idFormulario = idFormulario;
        this.idSolicitud = idSolicitud;
        this.resumenAtencion = resumenAtencion;
    }

    public Atencion(String canalizacion, String idSolicitud) {
        this.canalizacion = canalizacion;
        this.idSolicitud = idSolicitud;
    }

    public Atencion(String estatus, String estatusNotificado, String idSolicitud, String resumenAtencion) {
        this.estatus = estatus;
        this.estatusNotificado = estatusNotificado;
        this.idSolicitud = idSolicitud;
        this.resumenAtencion = resumenAtencion;
    }
    
    
    public int getIdAtencion() {
        return idAtencion;
    }

    public void setIdAtencion(int idAtencion) {
        this.idAtencion = idAtencion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCanalizacion() {
        return canalizacion;
    }

    public void setCanalizacion(String canalizacion) {
        this.canalizacion = canalizacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatusNotificado() {
        return estatusNotificado;
    }

    public void setEstatusNotificado(String estatusNotificado) {
        this.estatusNotificado = estatusNotificado;
    }

    public String getIdFormulario() {
        return idFormulario;
    }

    public void setIdFormulario(String idFormulario) {
        this.idFormulario = idFormulario;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getResumenAtencion() {
        return resumenAtencion;
    }

    public void setResumenAtencion(String resumenAtencion) {
        this.resumenAtencion = resumenAtencion;
    }
    
    
    public boolean crearAtencion(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "INSERT INTO atencion VALUES (NULL, NOW(), ?, 1, 0, NULL, ?, NULL)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, canalizacion);
            ps.setString(2, idSolicitud);
            
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos en atencion");
            return false;
        }
    }
    
    public boolean actualizar(){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "UPDATE atencion SET estatus = ?, estatusNotificado = ?, resumenAtencion = ? WHERE idSolicitud = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, estatus);
            ps.setString(2, estatusNotificado);
            ps.setString(3, resumenAtencion);
            ps.setString(4, idSolicitud);
            
            ps.executeUpdate();
            
            return true;
            
        }catch(Exception e){
            showMessageDialog(null, "Error al actualizar los datos en atencion");
            return false;
        }
    }
    
}
