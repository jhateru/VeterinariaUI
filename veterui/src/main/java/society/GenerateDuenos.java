package society;

import society.dao.DuenoDao;
import society.modell.recepcion.Dueno;
import java.util.Random;

public class GenerateDuenos {
    public static void main(String[] args) {
        DuenoDao dao = new DuenoDao();
        Random r = new Random();
        
        String[][] data = {
            {"Ana", "García Martínez", "12.345.678-K", "Femenino", "15/05/1985", "+56 9 1234 5678", "ana.garcia@correo.com", "Av. Principal #123, Depto 402", "Santiago", "8320000", "Luna, Milo"},
            {"Carlos", "López Rivera", "15.789.012-4", "Masculino", "22/08/1990", "+56 9 9876 5432", "carlos.lopez@correo.com", "Calle Los Pinos 45", "Valparaíso", "2340000", "Rex"},
            {"María", "Fernández Silva", "18.456.789-1", "Femenino", "10/11/1995", "+56 9 4567 8901", "m.fernandez@correo.com", "Pasaje El Sol #789", "Concepción", "4030000", "Simba, Nala"},
            {"Roberto", "Díaz Gómez", "11.223.344-5", "Masculino", "05/03/1980", "+56 9 3333 4444", "roberto.diaz@correo.com", "Av. Libertad 555", "Viña del Mar", "2520000", "Max"},
            {"Camila", "Soto Morales", "19.888.777-6", "Femenino", "30/01/2000", "+56 9 5555 6666", "cami.soto@correo.com", "Calle Las Rosas 234", "La Serena", "1700000", "Mia"},
            {"Javier", "Ruiz Castro", "14.555.666-7", "Masculino", "18/07/1988", "+56 9 7777 8888", "javier.ruiz@correo.com", "Av. Pedro de Valdivia 901", "Providencia", "7500000", "Thor, Loki"},
            {"Laura", "Herrera Pinto", "17.999.000-8", "Femenino", "12/12/1992", "+56 9 2222 1111", "laura.herrera@correo.com", "Pasaje Las Lomas 67", "Maipú", "9250000", "Bella"},
            {"Diego", "Vargas Torres", "16.111.222-9", "Masculino", "25/09/1991", "+56 9 9999 0000", "diego.vargas@correo.com", "Av. Las Condes 10200", "Las Condes", "7550000", "Rocky"}
        };
        
        for (String[] row : data) {
            Dueno d = new Dueno();
            d.setId(r.nextInt(100000));
            d.setNombre(row[0]);
            d.setApellidos(row[1]);
            d.setDni(row[2]);
            d.setGenero(row[3]);
            d.setFechaNacimiento(row[4]);
            d.setTelefono(row[5]);
            d.setEmail(row[6]);
            d.setDireccion(row[7]);
            d.setCiudad(row[8]);
            d.setCodigoPostal(row[9]);   
            
            // Random estado
            Dueno.EstadoDueno[] estados = Dueno.EstadoDueno.values();
            d.setEstado(estados[r.nextInt(estados.length)]);
            
            dao.save(d);
        }
        System.out.println("Registros generados.");
    }
}
