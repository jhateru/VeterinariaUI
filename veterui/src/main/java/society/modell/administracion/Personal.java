package society.modell.administracion;

import society.modell.Entity;

public class Personal implements Entity {
    // Existing fields
    private int id;
    private String nombre;
    private String cargo;
    private String departamento;
    private String estado;
    private String email;
    private String telefono;
    
    // New fields from RegistroNuevoEmpleado form
    private String dni;
    private String fechaNacimiento;
    private String genero;
    private String direccion;
    private String especialidad;
    private String colegiado;
    private String fechaContratacion;
    private String username;
    private String password;
    private String rolSistema;
    private String turno;
    private String diasLaborales;


    public Personal() {}

    public Personal(int id, String nombre, String cargo, String departamento, String estado, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.departamento = departamento;
        this.estado = estado;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Getters and Setters for New Fields
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this. genero = genero; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getColegiado() { return colegiado; }
    public void setColegiado(String colegiado) { this.colegiado = colegiado; }

    public String getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(String fechaContratacion) { this.fechaContratacion = fechaContratacion; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRolSistema() { return rolSistema; }
    public void setRolSistema(String rolSistema) { this.rolSistema = rolSistema; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getDiasLaborales() { return diasLaborales; }
    public void setDiasLaborales(String diasLaborales) { this.diasLaborales = diasLaborales; }
}
