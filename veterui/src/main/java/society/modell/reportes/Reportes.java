package society.modell.reportes;

public class Reportes {
    private String id;
    private String veterinarioNombre;
    private int consultas;
    private double calificacion;
    private String carga;

    public Reportes() {
    }

    public Reportes(String id, String veterinarioNombre, int consultas, double calificacion, String carga) {
        this.id = id;
        this.veterinarioNombre = veterinarioNombre;
        this.consultas = consultas;
        this.calificacion = calificacion;
        this.carga = carga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVeterinarioNombre() {
        return veterinarioNombre;
    }

    public void setVeterinarioNombre(String veterinarioNombre) {
        this.veterinarioNombre = veterinarioNombre;
    }

    public int getConsultas() {
        return consultas;
    }

    public void setConsultas(int consultas) {
        this.consultas = consultas;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }
}
