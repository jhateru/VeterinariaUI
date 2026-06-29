package society.dao;

import society.modell.recepcion.Paciente;
import society.modell.recepcion.Paciente.EspecieAnimal;
import society.modell.recepcion.Paciente.EstadoPaciente;

public class PacienteDao extends CsvDao<Paciente> {

    public PacienteDao() {
        super("pacientes.csv");
    }

    @Override
    protected Paciente fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 16) return null;

        try {
            int id = Integer.parseInt(parts[0]);
            String nombre = parts[1];
            EspecieAnimal especie = EspecieAnimal.valueOf(parts[2]);
            String raza = parts[3];
            String sexo = parts[4];
            String edadAproximada = parts[5];
            double peso = Double.parseDouble(parts[6].isEmpty() ? "0" : parts[6]);
            String nombreDueno = parts[7];
            String telefonoDueno = parts[8];
            String dniDueno = parts[9];
            String emailDueno = parts[10];
            String direccionDueno = parts[11];
            String alergias = parts[12];
            String motivoPrimeraConsulta = parts[13];
            String ultimaVisita = parts[14];
            EstadoPaciente estado = EstadoPaciente.valueOf(parts[15]);

            return new Paciente(id, nombre, especie, raza, sexo, edadAproximada, peso,
                    nombreDueno, telefonoDueno, dniDueno, emailDueno, direccionDueno,
                    alergias, motivoPrimeraConsulta, ultimaVisita, estado);
        } catch (Exception e) {
            System.err.println("Error parsing paciente CSV line: " + csvLine);
            return null;
        }
    }

    private String safe(String val) {
        if (val == null) return "";
        return val.replace(",", ";").replace("\n", " ");
    }

    @Override
    protected String toCsv(Paciente entity) {
        return entity.getId() + "," +
               safe(entity.getNombre()) + "," +
               (entity.getEspecie() != null ? entity.getEspecie().name() : EspecieAnimal.PERRO.name()) + "," +
               safe(entity.getRaza()) + "," +
               safe(entity.getSexo()) + "," +
               safe(entity.getEdadAproximada()) + "," +
               entity.getPeso() + "," +
               safe(entity.getNombreDueno()) + "," +
               safe(entity.getTelefonoDueno()) + "," +
               safe(entity.getDniDueno()) + "," +
               safe(entity.getEmailDueno()) + "," +
               safe(entity.getDireccionDueno()) + "," +
               safe(entity.getAlergias()) + "," +
               safe(entity.getMotivoPrimeraConsulta()) + "," +
               safe(entity.getUltimaVisita()) + "," +
               (entity.getEstado() != null ? entity.getEstado().name() : EstadoPaciente.EN_CLINICA.name());
    }
}
