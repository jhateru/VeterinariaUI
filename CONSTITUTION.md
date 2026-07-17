# Constitución Arquitectónica de VeterinariaUI

Este documento establece las normativas, patrones y reglas absolutas que rigen el desarrollo de la interfaz gráfica y la arquitectura del proyecto VeterinariaUI. Todo agente o desarrollador que modifique el código **debe** apegarse estrictamente a estos principios.

## 1. Arquitectura Dual (Dual View)
El proyecto implementa dos versiones de la interfaz gráfica de usuario. Ambas versiones deben compartir la misma estructura base de la capa de control y de datos (MVC), garantizando que cambiar de una vista a otra no afecte la lógica de negocio ni la persistencia.

### Vista Avanzada (JavaFX)
- **Ubicación:** `veterui/src/main/resources/society/`
- **Tecnología:** JavaFX, utilizando archivos `.fxml` y hojas de estilo `.css`.
- **Propósito:** Ofrecer una interfaz visual rica, moderna, estilizada y con personalizaciones de color, bordes y animaciones.

### Vista Básica (Swing)
- **Ubicación:** `veterui/src/main/java/society/view/`
- **Convención de Nombres:** Los archivos deben llevar el sufijo `ViewBasic.java` (ej. `CitasViewBasic.java`).
- **Tecnología:** Java puro mediante la librería Swing (`JFrame`, `JPanel`, `JButton`, `JTable`, etc.).
- **Regla Estricta de Diseño:**
  - **Estructura Espejo:** La estructura y disposición (layouts, campos, tablas, botones) debe guiarse y ser equivalente a la del archivo `.fxml` de la vista avanzada.
  - **Cero Colores Personalizados:** Está **estrictamente prohibido** el uso de colores personalizados en los archivos `ViewBasic.java` (Swing). No se deben utilizar métodos como `.setBackground()`, `.setForeground()`, ni crear paletas de `Color`. La interfaz debe depender exclusivamente del tema base (Look & Feel) por defecto del sistema, manteniendo un diseño sobrio, utilitario y rápido.
  - **Tablas de Datos y Formularios:** En los archivos `ViewBasic.java`, casi siempre debe existir una sección (generalmente en forma de tabla) destinada a mostrar todos los datos registrados, leyendo directamente de su respectivo archivo JSON de la base de datos. Además, siempre debe incluirse un botón que permita registrar nuevos datos; al hacer clic, este debe abrir su propia ventana emergente de formulario o registro. Dicha ventana deberá construirse basándose estrictamente en los diseños/imágenes que el usuario provea para el módulo correspondiente.

## 2. Flujo MVC y Mapeo de Archivos
Tanto la Vista Avanzada como la Básica apuntan a los mismos controladores y flujo de datos. Para mantener la cohesión del código, cada módulo o pantalla debe seguir un mapeo estricto a través de las capas del sistema.

**Estructura de Mapeo por Módulo:**
1. **Vista Avanzada:** `[Modulo]View.fxml` y su `[Modulo].css`
2. **Vista Básica:** `[Modulo]ViewBasic.java`
3. **Controlador:** `[Modulo]Controller.java`
4. **Persistencia (DAO):** `[Modulo]Dao.java`
5. **Entidad/Modelo:** `[Modulo].java`
6. **Datos Físicos:** `[modulo].json`

**Ejemplo Práctico (Módulo de Citas):**
- Vista FXML: `CitasView.fxml` (en `resources/society`)
- Vista Swing: `CitasViewBasic.java` (en `java/society/view`)
- Controlador: `CitasController.java` (en `java/society/controller/principales`)
- DAO: `CitasDao.java` (en `java/society/dao`)
- Modelo: `Cita.java` (en `java/society/modell/clinica`)
- Archivo de datos: `cita.json` (en `data/`)

## 3. Filosofía de Desarrollo
- **Conceptos > Código:** No aplicar parches sin entender el patrón de arquitectura MVC.
- **Sin atajos:** Todo nuevo botón o funcionalidad en la capa de UI debe enviar sus eventos al Controlador, nunca comunicarse directamente con el DAO desde el código de la vista (Swing o FXML).
