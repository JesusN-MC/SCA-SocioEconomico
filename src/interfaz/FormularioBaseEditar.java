/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;
import clases.CondicionesVivienda;
import clases.Conexion;
import clases.DatosAlumno;
import clases.DatosFamiliaresF;
import clases.DatosFamiliaresM;
import clases.Formulario;
import clases.Gastos;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author jobno
 */
public class FormularioBaseEditar extends javax.swing.JFrame {
    JFrame regresa;
    int idFormulario;
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormularioBaseEditar.class.getName());

    /**
     * Creates new form MenuEstudiantes
     */
    public FormularioBaseEditar(JFrame pantalla, String idAtencion) {
        regresa = pantalla;
        initComponents();
        boolean existe = consultarFormulario(idAtencion);
        refrescar(idAtencion, existe);
    }
    
    public void refrescar(String id, boolean existe){
        if(existe){
            cargarDatos(id);
        }else{
            crearFormulario(id);
            cargarDatos(id);
        }
    }
    
    public boolean consultarFormulario(String idA){
        boolean existe = false;
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;
        
        String sql = "SELECT idFormulario FROM atencion WHERE idAtencion = ?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idA);
        ResultSet datos = ps.executeQuery();
        
        if(datos.next()){
            if((datos.getInt("idFormulario")) != 0){
                idFormulario = datos.getInt("idFormulario");
                existe = true;
            }else{
                System.out.println("llego aca osea es 0");
            }
            
        }
        
        datos.close();
        ps.close();
        con.close();
        
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
        return existe;
    }
    
    public void crearFormulario(String id){
        Formulario formulario =  new Formulario();
        if(formulario.crearFormulario()){
            try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT idFormulario FROM formulario WHERE idFormulario = (SELECT MAX(idFormulario) FROM formulario)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet datos = ps.executeQuery();
            if(datos.next()){
                idFormulario = datos.getInt("idFormulario");
                try{
                    //actualizar la atencion para que tenga el formulario
                    Conexion conexion2 = new Conexion();
                    Connection con2 = conexion2.con;

                    String sql2 = "UPDATE atencion SET idFormulario = ? WHERE idAtencion = ?";
                    PreparedStatement ps2 = con2.prepareStatement(sql2);
                    ps2.setInt(1, idFormulario);
                    int ID = Integer.parseInt(id);
                    ps2.setInt(2, ID);
                    ps2.executeUpdate();
                    
                    //crear las tablas de los apartados y asignarlas al formulario.
                    DatosAlumno da = new DatosAlumno();
                    if(da.crear()){
                        Formulario form = new Formulario(idFormulario, da.consultarReciente(), "1");
                        form.updateDatosAlumno();
                        System.out.println("Apartado 1 Generado Exitosamente");
                    }
                    
                    CondicionesVivienda cv = new CondicionesVivienda();
                    if(cv.crear()){
                        Formulario form2 = new Formulario("2",idFormulario, cv.consultarReciente());
                        form2.updateCondicionesVivienda();
                        System.out.println("Apartado 2 Generado Exitosamente");
                    }
                    
                    Gastos g = new Gastos();
                    if(g.crear()){
                        Formulario form3 = new Formulario(idFormulario, "3", g.consultarReciente());
                        form3.updateGastos();
                        System.out.println("Apartado 3 Generado Exitosamente");
                    }
                    
                    DatosFamiliaresF dff = new DatosFamiliaresF();
                    if(dff.crear()){
                        Formulario form4 = new Formulario(idFormulario, dff.consultarReciente(), 4);
                        form4.updateDatosFamiliaresF();
                        System.out.println("Apartado 4 Generado Exitosamente");
                    }
                    
                    DatosFamiliaresM dfm = new DatosFamiliaresM();
                    if(dfm.crear()){
                        Formulario form5 = new Formulario(idFormulario, dfm.consultarReciente());
                        form5.updateDatosFamiliaresM();
                        System.out.println("Apartado 5 Generado Exitosamente");
                    }
                    
                    
                }catch(Exception e){
                    showMessageDialog(null, "Error al asignar el Formulario");

                }
            }
            
        }catch(Exception e){
            showMessageDialog(null, "Error al consultar el id del formulario");
        }
        }
    }
    
    public void cargarDatos(String id){
        
        //Consultar todo de Formulario
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT f.*, a.nombreAlumno , a.grupo , c.nombreCarrera , s.telefonoSolicitud , s.familiar , s.telefonoFamiliar\n" +
                        "FROM formulario f\n" +
                        "INNER JOIN atencion at ON f.idFormulario = at.idFormulario\n" +
                        "INNER JOIN solicitud s ON at.idSolicitud = s.idSolicitud\n" +
                        "INNER JOIN alumno a ON s.idAlumno = a.idAlumno\n" +
                        "INNER JOIN carrera c ON a.idCarrera = c.idCarrera\n" +
                        "WHERE f.idFormulario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idFormulario);
            
            ResultSet datos = ps.executeQuery();
            if(datos.next()){
                fecha.setText(datos.getString("fecha"));
                fecha.setEditable(false);
                nombre1.setText(datos.getString("nombreAlumno"));
                nombre1.setEditable(false);
                carrera1.setText(datos.getString("nombreCarrera"));
                carrera1.setEditable(false);
                grupo1.setText(datos.getString("grupo"));
                grupo1.setEditable(false);
                tel1.setText(datos.getString("telefonoSolicitud"));
                tel1.setEditable(false);
                familiar1.setText(datos.getString("familiar"));
                familiar1.setEditable(false);
                contactoF1.setText("telefonoFamiliar");
                contactoF1.setEditable(false);
                
                apartado1(datos.getInt("id_datosAlumno"));
                apartado2(datos.getInt("id_condicionesVivienda"));
                apartadoGastosEstudiante(datos.getInt("id_gastos"));
                apartadoDatosFamiliarF(datos.getInt("id_datosFamiliaresF"));
                apartadoDatosFamiliarM(datos.getInt("id_datosFamiliaresM"));
                
                
                
            }
            
        }catch (Exception e){
            
        }
    }
    
    public void apartado1(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT * FROM datosAlumno WHERE id_datosAlumno = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                txtReligion.setText(datos.getString("religion"));
                txtEntidadAlumno.setText(datos.getString("estado"));
                txtMunicipioAlumno.setText(datos.getString("municipio"));
                txtLocalidadAlumno.setText(datos.getString("localidad"));
                txtColoniaAlumno.setText(datos.getString("colonia"));
                txtCalleAlumno.setText(datos.getString("calle"));
                txtNumCasaAlumno.setText(datos.getString("numeroCasa"));
            }
                    
        }catch(Exception e){
            System.out.println("error en el apartado 1");
        }
    }
    
    public void apartado2(int id){
        //;
        try{
            Conexion conexion2 = new Conexion();
            Connection con2 = conexion2.con;
            
            String sql2 = "SELECT * FROM condicionesVivienda WHERE id_condicionesVivienda = ?";
            PreparedStatement ps2 = con2.prepareStatement(sql2);
            ps2.setInt(1, id);
            
            ResultSet datos = ps2.executeQuery();
            
            if(datos.next()){
                //carga datos
                String material = datos.getString("material");
                if("1".equals(material)){
                    cbxMaterial.setSelected(true);
                }
                
                String tejas  = datos.getString("tejas");
                if("1".equals(tejas)){
                    cbxTejas.setSelected(true);
                }
                
                String palma = datos.getString("palma");
                if("1".equals(palma)){
                    cbxPalma.setSelected(true);
                }
                
                String carton = datos.getString("carton");
                if("1".equals(carton)){
                    cbxCarton.setSelected(true);
                }
                
                String otro = datos.getString("otro");
                if("1".equals(otro)){
                    cbxOtro.setSelected(true);
                }
                
                int plantas = datos.getInt("numeroPlantas");
                if(plantas != 0){
                    txtNumeroPlantas.setText(plantas + "");
                }
                
                String ec = datos.getString("laCasaEs");
                if(ec != null){
                    txtEstadoCasa.setText(ec);
                }
                
                String piezas = datos.getString("piezas");
                if(piezas != null){
                    txtPiezas.setText(piezas);
                }
                
                String cuartod = datos.getString("cuartosDormir");
                if(cuartod != null){
                    txtNumCuartosDormir.setText(cuartod);
                }
                
                String cuartop = datos.getString("cuartoPropio");
                if(cuartop != null){
                    txtCuartoPropio.setText(cuartop);
                }
                
                String compartido = datos.getString("compartidoCon");
                if(compartido != null){
                    txtCompartidoCon.setText(compartido);
                }
                
                String pc = datos.getString("patioCochera");
                if(pc != null){
                    txtPatioCochera.setText(pc);
                }
                
                int ni = datos.getInt("cantidadIntegrantes");
                if(ni != 0){
                    txtCantIntegrantes.setText(ni + "");
                }
                
                String tr = datos.getString("informacionTraslado");
                if(tr != null){
                    txtInfoTraslado.setText(tr);
                }
                
                String et = datos.getString("espacioTareas");
                if(et != null){
                    txtEspacioTareas.setText(et);
                }
                
                String enf = datos.getString("enfermedades");
                if(enf != null){
                    txtEnfermedades.setText(enf);
                }
                
                String agua = datos.getString("agua");
                if("1".equals(agua)){
                    cbxAguaDrenaje.setSelected(true);
                }
                
                String luz = datos.getString("luz");
                if("1".equals(luz)){
                    cbxLuz.setSelected(true);
                }
                
                String cable = datos.getString("cable");
                if("1".equals(cable)){
                    cbxTelefono.setSelected(true);
                }
                
                String estufa = datos.getString("estufa");
                if("1".equals(estufa)){
                    cbxEstufa.setSelected(true);
                }
                
                String refrigerador = datos.getString("refrigerador");
                if("1".equals(refrigerador)){
                    cbxRefrigerador.setSelected(true);
                }
                
                String television = datos.getString("television");
                if("1".equals(television)){
                    cbxTelevicion.setSelected(true);
                }
                
                String lavadora = datos.getString("lavadora");
                if("1".equals(lavadora)){
                    cbxLavadora.setSelected(true);
                }
                
                String computadora = datos.getString("computadora");
                if("1".equals(computadora)){
                    cbxComputadora.setSelected(true);
                }
                
                String microondas = datos.getString("microondas");
                if("1".equals(microondas)){
                    cbxMicroondas.setSelected(true);
                }
                
                String comedor = datos.getString("comedor");
                if("1".equals(comedor)){
                    cbxComeador.setSelected(true);
                }
                
                String sala = datos.getString("sala");
                if("1".equals(sala)){
                    cbxSala.setSelected(true);
                }
                
                String celular = datos.getString("celular");
                if("1".equals(celular)){
                    cbxCelular.setSelected(true);
                }
                
                String minisplit = datos.getString("minisplit");
                if("1".equals(minisplit)){
                    cbxMinisplit.setSelected(true);
                }
                
                String abanico = datos.getString("abanico");
                if("1".equals(abanico)){
                    cbxAbanico.setSelected(true);
                }
                
                String piso = datos.getString("piso");
                if("1".equals(piso)){
                    cbxPiso.setSelected(true);
                }
                
                String ob = datos.getString("observaciones");
                if(ob != null){
                    txtObservacionesViv.setText(ob);
                }
                
                String inf = datos.getString("informacionExtra");
                if(inf != null){
                    txtInfoExtra.setText(inf);
                }
            }
                    
        }catch(Exception e){
            System.out.println("error en el apartado 2" + e.getMessage());
        }
    }
    
    public void apartadoGastosEstudiante(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT * FROM informacionGastosEstudiante WHERE id_informacionGastosEstudiante = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                txtEstadoCivil.setText(datos.getString("estadoCivil"));
                txtNombreEspos.setText(datos.getString("nombres"));
                txtApaternoEspo.setText(datos.getString("ap"));
                txtAmaternoEspos.setText(datos.getString("am"));
                txtTrabajaEspos.setText(datos.getString("trabaja"));
                txtTiempoCasados.setText(datos.getString("tiempoCasados"));
                txtHijos.setText(datos.getString("hijos"));
                txtNumHijos.setText(datos.getString("cantidadHijos"));
                txtUstedTrabaja.setText(datos.getString("ustedTrabaja"));
                txtDonde.setText(datos.getString("donde"));
                txtHorario.setText(datos.getString("horario"));
                txtCosteaGastos.setText(datos.getString("costeaGastos"));
                txtGastosPersonales.setText(datos.getString("gastrosPersonales"));
                txtBeca.setText(datos.getString("beca"));
                txtCualBeca.setText(datos.getString("cual"));
                txtPreparatoria.setText(datos.getString("preparatoria"));
                txtLocalidadPrep.setText(datos.getString("localidad"));
                txtAñoEgreso.setText(datos.getString("añoEgreso"));
                txtTiempoLibre.setText(datos.getString("tiempoLibre"));
                txtAdicciones.setText(datos.getString("addiciones"));
                
            }
                    
        }catch(Exception e){
            System.out.println("error al Cargar Informacion de Gastos");
        }
        
    }
    
    public void apartadoDatosFamiliarF(int id){
        
    }
    //familiarM
    public void apartadoDatosFamiliarM(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            //conexion 
            String sql = "SELECT * FROM datosFamiliaresM WHERE id_datosFamiliaresM = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if (datos.next()) {
            txtNombresFamM.setText(datos.getString("nombres"));
            txtApaternoFamM.setText(datos.getString("ap"));
            txtAmaternoFamM.setText(datos.getString("am"));
            txtEdadFamM.setText(datos.getString("edad"));
            txtEscolaridadFamM.setText(datos.getString("escolaridad"));
            txtIngresoFamM.setText(datos.getString("ingresoSemanal"));
            txtEstadoFamM.setText(datos.getString("estado"));
            txtMunicipioFamM.setText(datos.getString("municipio"));
            txtLocalidadFamM.setText(datos.getString("localidad"));
            txtColoniaFamM.setText(datos.getString("colonia"));
            txtCalleFamM.setText(datos.getString("calle"));
            txtNumCasaFamM.setText(datos.getString("numeroCasa")); 
            }
        }catch(Exception e){
            System.out.println("Error al guardar los datos del padre");
        }
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        BotonCancelar = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CuerpoFormulario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtReligion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        nombre1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtLocalidadAlumno = new javax.swing.JTextField();
        grupo1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        carrera1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtNumCasaAlumno = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMunicipioAlumno = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtEntidadAlumno = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        familiar1 = new javax.swing.JTextField();
        txtCalleAlumno = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtColoniaAlumno = new javax.swing.JTextField();
        contactoF1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cbxCelular = new javax.swing.JCheckBox();
        cbxPalma = new javax.swing.JCheckBox();
        cbxOtro = new javax.swing.JCheckBox();
        cbxTejas = new javax.swing.JCheckBox();
        cbxCarton = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        tel1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtNumeroPlantas = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtTiempoCasados = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtNumCuartosDormir = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInfoExtra = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();
        cbxMaterial = new javax.swing.JCheckBox();
        cbxMinisplit = new javax.swing.JCheckBox();
        cbxAbanico = new javax.swing.JCheckBox();
        cbxAguaDrenaje = new javax.swing.JCheckBox();
        cbxLuz = new javax.swing.JCheckBox();
        cbxPiso = new javax.swing.JCheckBox();
        cbxTelefono = new javax.swing.JCheckBox();
        cbxEstufa = new javax.swing.JCheckBox();
        cbxRefrigerador = new javax.swing.JCheckBox();
        cbxTelevicion = new javax.swing.JCheckBox();
        cbxLavadora = new javax.swing.JCheckBox();
        cbxComputadora = new javax.swing.JCheckBox();
        cbxMicroondas = new javax.swing.JCheckBox();
        cbxComeador = new javax.swing.JCheckBox();
        cbxSala = new javax.swing.JCheckBox();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtEnfermedades = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObservacionesViv = new javax.swing.JTextArea();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtEspacioTareas = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtApaternoEspo = new javax.swing.JTextField();
        txtHorario = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtNombreEspos = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtAmaternoEspos = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtAñoEgreso = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtGastosPersonales = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtDonde = new javax.swing.JTextField();
        txtCosteaGastos = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtEstadoCivil = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtCualBeca = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtAdicciones = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtLocalidadPrep = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtPreparatoria = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        txtIngresoSemFamF = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        txtTiempoLibre = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtEdadFamF = new javax.swing.JTextField();
        txtMunicipioFamF = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtNumCasaFamF = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtLocalidadFamF = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txtNombresF = new javax.swing.JTextField();
        txtApaternoF = new javax.swing.JTextField();
        txtAmaternoF = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        txtEstadoFamF = new javax.swing.JTextField();
        txtColoniaFamF = new javax.swing.JTextField();
        txtCalleFamF = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtAmaternoFamM = new javax.swing.JTextField();
        txtApaternoFamM = new javax.swing.JTextField();
        txtNombresFamM = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        txtIngresoFamM = new javax.swing.JTextField();
        txtEscolaridadFamF = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtLocalidadFamM = new javax.swing.JTextField();
        txtMunicipioFamM = new javax.swing.JTextField();
        txtEstadoFamM = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        txtColoniaFamM = new javax.swing.JTextField();
        txtCalleFamM = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        txtNumCasaFamM = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        txtEdadFamM = new javax.swing.JTextField();
        txtEscolaridadFamM = new javax.swing.JTextField();
        txtNumHijos = new javax.swing.JTextField();
        txtHijos = new javax.swing.JTextField();
        txtTrabajaEspos = new javax.swing.JTextField();
        txtUstedTrabaja = new javax.swing.JTextField();
        txtBeca = new javax.swing.JTextField();
        txtCuartoPropio = new javax.swing.JTextField();
        txtCantIntegrantes = new javax.swing.JTextField();
        txtCompartidoCon = new javax.swing.JTextField();
        txtEstadoCasa = new javax.swing.JTextField();
        txtPatioCochera = new javax.swing.JTextField();
        txtPiezas = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtInfoTraslado = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(760, 559));
        getContentPane().setLayout(null);

        jPanel5.setPreferredSize(new java.awt.Dimension(720, 520));
        jPanel5.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        jButton3.setBackground(new java.awt.Color(63, 164, 218));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setText("Completar formulario");
        jButton3.setBorderPainted(false);

        BotonCancelar.setBackground(new java.awt.Color(255, 102, 102));
        BotonCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonCancelar.setText("Cancelar");
        BotonCancelar.setBorderPainted(false);

        botonGuardar.setBackground(new java.awt.Color(153, 255, 153));
        botonGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonGuardar.setText("Guardar Avance");
        botonGuardar.setBorderPainted(false);
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(BotonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BotonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.add(jPanel3);
        jPanel3.setBounds(0, 480, 760, 40);

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel4);
        jPanel4.setBounds(0, 0, 750, 40);

        jPanel2.setBackground(new java.awt.Color(170, 170, 170));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Formulario");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, -1, 20));

        jPanel5.add(jPanel2);
        jPanel2.setBounds(0, 40, 750, 23);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        CuerpoFormulario.setBackground(new java.awt.Color(255, 255, 255));
        CuerpoFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LogoUtec (New).png"))); // NOI18N
        jLabel2.setEnabled(false);
        CuerpoFormulario.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 80, 70));

        jLabel94.setText(".");
        jLabel94.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Triangulo.png"))); // NOI18N
        jLabel94.setEnabled(false);
        CuerpoFormulario.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 80, 60));

        jPanel7.setBackground(new java.awt.Color(43, 138, 127));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        CuerpoFormulario.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 720, 30));

        jPanel6.setBackground(new java.awt.Color(43, 138, 127));
        jPanel6.setMaximumSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        CuerpoFormulario.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 100, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("UNIVERSIDAD TECNOLÓGICA DE ESCUINAPA");
        CuerpoFormulario.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 370, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Padre/Esposo/Tutor");
        CuerpoFormulario.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2680, 180, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Fecha:");
        CuerpoFormulario.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 130, 70, -1));

        fecha.setBackground(new java.awt.Color(195, 210, 197));
        fecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 130, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Religion:");
        CuerpoFormulario.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 290, 70, -1));

        txtReligion.setBackground(new java.awt.Color(195, 210, 197));
        txtReligion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReligionActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtReligion, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 320, 170, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Nombre:");
        CuerpoFormulario.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 70, -1));

        nombre1.setBackground(new java.awt.Color(195, 210, 197));
        nombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombre1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(nombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 380, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Localidad:");
        CuerpoFormulario.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 370, 80, -1));

        txtLocalidadAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtLocalidadAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 400, 220, 30));

        grupo1.setBackground(new java.awt.Color(195, 210, 197));
        grupo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupo1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(grupo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 80, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Grupo:");
        CuerpoFormulario.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 70, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Carrera:");
        CuerpoFormulario.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 70, -1));

        carrera1.setBackground(new java.awt.Color(195, 210, 197));
        carrera1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carrera1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(carrera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 270, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Número de casa:");
        CuerpoFormulario.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 130, -1));

        txtNumCasaAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCasaAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCasaAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumCasaAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 130, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setText("Municipio:");
        CuerpoFormulario.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 370, 80, -1));

        txtMunicipioAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtMunicipioAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, 220, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setText("Estado:");
        CuerpoFormulario.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 70, -1));

        txtEntidadAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtEntidadAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntidadAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEntidadAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 230, 30));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setText("Contacto Familiar:");
        CuerpoFormulario.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 530, 130, -1));

        familiar1.setBackground(new java.awt.Color(195, 210, 197));
        familiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                familiar1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(familiar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 560, 230, 30));

        txtCalleAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtCalleAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCalleAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 480, 230, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setText("Colonia:");
        CuerpoFormulario.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 70, -1));

        txtColoniaAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaAlumnoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtColoniaAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 230, 30));

        contactoF1.setBackground(new java.awt.Color(195, 210, 197));
        contactoF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactoF1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(contactoF1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 560, 180, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel19.setText("¿De que material está hecho la casa?");
        CuerpoFormulario.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 660, 260, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("Familiar:");
        CuerpoFormulario.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 530, 70, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setText("Datos del alumno:");
        CuerpoFormulario.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 160, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setText("¿Se cuenta con espacio para tareas?");
        CuerpoFormulario.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1050, 250, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setText("Calle:");
        CuerpoFormulario.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 450, 70, -1));

        cbxCelular.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxCelular.setText("Celular(es)");
        cbxCelular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCelularActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1290, 200, -1));

        cbxPalma.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxPalma.setText("Palma");
        cbxPalma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPalmaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxPalma, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, 70, -1));

        cbxOtro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxOtro.setText("Otro");
        cbxOtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOtroActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxOtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 690, 70, -1));

        cbxTejas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTejas.setText("Tejas");
        cbxTejas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTejasActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxTejas, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 690, 70, -1));

        cbxCarton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxCarton.setText("Cartón");
        cbxCarton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCartonActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxCarton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 690, 70, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setText("Teléfono:");
        CuerpoFormulario.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 70, -1));

        tel1.setBackground(new java.awt.Color(195, 210, 197));
        tel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tel1ActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(tel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 230, 30));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setText("Observaciones de la vivienda:");
        CuerpoFormulario.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1370, 310, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("Número de plantas de la casa:");
        CuerpoFormulario.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, 210, -1));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("Numero de plantas de la casa:");
        CuerpoFormulario.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, 210, -1));

        txtNumeroPlantas.setBackground(new java.awt.Color(195, 210, 197));
        txtNumeroPlantas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroPlantasActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumeroPlantas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 760, 80, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setText("Esposa(o)");
        CuerpoFormulario.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 1760, 70, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setText("La casa es:");
        CuerpoFormulario.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 730, 90, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setText("Compartido con:");
        CuerpoFormulario.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 930, 120, 30));

        txtTiempoCasados.setBackground(new java.awt.Color(195, 210, 197));
        txtTiempoCasados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTiempoCasadosActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtTiempoCasados, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1880, 170, 30));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setText("¿Número de cuartos para dormir?");
        CuerpoFormulario.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 810, 250, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setText("¿De cuantas piezas de conforma la casa?");
        CuerpoFormulario.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 810, 290, -1));

        txtNumCuartosDormir.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCuartosDormir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCuartosDormirActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumCuartosDormir, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 840, 80, 30));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setText("¿Tiene cuarto propio?");
        CuerpoFormulario.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 900, 170, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel34.setText("¿Cuenta con un patio y/o cochera?");
        CuerpoFormulario.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 900, 260, -1));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel35.setText("¿En que se transladara a la universidad?");
        CuerpoFormulario.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 980, 280, 20));

        txtInfoExtra.setBackground(new java.awt.Color(195, 210, 197));
        txtInfoExtra.setColumns(20);
        txtInfoExtra.setRows(5);
        jScrollPane2.setViewportView(txtInfoExtra);

        CuerpoFormulario.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1580, 700, 100));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel36.setText("¿Enfermedad cronica y/o psicomotrices en tu familia?");
        CuerpoFormulario.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1060, 380, -1));

        cbxMaterial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMaterial.setText("Material");
        cbxMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMaterialActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, -1, -1));

        cbxMinisplit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMinisplit.setText("Minisplit");
        cbxMinisplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMinisplitActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxMinisplit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1330, 130, -1));

        cbxAbanico.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxAbanico.setText("Abanico");
        cbxAbanico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAbanicoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxAbanico, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1330, 120, -1));

        cbxAguaDrenaje.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxAguaDrenaje.setText("Agua/drenaje");
        cbxAguaDrenaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAguaDrenajeActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxAguaDrenaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1210, 130, -1));

        cbxLuz.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxLuz.setText("Luz");
        cbxLuz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLuzActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxLuz, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1210, 120, -1));

        cbxPiso.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxPiso.setText("Piso");
        cbxPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPisoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1330, 150, -1));

        cbxTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTelefono.setText("Cable/telefono");
        cbxTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTelefonoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1210, 150, -1));

        cbxEstufa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxEstufa.setText("Estufa a gas/hornilla");
        cbxEstufa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEstufaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxEstufa, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1210, 200, -1));

        cbxRefrigerador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxRefrigerador.setText("Refrigerador");
        cbxRefrigerador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRefrigeradorActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxRefrigerador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1250, 130, -1));

        cbxTelevicion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTelevicion.setText("Television");
        cbxTelevicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTelevicionActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxTelevicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1250, 120, -1));

        cbxLavadora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxLavadora.setText("Lavadora");
        cbxLavadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLavadoraActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxLavadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1250, 150, -1));

        cbxComputadora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxComputadora.setText("Computadora");
        cbxComputadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxComputadoraActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxComputadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1250, 200, -1));

        cbxMicroondas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMicroondas.setText("Microondas");
        cbxMicroondas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMicroondasActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxMicroondas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1290, 130, -1));

        cbxComeador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxComeador.setText("Comedor");
        cbxComeador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxComeadorActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxComeador, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1290, 120, -1));

        cbxSala.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxSala.setText("Sala");
        cbxSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSalaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(cbxSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1290, 150, -1));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel37.setText("Número de integrantes que viven en la casa:");
        CuerpoFormulario.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 980, 310, -1));

        txtEnfermedades.setBackground(new java.awt.Color(195, 210, 197));
        txtEnfermedades.setColumns(20);
        txtEnfermedades.setRows(5);
        jScrollPane3.setViewportView(txtEnfermedades);

        CuerpoFormulario.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1090, 360, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Condiciones de la vivienda");
        CuerpoFormulario.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 620, 230, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel38.setText("¿Con que servicio y equipamiento cuenta la vivienda?");
        CuerpoFormulario.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1180, 400, -1));

        txtObservacionesViv.setBackground(new java.awt.Color(195, 210, 197));
        txtObservacionesViv.setColumns(20);
        txtObservacionesViv.setRows(5);
        jScrollPane4.setViewportView(txtObservacionesViv);

        CuerpoFormulario.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1400, 700, 100));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel39.setText("Información Extra");
        CuerpoFormulario.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1510, 160, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setText("¿Donde?");
        CuerpoFormulario.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1930, 70, -1));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setText("Horario:");
        CuerpoFormulario.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1930, 70, -1));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel42.setText("(En caso de que el alumno tenga pareja)");
        CuerpoFormulario.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1740, 290, -1));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setText("5.-¿Qué gastos personales pagas?");
        CuerpoFormulario.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2010, 300, -1));

        txtEspacioTareas.setBackground(new java.awt.Color(195, 210, 197));
        txtEspacioTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEspacioTareasActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEspacioTareas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1080, 80, 30));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel44.setText("Cuantos:");
        CuerpoFormulario.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 1880, -1, 30));

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel45.setText("Apellido Paterno:");
        CuerpoFormulario.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1780, 130, -1));

        txtApaternoEspo.setBackground(new java.awt.Color(195, 210, 197));
        txtApaternoEspo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApaternoEspoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtApaternoEspo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1810, 160, 30));

        txtHorario.setBackground(new java.awt.Color(195, 210, 197));
        txtHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHorarioActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1960, 240, 30));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel46.setText("Nombres:");
        CuerpoFormulario.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 1780, 100, -1));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setText("1.-¿Estado civil?");
        CuerpoFormulario.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1780, 120, -1));

        txtNombreEspos.setBackground(new java.awt.Color(195, 210, 197));
        txtNombreEspos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEsposActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNombreEspos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 1810, 180, 30));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setText("¿Hay alguna situación que está afectando tu desempeño academico?");
        CuerpoFormulario.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1550, 480, -1));

        txtAmaternoEspos.setBackground(new java.awt.Color(195, 210, 197));
        txtAmaternoEspos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmaternoEsposActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtAmaternoEspos, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1810, 170, 30));

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Apellido Materno:");
        CuerpoFormulario.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1780, 130, -1));

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("2.-¿Tiene hijos?");
        CuerpoFormulario.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1850, 110, -1));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("¿Trabaja?");
        CuerpoFormulario.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 1850, 70, -1));

        txtAñoEgreso.setBackground(new java.awt.Color(195, 210, 197));
        txtAñoEgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAñoEgresoActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtAñoEgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2120, 110, 30));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setText("Tiempo de casados:");
        CuerpoFormulario.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1850, 140, -1));

        txtGastosPersonales.setBackground(new java.awt.Color(195, 210, 197));
        txtGastosPersonales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGastosPersonalesActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtGastosPersonales, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2040, 310, 30));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setText("3.-¿Usted trabaja?");
        CuerpoFormulario.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1930, 130, 20));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel54.setText("Año que egresó:");
        CuerpoFormulario.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2090, 120, -1));

        txtDonde.setBackground(new java.awt.Color(195, 210, 197));
        txtDonde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDondeActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtDonde, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1960, 270, 30));

        txtCosteaGastos.setBackground(new java.awt.Color(195, 210, 197));
        txtCosteaGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCosteaGastosActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCosteaGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2040, 280, 30));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("4.-¿Quién costea los gastos del estudiante?");
        CuerpoFormulario.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2010, 300, 20));

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setText("7.-Preparatoria Cursada:");
        CuerpoFormulario.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2090, 200, 20));

        txtEstadoCivil.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoCivil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoCivilActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEstadoCivil, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1810, 150, 30));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel57.setText("9.-¿fumas, bebes alchol o alguna otra addición?");
        jLabel57.setToolTipText("");
        CuerpoFormulario.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 2170, 340, 20));

        txtCualBeca.setBackground(new java.awt.Color(195, 210, 197));
        txtCualBeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCualBecaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCualBeca, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 2120, 110, 30));

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("¿Cuál?");
        CuerpoFormulario.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 2120, 50, 30));

        txtAdicciones.setBackground(new java.awt.Color(195, 210, 197));
        txtAdicciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdiccionesActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtAdicciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 2200, 330, 30));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel59.setText("Apellido Materno:");
        CuerpoFormulario.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2330, 130, -1));

        txtLocalidadPrep.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadPrep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadPrepActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtLocalidadPrep, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2120, 140, 30));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setText("6.-¿Cuenta con alguna beca?");
        CuerpoFormulario.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2090, 200, 20));

        txtPreparatoria.setBackground(new java.awt.Color(195, 210, 197));
        txtPreparatoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPreparatoriaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtPreparatoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2120, 170, 30));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel61.setText("8.-¿Dónde pasas tu tiempo libre?");
        CuerpoFormulario.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2170, 230, 20));

        txtIngresoSemFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtIngresoSemFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIngresoSemFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtIngresoSemFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2430, 220, 30));

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel62.setText("Informacion sobre gastos del estudiante");
        CuerpoFormulario.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 1700, 350, -1));

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel63.setText("Datos Familiares");
        CuerpoFormulario.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 2260, 150, -1));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setText("Localidad:");
        CuerpoFormulario.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2090, 80, -1));

        txtTiempoLibre.setBackground(new java.awt.Color(195, 210, 197));
        txtTiempoLibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTiempoLibreActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtTiempoLibre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2200, 340, 30));

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel65.setText("Colonia:");
        CuerpoFormulario.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2590, 60, -1));

        txtEdadFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtEdadFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdadFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEdadFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2430, 80, 30));

        txtMunicipioFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtMunicipioFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2540, 220, 30));

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setText("Ingreso semanal:");
        CuerpoFormulario.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2400, 120, -1));

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel67.setText("Nombres:");
        CuerpoFormulario.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2330, 80, -1));

        txtNumCasaFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCasaFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCasaFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumCasaFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2620, 160, 30));

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel68.setText("Número de casa:");
        CuerpoFormulario.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2590, 120, -1));

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel69.setText("Escolaridad:");
        CuerpoFormulario.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2400, 120, -1));

        txtLocalidadFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtLocalidadFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2540, 220, 30));

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel70.setText("Edad:");
        CuerpoFormulario.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2400, 50, -1));

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel71.setText("Domicilio:");
        CuerpoFormulario.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2480, 80, -1));

        txtNombresF.setBackground(new java.awt.Color(195, 210, 197));
        txtNombresF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNombresF, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2360, 220, 30));

        txtApaternoF.setBackground(new java.awt.Color(195, 210, 197));
        txtApaternoF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApaternoFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtApaternoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2360, 220, 30));

        txtAmaternoF.setBackground(new java.awt.Color(195, 210, 197));
        txtAmaternoF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmaternoFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtAmaternoF, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2360, 220, 30));

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel72.setText("Apellido Paterno:");
        CuerpoFormulario.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2330, 120, -1));

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel73.setText("Calle:");
        CuerpoFormulario.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2590, 120, -1));

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel74.setText("Estado:");
        CuerpoFormulario.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2510, 60, -1));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel75.setText("Municipio:");
        CuerpoFormulario.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2510, 120, -1));

        jLabel76.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel76.setText("Localidad:");
        CuerpoFormulario.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2510, 120, -1));

        txtEstadoFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEstadoFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2540, 220, 30));

        txtColoniaFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtColoniaFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2620, 240, 30));

        txtCalleFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtCalleFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCalleFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2620, 250, 30));

        jLabel77.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel77.setText("Madre/Esposa/Tutora");
        CuerpoFormulario.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2290, 190, -1));

        jLabel78.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel78.setText("Nombres:");
        CuerpoFormulario.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2720, 80, -1));

        jLabel79.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel79.setText("Apellido Paterno:");
        CuerpoFormulario.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2720, 130, -1));

        jLabel80.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel80.setText("Apellido Materno:");
        CuerpoFormulario.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2720, 130, -1));

        txtAmaternoFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtAmaternoFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmaternoFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtAmaternoFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2750, 220, 30));

        txtApaternoFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtApaternoFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApaternoFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtApaternoFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2750, 220, 30));

        txtNombresFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtNombresFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNombresFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2750, 220, 30));

        jLabel81.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel81.setText("Edad:");
        CuerpoFormulario.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2790, 50, -1));

        jLabel82.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel82.setText("Escolaridad:");
        CuerpoFormulario.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2790, 120, -1));

        jLabel83.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel83.setText("Ingreso semanal:");
        CuerpoFormulario.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2790, 120, -1));

        txtIngresoFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtIngresoFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIngresoFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtIngresoFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2820, 220, 30));

        txtEscolaridadFamF.setBackground(new java.awt.Color(195, 210, 197));
        txtEscolaridadFamF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEscolaridadFamFActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEscolaridadFamF, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2430, 270, 30));

        jLabel84.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel84.setText("Domicilio:");
        CuerpoFormulario.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2870, 80, -1));

        jLabel85.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel85.setText("Estado:");
        CuerpoFormulario.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2900, 60, -1));

        jLabel86.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel86.setText("Municipio:");
        CuerpoFormulario.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2900, 120, -1));

        jLabel87.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel87.setText("Localidad:");
        CuerpoFormulario.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2900, 120, -1));

        txtLocalidadFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtLocalidadFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2930, 220, 30));

        txtMunicipioFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtMunicipioFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2930, 220, 30));

        txtEstadoFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEstadoFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2930, 220, 30));

        jLabel88.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel88.setText("Colonia:");
        CuerpoFormulario.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2980, 60, -1));

        txtColoniaFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtColoniaFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 3010, 240, 30));

        txtCalleFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtCalleFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCalleFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 3010, 250, 30));

        jLabel89.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel89.setText("Calle:");
        CuerpoFormulario.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2980, 120, -1));

        txtNumCasaFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCasaFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCasaFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumCasaFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 3010, 160, 30));

        jLabel90.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel90.setText("Número de casa:");
        CuerpoFormulario.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2980, 120, -1));

        jLabel95.setText("jLabel95");
        jLabel95.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/UTyP.png"))); // NOI18N
        jLabel95.setEnabled(false);
        CuerpoFormulario.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 50, 30));

        jLabel91.setText("jLabel91");
        jLabel91.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo sinaloa.png"))); // NOI18N
        jLabel91.setEnabled(false);
        CuerpoFormulario.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 16, 70, 20));

        jLabel92.setText("jLabel92");
        jLabel92.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo sep.png"))); // NOI18N
        jLabel92.setEnabled(false);
        CuerpoFormulario.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 80, -1));

        txtEdadFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtEdadFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdadFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEdadFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2820, 80, 30));

        txtEscolaridadFamM.setBackground(new java.awt.Color(195, 210, 197));
        txtEscolaridadFamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEscolaridadFamMActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEscolaridadFamM, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2820, 270, 30));

        txtNumHijos.setBackground(new java.awt.Color(195, 210, 197));
        txtNumHijos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumHijosActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtNumHijos, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1880, 60, 30));

        txtHijos.setBackground(new java.awt.Color(195, 210, 197));
        txtHijos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHijosActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtHijos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1880, 60, 30));

        txtTrabajaEspos.setBackground(new java.awt.Color(195, 210, 197));
        txtTrabajaEspos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrabajaEsposActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtTrabajaEspos, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 1880, 60, 30));

        txtUstedTrabaja.setBackground(new java.awt.Color(195, 210, 197));
        txtUstedTrabaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUstedTrabajaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtUstedTrabaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1960, 60, 30));

        txtBeca.setBackground(new java.awt.Color(195, 210, 197));
        txtBeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBecaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtBeca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2120, 60, 30));

        txtCuartoPropio.setBackground(new java.awt.Color(195, 210, 197));
        txtCuartoPropio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCuartoPropioActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCuartoPropio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 930, 80, 30));

        txtCantIntegrantes.setBackground(new java.awt.Color(195, 210, 197));
        txtCantIntegrantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantIntegrantesActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCantIntegrantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1010, 80, 30));

        txtCompartidoCon.setBackground(new java.awt.Color(195, 210, 197));
        txtCompartidoCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCompartidoConActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtCompartidoCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 930, 120, 30));

        txtEstadoCasa.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoCasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoCasaActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtEstadoCasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 760, 180, 30));

        txtPatioCochera.setBackground(new java.awt.Color(195, 210, 197));
        txtPatioCochera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPatioCocheraActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtPatioCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 930, 180, 30));

        txtPiezas.setBackground(new java.awt.Color(195, 210, 197));
        txtPiezas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPiezasActionPerformed(evt);
            }
        });
        CuerpoFormulario.add(txtPiezas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 840, 80, 30));

        txtInfoTraslado.setBackground(new java.awt.Color(195, 210, 197));
        txtInfoTraslado.setColumns(20);
        txtInfoTraslado.setRows(5);
        jScrollPane5.setViewportView(txtInfoTraslado);

        CuerpoFormulario.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 1000, 300, 60));

        jScrollPane1.setViewportView(CuerpoFormulario);

        jPanel5.add(jScrollPane1);
        jScrollPane1.setBounds(0, 60, 750, 420);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(0, 0, 750, 520);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPiezasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPiezasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPiezasActionPerformed

    private void txtPatioCocheraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPatioCocheraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPatioCocheraActionPerformed

    private void txtEstadoCasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoCasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoCasaActionPerformed

    private void txtCompartidoConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCompartidoConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompartidoConActionPerformed

    private void txtCantIntegrantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantIntegrantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantIntegrantesActionPerformed

    private void txtCuartoPropioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCuartoPropioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCuartoPropioActionPerformed

    private void txtBecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBecaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBecaActionPerformed

    private void txtUstedTrabajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUstedTrabajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUstedTrabajaActionPerformed

    private void txtTrabajaEsposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrabajaEsposActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrabajaEsposActionPerformed

    private void txtHijosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHijosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHijosActionPerformed

    private void txtNumHijosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumHijosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumHijosActionPerformed

    private void txtEscolaridadFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEscolaridadFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEscolaridadFamMActionPerformed

    private void txtEdadFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdadFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadFamMActionPerformed

    private void txtNumCasaFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCasaFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumCasaFamMActionPerformed

    private void txtCalleFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalleFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalleFamMActionPerformed

    private void txtColoniaFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaFamMActionPerformed

    private void txtEstadoFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoFamMActionPerformed

    private void txtMunicipioFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioFamMActionPerformed

    private void txtLocalidadFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadFamMActionPerformed

    private void txtEscolaridadFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEscolaridadFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEscolaridadFamFActionPerformed

    private void txtIngresoFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresoFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIngresoFamMActionPerformed

    private void txtNombresFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresFamMActionPerformed

    private void txtApaternoFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApaternoFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApaternoFamMActionPerformed

    private void txtAmaternoFamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmaternoFamMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmaternoFamMActionPerformed

    private void txtCalleFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalleFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalleFamFActionPerformed

    private void txtColoniaFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaFamFActionPerformed

    private void txtEstadoFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoFamFActionPerformed

    private void txtAmaternoFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmaternoFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmaternoFActionPerformed

    private void txtApaternoFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApaternoFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApaternoFActionPerformed

    private void txtNombresFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresFActionPerformed

    private void txtLocalidadFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadFamFActionPerformed

    private void txtNumCasaFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCasaFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumCasaFamFActionPerformed

    private void txtMunicipioFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioFamFActionPerformed

    private void txtEdadFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdadFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadFamFActionPerformed

    private void txtTiempoLibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTiempoLibreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTiempoLibreActionPerformed

    private void txtIngresoSemFamFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresoSemFamFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIngresoSemFamFActionPerformed

    private void txtPreparatoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPreparatoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreparatoriaActionPerformed

    private void txtLocalidadPrepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadPrepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadPrepActionPerformed

    private void txtAdiccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdiccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdiccionesActionPerformed

    private void txtCualBecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCualBecaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCualBecaActionPerformed

    private void txtEstadoCivilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoCivilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoCivilActionPerformed

    private void txtCosteaGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCosteaGastosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCosteaGastosActionPerformed

    private void txtDondeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDondeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDondeActionPerformed

    private void txtGastosPersonalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGastosPersonalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGastosPersonalesActionPerformed

    private void txtAñoEgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAñoEgresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAñoEgresoActionPerformed

    private void txtAmaternoEsposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmaternoEsposActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmaternoEsposActionPerformed

    private void txtNombreEsposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEsposActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEsposActionPerformed

    private void txtHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorarioActionPerformed

    private void txtApaternoEspoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApaternoEspoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApaternoEspoActionPerformed

    private void txtEspacioTareasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEspacioTareasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEspacioTareasActionPerformed

    private void cbxSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxSalaActionPerformed

    private void cbxComeadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxComeadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxComeadorActionPerformed

    private void cbxMicroondasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMicroondasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMicroondasActionPerformed

    private void cbxComputadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxComputadoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxComputadoraActionPerformed

    private void cbxLavadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLavadoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLavadoraActionPerformed

    private void cbxTelevicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTelevicionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTelevicionActionPerformed

    private void cbxRefrigeradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRefrigeradorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxRefrigeradorActionPerformed

    private void cbxEstufaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEstufaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxEstufaActionPerformed

    private void cbxTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTelefonoActionPerformed

    private void cbxPisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPisoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPisoActionPerformed

    private void cbxLuzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLuzActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLuzActionPerformed

    private void cbxAguaDrenajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxAguaDrenajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAguaDrenajeActionPerformed

    private void cbxAbanicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxAbanicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAbanicoActionPerformed

    private void cbxMinisplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMinisplitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMinisplitActionPerformed

    private void cbxMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMaterialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMaterialActionPerformed

    private void txtNumCuartosDormirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCuartosDormirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumCuartosDormirActionPerformed

    private void txtTiempoCasadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTiempoCasadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTiempoCasadosActionPerformed

    private void txtNumeroPlantasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroPlantasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroPlantasActionPerformed


    private void tel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tel1ActionPerformed


    private void cbxCartonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCartonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCartonActionPerformed

    private void cbxTejasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTejasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTejasActionPerformed

    private void cbxOtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOtroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOtroActionPerformed

    private void cbxPalmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPalmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPalmaActionPerformed

    private void cbxCelularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCelularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCelularActionPerformed

    private void contactoF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactoF1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactoF1ActionPerformed

    private void txtColoniaAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaAlumnoActionPerformed

    private void txtCalleAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalleAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalleAlumnoActionPerformed


    private void familiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_familiar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_familiar1ActionPerformed


    private void txtEntidadAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntidadAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntidadAlumnoActionPerformed

    private void txtMunicipioAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioAlumnoActionPerformed

    private void txtNumCasaAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCasaAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumCasaAlumnoActionPerformed

    private void carrera1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carrera1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_carrera1ActionPerformed

    private void grupo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grupo1ActionPerformed

    private void txtLocalidadAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadAlumnoActionPerformed

    private void nombre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombre1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombre1ActionPerformed

    private void txtReligionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReligionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReligionActionPerformed

    private void fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaActionPerformed

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonGuardarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());    
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //java.awt.EventQueue.invokeLater(() -> new FormularioBaseEditar().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonCancelar;
    private javax.swing.JPanel CuerpoFormulario;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JTextField carrera1;
    private javax.swing.JCheckBox cbxAbanico;
    private javax.swing.JCheckBox cbxAguaDrenaje;
    private javax.swing.JCheckBox cbxCarton;
    private javax.swing.JCheckBox cbxCelular;
    private javax.swing.JCheckBox cbxComeador;
    private javax.swing.JCheckBox cbxComputadora;
    private javax.swing.JCheckBox cbxEstufa;
    private javax.swing.JCheckBox cbxLavadora;
    private javax.swing.JCheckBox cbxLuz;
    private javax.swing.JCheckBox cbxMaterial;
    private javax.swing.JCheckBox cbxMicroondas;
    private javax.swing.JCheckBox cbxMinisplit;
    private javax.swing.JCheckBox cbxOtro;
    private javax.swing.JCheckBox cbxPalma;
    private javax.swing.JCheckBox cbxPiso;
    private javax.swing.JCheckBox cbxRefrigerador;
    private javax.swing.JCheckBox cbxSala;
    private javax.swing.JCheckBox cbxTejas;
    private javax.swing.JCheckBox cbxTelefono;
    private javax.swing.JCheckBox cbxTelevicion;
    private javax.swing.JTextField contactoF1;
    private javax.swing.JTextField familiar1;
    private javax.swing.JTextField fecha;
    private javax.swing.JTextField grupo1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField nombre1;
    private javax.swing.JTextField tel1;
    private javax.swing.JTextField txtAdicciones;
    private javax.swing.JTextField txtAmaternoEspos;
    private javax.swing.JTextField txtAmaternoF;
    private javax.swing.JTextField txtAmaternoFamM;
    private javax.swing.JTextField txtApaternoEspo;
    private javax.swing.JTextField txtApaternoF;
    private javax.swing.JTextField txtApaternoFamM;
    private javax.swing.JTextField txtAñoEgreso;
    private javax.swing.JTextField txtBeca;
    private javax.swing.JTextField txtCalleAlumno;
    private javax.swing.JTextField txtCalleFamF;
    private javax.swing.JTextField txtCalleFamM;
    private javax.swing.JTextField txtCantIntegrantes;
    private javax.swing.JTextField txtColoniaAlumno;
    private javax.swing.JTextField txtColoniaFamF;
    private javax.swing.JTextField txtColoniaFamM;
    private javax.swing.JTextField txtCompartidoCon;
    private javax.swing.JTextField txtCosteaGastos;
    private javax.swing.JTextField txtCualBeca;
    private javax.swing.JTextField txtCuartoPropio;
    private javax.swing.JTextField txtDonde;
    private javax.swing.JTextField txtEdadFamF;
    private javax.swing.JTextField txtEdadFamM;
    private javax.swing.JTextArea txtEnfermedades;
    private javax.swing.JTextField txtEntidadAlumno;
    private javax.swing.JTextField txtEscolaridadFamF;
    private javax.swing.JTextField txtEscolaridadFamM;
    private javax.swing.JTextField txtEspacioTareas;
    private javax.swing.JTextField txtEstadoCasa;
    private javax.swing.JTextField txtEstadoCivil;
    private javax.swing.JTextField txtEstadoFamF;
    private javax.swing.JTextField txtEstadoFamM;
    private javax.swing.JTextField txtGastosPersonales;
    private javax.swing.JTextField txtHijos;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextArea txtInfoExtra;
    private javax.swing.JTextArea txtInfoTraslado;
    private javax.swing.JTextField txtIngresoFamM;
    private javax.swing.JTextField txtIngresoSemFamF;
    private javax.swing.JTextField txtLocalidadAlumno;
    private javax.swing.JTextField txtLocalidadFamF;
    private javax.swing.JTextField txtLocalidadFamM;
    private javax.swing.JTextField txtLocalidadPrep;
    private javax.swing.JTextField txtMunicipioAlumno;
    private javax.swing.JTextField txtMunicipioFamF;
    private javax.swing.JTextField txtMunicipioFamM;
    private javax.swing.JTextField txtNombreEspos;
    private javax.swing.JTextField txtNombresF;
    private javax.swing.JTextField txtNombresFamM;
    private javax.swing.JTextField txtNumCasaAlumno;
    private javax.swing.JTextField txtNumCasaFamF;
    private javax.swing.JTextField txtNumCasaFamM;
    private javax.swing.JTextField txtNumCuartosDormir;
    private javax.swing.JTextField txtNumHijos;
    private javax.swing.JTextField txtNumeroPlantas;
    private javax.swing.JTextArea txtObservacionesViv;
    private javax.swing.JTextField txtPatioCochera;
    private javax.swing.JTextField txtPiezas;
    private javax.swing.JTextField txtPreparatoria;
    private javax.swing.JTextField txtReligion;
    private javax.swing.JTextField txtTiempoCasados;
    private javax.swing.JTextField txtTiempoLibre;
    private javax.swing.JTextField txtTrabajaEspos;
    private javax.swing.JTextField txtUstedTrabaja;
    // End of variables declaration//GEN-END:variables
}
