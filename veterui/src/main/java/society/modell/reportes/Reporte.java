package society.modell.reportes;

import java.time.LocalDateTime;

public class Reporte {
    private int id;
    private String tipo;
    private LocalDateTime fechaGeneracion;
    private String parametros;

    public Reporte() {}

    public Reporte(int id, String tipo, LocalDateTime fechaGeneracion, String parametros) {
        this.id = id;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.parametros = parametros;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }
}
