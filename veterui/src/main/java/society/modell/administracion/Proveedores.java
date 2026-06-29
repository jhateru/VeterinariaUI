package society.modell.administracion;

public class Proveedores {
    private int id;
    private String idProveedorStr; // e.g. "PROV-0012"
    private String nombre;
    private String categoria;      // e.g. Medicamentos, Equipamiento, Alimentos, Cirugía
    private String contactoNombre;
    private String contactoTelefono;
    private String estado;         // e.g. Activo, En Revisión, Inactivo
    private String ultimaOrdenFecha; // e.g. "12 Oct, 2023"

    public Proveedores() {}

    public Proveedores(int id, String idProveedorStr, String nombre, String categoria, String contactoNombre, String contactoTelefono, String estado, String ultimaOrdenFecha) {
        this.id = id;
        this.idProveedorStr = idProveedorStr;
        this.nombre = nombre;
        this.categoria = categoria;
        this.contactoNombre = contactoNombre;
        this.contactoTelefono = contactoTelefono;
        this.estado = estado;
        this.ultimaOrdenFecha = ultimaOrdenFecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdProveedorStr() { return idProveedorStr; }
    public void setIdProveedorStr(String idProveedorStr) { this.idProveedorStr = idProveedorStr; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getContactoNombre() { return contactoNombre; }
    public void setContactoNombre(String contactoNombre) { this.contactoNombre = contactoNombre; }

    public String getContactoTelefono() { return contactoTelefono; }
    public void setContactoTelefono(String contactoTelefono) { this.contactoTelefono = contactoTelefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUltimaOrdenFecha() { return ultimaOrdenFecha; }
    public void setUltimaOrdenFecha(String ultimaOrdenFecha) { this.ultimaOrdenFecha = ultimaOrdenFecha; }
}
