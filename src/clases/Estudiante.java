
package clases;



public class Estudiante {
    public int id, fkcarrera;
    public String nombre, matricula, grupo, telefono,fechaRegistro;
    public boolean estatus;
    

   public Estudiante(int id, int fkcarrera, String nombre, String matricula, String grupo, String telefono) {
        this.id = id;
        this.fkcarrera = fkcarrera;
        this.nombre = nombre;
        this.matricula = matricula;
        this.grupo = grupo;
        this.telefono = telefono;

}

    public Estudiante(int id, int fkcarrera, String nombre, String matricula, String grupo, String telefono, String fechaRegistro, boolean estatus) {
        this.id = id;
        this.fkcarrera = fkcarrera;
        this.nombre = nombre;
        this.matricula = matricula;
        this.grupo = grupo;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.estatus = estatus;
    }

    public Estudiante(String matricula, String nombre, String grupo, String fechaRegistro) {
        this.matricula = matricula;        
        this.nombre = nombre;
        this.grupo = grupo;
        this.fechaRegistro = fechaRegistro;
    }

    public Estudiante(int id, String nombre, String matricula, String grupo, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.matricula = matricula;
        this.grupo = grupo;
        this.fechaRegistro = fechaRegistro;
    }
    //Menu estudiantes.
    
   
   

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public int getFkcarrera() {
        return fkcarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
   
    

    @Override
    public String toString() {
        return nombre.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    
    }
    

