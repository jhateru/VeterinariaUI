package society.dao;

import society.modell.inventario.Inventario;

public class InventarioDao extends CsvDao<Inventario> {

    public InventarioDao() {
        super("inventario.csv");
    }

    @Override
    protected Inventario fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 8) {
            return new Inventario(
                parts[0], 
                parts[1], 
                parts[2], 
                Integer.parseInt(parts[3]), 
                parts[4], 
                parts[5], 
                parts[6], 
                parts[7]
            );
        }
        return null;
    }

    @Override
    protected String toCsv(Inventario entity) {
        return String.join(",",
            entity.getId(),
            entity.getProducto(),
            entity.getDescripcion(),
            String.valueOf(entity.getStock()),
            entity.getUnidad(),
            entity.getEstado(),
            entity.getFefo(),
            entity.getCategoria()
        );
    }
}
