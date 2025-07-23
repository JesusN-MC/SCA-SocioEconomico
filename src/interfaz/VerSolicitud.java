/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;

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
 * @author jobno
 */
public class VerSolicitud extends javax.swing.JFrame {
    
    JFrame regresa, actual;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VerSolicitud.class.getName());
    
    int idSolicitud;
    //Solicitud solic;
    

    /**
     * Creates new form verSolicitud
     */
    public VerSolicitud(JFrame pantalla,int idSoli ) {
        actual = this;
        regresa = pantalla;
        idSolicitud = idSoli;
        //this.solic = soli;

        initComponents();
        darEstilos();
        refrescar(idSoli);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refrescar(idSoli);
            }
        });
    }
    
    public void refrescar(int id1){
        cargarDatos(id1);
    }
    
    public void darEstilos(){
                btnlogo.setBorderPainted(false);
                btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png")));

                campoArgumentacion.setLineWrap(true);
                campoArgumentacion.setWrapStyleWord(true);
                campoArgumentacion.setEditable(false);

                campoFecha.setEditable(false);
                campoFecha.putClientProperty("JComponent.roundRect", true);
                
                campoEstatus.setEditable(false);
                campoEstatus.putClientProperty("JComponent.roundRect", true);
                
                campoNombre.putClientProperty("JComponent.roundRect", true);
                campoNombre.setEditable(false);
                        
                campoMatricula.putClientProperty("JComponent.roundRect", true);
                campoMatricula.setEditable(false);
                
                campoGrupo.putClientProperty("JComponent.roundRect", true);
                campoGrupo.setEditable(false);
                
                campoCarrera.putClientProperty("JComponent.roundRect", true);
                campoCarrera.setEditable(false);
                
                campoCanaliza.putClientProperty("JComponent.roundRect", true);
                campoCanaliza.setEditable(false);
                
                campoTelefono.putClientProperty("JComponent.roundRect", true);
                campoTelefono.setEditable(false);
                
                campoFamiliar.putClientProperty("JComponent.roundRect", true);
                campoFamiliar.setEditable(false);
                
                campoTelefonoFamiliar.putClientProperty("JComponent.roundRect", true);
                campoTelefonoFamiliar.setEditable(false);
                
                campoMotivo.putClientProperty("JComponent.roundRect", true);
                campoMotivo.setEditable(false);
                
                campoArgumentacion.putClientProperty("JComponent.roundRect", true);
                campoArgumentacion.setEditable(false);
                
                campoTipo.putClientProperty("JComponent.roundRect", true);
                campoTipo.setEditable(false);
    }
    
    public void cargarDatos(int idSoli){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT s.*, a.nombreAlumno, a.matricula, a.grupo, c.nombreCarrera   FROM solicitud s INNER JOIN alumno a ON s.idAlumno = a.idAlumno INNER JOIN carrera c ON a.idCarrera = c.idCarrera WHERE idSolicitud = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSoli);
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                String fecha = datos.getString("fecha");
                campoFecha.setText(fecha);
                
                String estatus1 = datos.getString("estatus");
                String estatus;

                if(estatus1.equals("0")){

                    estatus = "Pendiente";
                }else{
                    estatus = "Completada";
                };
                campoEstatus.setText(estatus);
                
                String nombreAlumno =  datos.getString("nombreAlumno");
                campoNombre.setText(nombreAlumno);
                
                String matricula = datos.getString("matricula");
                campoMatricula.setText(matricula);
                
                String grupo = datos.getString("grupo");
                campoGrupo.setText(grupo);
                
                String motivo = datos.getString("motivo");

                if(motivo.equals("1")){
                    motivo = "Socioeconomico";
                }else if (motivo.equals("2")){

                    motivo = "Salud";
                }else{
                    motivo = "Familiar";
                };
                campoMotivo.setText(motivo);
                
                String carrera = datos.getString("nombreCarrera");
                campoCarrera.setText(carrera);
                
                String canaliza = datos.getString("canaliza");
                campoCanaliza.setText(canaliza);
                
                String telefono = datos.getString("telefonoSolicitud");
                campoTelefono.setText(telefono);
                
                String familiar = datos.getString("familiar");
                campoFamiliar.setText(familiar);
                
                String telefonoFamiliar = datos.getString("telefonoFamiliar");
                campoTelefonoFamiliar.setText(telefonoFamiliar);
                
                String tipo =  datos.getString("tipo");

                if(tipo.equals("1")){
                    tipo = "Llamada";
                }else if (tipo.equals("2")){

                    tipo = "Oficina";
                }else{
                   tipo = "Visita";
                }
                campoTipo.setText(tipo);
                
                String argumentacion = datos.getString("argumentacion");
                campoArgumentacion.setText(argumentacion);
                
                
            }
            
        }catch(Exception e){
                showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        };
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
        btnCancelar = new javax.swing.JButton();
        btnFormulario = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();
        panelDatosGenerales = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoMotivo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        campoEstatus = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoMatricula = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        campoGrupo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        campoCanaliza = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        campoFamiliar = new javax.swing.JTextField();
        campoTelefonoFamiliar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        campoTelefono = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        campoArgumentacion = new javax.swing.JTextArea();
        campoCarrera = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        campoTipo = new javax.swing.JTextField();
        campoFecha = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        btnCancelar.setBackground(new java.awt.Color(83, 178, 167));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnFormulario.setBackground(new java.awt.Color(102, 153, 255));
        btnFormulario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnFormulario.setText("Ingresar al Formulario");
        btnFormulario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormularioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 489, Short.MAX_VALUE)
                .addComponent(btnFormulario)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 720, 40));

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnlogo.setBackground(new java.awt.Color(43, 138, 127));
        btnlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N
        jPanel4.add(btnlogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 40));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 40));

        panelDatosGenerales.setBackground(new java.awt.Color(153, 153, 153));
        panelDatosGenerales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel1.setText("Formato Solicitud");
        panelDatosGenerales.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, 20));

        jPanel1.add(panelDatosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 720, 23));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel2.setText("Fecha:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        campoMotivo.setBackground(new java.awt.Color(195, 210, 197));
        campoMotivo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoMotivo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMotivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoMotivoActionPerformed(evt);
            }
        });
        jPanel1.add(campoMotivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 150, 150, -1));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel3.setText("Estatus:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        campoEstatus.setBackground(new java.awt.Color(195, 210, 197));
        campoEstatus.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoEstatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoEstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoEstatusActionPerformed(evt);
            }
        });
        jPanel1.add(campoEstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 110, -1));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel4.setText("Nombre:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        campoMatricula.setBackground(new java.awt.Color(195, 210, 197));
        campoMatricula.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoMatricula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoMatriculaActionPerformed(evt);
            }
        });
        jPanel1.add(campoMatricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 120, -1));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel5.setText("Matricula:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, -1));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel6.setText("Grupo:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, -1, -1));

        campoGrupo.setBackground(new java.awt.Color(195, 210, 197));
        campoGrupo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoGrupo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoGrupoActionPerformed(evt);
            }
        });
        jPanel1.add(campoGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 150, 110, -1));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel7.setText("Carrera:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));

        campoNombre.setBackground(new java.awt.Color(195, 210, 197));
        campoNombre.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        campoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoNombreActionPerformed(evt);
            }
        });
        jPanel1.add(campoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 270, -1));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel8.setText("Canaliza:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 180, 80, -1));

        campoCanaliza.setBackground(new java.awt.Color(195, 210, 197));
        campoCanaliza.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoCanaliza.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        campoCanaliza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoCanalizaActionPerformed(evt);
            }
        });
        jPanel1.add(campoCanaliza, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 220, -1));

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel10.setText("Argumentacion:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, 20));

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel11.setText("Familiar:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, -1, -1));

        campoFamiliar.setBackground(new java.awt.Color(195, 210, 197));
        campoFamiliar.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoFamiliar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        campoFamiliar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoFamiliarActionPerformed(evt);
            }
        });
        jPanel1.add(campoFamiliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 220, -1));

        campoTelefonoFamiliar.setBackground(new java.awt.Color(195, 210, 197));
        campoTelefonoFamiliar.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoTelefonoFamiliar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoTelefonoFamiliar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTelefonoFamiliarActionPerformed(evt);
            }
        });
        jPanel1.add(campoTelefonoFamiliar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 250, 130, -1));

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel12.setText("Motivo:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 130, -1, -1));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel13.setText("Telefono Familiar:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, 120, -1));

        campoTelefono.setBackground(new java.awt.Color(195, 210, 197));
        campoTelefono.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTelefonoActionPerformed(evt);
            }
        });
        jPanel1.add(campoTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 120, -1));

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel14.setText("Telefono:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        campoArgumentacion.setBackground(new java.awt.Color(195, 210, 197));
        campoArgumentacion.setColumns(20);
        campoArgumentacion.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoArgumentacion.setRows(5);
        jScrollPane1.setViewportView(campoArgumentacion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 680, 160));

        campoCarrera.setBackground(new java.awt.Color(195, 210, 197));
        campoCarrera.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoCarrera.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        campoCarrera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoCarreraActionPerformed(evt);
            }
        });
        jPanel1.add(campoCarrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 440, -1));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel15.setText("Tipo:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, -1, -1));

        campoTipo.setBackground(new java.awt.Color(195, 210, 197));
        campoTipo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoTipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTipoActionPerformed(evt);
            }
        });
        jPanel1.add(campoTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, 150, -1));

        campoFecha.setBackground(new java.awt.Color(195, 210, 197));
        campoFecha.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoFechaActionPerformed(evt);
            }
        });
        jPanel1.add(campoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 110, -1));

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


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        regresa.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnFormularioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFormularioActionPerformed
        // TODO add your handling code here:
        
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;
        
        String sql = "SELECT estatus, estatusNotificado FROM atencion WHERE idSolicitud = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idSolicitud);
        ResultSet datos = ps.executeQuery();
        
        
        if(datos.next()){
            String estatus = datos.getString("estatus");
            String estatusNotificado = datos.getString("estatusNotificado");
            if((estatus.equals("3") || estatus.equals("4")) && estatusNotificado.equals("1")){
                //aqui queda pendiente para hacer una instancia a una pantalla de atencion completada
                
            }else{
                String id = Integer.toString(idSolicitud);
                EditarAtencion editar = new EditarAtencion(actual,id);
                editar.setVisible(true);
                editar.setLocationRelativeTo(null);
                //cambiamos de pantalla
                this.setVisible(false);
            }
        }else{
                String id = Integer.toString(idSolicitud);
                EditarAtencion editar = new EditarAtencion(actual,id);
                
                editar.setVisible(true);
                editar.setLocationRelativeTo(null);
                this.setVisible(false);
                
        }
        datos.close();
        ps.close();
        con.close();
        
        }catch(Exception e){
            showMessageDialog(null, "Error al cambiar de pantalla" + e.getMessage());
        }
    }//GEN-LAST:event_btnFormularioActionPerformed


    private void campoMotivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoMotivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoMotivoActionPerformed

    private void campoEstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoEstatusActionPerformed

    private void campoMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoMatriculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoMatriculaActionPerformed

    private void campoGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoGrupoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoGrupoActionPerformed

    private void campoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoNombreActionPerformed

    private void campoCanalizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoCanalizaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoCanalizaActionPerformed

    private void campoFamiliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoFamiliarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoFamiliarActionPerformed

    private void campoTelefonoFamiliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTelefonoFamiliarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTelefonoFamiliarActionPerformed

    private void campoTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTelefonoActionPerformed

    private void campoCarreraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoCarreraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoCarreraActionPerformed

    private void campoTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTipoActionPerformed

    private void campoFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoFechaActionPerformed

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
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());    
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        /* Create and display the form */

        //java.awt.EventQueue.invokeLater(() -> new VerSolicitud("login", 13).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFormulario;
    private javax.swing.JButton btnlogo;
    private javax.swing.JTextArea campoArgumentacion;
    private javax.swing.JTextField campoCanaliza;
    private javax.swing.JTextField campoCarrera;
    private javax.swing.JTextField campoEstatus;
    private javax.swing.JTextField campoFamiliar;
    private javax.swing.JTextField campoFecha;
    private javax.swing.JTextField campoGrupo;
    private javax.swing.JTextField campoMatricula;
    private javax.swing.JTextField campoMotivo;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoTelefono;
    private javax.swing.JTextField campoTelefonoFamiliar;
    private javax.swing.JTextField campoTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelDatosGenerales;
    // End of variables declaration//GEN-END:variables
}
