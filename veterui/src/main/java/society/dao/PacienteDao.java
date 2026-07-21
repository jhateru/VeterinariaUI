package society.dao;

import java.util.List;
import com.google.gson.reflect.TypeToken;

import society.modell.recepcion.Paciente;
import society.modell.recepcion.Paciente.EspecieAnimal;
import society.modell.recepcion.Paciente.EstadoPaciente;

public class PacienteDao extends MasterJsonDao<Paciente> {

    public PacienteDao() {
        super("pacientes", new TypeToken<List<Paciente>>(){}.getType());
    }

    public void create(Paciente paciente) {
        List<Paciente> pacientes = getAll();
        pacientes.add(paciente);
        saveAll(pacientes);
    }

    public Paciente getById(int id) {
        List<Paciente> pacientes = getAll();
        for (Paciente paciente : pacientes) {
            if (paciente.getId() == id) {
                return paciente;
            }
        }
        return null;
    }

    public void update(Paciente paciente) {
        List<Paciente> pacientes = getAll();
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                break;
            }
        }
        saveAll(pacientes);
    }

    public void delete(int id) {
        List<Paciente> pacientes = getAll();
        pacientes.removeIf(p -> p.getId() == id);
        saveAll(pacientes);
    }

    private String safe(String val) {
        if (val == null) return "";
        return val.replace(",", ";").replace("\n", " ");
    }
}
