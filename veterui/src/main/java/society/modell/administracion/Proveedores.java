package society.modell.administracion;
import society.modell.Entity;
import java.util.List;
import society.modell.administracion.Consumible;

public class Proveedores {
    private int id;
    private String idProveedorStr; // e.g. "PROV-0012"
    private String ruc;
    private String nombre;
    private String categoria;      // e.g. Medicamentos, Equipamiento, Alimentos, Cirugía
    private String contactoNombre;
    private String contactoTelefono;
    private String direccion;
    private String email;
    private String sitioWeb;
    private String estado;         // e.g. Activo, En Revisión, Inactivo
    private String ultimaOrdenFecha; // e.g. "12 Oct, 2023"
    
    // Lista de productos de inventario suministrados por el proveedor con sus cantidades
    private List<Consumible> inventariosSuministrados;

    public Proveedores() {}

    public Proveedores(int id, String idProveedorStr, String ruc, String nombre, String categoria, String contactoNombre, String contactoTelefono, String direccion, String email, String sitioWeb, String estado, String ultimaOrdenFecha, List<Consumible> inventariosSuministrados) {
        this.id = id;
        this.idProveedorStr = idProveedorStr;
        this.ruc = ruc;
        this.nombre = nombre;
        this.categoria = categoria;
        this.contactoNombre = contactoNombre;
        this.contactoTelefono = contactoTelefono;
        this.direccion = direccion;
        this.email = email;
        this.sitioWeb = sitioWeb;
        this.estado = estado;
        this.ultimaOrdenFecha = ultimaOrdenFecha;
        this.inventariosSuministrados = inventariosSuministrados;
    }
    
    // Constructor for retro-compatibility
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

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getContactoNombre() { return contactoNombre; }
    public void setContactoNombre(String contactoNombre) { this.contactoNombre = contactoNombre; }

    public String getContactoTelefono() { return contactoTelefono; }
    public void setContactoTelefono(String contactoTelefono) { this.contactoTelefono = contactoTelefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUltimaOrdenFecha() { return ultimaOrdenFecha; }
    public void setUltimaOrdenFecha(String ultimaOrdenFecha) { this.ultimaOrdenFecha = ultimaOrdenFecha; }

    public List<Consumible> getInventariosSuministrados() { return inventariosSuministrados; }
    public void setInventariosSuministrados(List<Consumible> inventariosSuministrados) { this.inventariosSuministrados = inventariosSuministrados; }
}
