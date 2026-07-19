package society.modell.administracion;

public class Consumible {
    private String inventarioId;
    private int cantidad;

    public Consumible() {}

    public Consumible(String inventarioId, int cantidad) {
        this.inventarioId = inventarioId;
        this.cantidad = cantidad;
    }

    public String getInventarioId() { return inventarioId; }
    public void setInventarioId(String inventarioId) { this.inventarioId = inventarioId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
