package society.modell.facturacion;

public class DetalleFacturacion {
    private CatalogoItem item;
    private int cantidad;
    private double descuentoFijo;

    public DetalleFacturacion(CatalogoItem item, int cantidad, double descuentoFijo) {
        this.item = item;
        this.cantidad = cantidad;
        this.descuentoFijo = descuentoFijo;
    }

    public CatalogoItem getItem() { return item; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getDescuentoFijo() { return descuentoFijo; }
    public void setDescuentoFijo(double descuentoFijo) { this.descuentoFijo = descuentoFijo; }

    public double getSubtotal() {
        return (item.getPrecio() * cantidad) - descuentoFijo;
    }
}
