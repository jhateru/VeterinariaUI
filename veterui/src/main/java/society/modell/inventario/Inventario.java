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
    
    // Nuevos campos
    private String lote;
    private int puntoReorden;
    private double precio;

    public Inventario() {}

    public Inventario(String id, String producto, String descripcion, int stock, String unidad, String estado, String fefo, String categoria, String lote, int puntoReorden, double precio) {
        this.id = id;
        this.producto = producto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.unidad = unidad;
        this.estado = estado;
        this.fefo = fefo;
        this.categoria = categoria;
        this.lote = lote;
        this.puntoReorden = puntoReorden;
        this.precio = precio;
    }

    // Constructor legacy
    public Inventario(String id, String producto, String descripcion, int stock, String unidad, String estado, String fefo, String categoria) {
        this(id, producto, descripcion, stock, unidad, estado, fefo, categoria, "N/A", 10, 0.0);
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

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public int getPuntoReorden() { return puntoReorden; }
    public void setPuntoReorden(int puntoReorden) { this.puntoReorden = puntoReorden; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
