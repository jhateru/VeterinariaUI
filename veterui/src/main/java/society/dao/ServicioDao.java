package society.dao;

import society.modell.administracion.Servicio;

public class ServicioDao extends CsvDao<Servicio> {
    
    public ServicioDao() {
        super("servicios.csv");
    }

    @Override
    protected Servicio fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 7) {
            try {
                int id = Integer.parseInt(parts[0]);
                String nombre = parts[1];
                String descripcion = parts[2];
                String categoria = parts[3];
                double precioBase = Double.parseDouble(parts[4]);
                int duracionEstimadaMinutos = Integer.parseInt(parts[5]);
                String estado = parts[6];
                return new Servicio(id, nombre, descripcion, categoria, precioBase, duracionEstimadaMinutos, estado);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected String toCsv(Servicio entity) {
        return entity.getId() + "," +
               entity.getNombre() + "," +
               entity.getDescripcion() + "," +
               entity.getCategoria() + "," +
               entity.getPrecioBase() + "," +
               entity.getDuracionEstimadaMinutos() + "," +
               entity.getEstado();
    }
}
