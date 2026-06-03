# Plan de Implementación: Creación de Controladores

> **Para trabajadores agentes:** SUB-HABILIDAD REQUERIDA: Use superpowers:subagent-driven-development (recomendado) o superpowers:executing-plans para implementar este plan tarea por tarea. Los pasos usan la sintaxis de casilla de verificación (`- [ ]`) para el seguimiento.

**Objetivo:** Crear la estructura de carpetas y los archivos de controlador para las vistas FXML del proyecto, organizándolos en categorías de "principales" y "reutilizables".

**Arquitectura:** Los controladores se ubicarán en paquetes específicos dentro de `society.controller` para mantener una separación clara entre la lógica de la vista y la estructura principal.

**Stack Tecnológico:** Java 11, JavaFX.

---

### Tarea 1: Crear estructura de directorios

**Archivos:**
- Crear: `veterui/src/main/java/society/controller/principales/`
- Crear: `veterui/src/main/java/society/controller/reutilizables/`

- [ ] **Paso 1: Crear carpetas de paquetes**

```bash
mkdir -p veterui/src/main/java/society/controller/principales
mkdir -p veterui/src/main/java/society/controller/reutilizables
```

- [ ] **Paso 2: Verificar creación**

```bash
ls -R veterui/src/main/java/society/controller/
```

- [ ] **Paso 3: Commit**

```bash
git add veterui/src/main/java/society/controller/
git commit -m "chore: crear estructura de carpetas para controladores"
```

### Tarea 2: Crear Controladores Principales

**Archivos:**
- Crear: `society.controller.principales.MainViewController`
- Crear: `society.controller.principales.SideBarController`
- Crear: `society.controller.principales.TopBarController`
- Crear: `society.controller.principales.DashboardResumenController`

- [ ] **Paso 1: Implementar MainViewController**

```java
package society.controller.principales;

import javafx.fxml.FXML;

public class MainViewController {
    @FXML
    public void initialize() {
        // Inicialización de la vista principal
    }
}
```

- [ ] **Paso 2: Implementar SideBarController**

```java
package society.controller.principales;

import javafx.fxml.FXML;

public class SideBarController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 3: Implementar TopBarController**

```java
package society.controller.principales;

import javafx.fxml.FXML;

public class TopBarController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 4: Implementar DashboardResumenController**

```java
package society.controller.principales;

import javafx.fxml.FXML;

public class DashboardResumenController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 5: Commit**

```bash
git add veterui/src/main/java/society/controller/principales/
git commit -m "feat: añadir controladores principales"
```

### Tarea 3: Crear Controladores Reutilizables

**Archivos:**
- Crear: `society.controller.reutilizables.AppointmentItemController`
- Crear: `society.controller.reutilizables.MetricCardController`
- Crear: `society.controller.reutilizables.RecentConsultationItemController`
- Crear: `society.controller.reutilizables.WaitingRoomItemController`

- [ ] **Paso 1: Implementar AppointmentItemController**

```java
package society.controller.reutilizables;

import javafx.fxml.FXML;

public class AppointmentItemController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 2: Implementar MetricCardController**

```java
package society.controller.reutilizables;

import javafx.fxml.FXML;

public class MetricCardController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 3: Implementar RecentConsultationItemController**

```java
package society.controller.reutilizables;

import javafx.fxml.FXML;

public class RecentConsultationItemController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 4: Implementar WaitingRoomItemController**

```java
package society.controller.reutilizables;

import javafx.fxml.FXML;

public class WaitingRoomItemController {
    @FXML
    public void initialize() {
    }
}
```

- [ ] **Paso 5: Commit**

```bash
git add veterui/src/main/java/society/controller/reutilizables/
git commit -m "feat: añadir controladores reutilizables"
```
