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

    public CondicionesVivienda() {
    }
    
     public CondicionesVivienda(
        int id_condicionesVivienda,
        String material,
        String tejas,
        String palma,
        String carton,
        String otro,
        int numeroPlantas,
        String laCasaEs,
        String piezas,
        String cuartosDormir,
        String cuartoPropio,
        String compartidoCon,
        String patioCochera,
        int cantidadIntegrantes,
        String informacionTraslado,
        String espacioTareas,
        String enfermedades,
        String agua,
        String luz,
        String cable,
        String estufa,
        String refrigerador,
        String television,
        String lavadora,
        String computadora,
        String microondas,
        String comedor,
        String sala,
        String celular,
        String minisplit,
        String abanico,
        String piso,
        String observaciones,
        String informacionExtra
    ) {
        this.id_condicionesVivienda = id_condicionesVivienda;
        this.material = material;
        this.tejas = tejas;
        this.palma = palma;
        this.carton = carton;
        this.otro = otro;
        this.numeroPlantas = numeroPlantas;
        this.laCasaEs = laCasaEs;
        this.piezas = piezas;
        this.cuartosDormir = cuartosDormir;
        this.cuartoPropio = cuartoPropio;
        this.compartidoCon = compartidoCon;
        this.patioCochera = patioCochera;
        this.cantidadIntegrantes = cantidadIntegrantes;
        this.informacionTraslado = informacionTraslado;
        this.espacioTareas = espacioTareas;
        this.enfermedades = enfermedades;
        this.agua = agua;
        this.luz = luz;
        this.cable = cable;
        this.estufa = estufa;
        this.refrigerador = refrigerador;
        this.television = television;
        this.lavadora = lavadora;
        this.computadora = computadora;
        this.microondas = microondas;
        this.comedor = comedor;
        this.sala = sala;
        this.celular = celular;
        this.minisplit = minisplit;
        this.abanico = abanico;
        this.piso = piso;
        this.observaciones = observaciones;
        this.informacionExtra = informacionExtra;
    }

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
    
    public boolean actualizar() {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;

            String sql = "UPDATE condicionesVivienda SET material=?, tejas=?, palma=?, carton=?, otro=?, numeroPlantas=?, " +
                         "laCasaEs=?, piezas=?, cuartosDormir=?, cuartoPropio=?, compartidoCon=?, patioCochera=?, " +
                         "cantidadIntegrantes=?, informacionTraslado=?, espacioTareas=?, enfermedades=?, agua=?, luz=?, " +
                         "cable=?, estufa=?, refrigerador=?, television=?, lavadora=?, computadora=?, microondas=?, comedor=?, " +
                         "sala=?, celular=?, minisplit=?, abanico=?, piso=?, observaciones=?, informacionExtra=? " +
                         "WHERE id_condicionesVivienda=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, material);
            ps.setString(2, tejas);
            ps.setString(3, palma);
            ps.setString(4, carton);
            ps.setString(5, otro);
            ps.setInt(6, numeroPlantas);
            ps.setString(7, laCasaEs);
            ps.setString(8, piezas);
            ps.setString(9, cuartosDormir);
            ps.setString(10, cuartoPropio);
            ps.setString(11, compartidoCon);
            ps.setString(12, patioCochera);
            ps.setInt(13, cantidadIntegrantes);
            ps.setString(14, informacionTraslado);
            ps.setString(15, espacioTareas);
            ps.setString(16, enfermedades);
            ps.setString(17, agua);
            ps.setString(18, luz);
            ps.setString(19, cable);
            ps.setString(20, estufa);
            ps.setString(21, refrigerador);
            ps.setString(22, television);
            ps.setString(23, lavadora);
            ps.setString(24, computadora);
            ps.setString(25, microondas);
            ps.setString(26, comedor);
            ps.setString(27, sala);
            ps.setString(28, celular);
            ps.setString(29, minisplit);
            ps.setString(30, abanico);
            ps.setString(31, piso);
            ps.setString(32, observaciones);
            ps.setString(33, informacionExtra);
            ps.setInt(34, id_condicionesVivienda);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar condiciones de vivienda: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
