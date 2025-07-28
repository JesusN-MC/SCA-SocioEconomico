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
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author jobno
 */
public class VerFormularioBaseEditar extends javax.swing.JFrame {
    JFrame regresa;
    String idAt;
    int idFormulario;
    int id_datosAlumno,
        id_condicionesVivienda,
        id_gastos,
        id_datosFamiliaresF,
        id_datosFamiliaresM;
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VerFormularioBaseEditar.class.getName());

    /**
     * Creates new form MenuEstudiantes
     */
    public VerFormularioBaseEditar(JFrame pantalla, String idAtencion) {
        idAt = idAtencion;
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
                contactoF1.setText(datos.getString("telefonoFamiliar"));
                contactoF1.setEditable(false);
                

                
                apartado1(datos.getInt("id_datosAlumno"));
                id_datosAlumno = datos.getInt("id_datosAlumno");
                
                apartado2(datos.getInt("id_condicionesVivienda"));
                id_condicionesVivienda = datos.getInt("id_condicionesVivienda");
                
                apartado3(datos.getInt("id_gastos"));
                id_gastos = datos.getInt("id_gastos");
                
                apartado4(datos.getInt("id_datosFamiliaresF"));
                id_datosFamiliaresF = datos.getInt("id_datosFamiliaresF");
                
                apartado5(datos.getInt("id_datosFamiliaresM"));
                id_datosFamiliaresM = datos.getInt("id_datosFamiliaresM");
                
                
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
                    txtNumPlantas.setText(plantas + "");
                }
                
                String ec = datos.getString("laCasaEs");
                if(ec != null){
                    txtEstadoCasa.setText(ec);
                }
                
                String piezas = datos.getString("piezas");
                if(piezas != null){
                    txtNumPiezasCasa.setText(piezas);
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
                    txtNumIntegrantes.setText(ni + "");
                }
                
                String tr = datos.getString("informacionTraslado");
                if(tr != null){
                    txtTraslado.setText(tr);
                }
                
                String et = datos.getString("espacioTareas");
                if(et != null){
                    txtEspacioTareas.setText(et);
                }
                
                String enf = datos.getString("enfermedades");
                if(enf != null){
                    txtEnfermedad.setText(enf);
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
    
    public void apartado3(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT * FROM informacionGastosEstudiante WHERE id_gastos = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                String estadoCivil = datos.getString("estadoCivil");
                if(estadoCivil != null) {
                    txtEstadoCV.setText(estadoCivil);
                }

                String nombres = datos.getString("nombres");
                if(nombres != null) {
                    txtNombreEspose.setText(nombres);
                }

                String ap = datos.getString("ap");
                if(ap != null) {
                    txtApEspose.setText(ap);
                }

                String am = datos.getString("am");
                if(am != null) {
                    txtAmEspose.setText(am);
                }

                String hijos = datos.getString("hijos");
                if(hijos != null) {
                    txtHijos.setText(hijos);
                }

                int cantidadHijos = datos.getInt("cantidadHijos");
                if(cantidadHijos != 0) {
                    txtNumHijos.setText(cantidadHijos + "");
                }

                String trabaja = datos.getString("trabaja");
                if(trabaja != null) {
                    txtTrabajaEspose.setText(trabaja);
                }

                String tiempoCasados = datos.getString("tiempoCasados");
                if(tiempoCasados != null) {
                    txtTiempoCasados.setText(tiempoCasados);
                }

                String ustedTrabaja = datos.getString("ustedTrabaja");
                if(ustedTrabaja != null) {
                    txtAlumnoTrabaja.setText(ustedTrabaja);
                }

                String donde = datos.getString("donde");
                if(donde != null) {
                    txtLugarTrabajoAl.setText(donde);
                }

                String horario = datos.getString("horario");
                if(horario != null) {
                    txtHorarioTrabajoAl.setText(horario);
                }

                String costeaGastos = datos.getString("costeaGastos");
                if(costeaGastos != null) {
                    txtCosteaGastos.setText(costeaGastos);
                }

                String gastosPersonales = datos.getString("gastosPersonales");
                if(gastosPersonales != null) {
                    txtGastosPersonales.setText(gastosPersonales);
                }

                String beca = datos.getString("beca");
                if(beca != null) {
                    txtBeca.setText(beca);
                }

                String cual = datos.getString("cual");
                if(cual != null) {
                    txtCualBeca.setText(cual);
                }

                String preparatoria = datos.getString("preparatoria");
                if(preparatoria != null) {
                    txtPrepaCursada.setText(preparatoria);
                }

                String localidad = datos.getString("localidad");
                if(localidad != null) {
                    txtLocalidadPrep.setText(localidad);
                }

                String añoEgreso = datos.getString("añoEgreso");
                if(añoEgreso != null) {
                    txtAñoEgreso.setText(añoEgreso);
                }

                String tiempoLibre = datos.getString("tiempoLibre");
                if(tiempoLibre != null) {
                    txtTiempoLibre.setText(tiempoLibre);
                }

                String addicciones = datos.getString("addiciones");
                if(addicciones != null) {
                    txtAdicciones.setText(addicciones);
                }
            }
                    
        }catch(Exception e){
            System.out.println("error en el apartado 3");
        }
    }
    
    public void apartado4(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT * FROM datosFamiliaresF WHERE id_datosFamiliaresF = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                String nombres = datos.getString("nombres");
                if(nombres != null) {
                    txtNombresMET.setText(nombres);
                }

                String ap = datos.getString("ap");
                if(ap != null) {
                    txtApMET.setText(ap);
                }

                String am = datos.getString("am");
                if(am != null) {
                    txtAmMET.setText(am);
                }

                int edad = datos.getInt("edad");
                if(edad != 0) {
                    txtEdadMET.setText(edad + "");
                }

                String escolaridad = datos.getString("escolaridad");
                if(escolaridad != null) {
                    txtEscolaridadMET.setText(escolaridad);
                }

                int ingresoSemanal = datos.getInt("ingresoSemanal");
                if(ingresoSemanal != 0) {
                    txtIngresoSemanalMET.setText(ingresoSemanal + "");
                }

                String estado = datos.getString("estado");
                if(estado != null) {
                    txtEntidadMET.setText(estado);
                }

                String municipio = datos.getString("municipio");
                if(municipio != null) {
                    txtMunicipioMET.setText(municipio);
                }

                String localidad = datos.getString("localidad");
                if(localidad != null) {
                    txtLocalidadMET.setText(localidad);
                }

                String colonia = datos.getString("colonia");
                if(colonia != null) {
                    txtColoniaMET.setText(colonia);
                }

                String calle = datos.getString("calle");
                if(calle != null) {
                    txtCalleMET.setText(calle);
                }

                String numeroCasa = datos.getString("numeroCasa");
                if(numeroCasa != null) {
                    txtNUmCasaMET.setText(numeroCasa);
                }
            }
                    
        }catch(Exception e){
            System.out.println("error en el apartado 4");
        }
    }
    
    public void apartado5(int id){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            
            String sql = "SELECT * FROM datosFamiliaresm WHERE id_datosFamiliaresm = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                String nombres = datos.getString("nombres");
                if(nombres != null) {
                    txtNombresPET.setText(nombres);
                }

                String ap = datos.getString("ap");
                if(ap != null) {
                    txtApPET.setText(ap);
                }

                String am = datos.getString("am");
                if(am != null) {
                    txtAmPET.setText(am);
                }

                int edad = datos.getInt("edad");
                if(edad != 0) {
                    txtEdadPET.setText(edad +"");
                }

                String escolaridad = datos.getString("escolaridad");
                if(escolaridad != null) {
                    txtEscolaridadPET.setText(escolaridad);
                }

                int ingresoSemanal = datos.getInt("ingresoSemanal");
                if(ingresoSemanal != 0) {
                    txtIngresoPET.setText(ingresoSemanal + "");
                }

                String estado = datos.getString("estado");
                if(estado != null) {
                    txtEntidadPET.setText(estado);
                }

                String municipio = datos.getString("municipio");
                if(municipio != null) {
                    txtMunicipioPET.setText(municipio);
                }

                String localidad = datos.getString("localidad");
                if(localidad != null) {
                    txtLocalidadPET.setText(localidad);
                }

                String colonia = datos.getString("colonia");
                if(colonia != null) {
                    txtColoniaPET.setText(colonia);
                }

                String calle = datos.getString("calle");
                if(calle != null) {
                    txtCallePET.setText(calle);
                }

                String numeroCasa = datos.getString("numeroCasa");
                if(numeroCasa != null) {
                    txtNumCasaPET.setText(numeroCasa);
                }
            }
                    
        }catch(Exception e){
            System.out.println("error en el apartado 4");
        }
    }
    
    public void capturaDatos(){
        capturaApartado1();
        capturaApartado2();
        capturaApartado3();
        capturaApartado4();
        capturaApartado5();
        
    }
    
    public void capturaApartado5(){
        String valorEdad = txtEdadPET.getText();
        int edad;
        if(valorEdad.equals("")){
            edad = 0;
        }else{
            edad = Integer.parseInt(txtEdadPET.getText());
        }
        
        String valorIngreso = txtIngresoPET.getText();
        int ingresoSemanal;
        if(valorIngreso.equals("")){
            ingresoSemanal = 0;
        }else{
            ingresoSemanal = Integer.parseInt(txtIngresoPET.getText());
        }
        

        DatosFamiliaresM m = new DatosFamiliaresM(
            id_datosFamiliaresM,
            txtNombresPET.getText(),
            txtApPET.getText(),
            txtAmPET.getText(),
            edad,
            txtEscolaridadPET.getText(),
            ingresoSemanal,
            txtEntidadPET.getText(),
            txtMunicipioPET.getText(),
            txtLocalidadPET.getText(),
            txtColoniaPET.getText(),
            txtCallePET.getText(),
            txtNumCasaPET.getText(),
            null,
            null,
            "estaticos"
        );

        if (m.actualizar()) {
            System.out.println("apartado 5 actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el registro.");
        }
    }
    
    public void capturaApartado4(){
        String valorEdad = txtEdadPET.getText();
        int edad;
        if(valorEdad.equals("")){
            edad = 0;
        }else{
            edad = Integer.parseInt(txtEdadPET.getText());
        }
        
        String valorIngreso = txtIngresoPET.getText();
        int ingresoSemanal;
        if(valorIngreso.equals("")){
            ingresoSemanal = 0;
        }else{
            ingresoSemanal = Integer.parseInt(txtIngresoPET.getText());
        }    
        
            DatosFamiliaresF padre = new DatosFamiliaresF(
            id_datosFamiliaresF,
            txtNombresMET.getText().trim(),
            txtApMET.getText().trim(),
            txtAmMET.getText().trim(),
            edad,
            txtEscolaridadMET.getText().trim(),
            ingresoSemanal,
            txtEntidadMET.getText().trim(),
            txtMunicipioMET.getText().trim(),
            txtLocalidadMET.getText().trim(),
            txtColoniaMET.getText().trim(),
            txtCalleMET.getText().trim(),
            txtNUmCasaMET.getText().trim()
        );

        if (padre.actualizar()) {
            System.out.println("apartado 4 actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el registro del padre/tutor.");
        }
    }
    
    public void capturaApartado3(){
        
        String valorCant = txtNumHijos.getText();
        int cantidadHijos;
        if(valorCant.equals("")){
            cantidadHijos = 0;
        }else{
            cantidadHijos = Integer.parseInt(txtNumHijos.getText());
        }
        
        Gastos gastos = new Gastos(
            id_gastos,
            txtEstadoCV.getText().trim(),
            txtNombreEspose.getText().trim(),
            txtApEspose.getText().trim(),
            txtAmEspose.getText().trim(),
            txtHijos.getText().trim(),
            cantidadHijos,
            txtTrabajaEspose.getText().trim(),
            txtTiempoCasados.getText().trim(),
            txtAlumnoTrabaja.getText().trim(),
            txtLugarTrabajoAl.getText().trim(),
            txtHorarioTrabajoAl.getText().trim(),
            txtCosteaGastos.getText().trim(),
            txtGastosPersonales.getText().trim(),
            txtBeca.getText().trim(),
            txtCualBeca.getText().trim(),
            txtPrepaCursada.getText().trim(),
            txtLocalidadPrep.getText().trim(),
            txtAñoEgreso.getText().trim(),
            txtTiempoLibre.getText().trim(),
            txtAdicciones.getText().trim()
        );

        if (gastos.actualizar()) {
            System.out.println("apartado 3 actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el registro de gastos.");
        }
    }
    
    public void capturaApartado2(){
        
        String valorPlantas = txtNumPlantas.getText();
        int numeroPlantas;
        if (valorPlantas.equals("")) {
            numeroPlantas = 0;
        } else {
            numeroPlantas = Integer.parseInt(valorPlantas);
        }

        
        String valorIntegrantes = txtNumIntegrantes.getText();
        int cantidadIntegrantes;
        if (valorIntegrantes.equals("")) {
            cantidadIntegrantes = 0;
        } else {
            cantidadIntegrantes = Integer.parseInt(valorIntegrantes);
        }

        // Checkbox → "1" si está seleccionado, null si no
        String material = null;
        if (cbxMaterial.isSelected()) {
            material = "1";
        }

        String tejas = null;
        if (cbxTejas.isSelected()) {
            tejas = "1";
        }

        String palma = null;
        if (cbxPalma.isSelected()) {
            palma = "1";
        }

        String carton = null;
        if (cbxCarton.isSelected()) {
            carton = "1";
        }

        String otro = null;
        if (cbxOtro.isSelected()) {
            otro = "1";
        }

        String agua = null;
        if (cbxAguaDrenaje.isSelected()) {
            agua = "1";
        }

        String luz = null;
        if (cbxLuz.isSelected()) {
            luz = "1";
        }

        String cable = null;
        if (cbxTelefono.isSelected()) {
            cable = "1";
        }

        String estufa = null;
        if (cbxEstufa.isSelected()) {
            estufa = "1";
        }

        String refrigerador = null;
        if (cbxRefrigerador.isSelected()) {
            refrigerador = "1";
        }

        String television = null;
        if (cbxTelevicion.isSelected()) {
            television = "1";
        }

        String lavadora = null;
        if (cbxLavadora.isSelected()) {
            lavadora = "1";
        }

        String computadora = null;
        if (cbxComputadora.isSelected()) {
            computadora = "1";
        }

        String microondas = null;
        if (cbxMicroondas.isSelected()) {
            microondas = "1";
        }

        String comedor = null;
        if (cbxComeador.isSelected()) {
            comedor = "1";
        }

        String sala = null;
        if (cbxSala.isSelected()) {
            sala = "1";
        }

        String celular = null;
        if (cbxCelular.isSelected()) {
            celular = "1";
        }

        String minisplit = null;
        if (cbxMinisplit.isSelected()) {
            minisplit = "1";
        }

        String abanico = null;
        if (cbxAbanico.isSelected()) {
            abanico = "1";
        }

        String piso = null;
        if (cbxPiso.isSelected()) {
            piso = "1";
        }

        // Crear objeto y actualizar
        CondicionesVivienda vivienda = new CondicionesVivienda(
            id_condicionesVivienda,
            material,
            tejas,
            palma,
            carton,
            otro,
            numeroPlantas,
            txtEstadoCasa.getText(),
            txtNumPiezasCasa.getText(),
            txtNumCuartosDormir.getText(),
            txtCuartoPropio.getText(),
            txtCompartidoCon.getText(),
            txtPatioCochera.getText(),
            cantidadIntegrantes,
            txtTraslado.getText(),
            txtEspacioTareas.getText(),
            txtEnfermedad.getText(),
            agua,
            luz,
            cable,
            estufa,
            refrigerador,
            television,
            lavadora,
            computadora,
            microondas,
            comedor,
            sala,
            celular,
            minisplit,
            abanico,
            piso,
            txtObservacionesViv.getText(),
            txtInfoExtra.getText()
        );

        if (vivienda.actualizar()) {
            System.out.println("apartado 2 actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar condiciones de vivienda.");
        }
    }
    
    public void capturaApartado1(){
        DatosAlumno alumno = new DatosAlumno(
            id_datosAlumno,
            txtReligion.getText().trim(),
            txtEntidadAlumno.getText().trim(),
            txtMunicipioAlumno.getText().trim(),
            txtLocalidadAlumno.getText().trim(),
            txtColoniaAlumno.getText().trim(),
            txtCalleAlumno.getText().trim(),
            txtNumCasaAlumno.getText().trim()
        );

        if (alumno.actualizar()) {
            System.out.println("apartado 1 actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el registro del alumno.");
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
        btnCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
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
        txtNumPlantas = new javax.swing.JTextField();
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
        txtEnfermedad = new javax.swing.JTextArea();
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
        txtApEspose = new javax.swing.JTextField();
        txtHorarioTrabajoAl = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtNombreEspose = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtAmEspose = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtAñoEgreso = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtGastosPersonales = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtLugarTrabajoAl = new javax.swing.JTextField();
        txtCosteaGastos = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtEstadoCV = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtCualBeca = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtAdicciones = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtLocalidadPrep = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtPrepaCursada = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        txtIngresoSemanalMET = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        txtTiempoLibre = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtEdadMET = new javax.swing.JTextField();
        txtMunicipioMET = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtNUmCasaMET = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtLocalidadMET = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txtNombresMET = new javax.swing.JTextField();
        txtApMET = new javax.swing.JTextField();
        txtAmMET = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        txtEntidadMET = new javax.swing.JTextField();
        txtColoniaMET = new javax.swing.JTextField();
        txtCalleMET = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtAmPET = new javax.swing.JTextField();
        txtApPET = new javax.swing.JTextField();
        txtNombresPET = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        txtIngresoPET = new javax.swing.JTextField();
        txtEscolaridadMET = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtLocalidadPET = new javax.swing.JTextField();
        txtMunicipioPET = new javax.swing.JTextField();
        txtEntidadPET = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        txtColoniaPET = new javax.swing.JTextField();
        txtCallePET = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        txtNumCasaPET = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        txtEdadPET = new javax.swing.JTextField();
        txtEscolaridadPET = new javax.swing.JTextField();
        txtNumHijos = new javax.swing.JTextField();
        txtHijos = new javax.swing.JTextField();
        txtTrabajaEspose = new javax.swing.JTextField();
        txtAlumnoTrabaja = new javax.swing.JTextField();
        txtBeca = new javax.swing.JTextField();
        txtCuartoPropio = new javax.swing.JTextField();
        txtNumIntegrantes = new javax.swing.JTextField();
        txtCompartidoCon = new javax.swing.JTextField();
        txtEstadoCasa = new javax.swing.JTextField();
        txtPatioCochera = new javax.swing.JTextField();
        txtNumPiezasCasa = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtTraslado = new javax.swing.JTextArea();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(760, 559));
        getContentPane().setLayout(null);

        jPanel5.setPreferredSize(new java.awt.Dimension(720, 520));
        jPanel5.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        btnCancelar.setBackground(new java.awt.Color(83, 178, 167));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(744, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel3);
        jPanel3.setBounds(0, 480, 790, 40);

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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LogoUtec (New).png"))); // NOI18N
        jLabel2.setEnabled(false);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 80, 70));

        jLabel94.setText(".");
        jLabel94.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Triangulo.png"))); // NOI18N
        jLabel94.setEnabled(false);
        jPanel1.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 80, 60));

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

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 720, 30));

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

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 100, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("UNIVERSIDAD TECNOLÓGICA DE ESCUINAPA");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 370, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Padre/Esposo/Tutor");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2680, 180, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Fecha:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 130, 70, -1));

        fecha.setBackground(new java.awt.Color(195, 210, 197));
        fecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaActionPerformed(evt);
            }
        });
        jPanel1.add(fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 130, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Religion:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 290, 70, -1));

        txtReligion.setBackground(new java.awt.Color(195, 210, 197));
        txtReligion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReligionActionPerformed(evt);
            }
        });
        jPanel1.add(txtReligion, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 320, 170, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Nombre:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 70, -1));

        nombre1.setBackground(new java.awt.Color(195, 210, 197));
        nombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombre1ActionPerformed(evt);
            }
        });
        jPanel1.add(nombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 380, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Localidad:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 370, 80, -1));

        txtLocalidadAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtLocalidadAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 400, 220, 30));

        grupo1.setBackground(new java.awt.Color(195, 210, 197));
        grupo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupo1ActionPerformed(evt);
            }
        });
        jPanel1.add(grupo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 80, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Grupo:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 70, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Carrera:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 70, -1));

        carrera1.setBackground(new java.awt.Color(195, 210, 197));
        carrera1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carrera1ActionPerformed(evt);
            }
        });
        jPanel1.add(carrera1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 270, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Número de casa:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 130, -1));

        txtNumCasaAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCasaAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCasaAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumCasaAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 130, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setText("Municipio:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 370, 80, -1));

        txtMunicipioAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtMunicipioAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, 220, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setText("Estado:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 70, -1));

        txtEntidadAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtEntidadAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntidadAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtEntidadAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 230, 30));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setText("Contacto Familiar:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 530, 130, -1));

        familiar1.setBackground(new java.awt.Color(195, 210, 197));
        familiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                familiar1ActionPerformed(evt);
            }
        });
        jPanel1.add(familiar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 560, 230, 30));

        txtCalleAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtCalleAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtCalleAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 480, 230, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setText("Colonia:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 70, -1));

        txtColoniaAlumno.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(txtColoniaAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 230, 30));

        contactoF1.setBackground(new java.awt.Color(195, 210, 197));
        contactoF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactoF1ActionPerformed(evt);
            }
        });
        jPanel1.add(contactoF1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 560, 180, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel19.setText("¿De que material está hecho la casa?");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 660, 260, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("Familiar:");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 530, 70, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setText("Datos del alumno:");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 160, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setText("¿Se cuenta con espacio para tareas?");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1050, 250, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setText("Calle:");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 450, 70, -1));

        cbxCelular.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxCelular.setText("Celular(es)");
        cbxCelular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCelularActionPerformed(evt);
            }
        });
        jPanel1.add(cbxCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1290, 200, -1));

        cbxPalma.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxPalma.setText("Palma");
        cbxPalma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPalmaActionPerformed(evt);
            }
        });
        jPanel1.add(cbxPalma, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, 70, -1));

        cbxOtro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxOtro.setText("Otro");
        cbxOtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOtroActionPerformed(evt);
            }
        });
        jPanel1.add(cbxOtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 690, 70, -1));

        cbxTejas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTejas.setText("Tejas");
        cbxTejas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTejasActionPerformed(evt);
            }
        });
        jPanel1.add(cbxTejas, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 690, 70, -1));

        cbxCarton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxCarton.setText("Cartón");
        cbxCarton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCartonActionPerformed(evt);
            }
        });
        jPanel1.add(cbxCarton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 690, 70, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setText("Teléfono:");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 70, -1));

        tel1.setBackground(new java.awt.Color(195, 210, 197));
        tel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tel1ActionPerformed(evt);
            }
        });
        jPanel1.add(tel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 230, 30));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setText("Observaciones de la vivienda:");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1370, 310, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("Número de plantas de la casa:");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, 210, -1));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("Numero de plantas de la casa:");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, 210, -1));

        txtNumPlantas.setBackground(new java.awt.Color(195, 210, 197));
        txtNumPlantas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumPlantasActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumPlantas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 760, 80, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setText("Esposa(o)");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 1760, 70, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setText("La casa es:");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 730, 90, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setText("Compartido con:");
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 930, 120, 30));

        txtTiempoCasados.setBackground(new java.awt.Color(195, 210, 197));
        txtTiempoCasados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTiempoCasadosActionPerformed(evt);
            }
        });
        jPanel1.add(txtTiempoCasados, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1880, 170, 30));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setText("¿Número de cuartos para dormir?");
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 810, 250, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setText("¿De cuantas piezas de conforma la casa?");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 810, 290, -1));

        txtNumCuartosDormir.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCuartosDormir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCuartosDormirActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumCuartosDormir, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 840, 80, 30));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setText("¿Tiene cuarto propio?");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 900, 170, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel34.setText("¿Cuenta con un patio y/o cochera?");
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 900, 260, -1));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel35.setText("¿En que se transladara a la universidad?");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 980, 280, 20));

        txtInfoExtra.setBackground(new java.awt.Color(195, 210, 197));
        txtInfoExtra.setColumns(20);
        txtInfoExtra.setRows(5);
        jScrollPane2.setViewportView(txtInfoExtra);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1580, 700, 100));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel36.setText("¿Enfermedad cronica y/o psicomotrices en tu familia?");
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1060, 380, -1));

        cbxMaterial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMaterial.setText("Material");
        cbxMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMaterialActionPerformed(evt);
            }
        });
        jPanel1.add(cbxMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, -1, -1));

        cbxMinisplit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMinisplit.setText("Minisplit");
        cbxMinisplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMinisplitActionPerformed(evt);
            }
        });
        jPanel1.add(cbxMinisplit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1330, 130, -1));

        cbxAbanico.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxAbanico.setText("Abanico");
        cbxAbanico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAbanicoActionPerformed(evt);
            }
        });
        jPanel1.add(cbxAbanico, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1330, 120, -1));

        cbxAguaDrenaje.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxAguaDrenaje.setText("Agua/drenaje");
        cbxAguaDrenaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAguaDrenajeActionPerformed(evt);
            }
        });
        jPanel1.add(cbxAguaDrenaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1210, 130, -1));

        cbxLuz.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxLuz.setText("Luz");
        cbxLuz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLuzActionPerformed(evt);
            }
        });
        jPanel1.add(cbxLuz, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1210, 120, -1));

        cbxPiso.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxPiso.setText("Piso");
        cbxPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPisoActionPerformed(evt);
            }
        });
        jPanel1.add(cbxPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1330, 150, -1));

        cbxTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTelefono.setText("Cable/telefono");
        cbxTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTelefonoActionPerformed(evt);
            }
        });
        jPanel1.add(cbxTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1210, 150, -1));

        cbxEstufa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxEstufa.setText("Estufa a gas/hornilla");
        cbxEstufa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEstufaActionPerformed(evt);
            }
        });
        jPanel1.add(cbxEstufa, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1210, 200, -1));

        cbxRefrigerador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxRefrigerador.setText("Refrigerador");
        cbxRefrigerador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRefrigeradorActionPerformed(evt);
            }
        });
        jPanel1.add(cbxRefrigerador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1250, 130, -1));

        cbxTelevicion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxTelevicion.setText("Television");
        cbxTelevicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTelevicionActionPerformed(evt);
            }
        });
        jPanel1.add(cbxTelevicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1250, 120, -1));

        cbxLavadora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxLavadora.setText("Lavadora");
        cbxLavadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLavadoraActionPerformed(evt);
            }
        });
        jPanel1.add(cbxLavadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1250, 150, -1));

        cbxComputadora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxComputadora.setText("Computadora");
        cbxComputadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxComputadoraActionPerformed(evt);
            }
        });
        jPanel1.add(cbxComputadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 1250, 200, -1));

        cbxMicroondas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxMicroondas.setText("Microondas");
        cbxMicroondas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMicroondasActionPerformed(evt);
            }
        });
        jPanel1.add(cbxMicroondas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1290, 130, -1));

        cbxComeador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxComeador.setText("Comedor");
        cbxComeador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxComeadorActionPerformed(evt);
            }
        });
        jPanel1.add(cbxComeador, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 1290, 120, -1));

        cbxSala.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxSala.setText("Sala");
        cbxSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSalaActionPerformed(evt);
            }
        });
        jPanel1.add(cbxSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1290, 150, -1));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel37.setText("Número de integrantes que viven en la casa:");
        jPanel1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 980, 310, -1));

        txtEnfermedad.setBackground(new java.awt.Color(195, 210, 197));
        txtEnfermedad.setColumns(20);
        txtEnfermedad.setRows(5);
        jScrollPane3.setViewportView(txtEnfermedad);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1090, 360, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Condiciones de la vivienda");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 620, 230, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel38.setText("¿Con que servicio y equipamiento cuenta la vivienda?");
        jPanel1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1180, 400, -1));

        txtObservacionesViv.setBackground(new java.awt.Color(195, 210, 197));
        txtObservacionesViv.setColumns(20);
        txtObservacionesViv.setRows(5);
        jScrollPane4.setViewportView(txtObservacionesViv);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1400, 700, 100));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel39.setText("Información Extra");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 1510, 160, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setText("¿Donde?");
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1930, 70, -1));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setText("Horario:");
        jPanel1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1930, 70, -1));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel42.setText("(En caso de que el alumno tenga pareja)");
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1740, 290, -1));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setText("5.-¿Qué gastos personales pagas?");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2010, 300, -1));

        txtEspacioTareas.setBackground(new java.awt.Color(195, 210, 197));
        txtEspacioTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEspacioTareasActionPerformed(evt);
            }
        });
        jPanel1.add(txtEspacioTareas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1080, 80, 30));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel44.setText("Cuantos:");
        jPanel1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 1880, -1, 30));

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel45.setText("Apellido Paterno:");
        jPanel1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1780, 130, -1));

        txtApEspose.setBackground(new java.awt.Color(195, 210, 197));
        txtApEspose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApEsposeActionPerformed(evt);
            }
        });
        jPanel1.add(txtApEspose, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 1810, 160, 30));

        txtHorarioTrabajoAl.setBackground(new java.awt.Color(195, 210, 197));
        txtHorarioTrabajoAl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHorarioTrabajoAlActionPerformed(evt);
            }
        });
        jPanel1.add(txtHorarioTrabajoAl, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1960, 240, 30));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel46.setText("Nombres:");
        jPanel1.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 1780, 100, -1));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setText("1.-¿Estado civil?");
        jPanel1.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1780, 120, -1));

        txtNombreEspose.setBackground(new java.awt.Color(195, 210, 197));
        txtNombreEspose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEsposeActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombreEspose, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 1810, 180, 30));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setText("¿Hay alguna situación que está afectando tu desempeño academico?");
        jPanel1.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1550, 480, -1));

        txtAmEspose.setBackground(new java.awt.Color(195, 210, 197));
        txtAmEspose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmEsposeActionPerformed(evt);
            }
        });
        jPanel1.add(txtAmEspose, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1810, 170, 30));

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Apellido Materno:");
        jPanel1.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1780, 130, -1));

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("2.-¿Tiene hijos?");
        jPanel1.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1850, 110, -1));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("¿Trabaja?");
        jPanel1.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 1850, 70, -1));

        txtAñoEgreso.setBackground(new java.awt.Color(195, 210, 197));
        txtAñoEgreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAñoEgresoActionPerformed(evt);
            }
        });
        jPanel1.add(txtAñoEgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2120, 110, 30));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setText("Tiempo de casados:");
        jPanel1.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 1850, 140, -1));

        txtGastosPersonales.setBackground(new java.awt.Color(195, 210, 197));
        txtGastosPersonales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGastosPersonalesActionPerformed(evt);
            }
        });
        jPanel1.add(txtGastosPersonales, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2040, 310, 30));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setText("3.-¿Usted trabaja?");
        jPanel1.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1930, 130, 20));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel54.setText("Año que egresó:");
        jPanel1.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 2090, 120, -1));

        txtLugarTrabajoAl.setBackground(new java.awt.Color(195, 210, 197));
        txtLugarTrabajoAl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLugarTrabajoAlActionPerformed(evt);
            }
        });
        jPanel1.add(txtLugarTrabajoAl, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1960, 270, 30));

        txtCosteaGastos.setBackground(new java.awt.Color(195, 210, 197));
        txtCosteaGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCosteaGastosActionPerformed(evt);
            }
        });
        jPanel1.add(txtCosteaGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2040, 280, 30));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("4.-¿Quién costea los gastos del estudiante?");
        jPanel1.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2010, 300, 20));

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setText("7.-Preparatoria Cursada:");
        jPanel1.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2090, 200, 20));

        txtEstadoCV.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoCVActionPerformed(evt);
            }
        });
        jPanel1.add(txtEstadoCV, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1810, 150, 30));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel57.setText("9.-¿fumas, bebes alchol o alguna otra addición?");
        jLabel57.setToolTipText("");
        jPanel1.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 2170, 340, 20));

        txtCualBeca.setBackground(new java.awt.Color(195, 210, 197));
        txtCualBeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCualBecaActionPerformed(evt);
            }
        });
        jPanel1.add(txtCualBeca, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 2120, 110, 30));

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("¿Cuál?");
        jPanel1.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 2120, 50, 30));

        txtAdicciones.setBackground(new java.awt.Color(195, 210, 197));
        txtAdicciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdiccionesActionPerformed(evt);
            }
        });
        jPanel1.add(txtAdicciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 2200, 330, 30));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel59.setText("Apellido Materno:");
        jPanel1.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2330, 130, -1));

        txtLocalidadPrep.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadPrep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadPrepActionPerformed(evt);
            }
        });
        jPanel1.add(txtLocalidadPrep, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2120, 140, 30));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setText("6.-¿Cuenta con alguna beca?");
        jPanel1.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2090, 200, 20));

        txtPrepaCursada.setBackground(new java.awt.Color(195, 210, 197));
        txtPrepaCursada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrepaCursadaActionPerformed(evt);
            }
        });
        jPanel1.add(txtPrepaCursada, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2120, 170, 30));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel61.setText("8.-¿Dónde pasas tu tiempo libre?");
        jPanel1.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2170, 230, 20));

        txtIngresoSemanalMET.setBackground(new java.awt.Color(195, 210, 197));
        txtIngresoSemanalMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIngresoSemanalMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtIngresoSemanalMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2430, 220, 30));

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel62.setText("Informacion sobre gastos del estudiante");
        jPanel1.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 1700, 350, -1));

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel63.setText("Datos Familiares");
        jPanel1.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 2260, 150, -1));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setText("Localidad:");
        jPanel1.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2090, 80, -1));

        txtTiempoLibre.setBackground(new java.awt.Color(195, 210, 197));
        txtTiempoLibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTiempoLibreActionPerformed(evt);
            }
        });
        jPanel1.add(txtTiempoLibre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2200, 340, 30));

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel65.setText("Colonia:");
        jPanel1.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2590, 60, -1));

        txtEdadMET.setBackground(new java.awt.Color(195, 210, 197));
        txtEdadMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdadMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEdadMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2430, 80, 30));

        txtMunicipioMET.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtMunicipioMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2540, 220, 30));

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setText("Ingreso semanal:");
        jPanel1.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2400, 120, -1));

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel67.setText("Nombres:");
        jPanel1.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2330, 80, -1));

        txtNUmCasaMET.setBackground(new java.awt.Color(195, 210, 197));
        txtNUmCasaMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNUmCasaMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtNUmCasaMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2620, 160, 30));

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel68.setText("Número de casa:");
        jPanel1.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2590, 120, -1));

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel69.setText("Escolaridad:");
        jPanel1.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2400, 120, -1));

        txtLocalidadMET.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtLocalidadMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2540, 220, 30));

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel70.setText("Edad:");
        jPanel1.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2400, 50, -1));

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel71.setText("Domicilio:");
        jPanel1.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2480, 80, -1));

        txtNombresMET.setBackground(new java.awt.Color(195, 210, 197));
        txtNombresMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombresMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2360, 220, 30));

        txtApMET.setBackground(new java.awt.Color(195, 210, 197));
        txtApMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtApMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2360, 220, 30));

        txtAmMET.setBackground(new java.awt.Color(195, 210, 197));
        txtAmMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtAmMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2360, 220, 30));

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel72.setText("Apellido Paterno:");
        jPanel1.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2330, 120, -1));

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel73.setText("Calle:");
        jPanel1.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2590, 120, -1));

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel74.setText("Estado:");
        jPanel1.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2510, 60, -1));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel75.setText("Municipio:");
        jPanel1.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2510, 120, -1));

        jLabel76.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel76.setText("Localidad:");
        jPanel1.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2510, 120, -1));

        txtEntidadMET.setBackground(new java.awt.Color(195, 210, 197));
        txtEntidadMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntidadMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEntidadMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2540, 220, 30));

        txtColoniaMET.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtColoniaMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2620, 240, 30));

        txtCalleMET.setBackground(new java.awt.Color(195, 210, 197));
        txtCalleMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtCalleMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2620, 250, 30));

        jLabel77.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel77.setText("Madre/Esposa/Tutora");
        jPanel1.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2290, 190, -1));

        jLabel78.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel78.setText("Nombres:");
        jPanel1.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2720, 80, -1));

        jLabel79.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel79.setText("Apellido Paterno:");
        jPanel1.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2720, 130, -1));

        jLabel80.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel80.setText("Apellido Materno:");
        jPanel1.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2720, 130, -1));

        txtAmPET.setBackground(new java.awt.Color(195, 210, 197));
        txtAmPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtAmPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2750, 220, 30));

        txtApPET.setBackground(new java.awt.Color(195, 210, 197));
        txtApPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtApPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2750, 220, 30));

        txtNombresPET.setBackground(new java.awt.Color(195, 210, 197));
        txtNombresPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombresPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2750, 220, 30));

        jLabel81.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel81.setText("Edad:");
        jPanel1.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2790, 50, -1));

        jLabel82.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel82.setText("Escolaridad:");
        jPanel1.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2790, 120, -1));

        jLabel83.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel83.setText("Ingreso semanal:");
        jPanel1.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2790, 120, -1));

        txtIngresoPET.setBackground(new java.awt.Color(195, 210, 197));
        txtIngresoPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIngresoPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtIngresoPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 2820, 220, 30));

        txtEscolaridadMET.setBackground(new java.awt.Color(195, 210, 197));
        txtEscolaridadMET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEscolaridadMETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEscolaridadMET, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2430, 270, 30));

        jLabel84.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel84.setText("Domicilio:");
        jPanel1.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2870, 80, -1));

        jLabel85.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel85.setText("Estado:");
        jPanel1.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2900, 60, -1));

        jLabel86.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel86.setText("Municipio:");
        jPanel1.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2900, 120, -1));

        jLabel87.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel87.setText("Localidad:");
        jPanel1.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2900, 120, -1));

        txtLocalidadPET.setBackground(new java.awt.Color(195, 210, 197));
        txtLocalidadPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalidadPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtLocalidadPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2930, 220, 30));

        txtMunicipioPET.setBackground(new java.awt.Color(195, 210, 197));
        txtMunicipioPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtMunicipioPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 2930, 220, 30));

        txtEntidadPET.setBackground(new java.awt.Color(195, 210, 197));
        txtEntidadPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntidadPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEntidadPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2930, 220, 30));

        jLabel88.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel88.setText("Colonia:");
        jPanel1.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2980, 60, -1));

        txtColoniaPET.setBackground(new java.awt.Color(195, 210, 197));
        txtColoniaPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtColoniaPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 3010, 240, 30));

        txtCallePET.setBackground(new java.awt.Color(195, 210, 197));
        txtCallePET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCallePETActionPerformed(evt);
            }
        });
        jPanel1.add(txtCallePET, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 3010, 250, 30));

        jLabel89.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel89.setText("Calle:");
        jPanel1.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 2980, 120, -1));

        txtNumCasaPET.setBackground(new java.awt.Color(195, 210, 197));
        txtNumCasaPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCasaPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumCasaPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 3010, 160, 30));

        jLabel90.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel90.setText("Número de casa:");
        jPanel1.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 2980, 120, -1));

        jLabel95.setText("jLabel95");
        jLabel95.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/UTyP.png"))); // NOI18N
        jLabel95.setEnabled(false);
        jPanel1.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 50, 30));

        jLabel91.setText("jLabel91");
        jLabel91.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo sinaloa.png"))); // NOI18N
        jLabel91.setEnabled(false);
        jPanel1.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 16, 70, 20));

        jLabel92.setText("jLabel92");
        jLabel92.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo sep.png"))); // NOI18N
        jLabel92.setEnabled(false);
        jPanel1.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 80, -1));

        txtEdadPET.setBackground(new java.awt.Color(195, 210, 197));
        txtEdadPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdadPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEdadPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2820, 80, 30));

        txtEscolaridadPET.setBackground(new java.awt.Color(195, 210, 197));
        txtEscolaridadPET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEscolaridadPETActionPerformed(evt);
            }
        });
        jPanel1.add(txtEscolaridadPET, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 2820, 270, 30));

        txtNumHijos.setBackground(new java.awt.Color(195, 210, 197));
        txtNumHijos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumHijosActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumHijos, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1880, 60, 30));

        txtHijos.setBackground(new java.awt.Color(195, 210, 197));
        txtHijos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHijosActionPerformed(evt);
            }
        });
        jPanel1.add(txtHijos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1880, 60, 30));

        txtTrabajaEspose.setBackground(new java.awt.Color(195, 210, 197));
        txtTrabajaEspose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrabajaEsposeActionPerformed(evt);
            }
        });
        jPanel1.add(txtTrabajaEspose, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 1880, 60, 30));

        txtAlumnoTrabaja.setBackground(new java.awt.Color(195, 210, 197));
        txtAlumnoTrabaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlumnoTrabajaActionPerformed(evt);
            }
        });
        jPanel1.add(txtAlumnoTrabaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1960, 60, 30));

        txtBeca.setBackground(new java.awt.Color(195, 210, 197));
        txtBeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBecaActionPerformed(evt);
            }
        });
        jPanel1.add(txtBeca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 2120, 60, 30));

        txtCuartoPropio.setBackground(new java.awt.Color(195, 210, 197));
        txtCuartoPropio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCuartoPropioActionPerformed(evt);
            }
        });
        jPanel1.add(txtCuartoPropio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 930, 80, 30));

        txtNumIntegrantes.setBackground(new java.awt.Color(195, 210, 197));
        txtNumIntegrantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumIntegrantesActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumIntegrantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1010, 80, 30));

        txtCompartidoCon.setBackground(new java.awt.Color(195, 210, 197));
        txtCompartidoCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCompartidoConActionPerformed(evt);
            }
        });
        jPanel1.add(txtCompartidoCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 930, 120, 30));

        txtEstadoCasa.setBackground(new java.awt.Color(195, 210, 197));
        txtEstadoCasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoCasaActionPerformed(evt);
            }
        });
        jPanel1.add(txtEstadoCasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 760, 180, 30));

        txtPatioCochera.setBackground(new java.awt.Color(195, 210, 197));
        txtPatioCochera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPatioCocheraActionPerformed(evt);
            }
        });
        jPanel1.add(txtPatioCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 930, 180, 30));

        txtNumPiezasCasa.setBackground(new java.awt.Color(195, 210, 197));
        txtNumPiezasCasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumPiezasCasaActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumPiezasCasa, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 840, 80, 30));

        txtTraslado.setBackground(new java.awt.Color(195, 210, 197));
        txtTraslado.setColumns(20);
        txtTraslado.setRows(5);
        jScrollPane5.setViewportView(txtTraslado);

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 1000, 300, 60));

        jToggleButton1.setBackground(new java.awt.Color(83, 192, 147));
        jToggleButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jToggleButton1.setText("Añadir campo");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 3070, 150, 40));

        jScrollPane1.setViewportView(jPanel1);

        jPanel5.add(jScrollPane1);
        jScrollPane1.setBounds(0, 60, 750, 420);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(0, 0, 750, 520);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void txtNumPiezasCasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumPiezasCasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumPiezasCasaActionPerformed

    private void txtPatioCocheraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPatioCocheraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPatioCocheraActionPerformed

    private void txtEstadoCasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoCasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoCasaActionPerformed

    private void txtCompartidoConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCompartidoConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompartidoConActionPerformed

    private void txtNumIntegrantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumIntegrantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumIntegrantesActionPerformed

    private void txtCuartoPropioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCuartoPropioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCuartoPropioActionPerformed

    private void txtBecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBecaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBecaActionPerformed

    private void txtAlumnoTrabajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlumnoTrabajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlumnoTrabajaActionPerformed

    private void txtTrabajaEsposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrabajaEsposeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrabajaEsposeActionPerformed

    private void txtHijosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHijosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHijosActionPerformed

    private void txtNumHijosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumHijosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumHijosActionPerformed

    private void txtEscolaridadPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEscolaridadPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEscolaridadPETActionPerformed

    private void txtEdadPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdadPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadPETActionPerformed

    private void txtNumCasaPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCasaPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumCasaPETActionPerformed

    private void txtCallePETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCallePETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCallePETActionPerformed

    private void txtColoniaPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaPETActionPerformed

    private void txtEntidadPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntidadPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntidadPETActionPerformed

    private void txtMunicipioPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioPETActionPerformed

    private void txtLocalidadPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadPETActionPerformed

    private void txtEscolaridadMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEscolaridadMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEscolaridadMETActionPerformed

    private void txtIngresoPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresoPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIngresoPETActionPerformed

    private void txtNombresPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresPETActionPerformed

    private void txtApPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApPETActionPerformed

    private void txtAmPETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmPETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmPETActionPerformed

    private void txtCalleMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalleMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalleMETActionPerformed

    private void txtColoniaMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaMETActionPerformed

    private void txtEntidadMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntidadMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntidadMETActionPerformed

    private void txtAmMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmMETActionPerformed

    private void txtApMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApMETActionPerformed

    private void txtNombresMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresMETActionPerformed

    private void txtLocalidadMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadMETActionPerformed

    private void txtNUmCasaMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNUmCasaMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNUmCasaMETActionPerformed

    private void txtMunicipioMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioMETActionPerformed

    private void txtEdadMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdadMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadMETActionPerformed

    private void txtTiempoLibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTiempoLibreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTiempoLibreActionPerformed

    private void txtIngresoSemanalMETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresoSemanalMETActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIngresoSemanalMETActionPerformed

    private void txtPrepaCursadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrepaCursadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrepaCursadaActionPerformed

    private void txtLocalidadPrepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalidadPrepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalidadPrepActionPerformed

    private void txtAdiccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdiccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdiccionesActionPerformed

    private void txtCualBecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCualBecaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCualBecaActionPerformed

    private void txtEstadoCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoCVActionPerformed

    private void txtCosteaGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCosteaGastosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCosteaGastosActionPerformed

    private void txtLugarTrabajoAlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLugarTrabajoAlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLugarTrabajoAlActionPerformed

    private void txtGastosPersonalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGastosPersonalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGastosPersonalesActionPerformed

    private void txtAñoEgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAñoEgresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAñoEgresoActionPerformed

    private void txtAmEsposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmEsposeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmEsposeActionPerformed

    private void txtNombreEsposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEsposeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEsposeActionPerformed

    private void txtHorarioTrabajoAlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHorarioTrabajoAlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorarioTrabajoAlActionPerformed

    private void txtApEsposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApEsposeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApEsposeActionPerformed

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

    private void txtNumPlantasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumPlantasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumPlantasActionPerformed

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

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        regresa.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

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
    private javax.swing.JButton btnCancelar;
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
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField nombre1;
    private javax.swing.JTextField tel1;
    private javax.swing.JTextField txtAdicciones;
    private javax.swing.JTextField txtAlumnoTrabaja;
    private javax.swing.JTextField txtAmEspose;
    private javax.swing.JTextField txtAmMET;
    private javax.swing.JTextField txtAmPET;
    private javax.swing.JTextField txtApEspose;
    private javax.swing.JTextField txtApMET;
    private javax.swing.JTextField txtApPET;
    private javax.swing.JTextField txtAñoEgreso;
    private javax.swing.JTextField txtBeca;
    private javax.swing.JTextField txtCalleAlumno;
    private javax.swing.JTextField txtCalleMET;
    private javax.swing.JTextField txtCallePET;
    private javax.swing.JTextField txtColoniaAlumno;
    private javax.swing.JTextField txtColoniaMET;
    private javax.swing.JTextField txtColoniaPET;
    private javax.swing.JTextField txtCompartidoCon;
    private javax.swing.JTextField txtCosteaGastos;
    private javax.swing.JTextField txtCualBeca;
    private javax.swing.JTextField txtCuartoPropio;
    private javax.swing.JTextField txtEdadMET;
    private javax.swing.JTextField txtEdadPET;
    private javax.swing.JTextArea txtEnfermedad;
    private javax.swing.JTextField txtEntidadAlumno;
    private javax.swing.JTextField txtEntidadMET;
    private javax.swing.JTextField txtEntidadPET;
    private javax.swing.JTextField txtEscolaridadMET;
    private javax.swing.JTextField txtEscolaridadPET;
    private javax.swing.JTextField txtEspacioTareas;
    private javax.swing.JTextField txtEstadoCV;
    private javax.swing.JTextField txtEstadoCasa;
    private javax.swing.JTextField txtGastosPersonales;
    private javax.swing.JTextField txtHijos;
    private javax.swing.JTextField txtHorarioTrabajoAl;
    private javax.swing.JTextArea txtInfoExtra;
    private javax.swing.JTextField txtIngresoPET;
    private javax.swing.JTextField txtIngresoSemanalMET;
    private javax.swing.JTextField txtLocalidadAlumno;
    private javax.swing.JTextField txtLocalidadMET;
    private javax.swing.JTextField txtLocalidadPET;
    private javax.swing.JTextField txtLocalidadPrep;
    private javax.swing.JTextField txtLugarTrabajoAl;
    private javax.swing.JTextField txtMunicipioAlumno;
    private javax.swing.JTextField txtMunicipioMET;
    private javax.swing.JTextField txtMunicipioPET;
    private javax.swing.JTextField txtNUmCasaMET;
    private javax.swing.JTextField txtNombreEspose;
    private javax.swing.JTextField txtNombresMET;
    private javax.swing.JTextField txtNombresPET;
    private javax.swing.JTextField txtNumCasaAlumno;
    private javax.swing.JTextField txtNumCasaPET;
    private javax.swing.JTextField txtNumCuartosDormir;
    private javax.swing.JTextField txtNumHijos;
    private javax.swing.JTextField txtNumIntegrantes;
    private javax.swing.JTextField txtNumPiezasCasa;
    private javax.swing.JTextField txtNumPlantas;
    private javax.swing.JTextArea txtObservacionesViv;
    private javax.swing.JTextField txtPatioCochera;
    private javax.swing.JTextField txtPrepaCursada;
    private javax.swing.JTextField txtReligion;
    private javax.swing.JTextField txtTiempoCasados;
    private javax.swing.JTextField txtTiempoLibre;
    private javax.swing.JTextField txtTrabajaEspose;
    private javax.swing.JTextArea txtTraslado;
    // End of variables declaration//GEN-END:variables
}
