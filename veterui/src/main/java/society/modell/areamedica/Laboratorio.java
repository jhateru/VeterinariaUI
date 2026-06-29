package society.modell.areamedica;

import society.modell.recepcion.Paciente;
import java.time.LocalDateTime;

public class Laboratorio {
    private int id;
    private Paciente paciente;
    private String tipoExamen;
    private String doctorAsignado;
    private String prioridad; // Normal, URGENTE
    private String estado;    // En Proceso, Pendiente Recolección, Resultado Listo
    private String resultadosCortos; // Para la tarjeta de Resultados Recientes
    private boolean requiereAtencion; 
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResultado;
    
    // Campo temporal simulado (ej: "hace 12 min") para propósitos de UI
    private String tiempoFinalizadoStr; 

    public Laboratorio() {}

    public Laboratorio(int id, Paciente paciente, String tipoExamen, String doctorAsignado, String prioridad, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.tipoExamen = tipoExamen;
        this.doctorAsignado = doctorAsignado;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }
    public String getDoctorAsignado() { return doctorAsignado; }
    public void setDoctorAsignado(String doctorAsignado) { this.doctorAsignado = doctorAsignado; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getResultadosCortos() { return resultadosCortos; }
    public void setResultadosCortos(String resultadosCortos) { this.resultadosCortos = resultadosCortos; }
    public boolean isRequiereAtencion() { return requiereAtencion; }
    public void setRequiereAtencion(boolean requiereAtencion) { this.requiereAtencion = requiereAtencion; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public LocalDateTime getFechaResultado() { return fechaResultado; }
    public void setFechaResultado(LocalDateTime fechaResultado) { this.fechaResultado = fechaResultado; }
    public String getTiempoFinalizadoStr() { return tiempoFinalizadoStr; }
    public void setTiempoFinalizadoStr(String tiempoFinalizadoStr) { this.tiempoFinalizadoStr = tiempoFinalizadoStr; }
}
