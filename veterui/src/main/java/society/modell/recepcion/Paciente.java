package society.modell.recepcion;
import society.modell.Entity;

import java.time.LocalDate;

public class Paciente {
    public enum EspecieAnimal {
        PERRO, GATO, EXOTICO
    }

    public enum EstadoPaciente {
        EN_CLINICA, ALTA, SEGUIMIENTO
    }

    private int id;
    private String nombre;
    private EspecieAnimal especie;
    private String raza;
    private String sexo;
    private LocalDate fechaNacimiento; // Can be null if only age is known
    private String edadAproximada; // For UI like "4 años", "6 meses"
    private double peso;
    
    // Relación con el dueño
    private int duenoId;
    
    // Detalles Médicos Iniciales
    private String alergias;
    private String motivoPrimeraConsulta;
    private String ultimaVisita; // String formatted for UI like "12 Oct, 2023" or "Hoy, 09:15"
    private EstadoPaciente estado;

    public Paciente() {
        this.estado = EstadoPaciente.EN_CLINICA;
    }

    public Paciente(int id, String nombre, EspecieAnimal especie, String raza, String sexo, String edadAproximada, double peso, 
                    int duenoId,
                    String alergias, String motivoPrimeraConsulta, String ultimaVisita, EstadoPaciente estado) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edadAproximada = edadAproximada;
        this.peso = peso;
        this.duenoId = duenoId;
        this.alergias = alergias;
        this.motivoPrimeraConsulta = motivoPrimeraConsulta;
        this.ultimaVisita = ultimaVisita;
        this.estado = estado;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public EspecieAnimal getEspecie() { return especie; }
    public void setEspecie(EspecieAnimal especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getEdadAproximada() { return edadAproximada; }
    public void setEdadAproximada(String edadAproximada) { this.edadAproximada = edadAproximada; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    
    public int getDuenoId() { return duenoId; }
    public void setDuenoId(int duenoId) { this.duenoId = duenoId; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getMotivoPrimeraConsulta() { return motivoPrimeraConsulta; }
    public void setMotivoPrimeraConsulta(String motivoPrimeraConsulta) { this.motivoPrimeraConsulta = motivoPrimeraConsulta; }
    public String getUltimaVisita() { return ultimaVisita; }
    public void setUltimaVisita(String ultimaVisita) { this.ultimaVisita = ultimaVisita; }
    public EstadoPaciente getEstado() { return estado; }
    public void setEstado(EstadoPaciente estado) { this.estado = estado; }
}


