package society.dao;

import society.modell.areamedica.HistoriaClinica;
import society.modell.recepcion.Paciente;

import java.time.LocalDate;
import java.util.Optional;

public class HistoriaClinicaDao extends CsvDao<HistoriaClinica> {

    private PacienteDao pacienteDao;

    public HistoriaClinicaDao() {
        super("historias_clinicas.csv");
        this.pacienteDao = new PacienteDao();
    }

    @Override
    protected HistoriaClinica fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 8) return null;

        try {
            int id = Integer.parseInt(parts[0]);
            int pacienteId = Integer.parseInt(parts[1]);
            LocalDate fechaApertura = LocalDate.parse(parts[2]);
            String alergias = parts[3];
            String antecedentes = parts[4];
            double pesoActual = Double.parseDouble(parts[5]);
            String evoluciones = parts[6];
            String tratamientos = parts[7];

            Paciente paciente = null;
            Optional<Paciente> optPaciente = pacienteDao.getAll().stream().filter(p -> p.getId() == pacienteId).findFirst();
            if (optPaciente.isPresent()) {
                paciente = optPaciente.get();
            } else {
                // mock paciente si no se encuentra
                paciente = new Paciente(pacienteId, "Paciente " + pacienteId, Paciente.EspecieAnimal.PERRO, "Desconocida", "Macho", "Desconocida", 0.0,
                        "Desconocido", "", "", "", "", "Ninguna", "", "", Paciente.EstadoPaciente.EN_CLINICA);
            }

            return new HistoriaClinica(id, paciente, fechaApertura, alergias, antecedentes, pesoActual, evoluciones, tratamientos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String toCsv(HistoriaClinica entity) {
        return entity.getId() + "," +
               (entity.getPaciente() != null ? entity.getPaciente().getId() : 0) + "," +
               entity.getFechaApertura() + "," +
               entity.getAlergias() + "," +
               entity.getAntecedentes() + "," +
               entity.getPesoActual() + "," +
               entity.getEvoluciones() + "," +
               entity.getTratamientos();
    }

    public Optional<HistoriaClinica> findById(int pacienteId) {
        return getAll().stream()
                .filter(hc -> hc.getPaciente() != null && hc.getPaciente().getId() == pacienteId)
                .findFirst();
    }
}
