package society.modell.areamedica;

import society.modell.recepcion.Paciente;
import java.time.LocalDateTime;

public class Hospitalizacion {
    public enum EstadoHospitalizacion {
        INGRESADO, ALTA_MEDICA, DECESO
    }

    private int id;
    private Paciente paciente;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaAlta;
    private String motivo;
    private String jaulaAsignada;
    private EstadoHospitalizacion estado;
    
    // Nuevos campos para Registro de Ingreso
    private String estadoMedicoInicial; // Crítico, Estable, Reservado
    private String frecuenciaMonitoreo; // Cada 4 horas, etc.
    private String dietaEspecial;
    private String cronogramaMedicacion;
    private String alertasSignos;
    private String temperaturaIngreso;
    private String frecCardiacaIngreso;
    private String frecRespiratoriaIngreso;

    public Hospitalizacion() {
        this.estado = EstadoHospitalizacion.INGRESADO;
    }

    public Hospitalizacion(int id, Paciente paciente, LocalDateTime fechaIngreso, String motivo, String jaulaAsignada) {
        this.id = id;
        this.paciente = paciente;
        this.fechaIngreso = fechaIngreso;
        this.motivo = motivo;
        this.jaulaAsignada = jaulaAsignada;
        this.estado = EstadoHospitalizacion.INGRESADO;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getJaulaAsignada() { return jaulaAsignada; }
    public void setJaulaAsignada(String jaulaAsignada) { this.jaulaAsignada = jaulaAsignada; }
    public EstadoHospitalizacion getEstado() { return estado; }
    public void setEstado(EstadoHospitalizacion estado) { this.estado = estado; }

    public String getEstadoMedicoInicial() { return estadoMedicoInicial; }
    public void setEstadoMedicoInicial(String estadoMedicoInicial) { this.estadoMedicoInicial = estadoMedicoInicial; }
    public String getFrecuenciaMonitoreo() { return frecuenciaMonitoreo; }
    public void setFrecuenciaMonitoreo(String frecuenciaMonitoreo) { this.frecuenciaMonitoreo = frecuenciaMonitoreo; }
    public String getDietaEspecial() { return dietaEspecial; }
    public void setDietaEspecial(String dietaEspecial) { this.dietaEspecial = dietaEspecial; }
    public String getCronogramaMedicacion() { return cronogramaMedicacion; }
    public void setCronogramaMedicacion(String cronogramaMedicacion) { this.cronogramaMedicacion = cronogramaMedicacion; }
    public String getAlertasSignos() { return alertasSignos; }
    public void setAlertasSignos(String alertasSignos) { this.alertasSignos = alertasSignos; }
    public String getTemperaturaIngreso() { return temperaturaIngreso; }
    public void setTemperaturaIngreso(String temperaturaIngreso) { this.temperaturaIngreso = temperaturaIngreso; }
    public String getFrecCardiacaIngreso() { return frecCardiacaIngreso; }
    public void setFrecCardiacaIngreso(String frecCardiacaIngreso) { this.frecCardiacaIngreso = frecCardiacaIngreso; }
    public String getFrecRespiratoriaIngreso() { return frecRespiratoriaIngreso; }
    public void setFrecRespiratoriaIngreso(String frecRespiratoriaIngreso) { this.frecRespiratoriaIngreso = frecRespiratoriaIngreso; }
}
