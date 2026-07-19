package society.modell.administracion;
import society.modell.Entity;
import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precioBase;
    private int duracionEstimadaMinutos;
    private String estado;
    private List<Consumible> materialesUsados;

    public Servicio() {
        this.materialesUsados = new ArrayList<>();
    }

    public Servicio(int id, String nombre, String descripcion, String categoria, double precioBase, int duracionEstimadaMinutos, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precioBase = precioBase;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.estado = estado;
        this.materialesUsados = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public int getDuracionEstimadaMinutos() { return duracionEstimadaMinutos; }
    public void setDuracionEstimadaMinutos(int duracionEstimadaMinutos) { this.duracionEstimadaMinutos = duracionEstimadaMinutos; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Consumible> getMaterialesUsados() { return materialesUsados; }
    public void setMaterialesUsados(List<Consumible> materialesUsados) { this.materialesUsados = materialesUsados; }
}
