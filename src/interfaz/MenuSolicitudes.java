/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaz;
import clases.Conexion;
import clases.Estudiante;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import clases.Solicitud;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



/**
 *
 * @author jobno
 */
public class MenuSolicitudes extends javax.swing.JFrame {
    JFrame actual = this;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuSolicitudes.class.getName());

    /**
     * Creates new form MenuEstudiantes
     */
    public MenuSolicitudes() {
        initComponents();
        DarEstilos();
        evento();
        
        
    } 
    
    public void refrescar(){
        mostrarListaSolicitudes();
    }
    
    public void DarEstilos(){/*A toda está función de DarEstilos
        no le moví nadita*/
        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/busqueda.png")));
        btnBusqueda.setBorderPainted(false);
        campoBusqueda.putClientProperty("Component.arc",      20);
        campoBusqueda.putClientProperty("JComponent.roundRect", true);
        btnlogo.setBorderPainted(false);
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
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refrescar();
            }
        });
    }
    
    public void mostrarListaSolicitudes(){/* Es la función 
        para mostra la lista*/
        DefaultTableModel modelo = new DefaultTableModel();/*
        Sepa para que sea, pero se queda*/
        modelo.addColumn("Fecha de Elaboracio");
        /*Le pones la primera columna que tengas en la tablá*/
        modelo.addColumn("Estudiante");
        modelo.addColumn("Estatus");
        modelo.addColumn("Motivo");
        /*Pones el resto de campos, misma sintaxis modelo.addColumn("")*/
        
        //Inicias el try, yo copie y pegué del de Tareas
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.con;
            //Se queda igual
            
            //hago una string sql2 para primero validar el valor del campoBusqueda
            String sql2 = "SELECT s.idSolicitud, s.fecha, a.idAlumno, a.nombreAlumno, s.estatus, s.motivo FROM alumno a INNER JOIN solicitud s WHERE a.idAlumno=s.idAlumno AND s.estatus = 0 AND a.matricula = ?;"; 
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, campoBusqueda.getText());
            ResultSet datos2 = ps2.executeQuery();
            ResultSet datos;
            //si existe vuelvo a hacer la consulta por que el.next me hizo perder el valor ahorita dentro del if lo muestra pero en el while avanza al siguiente y no existe
            if(datos2.next()){
                PreparedStatement ps3 = con.prepareStatement(sql2);
                ps3.setString(1, campoBusqueda.getText());
                datos = ps3.executeQuery();
                System.out.println("Consulta encontrada: ");
            }else{
            //De aquí...
                String sql = "SELECT s.idSolicitud, s.fecha, a.idAlumno, a.nombreAlumno, s.estatus, s.motivo FROM alumno a INNER JOIN solicitud s WHERE a.idAlumno=s.idAlumno AND s.estatus = 0;";
                PreparedStatement ps = con.prepareStatement(sql);
                datos = ps.executeQuery();
                System.out.println("consulta individual no encontrada");
            }
            
            ArrayList<Solicitud> datosSolicitud = new ArrayList<>();
            
            while (datos.next()){//a aquí se queda igual,
                //solo adaptas la consulta
                int idSolicitud = datos.getInt("idSolicitud");
                /*Mando a llamar el id de la solicitud, sería el id
                de lo que quieras mandar a llamar, depende el orden 
                del constructor en la clase creo*/
                String fecha = datos.getString("fecha");
                int idAlumno = datos.getInt("idAlumno");
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
                if(motivo.equals("1")){
                    motivo = "Socioeconomico";
                }else if (motivo.equals("2") ){
                    motivo = "Salud";
                }else{
                    motivo = "Familiar";
                };
                
                //Aqui se que tiene que ver con las clases que utilicez
                Estudiante est = new Estudiante(idAlumno, nombreAlumno);
                Solicitud soli = new Solicitud(idSolicitud, nombreAlumno, fecha, estatus, motivo);
                //
                modelo.addRow(new Object[]{
                soli.getFecha(),
                est.getNombre(),
                soli.getEstatus(),
                soli.getMotivo(),
                
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

            if((col==0)||(col==1)||(col==2)||(col==3)){
                Solicitud soli= datosSolicitud.get(row);
                int id = soli.getIdSolicitud();
                VerSolicitud o = new VerSolicitud(actual, id);
                o.setVisible(true);
                o.setLocationRelativeTo(null);
                dispose();
            }
        }
        });
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al cargar los datos"+e.getMessage());
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
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        botonSolicitudes = new javax.swing.JButton();
        botonEstudiantes = new javax.swing.JButton();
        btnBusqueda = new javax.swing.JButton();
        campoBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_solicitudes = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        btnlogo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(43, 138, 127));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, -1));

        jPanel2.setBackground(new java.awt.Color(168, 204, 193));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonSolicitudes.setBackground(new java.awt.Color(80, 80, 80));
        botonSolicitudes.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        botonSolicitudes.setForeground(new java.awt.Color(255, 255, 255));
        botonSolicitudes.setText("Solicitudes");
        botonSolicitudes.setMargin(new java.awt.Insets(2, 0, 3, 0));
        botonSolicitudes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSolicitudesActionPerformed(evt);
            }
        });
        jPanel2.add(botonSolicitudes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 100, 27));

        botonEstudiantes.setBackground(new java.awt.Color(204, 204, 204));
        botonEstudiantes.setText("Estudiantes");
        botonEstudiantes.setMargin(new java.awt.Insets(2, 0, 3, 0));
        botonEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEstudiantesActionPerformed(evt);
            }
        });
        jPanel2.add(botonEstudiantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, -1));

        btnBusqueda.setBackground(new java.awt.Color(255, 255, 255));
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

        tabla_solicitudes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Fecha de Elaboracion", "Estudiante", "Motivo", "Estatus"
            }
        ));
        jScrollPane1.setViewportView(tabla_solicitudes);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 720, 430));

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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSolicitudesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSolicitudesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonSolicitudesActionPerformed

    private void botonEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEstudiantesActionPerformed
        // TODO add your handling code here:
        MenuEstudiantes ver = new MenuEstudiantes();
            //Indicamos que se hace visible
            ver.setVisible(true);
            ver.setLocationRelativeTo(null);
            //cerramos esta ventana
            dispose();
    }//GEN-LAST:event_botonEstudiantesActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MenuSolicitudes().setVisible(true));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonEstudiantes;
    private javax.swing.JButton botonSolicitudes;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnlogo;
    private javax.swing.JTextField campoBusqueda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla_solicitudes;
    // End of variables declaration//GEN-END:variables
}
