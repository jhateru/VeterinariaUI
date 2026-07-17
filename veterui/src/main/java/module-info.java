module society {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires jcalendar;

    opens society to javafx.fxml;
    exports society;

    opens society.controller.principales to javafx.fxml;
    exports society.controller.principales;

    opens society.controller.reutilizables to javafx.fxml;
    exports society.controller.reutilizables;

    opens society.modell.inventario to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.inventario;

    opens society.modell.administracion to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.administracion;

    opens society.modell.areamedica to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.areamedica;

    opens society.modell.configuracion to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.configuracion;

    opens society.modell.facturacion to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.facturacion;

    opens society.modell.recepcion to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.recepcion;

    opens society.modell.reportes to javafx.base, javafx.fxml, com.google.gson;
    exports society.modell.reportes;
}
