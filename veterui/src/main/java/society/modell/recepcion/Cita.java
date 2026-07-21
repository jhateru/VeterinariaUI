package society.modell.recepcion;
import society.modell.Entity;

import java.time.LocalDateTime;

public class Cita {
    public enum EstadoCita {
        PENDIENTE, COMPLETADA, CANCELADA, URGENCIA
    }

    private int id;
    private LocalDateTime fechaHora;
    private EstadoCita estado;
    private String motivo;
    
    // References for full DB relationships
    private int pacienteId;
    private int veterinarioId;

    public Cita() {
        this.estado = EstadoCita.PENDIENTE;
    }

    public Cita(int id, LocalDateTime fechaHora, EstadoCita estado, int pacienteId, int veterinarioId, String motivo) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.pacienteId = pacienteId;
        this.veterinarioId = veterinarioId;
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
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(int veterinarioId) { this.veterinarioId = veterinarioId; }
    
    // Dynamic lookup for FXML compatibility
    public String getPacienteNombre() {
        society.modell.recepcion.Paciente p = new society.dao.PacienteDao().getById(this.pacienteId);
        return p != null ? p.getNombre() : "Desconocido";
    }
    
    public String getVeterinarioNombre() {
        society.modell.administracion.Personal per = new society.dao.PersonalDao().getById(this.veterinarioId);
        return per != null ? per.getNombre() : "Desconocido";
    }
}


