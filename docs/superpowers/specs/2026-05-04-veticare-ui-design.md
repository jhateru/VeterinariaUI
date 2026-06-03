# VetiCare UI Design Specification
Date: 2026-05-04
Topic: Main User Interface for Veterinary Management

## 1. Architecture Overview
The UI is built using JavaFX and FXML, following a modular layout approach to ensure maintainability and reuse of components.

### Layout Hierarchy
- `MainView.fxml` (BorderPane)
    - Top: `TopBar.fxml` (Header)
    - Left: `SideBar.fxml` (Navigation)
    - Center: Dynamic content area (StackPane) $\rightarrow$ `DashboardResumen.fxml`

## 2. Component Details

### 2.1 Structural Components
- **MainView**: Root container managing the global layout.
- **SideBar**: Vertical navigation menu containing:
    - Logo: VetiCare
    - Navigation Items: Resumen, Citas, Pacientes, Dueños, Historia Clínica, Facturación, Inventario, Reportes, Configuración.
- **TopBar**: Horizontal header containing:
    - Clinic Selector: ComboBox for "Clínica Veterinaria 'Patas Felices'".
    - Search Bar: TextField with search icon.
    - Notifications: Button with a red badge.
    - Profile: Dra. Ana Pérez (Administradora) with avatar.

### 2.2 Dashboard Content (`DashboardResumen.fxml`)
- **Header**: "Resumen de Hoy: Martes 15 Oct 2024".
- **Metrics Grid**: HBox of 4 `MetricCard` components.
- **Main Grid**:
    - **Appointment Agenda**: Timeline view with `AppointmentItem` entries.
    - **Waiting Room**: List of `WaitingRoomItem` entries.
    - **Recent Consultations**: List of `RecentConsultationItem` entries.
    - **Quick Actions**: HBox with buttons "Nueva Cita", "Nuevo Paciente", "Nueva Venta".

### 2.3 Reusable Widgets
- **MetricCard**: (Icon, Title, Value).
- **AppointmentItem**: (Time, Pet Avatar, Pet Name, Owner, Status Badge).
- **WaitingRoomItem**: (Avatar, Name, Species).
- **RecentConsultationItem**: (Avatar, Name, Time, Diagnosis).

## 3. Visual Identity (Styles.css)
- **Color Palette**:
    - Primary Background: `#F8FAFC`
    - Sidebar Background: `#1E293B`
    - Accent Color: `#0EA5E9`
    - Text Primary: `#334155`
    - Status Colors: 
        - Waiting: `#FDE047` (Yellow)
        - Attending: `#4ADE80` (Green)
        - Pending: `#FCA5A5` (Red)
- **Styling**:
    - Border Radius: 10px for cards and buttons.
    - Font: System default Sans-Serif.
    - Layout: Generous padding and gap between elements for a modern, clean feel.
