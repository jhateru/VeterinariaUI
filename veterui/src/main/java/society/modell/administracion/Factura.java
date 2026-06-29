package society.modell.administracion;

import society.modell.recepcion.Dueno;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Factura {
    private int id;
    private Dueno cliente;
    private LocalDateTime fechaEmision;
    private double subtotal;
    private double impuestos;
    private double total;
    private List<DetalleFactura> detalles;

    public Factura() {
        this.detalles = new ArrayList<>();
    }

    public Factura(int id, Dueno cliente, LocalDateTime fechaEmision) {
        this.id = id;
        this.cliente = cliente;
        this.fechaEmision = fechaEmision;
        this.detalles = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Dueno getCliente() { return cliente; }
    public void setCliente(Dueno cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getImpuestos() { return impuestos; }
    public void setImpuestos(double impuestos) { this.impuestos = impuestos; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }
}
