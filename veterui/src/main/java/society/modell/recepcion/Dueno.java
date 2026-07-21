package society.modell.recepcion;
import society.modell.Entity;

import java.util.List;
import java.util.ArrayList;

public class Dueno {
    public enum EstadoDueno {
        ACTIVO, FRECUENTE, DEUDA_PENDIENTE, NUEVO
    }

    private int id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private String direccion;
    private EstadoDueno estado;
    private List<Paciente> mascotas;
    
    private String genero;
    private String fechaNacimiento;
    private String telefonoEmergencia;
    private String ciudad;
    private String codigoPostal;

    public Dueno() {
        this.mascotas = new ArrayList<>();
        this.estado = EstadoDueno.NUEVO;
    }

    public Dueno(int id, String nombre, String apellidos, String dni, String telefono, String email, String direccion, EstadoDueno estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.mascotas = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public EstadoDueno getEstado() { return estado; }
    public void setEstado(EstadoDueno estado) { this.estado = estado; }
    public List<Paciente> getMascotas() { return mascotas; }
    public void setMascotas(List<Paciente> mascotas) { this.mascotas = mascotas; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
}


