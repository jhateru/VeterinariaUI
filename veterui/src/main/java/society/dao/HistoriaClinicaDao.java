package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.areamedica.HistoriaClinica;
import society.modell.recepcion.Paciente;

import java.time.LocalDate;
import java.util.Optional;

public class HistoriaClinicaDao extends JsonDao<HistoriaClinica> {

    private PacienteDao pacienteDao;

    public HistoriaClinicaDao() {
        super("historias_clinicas.json", new TypeToken<List<HistoriaClinica>>(){}.getType());
        this.pacienteDao = new PacienteDao();
    }

    

    

    public Optional<HistoriaClinica> findById(int pacienteId) {
        return getAll().stream()
                .filter(hc -> hc.getPaciente() != null && hc.getPaciente().getId() == pacienteId)
                .findFirst();
    }
}
