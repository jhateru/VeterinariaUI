package society.modell.administracion;

import java.time.LocalDate;

public class Empleado extends Persona {
    protected String cargo;
    protected double salario;
    protected LocalDate fechaContratacion;

    public Empleado() {}

    public Empleado(int id, String nombre, String apellidos, String dni, String telefono, String email, String direccion, String cargo, double salario, LocalDate fechaContratacion) {
        super(id, nombre, apellidos, dni, telefono, email, direccion);
        this.cargo = cargo;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
}
