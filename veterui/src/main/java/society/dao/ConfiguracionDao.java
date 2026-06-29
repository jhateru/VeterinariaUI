package society.dao;

import society.modell.configuracion.Configuracion;

public class ConfiguracionDao extends CsvDao<Configuracion> {

    public ConfiguracionDao() {
        super("configuracion.csv");
    }

    @Override
    protected Configuracion fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 12) {
            try {
                int id = Integer.parseInt(parts[0]);
                String nombreClinica = parts[1];
                String idFiscal = parts[2];
                String direccion = parts[3];
                String telefono = parts[4];
                String correo = parts[5];
                String idioma = parts[6];
                String moneda = parts[7];
                boolean dosFaActivo = Boolean.parseBoolean(parts[8]);
                boolean notifCitas = Boolean.parseBoolean(parts[9]);
                boolean notifLab = Boolean.parseBoolean(parts[10]);
                boolean notifResumen = Boolean.parseBoolean(parts[11]);
                
                return new Configuracion(id, nombreClinica, idFiscal, direccion, telefono, correo, 
                                         idioma, moneda, dosFaActivo, notifCitas, notifLab, notifResumen);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected String toCsv(Configuracion entity) {
        return entity.getId() + "," +
               entity.getNombreClinica() + "," +
               entity.getIdFiscal() + "," +
               entity.getDireccion() + "," +
               entity.getTelefono() + "," +
               entity.getCorreo() + "," +
               entity.getIdioma() + "," +
               entity.getMoneda() + "," +
               entity.isDosFaActivo() + "," +
               entity.isNotifRecordatorioCitas() + "," +
               entity.isNotifResultadosLab() + "," +
               entity.isNotifResumenSemanal();
    }
}
