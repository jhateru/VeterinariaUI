package society.modell.administracion;

public class DetalleFactura {
    private int id;
    private String descripcion;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleFactura() {}

    public DetalleFactura(int id, String descripcion, int cantidad, double precioUnitario) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
