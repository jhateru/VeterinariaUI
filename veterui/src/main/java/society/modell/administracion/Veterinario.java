package society.modell.administracion;

import java.time.LocalDate;

public class Veterinario extends Empleado {
    private String especialidad;
    private String numeroColegiatura;

    public Veterinario() {}

    public Veterinario(int id, String nombre, String apellidos, String dni, String telefono, String email, String direccion, double salario, LocalDate fechaContratacion, String especialidad, String numeroColegiatura) {
        super(id, nombre, apellidos, dni, telefono, email, direccion, "Veterinario", salario, fechaContratacion);
        this.especialidad = especialidad;
        this.numeroColegiatura = numeroColegiatura;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getNumeroColegiatura() { return numeroColegiatura; }
    public void setNumeroColegiatura(String numeroColegiatura) { this.numeroColegiatura = numeroColegiatura; }
}
