package society;

import society.dao.PersonalDao;
import society.modell.administracion.Personal;
import java.util.List;

public class InjectUsuarios {
    public static void main(String[] args) {
        PersonalDao dao = new PersonalDao();
        List<Personal> personalList = dao.getAll();
        
        if (personalList.isEmpty()) {
            System.out.println("No hay empleados en la base de datos. Creando Admin por defecto...");
            Personal admin = new Personal();
            admin.setNombre("Administrador");
            admin.setCargo("Gerente");
            admin.setDepartamento("Administración");
            admin.setEstado("Activo");
            admin.setUsername("@admin");
            admin.setPassword("admin123");
            admin.setRolSistema("Administrador");
            dao.save(admin);
        } else {
            System.out.println("Actualizando empleados existentes...");
            for (Personal p : personalList) {
                if (p.getUsername() == null || p.getUsername().isEmpty()) {
                    String firstName = p.getNombre().split(" ")[0].toLowerCase();
                    p.setUsername("@" + firstName);
                }
                if (p.getPassword() == null || p.getPassword().isEmpty()) {
                    p.setPassword("vet123"); // Contraseña por defecto
                }
                dao.update(p);
            }
        }
        System.out.println("Usuarios inyectados con éxito.");
    }
}
