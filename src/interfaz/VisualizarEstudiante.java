/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;
import clases.Conexion;
import clases.Estudiante;
import clases.Solicitud;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author jobno
 */
public class VisualizarEstudiante extends javax.swing.JFrame {
    JFrame actual = this;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisualizarEstudiante.class.getName());
    String idAl;
    int idAlu;
    /**
     * Creates new form MenuEstudiantes
     */
    public VisualizarEstudiante(int idAlumno) {
        idAlu=idAlumno;
        idAl=Integer.toString(idAlu);
        initComponents();
        refrescar();
        
        //cada que el usuario hace visible una pagina este evento se ejecuta y refresca la tabla 
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refrescar();
            }
        });
    }
    
    public void refrescar(){
        cargarDatos(idAlu);
        DarEstilos();
        mostrarListaSolicitudes(idAl);
    }
            
            
    public void mostrarListaSolicitudes(String id){/* Es la función 
        para mostra la lista*/
        DefaultTableModel modelo = new DefaultTableModel();/*
        Sepa para que sea, pero se queda*/
        modelo.addColumn("Fecha de Elaboracio");
        /*Le pones la primera columna que tengas en la tablá*/
        modelo.addColumn("Motivo");
        modelo.addColumn("Estatus");
        /*Pones el resto de campos, misma sintaxis modelo.addColumn("")*/
        
        //Inicias el try, yo copie y pegué del de Tareas
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            //Se queda igual
            
            //De aquí...
            String sql = "SELECT s.idSolicitud, s.fecha, a.idAlumno, a.nombreAlumno, s.estatus, s.motivo FROM alumno a INNER JOIN solicitud s ON a.idAlumno = s.idAlumno WHERE a.idAlumno = ?;";
            //SELECT s.idSolicitud, s.fecha, a.idAlumno, a.nombreAlumno, s.estatus, s.motivo FROM alumno a INNER JOIN solicitud s ON a.idAlumno=s.idAlumno WHERE s.idAlumno=?;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);//Agregue esto para que solo filtre las solicitudes con el id del estudiante seleccionado
            ResultSet datos = ps.executeQuery();
            //ps.setInt(1, Integer.parseInt(idAl));
            //ps.setInt(1, idAlumno);
            ArrayList<Solicitud> datosSolicitud = new ArrayList<>();
            
            while (datos.next()){//a aquí se queda igual,
                //solo adaptas la consulta
                int idSolicitud = datos.getInt("idSolicitud");
                /*Mando a llamar el id de la solicitud, sería el id
                de lo que quieras mandar a llamar, depende el orden 
                del constructor en la clase creo*/
                String fecha = datos.getString("fecha");
                int idA = datos.getInt("idAlumno");
                String nombreAlumno = datos.getString("nombreAlumno");
                //Pongo el resto de datos
                
                /*Este es para cuando tengo un CHAR, ejemplo, 1 y para
                decir que 1 sea equivalente a algo como "Socioeconomico"
                y es lo que me va a mostrar*/
                String estatus1 = datos.getString("estatus");
                String estatus;
                if(estatus1.equals("0")){
                    estatus = "Pendiente";
                }else{
                    estatus = "Completada";
                };
                //Mismo ejemplo  que el anterior pero con motivo
                String motivo = datos.getString("motivo");
                if(motivo.equals("1") ){
                    motivo = "Socioeconomico";
                }else if (motivo.equals("2") ){
                    motivo = "Salud";
                }else{
                    motivo = "Familiar";
                };
                
                //Aqui se que tiene que ver con las clases que utilicez
                Estudiante est = new Estudiante(idA, nombreAlumno);
                Solicitud soli = new Solicitud(idSolicitud, nombreAlumno, fecha, estatus, motivo);
                //
                modelo.addRow(new Object[]{
                soli.getFecha(),
                //est.getNombre(),
                soli.getMotivo(),
                soli.getEstatus(),
            });
                datosSolicitud.add(soli);
            }
        tabla_solicitudes.setModel(modelo);
        
        //esta linea remueve el los eventos para que no se agregue de manera repetida este evento al ejecutar la funcion refrescar
        for (MouseListener listener : tabla_solicitudes.getMouseListeners()) {
            tabla_solicitudes.removeMouseListener(listener);
        }
        
        tabla_solicitudes.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
            //La fila que seleccione
            int row =tabla_solicitudes.rowAtPoint(evt.getPoint());
            //La columna que seleccione
            int col =tabla_solicitudes.columnAtPoint(evt.getPoint());

            if((col==0)||(col==1)||(col==2)){
                Solicitud soli= datosSolicitud.get(row);
                int id = soli.getIdSolicitud();
                VerSolicitud o = new VerSolicitud(actual, id);
                o.setVisible(true);
                o.setLocationRelativeTo(null);
                dispose();//Cierra la pantalla actual
            }
        }
        });
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al cargar los datos solicitudes"+e.getMessage());
        }
            
        };
    public void DarEstilos(){
        
      btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png")));
      btnlogo.setBorderPainted(false);
        
      /*txtNombre.putClientProperty("JComponent.roundRect", true);
      
      txtMatricula.putClientProperty("JComponent.roundRect", true);
      
      txtGrupo.putClientProperty("JComponent.roundRect", true);
      
      txtCarrera.putClientProperty("JComponent.roundRect", true);*/

    }
    public void cargarDatos(int idAlumno){
        try{
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql = "SELECT a.*, c.nombreCarrera FROM carrera c INNER JOIN alumno a ON c.idCarrera = a.idCarrera WHERE idAlumno = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ResultSet datos = ps.executeQuery();
            
            if(datos.next()){
                String nombreAlumno = datos.getString("nombreAlumno");
                txtNombre.setText(nombreAlumno);
                
                String matricula = datos.getString("matricula");
                txtMatricula.setText(matricula);
                
                String grupo = datos.getString("grupo");
                txtGrupo.setText(grupo);
                
                String fkcarrera = datos.getString("nombreCarrera");
                txtCarrera.setText(fkcarrera);
            }
            
        }catch(Exception e){
                showMessageDialog(null, "Error al cargar los datos alumnos" + e.getMessage());
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

        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        botonCrearSoli = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_solicitudes = new javax.swing.JTable();
        txtNombre = new javax.swing.JTextField();
        txtMatricula = new javax.swing.JTextField();
        txtGrupo = new javax.swing.JTextField();
        txtCarrera = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Datos Generales del Estudiante");
        jPanel8.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, -1, -1));

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Solicitudes recientes ");
        jPanel11.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("Solicitudes recientes ");
        jPanel12.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel11.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 720, 23));

        jScrollPane3.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));

        botonCrearSoli.setBackground(new java.awt.Color(102, 153, 255));
        botonCrearSoli.setText("Crear solicitud");
        botonCrearSoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearSoliActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(83, 178, 167));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 544, Short.MAX_VALUE)
                .addComponent(botonCrearSoli)
                .addGap(16, 16, 16))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCrearSoli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, -1, -1));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Solicitudes recientes ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("Solicitudes recientes ");
        jPanel10.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 720, 23));

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("Solicitudes recientes ");
        jPanel13.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("Solicitudes recientes ");
        jPanel14.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel13.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 720, 23));

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 720, 23));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 720, 23));

        jLabel1.setText("Nombre:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel2.setText("Matricula:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, -1, -1));

        jLabel3.setText("Grupo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, -1, -1));

        jLabel4.setText("Carrera:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 50, -1));

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Historial Atenciones");
        jPanel9.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 720, 23));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha elaboracion", "Motivo", "Estatus"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 720, 150));

        jPanel15.setBackground(new java.awt.Color(204, 204, 204));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setText("Datos Generales del Estudiante");
        jPanel15.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, -1, -1));

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 720, 23));

        tabla_solicitudes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha elaboracion", "Motivo", "Estatus"
            }
        ));
        jScrollPane2.setViewportView(tabla_solicitudes);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 720, 130));

        txtNombre.setEditable(false);
        txtNombre.setBackground(new java.awt.Color(204, 204, 204));
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 230, -1));

        txtMatricula.setEditable(false);
        txtMatricula.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtMatricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 100, -1));

        txtGrupo.setEditable(false);
        txtGrupo.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 80, -1));

        txtCarrera.setEditable(false);
        txtCarrera.setBackground(new java.awt.Color(204, 204, 204));
        txtCarrera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarreraActionPerformed(evt);
            }
        });
        jPanel1.add(txtCarrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, 190, -1));

        jPanel5.setBackground(new java.awt.Color(43, 138, 127));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnlogo.setBackground(new java.awt.Color(43, 138, 127));
        btnlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N
        jPanel5.add(btnlogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 40));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCrearSoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearSoliActionPerformed
     CrearSolicitud crearSoli= new CrearSolicitud(this ,idAl);
     crearSoli.setVisible(true);
     crearSoli.setLocationRelativeTo(null);
     this.setVisible(false);
     

    }//GEN-LAST:event_botonCrearSoliActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        MenuEstudiantes re = new MenuEstudiantes();
        re.setVisible(true);
        re.setLocationRelativeTo(null);
        dispose();   
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCarreraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarreraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarreraActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VisualizarEstudiante(5).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCrearSoli;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnlogo;
    private javax.swing.JEditorPane jEditorPane1;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tabla_solicitudes;
    private javax.swing.JTextField txtCarrera;
    private javax.swing.JTextField txtGrupo;
    private javax.swing.JTextField txtMatricula;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
