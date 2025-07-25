/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;

import clases.Conexion;
import clases.Solicitud;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**

 *
 * @author jobno
 */
public class CrearSolicitud extends javax.swing.JFrame {
    JFrame regresa;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CrearSolicitud.class.getName());
    String id;
    int idSolicitud;

    /**
     * Creates new form RegistrarAlumno
     */

    public CrearSolicitud(JFrame pantalla,String idAlumno) {
        regresa = pantalla;
        id = idAlumno;
        initComponents();
        darEstilos();
        cargarDatos(id);
    }
    
    public void cargarDatos(String id){
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;

        String sql = "SELECT a.*, c.nombreCarrera AS nombre_carrera FROM alumno a INNER JOIN carrera c ON a.idCarrera = c.idCarrera WHERE a.idAlumno = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet datos = ps.executeQuery();
        if(datos.next()){
            String nombre = datos.getString("nombreAlumno");
            campoNombre.setText(nombre);

            String matricula = datos.getString("matricula");
            campoMatricula.setText(matricula);

            String grupo = datos.getString("grupo");
            campoGrupo.setText(grupo);

            String carrera = datos.getString("nombre_carrera");
            campoCarrera.setText(carrera);

            String telefono = datos.getString("telefonoAlumno");
            if(telefono != null){
                campoTelefono.setText(telefono);
            };
            datos.close();
            ps.close();
            con.close();
        };
        
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
    }
    
    
    
    public void darEstilos(){
                campoArgumentacion.setLineWrap(true);
                campoArgumentacion.setWrapStyleWord(true);
                
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String fechaFormateada = hoy.format(formato);
                
                campoFecha.setText(fechaFormateada);
                campoFecha.setEditable(false);
                campoFecha.putClientProperty("JComponent.roundRect", true);
                
                campoEstatus.setText("En Creacion");
                //campoEstatus.putClientProperty("JComponent.roundRect", true);
                campoEstatus.setEditable(false);
                //campoNombre.putClientProperty("JComponent.roundRect", true);
                campoNombre.setEditable(false);
                
                //campoMatricula.putClientProperty("JComponent.roundRect", true);
                campoMatricula.setEditable(false);
                
                //campoGrupo.putClientProperty("JComponent.roundRect", true);
                
                
                //campoCarrera.putClientProperty("JComponent.roundRect", true);
                campoCarrera.setEditable(false);
                
                //campoCanaliza.putClientProperty("JComponent.roundRect", true);
                
                //campoTelefono.putClientProperty("JComponent.roundRect", true);
                
                /*campoFamiliar.putClientProperty("JComponent.roundRect", true);
                
                campoTelefonoFamiliar.putClientProperty("JComponent.roundRect", true);
                
                comboMotivo.putClientProperty("JComponent.roundRect", true);*/
                comboMotivo.addItem("Socioeconomico");
                comboMotivo.addItem("Salud");
                comboMotivo.addItem("Familiar");
                
                //campoArgumentacion.putClientProperty("JComponent.roundRect", true);
                
                //comboTipo.putClientProperty("JComponent.roundRect", true);*/
                comboTipo.addItem("Llamada");
                comboTipo.addItem("Oficina");
                comboTipo.addItem("Visita");
                
                btnlogo.setBorderPainted(false);
    }
    
    public int ConocerIdSolicitud(){
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;

        String sql = "SELECT MAX(idSolicitud) FROM solicitud";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet datos = ps.executeQuery();
        if(datos.next()){
             idSolicitud = datos.getInt("MAX(idSolicitud)");
        }
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
        return idSolicitud;
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
        btnGuardar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();
        panelDatosGenerales = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoFecha = new javax.swing.JTextField();
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
        comboMotivo = new javax.swing.JComboBox<>();
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
        comboTipo = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        btnCancelar.setBackground(new java.awt.Color(255, 102, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(153, 255, 153));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(530, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        campoFecha.setBackground(new java.awt.Color(195, 210, 197));
        campoFecha.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        campoFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campoFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoFechaActionPerformed(evt);
            }
        });
        jPanel1.add(campoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 110, -1));

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

        comboMotivo.setBackground(new java.awt.Color(195, 210, 197));
        comboMotivo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jPanel1.add(comboMotivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 150, 150, -1));

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

        comboTipo.setBackground(new java.awt.Color(195, 210, 197));
        comboTipo.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jPanel1.add(comboTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, 150, -1));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel15.setText("Tipo:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, -1, -1));

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

    private void campoFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoFechaActionPerformed

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

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
         regresa.setVisible(true);
         dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        String idAlumno = id;
        String motivo = (String) comboMotivo.getSelectedItem();

        if(motivo.equals("Socioeconomico")){
            motivo = "1";
        }else if (motivo.equals("Salud")){

            motivo = "2";
        }else{
            motivo = "3";
        };
        String nombre = campoNombre.getText();
        System.out.println(nombre);
        String telefonoSolicitud = campoTelefono.getText();
        String grupo = campoGrupo.getText();
        try{
        Conexion conexion = new Conexion();
        Connection con = conexion.con;

        String sql = "UPDATE alumno SET nombreAlumno = ?, telefonoAlumno = ?, grupo = ? WHERE idAlumno = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);       
        ps.setString(2, telefonoSolicitud);
        ps.setString(3, grupo);
        ps.setString(4, id);
        ps.executeUpdate();
        }catch (Exception e){
            System.out.println("error al actualizar: " + e);
        }
        
        String canaliza = campoCanaliza.getText();
        String familiar = campoFamiliar.getText();
        String telefonoFamiliar = campoTelefonoFamiliar.getText();
        String tipo = (String) comboTipo.getSelectedItem();

        if(tipo.equals("Llamada")){
            tipo = "1";
        }else if(tipo.equals("Oficina") ){

            tipo = "2";
        }else{
            tipo = "3";
        };
        String argumentacion = campoArgumentacion.getText();
        
        Solicitud solicitud = new Solicitud(idAlumno, motivo, telefonoSolicitud, canaliza, familiar, telefonoFamiliar, tipo, argumentacion);
        if(solicitud.Guardar()){
            showMessageDialog(null, "Guardado" );
            
            int idSoli = ConocerIdSolicitud();

            VerSolicitud soli = new VerSolicitud(regresa,idSoli);

            soli.setVisible(true);
            soli.setLocationRelativeTo(null);
            //cambiamos de pantalla
            dispose();

        }
        

    }//GEN-LAST:event_btnGuardarActionPerformed

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

        //java.awt.EventQueue.invokeLater(() -> new CrearSolicitud( "anterior" ,"1").setVisible(true));

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnlogo;
    private javax.swing.JTextArea campoArgumentacion;
    private javax.swing.JTextField campoCanaliza;
    private javax.swing.JTextField campoCarrera;
    private javax.swing.JTextField campoEstatus;
    private javax.swing.JTextField campoFamiliar;
    private javax.swing.JTextField campoFecha;
    private javax.swing.JTextField campoGrupo;
    private javax.swing.JTextField campoMatricula;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoTelefono;
    private javax.swing.JTextField campoTelefonoFamiliar;
    private javax.swing.JComboBox<String> comboMotivo;
    private javax.swing.JComboBox<String> comboTipo;
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
