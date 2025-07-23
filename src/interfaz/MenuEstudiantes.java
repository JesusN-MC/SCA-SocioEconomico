/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;

import clases.Carrera;
import clases.Conexion;
import clases.Estudiante;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/**
 *
 * @author jobno
 */
public class MenuEstudiantes extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuEstudiantes.class.getName());

    /**
     * Creates new form MenuEstudiantes
     */
    public MenuEstudiantes() {
        initComponents();
        DarEstilos();
        refrescar();
        evento();
    }
    
    public void refrescar(){
        mostrarEstudiante();
    }
    
    public void DarEstilos(){
        btnlogo.setBorderPainted(false);
        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/busqueda.png")));
        btnBusqueda.setBorderPainted(false);
        campoBusqueda.putClientProperty("Component.arc",      20);
        campoBusqueda.putClientProperty("JComponent.roundRect", true);
        
        //estilos a encabezado de la tabla 
        JTableHeader header = tabla_estudiantes.getTableHeader();
        header.setFont(new Font("Poppins", Font.BOLD, 12));
        header.setBackground(new Color(190, 190, 190));
        header.setForeground(Color.BLACK);
        header.setOpaque(true);
        
    }
    
    //esta funcion sirve para agregar un evento que elimine el texto por defecto al hacer clic
    public void evento(){
        campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campoBusqueda.getText().equals("Ingresa la Matricula del Alumno")) {
                    campoBusqueda.setText("");
                }
            }
        });
    }
    

    public void mostrarEstudiante(){

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Matricula");
        modelo.addColumn("Estudiante");
        modelo.addColumn("Carrera");
        modelo.addColumn("Grupo");
        modelo.addColumn("Fecha Registro");
        
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            String sql2 = "SELECT a.*,c.nombreCarrera FROM alumno a INNER JOIN carrera c ON a.idCarrera=c.idCarrera WHERE matricula = ?;"; 
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, campoBusqueda.getText());
            ResultSet datos2 = ps2.executeQuery();
            ResultSet datos;
            
            if(datos2.next()){
                PreparedStatement ps3 = con.prepareStatement(sql2);
                ps3.setString(1, campoBusqueda.getText());
                datos = ps3.executeQuery();
                System.out.println("alumno encontrado: " + datos2.getString("nombreAlumno"));
            }else{
                String sql = "SELECT a.*,c.nombreCarrera FROM alumno a INNER JOIN carrera c ON a.idCarrera=c.idCarrera;";
                PreparedStatement ps = con.prepareStatement(sql);
                datos = ps.executeQuery();
            }
            ArrayList<Estudiante> datosEstudiante = new ArrayList<>();
            
            while(datos.next()){
            int id= datos.getInt("idAlumno");
            String matricula = datos.getString("matricula");
            String estudiante = datos.getString("nombreAlumno");
            String carrera = datos.getString("nombreCarrera");
            String grupo = datos.getString("grupo");
            String fecha = datos.getString("fechaRegistro");
            Estudiante alumno = new Estudiante( id, estudiante, matricula, grupo, fecha);
            Carrera carreras = new Carrera(carrera);
                
            
            modelo.addRow(new Object[]{
            alumno.getMatricula(),
            alumno.getNombre(),
            carreras.getNombreCarrera(),
            alumno.getGrupo(),
            alumno.getFechaRegistro()
            });
            datosEstudiante.add(alumno);
            }
            tabla_estudiantes.setModel(modelo);
            
            //con esto defines las medidas de la tabla
            tabla_estudiantes.getColumnModel().getColumn(0).setPreferredWidth(80);  // Matrícula
            tabla_estudiantes.getColumnModel().getColumn(1).setPreferredWidth(280); // Estudiante
            tabla_estudiantes.getColumnModel().getColumn(2).setPreferredWidth(180); // Carrera
            tabla_estudiantes.getColumnModel().getColumn(3).setPreferredWidth(60);  // Grupo
            tabla_estudiantes.getColumnModel().getColumn(4).setPreferredWidth(120); // Fecha de Registro

            
            //esta linea remueve el los eventos para que no se agregue de manera repetida este evento al ejecutar la funcion refrescar
            for (MouseListener listener : tabla_estudiantes.getMouseListeners()) {
                tabla_estudiantes.removeMouseListener(listener);
           }
            
            tabla_estudiantes.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
            //La fila que seleccione
            int row =tabla_estudiantes.rowAtPoint(evt.getPoint());
            //La columna que seleccione
            int col =tabla_estudiantes.columnAtPoint(evt.getPoint());

            if((col==0)||(col==1)||(col==2)||(col==3)||(col==4)){
                Estudiante alumno= datosEstudiante.get(row);
                int idE = alumno.getId();
                VisualizarEstudiante o = new VisualizarEstudiante(idE);
                o.setVisible(true);
                o.setLocationRelativeTo(null);
                dispose();//Cierra la pantalla actual
            }
        }
        });
            
        }catch(Exception e){
            showMessageDialog(null, "Error al cargar los datos" + e.getMessage());
        }
            
        };

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
        botonAñadirAlumno = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        botonAtenciones = new javax.swing.JButton();
        botonSolicitudes = new javax.swing.JButton();
        botonEstudiantes = new javax.swing.JButton();
        btnBusqueda = new javax.swing.JButton();
        campoBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_estudiantes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(43, 138, 127));

        botonAñadirAlumno.setBackground(new java.awt.Color(102, 153, 255));
        botonAñadirAlumno.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonAñadirAlumno.setText("Nuevo Alumno");
        botonAñadirAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirAlumnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(598, Short.MAX_VALUE)
                .addComponent(botonAñadirAlumno)
                .addGap(16, 16, 16))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonAñadirAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, -1, -1));

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnlogo.setBackground(new java.awt.Color(43, 138, 127));
        btnlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N
        jPanel4.add(btnlogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 40));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 40));

        jPanel2.setBackground(new java.awt.Color(168, 204, 193));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonAtenciones.setBackground(new java.awt.Color(204, 204, 204));
        botonAtenciones.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        botonAtenciones.setText("Atenciones");
        botonAtenciones.setMargin(new java.awt.Insets(2, 0, 3, 0));
        botonAtenciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAtencionesActionPerformed(evt);
            }
        });
        jPanel2.add(botonAtenciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 100, 27));

        botonSolicitudes.setBackground(new java.awt.Color(204, 204, 204));
        botonSolicitudes.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        botonSolicitudes.setText("Solicitudes");
        botonSolicitudes.setMargin(new java.awt.Insets(2, 0, 3, 0));
        botonSolicitudes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSolicitudesActionPerformed(evt);
            }
        });
        jPanel2.add(botonSolicitudes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 100, 27));

        botonEstudiantes.setBackground(new java.awt.Color(80, 80, 80));
        botonEstudiantes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        botonEstudiantes.setForeground(new java.awt.Color(255, 255, 255));
        botonEstudiantes.setText("Estudiantes");
        botonEstudiantes.setMargin(new java.awt.Insets(2, 0, 3, 0));
        botonEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEstudiantesActionPerformed(evt);
            }
        });
        jPanel2.add(botonEstudiantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, -1));

        btnBusqueda.setBackground(new java.awt.Color(255, 255, 255));
        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/busqueda.png"))); // NOI18N
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });
        jPanel2.add(btnBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 20, 20));

        campoBusqueda.setFont(new java.awt.Font("Poppins Medium", 0, 10)); // NOI18N
        campoBusqueda.setForeground(new java.awt.Color(102, 102, 102));
        campoBusqueda.setText("Ingresa la Matricula del Alumno");
        jPanel2.add(campoBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 208, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 720, 23));

        tabla_estudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Matricula", "Estudiante", "Carrera", "Grupo", "Fecha de Registro"
            }
        ));
        tabla_estudiantes.setColumnSelectionAllowed(true);
        tabla_estudiantes.setDragEnabled(true);
        jScrollPane1.setViewportView(tabla_estudiantes);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 720, 420));

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

    private void botonAtencionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAtencionesActionPerformed
        MenuAtenciones ma = new MenuAtenciones();           
            ma.setVisible(true);
            ma.setLocationRelativeTo(null);
            dispose();
    }//GEN-LAST:event_botonAtencionesActionPerformed

    private void botonSolicitudesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSolicitudesActionPerformed
        MenuSolicitudes ms = new MenuSolicitudes();
            ms.setVisible(true);
            ms.setLocationRelativeTo(null);
            dispose();
    }//GEN-LAST:event_botonSolicitudesActionPerformed

    private void botonEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEstudiantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEstudiantesActionPerformed

    private void botonAñadirAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirAlumnoActionPerformed
CrearEstudiante ver = new CrearEstudiante();
            //Indicamos que se hace visible
            ver.setVisible(true);
            ver.setLocationRelativeTo(null);
            //cerramos esta ventana
            dispose();

    }//GEN-LAST:event_botonAñadirAlumnoActionPerformed

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
        // TODO add your handling code here:
        this.refrescar();
        
        //para que regrese el texto si campoBusqueda esta vacio
        if((campoBusqueda.getText()).equals("")){
            campoBusqueda.setText("Ingresa la Matricula del Alumno");
        }
    }//GEN-LAST:event_btnBusquedaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MenuEstudiantes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAtenciones;
    private javax.swing.JButton botonAñadirAlumno;
    private javax.swing.JButton botonEstudiantes;
    private javax.swing.JButton botonSolicitudes;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnlogo;
    private javax.swing.JTextField campoBusqueda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla_estudiantes;
    // End of variables declaration//GEN-END:variables
}
