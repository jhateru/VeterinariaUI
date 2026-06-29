package society.modell.areamedica;

import society.modell.recepcion.Paciente;
import java.time.LocalDate;

public class HistoriaClinica {
    private int id;
    private Paciente paciente;
    private LocalDate fechaApertura;
    private String alergias;
    private String antecedentes;
    private double pesoActual;
    private String evoluciones; // e.g., "Consulta General - Dermatología;14 Octubre 2023;Paciente presenta prurito...|Vacunación...;..."
    private String tratamientos; // e.g., "Apoquel 16mg;1 tableta cada 24 horas|Bravecto (Spot-on);Dosis única trimestral"

    public HistoriaClinica() {
    }

    public HistoriaClinica(int id, Paciente paciente, LocalDate fechaApertura, String alergias, String antecedentes, double pesoActual, String evoluciones, String tratamientos) {
        this.id = id;
        this.paciente = paciente;
        this.fechaApertura = fechaApertura;
        this.alergias = alergias;
        this.antecedentes = antecedentes;
        this.pesoActual = pesoActual;
        this.evoluciones = evoluciones;
        this.tratamientos = tratamientos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getAntecedentes() { return antecedentes; }
    public void setAntecedentes(String antecedentes) { this.antecedentes = antecedentes; }
    public double getPesoActual() { return pesoActual; }
    public void setPesoActual(double pesoActual) { this.pesoActual = pesoActual; }
    public String getEvoluciones() { return evoluciones; }
    public void setEvoluciones(String evoluciones) { this.evoluciones = evoluciones; }
    public String getTratamientos() { return tratamientos; }
    public void setTratamientos(String tratamientos) { this.tratamientos = tratamientos; }
}
