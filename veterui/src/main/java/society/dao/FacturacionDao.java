package society.dao;

import society.modell.facturacion.Facturacion;
import society.modell.facturacion.Facturacion.EstadoFactura;
import society.modell.facturacion.DetalleFacturacion;
import society.modell.facturacion.CatalogoItem;

public class FacturacionDao extends CsvDao<Facturacion> {

    public FacturacionDao() {
        super("facturacion.csv");
    }

    private String safe(String val) {
        if (val == null) return "";
        return val.replace(",", ";").replace("\n", " ");
    }

    @Override
    protected Facturacion fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 7) return null;

        try {
            Facturacion f = new Facturacion();
            f.setId(parts[0]);
            f.setFechaHora(parts[1]);
            f.setClienteInfo(parts[2]);
            f.setDescuentoPorcentaje(Double.parseDouble(parts[3].isEmpty() ? "0" : parts[3]));
            f.setIvaPorcentaje(Double.parseDouble(parts[4].isEmpty() ? "0" : parts[4]));
            f.setEstado(EstadoFactura.valueOf(parts[5]));
            
            // Detalles serializados rudimentariamente como "idItem:cantidad:descuentoFijo|idItem:cantidad:descuentoFijo"
            String detallesStr = parts[6];
            if (!detallesStr.isEmpty()) {
                String[] items = detallesStr.split("\\|");
                for (String itemStr : items) {
                    String[] itemParts = itemStr.split(":");
                    if (itemParts.length == 3) {
                        // En una app real, aquí buscaríamos el CatalogoItem en la BD por ID.
                        // Para este prototipo, instanciamos un item dummy con ese ID para mantener la info.
                        CatalogoItem dummyItem = new CatalogoItem(itemParts[0], "Item " + itemParts[0], "", 0, "", "", "", "");
                        int cant = Integer.parseInt(itemParts[1]);
                        double desc = Double.parseDouble(itemParts[2]);
                        f.addDetalle(new DetalleFacturacion(dummyItem, cant, desc));
                    }
                }
            }
            return f;
        } catch (Exception e) {
            System.err.println("Error parsing facturacion CSV line: " + csvLine);
            return null;
        }
    }

    @Override
    protected String toCsv(Facturacion entity) {
        StringBuilder sbDetalles = new StringBuilder();
        for (int i = 0; i < entity.getDetalles().size(); i++) {
            DetalleFacturacion df = entity.getDetalles().get(i);
            sbDetalles.append(safe(df.getItem().getId())).append(":")
                      .append(df.getCantidad()).append(":")
                      .append(df.getDescuentoFijo());
            if (i < entity.getDetalles().size() - 1) {
                sbDetalles.append("|");
            }
        }

        return safe(entity.getId()) + "," +
               safe(entity.getFechaHora()) + "," +
               safe(entity.getClienteInfo()) + "," +
               entity.getDescuentoPorcentaje() + "," +
               entity.getIvaPorcentaje() + "," +
               (entity.getEstado() != null ? entity.getEstado().name() : EstadoFactura.PROFORMA.name()) + "," +
               sbDetalles.toString();
    }
}
