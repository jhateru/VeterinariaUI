package society.dao;

import society.modell.reportes.Reportes;

public class ReportesDao extends CsvDao<Reportes> {
    public ReportesDao() {
        super("reportes.csv");
    }

    @Override
    protected Reportes fromCsv(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length < 5) return null;
        return new Reportes(
                data[0],
                data[1],
                Integer.parseInt(data[2]),
                Double.parseDouble(data[3]),
                data[4]
        );
    }

    @Override
    protected String toCsv(Reportes r) {
        return String.join(",",
                r.getId(),
                r.getVeterinarioNombre(),
                String.valueOf(r.getConsultas()),
                String.valueOf(r.getCalificacion()),
                r.getCarga()
        );
    }
}
