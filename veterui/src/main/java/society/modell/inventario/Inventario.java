package society.modell.inventario;

public class Inventario {
    private String id;
    private String producto;
    private String descripcion;
    private int stock;
    private String unidad;
    private String estado;
    private String fefo;
    private String categoria;

    public Inventario() {}

    public Inventario(String id, String producto, String descripcion, int stock, String unidad, String estado, String fefo, String categoria) {
        this.id = id;
        this.producto = producto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.unidad = unidad;
        this.estado = estado;
        this.fefo = fefo;
        this.categoria = categoria;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFefo() { return fefo; }
    public void setFefo(String fefo) { this.fefo = fefo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
