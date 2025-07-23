/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
/**
 *
 * @author Sergi
 */
public class Carrera {
    public int idCarrera;
    public String nombreCarrera, nivelCarrera, estatusCarrera;

    public Carrera(int idCarrera, String nombreCarrera, String nivelCarrera, String estatusCarrera) {
        this.idCarrera = idCarrera;
        this.nombreCarrera = nombreCarrera;
        this.nivelCarrera = nivelCarrera;
        this.estatusCarrera = estatusCarrera;
    }

    public Carrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }
    

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public void setNivelCarrera(String nivelCarrera) {
        this.nivelCarrera = nivelCarrera;
    }

    public void setEstatusCarrera(String estatusCarrera) {
        this.estatusCarrera = estatusCarrera;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public String getNivelCarrera() {
        return nivelCarrera;
    }

    public String getEstatusCarrera() {
        return estatusCarrera;
    }
    

    @Override
    public String toString() {
        return nombreCarrera.toString(); 
    }

    
 }
    

