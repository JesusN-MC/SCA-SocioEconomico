/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;
import clases.Atencion;
import clases.Conexion;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author Lizbeth
 */
public class EditarAtencion extends javax.swing.JFrame {
    JFrame regresar;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditarAtencion.class.getName());
    boolean existe;
    String idsoli;
    String idAtencionNueva = "0";
    /**
     * Creates new form MenuEstudiantes
     */
    public EditarAtencion(JFrame pantalla ,String idSolicitud) {
        regresar = pantalla;
        idsoli = idSolicitud;
        existe = consultarAtencion(idsoli);
        initComponents();
        DarEstilos();
        if(existe){
            cargarDatos(idSolicitud);
        }else{
            iniciarAtencion(idSolicitud);
            cargarDatos(idSolicitud);
        }
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatos(idSolicitud);
            }
        });
    }
    
    public void iniciarAtencion(String idSolicitud){
        
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;
        
        String sql = "SELECT canaliza FROM solicitud WHERE idSolicitud = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idSolicitud);
        ResultSet datos = ps.executeQuery();
        
        String canalizado = "0";
        if(datos.next()){
            canalizado = datos.getString("canaliza");
            if(canalizado == null){
                canalizado = "0";
            }else{
                canalizado = "1";
            }
        }
        datos.close();
        ps.close();
        con.close();
        
        Atencion atencion = new Atencion(canalizado, idSolicitud);
        if(atencion.crearAtencion()){
            Conexion conexion2 = new Conexion();
            Connection con2 = conexion2.con;

            String sql2 = "SELECT idAtencion FROM atencion WHERE idAtencion = (SELECT MAX(idAtencion) FROM atencion)";
            PreparedStatement ps2 = con2.prepareStatement(sql2);
            ResultSet datos2 = ps2.executeQuery();
            
            if(datos2.next()){
                idAtencionNueva = datos2.getString("idAtencion");       
            }
            datos2.close();
            ps2.close();
            con2.close();
        }
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
    }
    
    public void cargarDatos(String idSolicitud){
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;
        
        String sql = "SELECT a.idAtencion, a.fecha, a.canalizacion, a.estatus, a.estatusNotificado,a.resumenAtencion, s.motivo, s.tipo, b.nombreAlumno, b.matricula, b.grupo, c.nombreCarrera FROM atencion a INNER JOIN solicitud s ON a.idSolicitud = s.idSolicitud INNER JOIN alumno b ON s.idAlumno = b.idAlumno INNER JOIN carrera c ON b.idCarrera = c.idCarrera WHERE a.idSolicitud = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idSolicitud);
        ResultSet datos = ps.executeQuery();
        
        if(datos.next()){
            String fecha = datos.getString("fecha");
            CampoFecha.setText(fecha);
            
            String motivo = datos.getString("motivo");
            if(motivo.equals("1")){
                    motivo = "Socioeconomico";
                }else if (motivo.equals("2")){
                    motivo = "Salud";
                }else{
                    motivo = "Familiar";
                };
            campoMotivo.setText(motivo);
            
            String tipo = datos.getString("tipo");
            if(tipo.equals("1")){
                    tipo = "Llamada";
                }else if (tipo.equals("2")){
                    tipo = "Oficina";
                }else{
                   tipo = "Visita";
                }
            campoTipo.setText(tipo);
            
            String canalizacion = datos.getString("canalizacion");
            if(canalizacion.equals("1")){
                canalizacion = "si";
            }else{
                canalizacion = "no";
            }
            campoCanalizacion.setText(canalizacion);
            
            //PENDIENTE EL ESTATUS 
            String estatus = datos.getString("estatus");
            if(estatus.equals("1")){
                comboEstatus.addItem("Formulario Pendiente");
            }else if(estatus.equals("2")){
                comboEstatus.addItem("Esperando Respuesta");
                comboEstatus.addItem("Aprobado");
                comboEstatus.addItem("Rechazado");
            }
                
            if(estatus.equals("3")){
                comboEstatus.addItem("Aprobado");
                notificado.setEnabled(true);
                
            }else if(estatus.equals("4")){
                comboEstatus.addItem("Rechazado");
                notificado.setEnabled(true);
                
            }
            String estatusNotificado = datos.getString("estatusNotificado");
            if(estatusNotificado.equals("1")){
               notificado.setSelected(true);
            }
            
            String nombre = datos.getString("nombreAlumno");
            campoNombre.setText (nombre);
            
            String matricula = datos.getString("matricula");
            campoMatricula.setText(matricula);
            
            String grupo = datos.getString("grupo");
            campoGrupo.setText(grupo);
            
            String carrera = datos.getString("nombreCarrera");
            campoCarrera.setText(carrera);
            
            String resumen = datos.getString ("resumenAtencion");
            campoResumen.setText(resumen);

            idAtencionNueva = datos.getString("idAtencion");
        }
        
        datos.close();
        ps.close();
        con.close();
        
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
    }
 

    public void DarEstilos(){
        btnlogo.setBorderPainted(false);
        notificado.setEnabled(false);
        CampoFecha.putClientProperty("JComponent.roundRect", true);
        CampoFecha.setEditable(false);
        
        //campoMotivo.putClientProperty("JComponent.roundRect", true);
        campoMotivo.setEditable(false);
        
        //campoTipo.putClientProperty("JComponent.roundRect", true);
        campoTipo.setEditable(false);
        
        //campoCanalizacion.putClientProperty("JComponent.roundRect", true);
        campoCanalizacion.setEditable(false);
        
        //comboEstatus.putClientProperty("JComponent.roundRect", true);
        
        //campoNombre.putClientProperty("JComponent.roundRect", true);
        campoNombre.setEditable(false);
        
        //campoMatricula.putClientProperty("JComponent.roundRect", true);
        campoMatricula.setEditable(false);
        
        //campoGrupo.putClientProperty("JComponent.roundRect", true);
        campoGrupo.setEditable(false);
        
        //campoCarrera.putClientProperty("JComponent.roundRect", true);
        campoCarrera.setEditable(false);
    }
    
    public boolean consultarAtencion(String idSoli){
        boolean existe = false;
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;
        
        String sql = "SELECT * FROM atencion WHERE idSolicitud = ?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idSoli);
        ResultSet datos = ps.executeQuery();
        
        if(datos.next()){
            existe = true;
            
        }
        
        datos.close();
        ps.close();
        con.close();
        
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
        return existe;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();
        resultadoError = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        campoMatricula = new javax.swing.JTextField();
        campoGrupo = new javax.swing.JTextField();
        campoCarrera = new javax.swing.JTextField();
        campoCanalizacion = new javax.swing.JTextField();
        campoMotivo = new javax.swing.JTextField();
        campoTipo = new javax.swing.JTextField();
        comboEstatus = new javax.swing.JComboBox<>();
        CampoFecha = new javax.swing.JTextField();
        notificado = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        Formulario = new javax.swing.JPanel();
        botonCancelar = new javax.swing.JButton();
        botonFormulario = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        campoResumen = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, -1));

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(43, 138, 127));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnlogo.setBackground(new java.awt.Color(43, 138, 127));
        btnlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N
        jPanel7.add(btnlogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 40));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 40));

        resultadoError.setForeground(new java.awt.Color(204, 0, 51));
        jPanel5.add(resultadoError, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 330, 200, -1));

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));

        jLabel12.setText("Formato Atencion");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(311, 311, 311)
                .addComponent(jLabel12)
                .addContainerGap(314, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 720, 20));

        jLabel1.setText("Fecha:");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 50, -1));

        jLabel2.setText("Motivo:");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 50, -1));

        jLabel3.setText("Tipo:");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 50, -1));

        jLabel4.setText("Canalización:");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, 90, -1));

        jLabel5.setText("Estatus:");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 80, -1));

        jPanel2.setBackground(new java.awt.Color(168, 204, 193));

        jLabel6.setText("Datos del alumno");

        jLabel7.setText("Nombre:");

        jLabel8.setText("Matrícula:");

        jLabel9.setText("Grupo:");

        jLabel10.setText("Carrera:");

        campoNombre.setBackground(new java.awt.Color(195, 210, 197));
        campoNombre.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        campoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoNombreActionPerformed(evt);
            }
        });

        campoMatricula.setBackground(new java.awt.Color(195, 210, 197));
        campoMatricula.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoMatricula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoMatriculaActionPerformed(evt);
            }
        });

        campoGrupo.setBackground(new java.awt.Color(195, 210, 197));
        campoGrupo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoGrupo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoGrupoActionPerformed(evt);
            }
        });

        campoCarrera.setBackground(new java.awt.Color(195, 210, 197));
        campoCarrera.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoCarrera.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoCarrera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoCarreraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(campoMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(campoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)))
                        .addGap(39, 39, 39))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(282, 282, 282))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 680, 150));

        campoCanalizacion.setBackground(new java.awt.Color(195, 210, 197));
        campoCanalizacion.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoCanalizacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoCanalizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoCanalizacionActionPerformed(evt);
            }
        });
        jPanel5.add(campoCanalizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 90, -1));

        campoMotivo.setBackground(new java.awt.Color(195, 210, 197));
        campoMotivo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoMotivo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMotivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoMotivoActionPerformed(evt);
            }
        });
        jPanel5.add(campoMotivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 150, -1));

        campoTipo.setBackground(new java.awt.Color(195, 210, 197));
        campoTipo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoTipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTipoActionPerformed(evt);
            }
        });
        jPanel5.add(campoTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 110, -1));

        comboEstatus.setBackground(new java.awt.Color(195, 210, 197));
        comboEstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEstatusActionPerformed(evt);
            }
        });
        jPanel5.add(comboEstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 170, -1));

        CampoFecha.setBackground(new java.awt.Color(195, 210, 197));
        CampoFecha.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        CampoFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CampoFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CampoFechaActionPerformed(evt);
            }
        });
        jPanel5.add(CampoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, -1));
        jPanel5.add(notificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 70, -1, -1));

        jLabel13.setText("Notificado:");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, -1, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 310));

        Formulario.setBackground(new java.awt.Color(43, 138, 127));
        Formulario.setPreferredSize(new java.awt.Dimension(720, 40));

        botonCancelar.setBackground(new java.awt.Color(255, 102, 102));
        botonCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        botonFormulario.setBackground(new java.awt.Color(102, 153, 255));
        botonFormulario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonFormulario.setText("Formuiario");
        botonFormulario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFormularioActionPerformed(evt);
            }
        });

        botonGuardar.setBackground(new java.awt.Color(153, 255, 153));
        botonGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FormularioLayout = new javax.swing.GroupLayout(Formulario);
        Formulario.setLayout(FormularioLayout);
        FormularioLayout.setHorizontalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FormularioLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(botonCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 479, Short.MAX_VALUE)
                .addComponent(botonFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(FormularioLayout.createSequentialGroup()
                    .addGap(124, 124, 124)
                    .addComponent(botonGuardar)
                    .addContainerGap(520, Short.MAX_VALUE)))
        );
        FormularioLayout.setVerticalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FormularioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(FormularioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel1.add(Formulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, -1, 40));

        campoResumen.setBackground(new java.awt.Color(195, 210, 197));
        campoResumen.setColumns(20);
        campoResumen.setRows(5);
        jScrollPane1.setViewportView(campoResumen);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 680, 130));

        jLabel11.setText("Resumen de atención:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 140, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoCanalizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoCanalizacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoCanalizacionActionPerformed
       
    private void campoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoNombreActionPerformed

    private void campoMotivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoMotivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoMotivoActionPerformed

    private void campoTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTipoActionPerformed

    private void campoMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoMatriculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoMatriculaActionPerformed

    private void campoGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoGrupoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoGrupoActionPerformed

    private void CampoFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoFechaActionPerformed

    private void campoCarreraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoCarreraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoCarreraActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        // TODO add your handling code here:
        regresar.setVisible(true);
        dispose();
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void botonFormularioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFormularioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonFormularioActionPerformed

    private void comboEstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboEstatusActionPerformed

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        // TODO add your handling code here:
        String estatus = (String) comboEstatus.getSelectedItem();
        if(estatus.equals("Formulario Pendiente")){
                estatus = ("1");
        }else if(estatus.equals("Esperando Respuesta")){
            estatus = ("2");
        }else if(estatus.equals("Aprobado")){
            estatus = ("3");
        }else if(estatus.equals("Rechazado")){
            estatus = ("4");
        }
        
        String noti;
        
        if(notificado.isSelected()){
            noti = "1";
        }else{
            noti = "0";
        }
        String id = idsoli;
     
        String resumen = campoResumen.getText();
        
        Atencion at = new Atencion(estatus, noti, id, resumen);
        if(at.actualizar()){
            showMessageDialog(null, "Guardado" );
        }else{
            showMessageDialog(null, "estatus: " + estatus + " notificado: "+ noti+ " id: "+ id + " resumen: " + resumen );
        } 
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
        //java.awt.EventQueue.invokeLater(() -> new EditarAtencion("13").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CampoFecha;
    private javax.swing.JPanel Formulario;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonFormulario;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton btnlogo;
    private javax.swing.JTextField campoCanalizacion;
    private javax.swing.JTextField campoCarrera;
    private javax.swing.JTextField campoGrupo;
    private javax.swing.JTextField campoMatricula;
    private javax.swing.JTextField campoMotivo;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextArea campoResumen;
    private javax.swing.JTextField campoTipo;
    private javax.swing.JComboBox<String> comboEstatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox notificado;
    private javax.swing.JLabel resultadoError;
    // End of variables declaration//GEN-END:variables
}
