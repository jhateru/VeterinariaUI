package society.modell.configuracion;

public class Configuracion {
    private int id;
    private String nombreClinica;
    private String idFiscal;
    private String direccion;
    private String telefono;
    private String correo;
    private String idioma;
    private String moneda;
    private boolean dosFaActivo;
    private boolean notifRecordatorioCitas;
    private boolean notifResultadosLab;
    private boolean notifResumenSemanal;

    public Configuracion() {}

    public Configuracion(int id, String nombreClinica, String idFiscal, String direccion, String telefono, String correo, String idioma, String moneda, boolean dosFaActivo, boolean notifRecordatorioCitas, boolean notifResultadosLab, boolean notifResumenSemanal) {
        this.id = id;
        this.nombreClinica = nombreClinica;
        this.idFiscal = idFiscal;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.idioma = idioma;
        this.moneda = moneda;
        this.dosFaActivo = dosFaActivo;
        this.notifRecordatorioCitas = notifRecordatorioCitas;
        this.notifResultadosLab = notifResultadosLab;
        this.notifResumenSemanal = notifResumenSemanal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreClinica() { return nombreClinica; }
    public void setNombreClinica(String nombreClinica) { this.nombreClinica = nombreClinica; }
    public String getIdFiscal() { return idFiscal; }
    public void setIdFiscal(String idFiscal) { this.idFiscal = idFiscal; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public boolean isDosFaActivo() { return dosFaActivo; }
    public void setDosFaActivo(boolean dosFaActivo) { this.dosFaActivo = dosFaActivo; }
    public boolean isNotifRecordatorioCitas() { return notifRecordatorioCitas; }
    public void setNotifRecordatorioCitas(boolean notifRecordatorioCitas) { this.notifRecordatorioCitas = notifRecordatorioCitas; }
    public boolean isNotifResultadosLab() { return notifResultadosLab; }
    public void setNotifResultadosLab(boolean notifResultadosLab) { this.notifResultadosLab = notifResultadosLab; }
    public boolean isNotifResumenSemanal() { return notifResumenSemanal; }
    public void setNotifResumenSemanal(boolean notifResumenSemanal) { this.notifResumenSemanal = notifResumenSemanal; }
}
