package society.modell.configuracion;

public class ConfiguracionClinica {
    private String nombreClinica;
    private String direccion;
    private String telefono;
    private String ruc;
    private String logoPath;
    private String horarioAtencion;

    public ConfiguracionClinica() {}

    public ConfiguracionClinica(String nombreClinica, String direccion, String telefono, String ruc, String logoPath, String horarioAtencion) {
        this.nombreClinica = nombreClinica;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ruc = ruc;
        this.logoPath = logoPath;
        this.horarioAtencion = horarioAtencion;
    }

    public String getNombreClinica() { return nombreClinica; }
    public void setNombreClinica(String nombreClinica) { this.nombreClinica = nombreClinica; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }
}
