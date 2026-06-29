# VeterinariaUI - Proyecto Arquitectura MVC

Este proyecto implementa una arquitectura Modelo-Vista-Controlador (MVC) para una clínica veterinaria, estructurado de la siguiente manera:

## 1. Modelo (Model)
Ubicación: `veterui/src/main/java/society/modell`
Contiene las entidades del dominio o negocio, como `Veterinario`, `cliente`, `empleado`, `mascota`, `persona`, y demás subpaquetes (`clinica`, `facturacion`, `inventario`, `personas`).

## 2. Vista (View)
Ubicación: `veterui/src/main/resources/society`
Contiene los archivos de interfaz de usuario (`.fxml`) y hojas de estilo (`.css`). La vista se encarga exclusivamente de la presentación de los datos y de capturar las interacciones del usuario.

## 3. Controlador (Controller)
Ubicación: `veterui/src/main/java/society/controller`
Actúa como intermediario entre la Vista y el Modelo. Existen subpaquetes como `principales/` para las vistas principales y `reutilizables/` para componentes. Cada archivo FXML está enlazado a un controlador.

## 4. Acceso a Datos (DAO - Data Access Object)
Ubicación: `veterui/src/main/java/society/dao`
Se encarga de la persistencia de datos. Actualmente configurado para guardar y leer información desde archivos `.csv` ubicados en la carpeta `veterui/data/` del proyecto.
