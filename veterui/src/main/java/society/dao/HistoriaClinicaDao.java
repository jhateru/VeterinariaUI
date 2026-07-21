package society.dao;

import java.util.List;
import java.util.Optional;
import com.google.gson.reflect.TypeToken;

import society.modell.areamedica.HistoriaClinica;

public class HistoriaClinicaDao extends MasterJsonDao<HistoriaClinica> {

    public HistoriaClinicaDao() {
        super("historiasClinicas", new TypeToken<List<HistoriaClinica>>(){}.getType());
    }

    /** Busca la historia clínica cuyo paciente.id coincide con pacienteId */
    public Optional<HistoriaClinica> findById(int pacienteId) {
        return getAll().stream()
                .filter(hc -> hc.getPaciente() != null && hc.getPaciente().getId() == pacienteId)
                .findFirst();
    }

    /**
     * Persiste (crea o actualiza) la historia clínica dada.
     * La clave de matching es el campo 'id' de la propia HistoriaClinica.
     */
    public void saveOrUpdate(HistoriaClinica historia) {
        update(historia, HistoriaClinica::getId);
    }
}
