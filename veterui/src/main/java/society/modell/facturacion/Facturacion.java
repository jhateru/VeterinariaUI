package society.modell.facturacion;

import java.util.ArrayList;
import java.util.List;

public class Facturacion {
    public enum EstadoFactura {
        PROFORMA, PAGADA
    }

    private String id;
    private String fechaHora;
    private String clienteInfo; // Max, Golden Retriever, Dueño: Carlos R.
    private List<DetalleFacturacion> detalles;
    private double descuentoPorcentaje; // ej. 10 para 10%
    private double ivaPorcentaje; // ej. 15 para 15%
    private EstadoFactura estado;

    public Facturacion() {
        this.detalles = new ArrayList<>();
        this.descuentoPorcentaje = 0;
        this.ivaPorcentaje = 15; // default
        this.estado = EstadoFactura.PROFORMA;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getClienteInfo() { return clienteInfo; }
    public void setClienteInfo(String clienteInfo) { this.clienteInfo = clienteInfo; }

    public List<DetalleFacturacion> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFacturacion> detalles) { this.detalles = detalles; }
    public void addDetalle(DetalleFacturacion detalle) { this.detalles.add(detalle); }

    public double getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(double descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }

    public double getIvaPorcentaje() { return ivaPorcentaje; }
    public void setIvaPorcentaje(double ivaPorcentaje) { this.ivaPorcentaje = ivaPorcentaje; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public double getSubtotal() {
        return detalles.stream().mapToDouble(DetalleFacturacion::getSubtotal).sum();
    }

    public double getDescuentoTotal() {
        return getSubtotal() * (descuentoPorcentaje / 100.0);
    }

    public double getIvaTotal() {
        return (getSubtotal() - getDescuentoTotal()) * (ivaPorcentaje / 100.0);
    }

    public double getTotal() {
        return (getSubtotal() - getDescuentoTotal()) + getIvaTotal();
    }
}
