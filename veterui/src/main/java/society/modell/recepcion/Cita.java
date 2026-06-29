package society.modell.recepcion;

import java.time.LocalDateTime;

public class Cita {
    public enum EstadoCita {
        PENDIENTE, COMPLETADA, CANCELADA, URGENCIA
    }

    private int id;
    private LocalDateTime fechaHora;
    private EstadoCita estado;
    private String motivo;
    
    // String representations for simple UI/CSV without full complex joins
    private String pacienteNombre;
    private String veterinarioNombre;
    
    // References for full DB relationships
    private Paciente paciente;
    // private Veterinario asignado; // We'll link this from administracion later

    public Cita() {
        this.estado = EstadoCita.PENDIENTE;
    }

    public Cita(int id, LocalDateTime fechaHora, EstadoCita estado, String pacienteNombre, String veterinarioNombre, String motivo) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.pacienteNombre = pacienteNombre;
        this.veterinarioNombre = veterinarioNombre;
        this.motivo = motivo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getVeterinarioNombre() { return veterinarioNombre; }
    public void setVeterinarioNombre(String veterinarioNombre) { this.veterinarioNombre = veterinarioNombre; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
}
