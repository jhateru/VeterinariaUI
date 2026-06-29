package society.dao;

import society.modell.administracion.Personal;

public class PersonalDao extends CsvDao<Personal> {

    public PersonalDao() {
        super("personal.csv");
    }

    @Override
    protected Personal fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 7) {
            try {
                int id = Integer.parseInt(parts[0]);
                String nombre = parts[1];
                String cargo = parts[2];
                String departamento = parts[3];
                String estado = parts[4];
                String email = parts[5];
                String telefono = parts[6];
                return new Personal(id, nombre, cargo, departamento, estado, email, telefono);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected String toCsv(Personal entity) {
        return entity.getId() + "," +
               entity.getNombre() + "," +
               entity.getCargo() + "," +
               entity.getDepartamento() + "," +
               entity.getEstado() + "," +
               entity.getEmail() + "," +
               entity.getTelefono();
    }
}
