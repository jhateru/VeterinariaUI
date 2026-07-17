package society.view;

import society.modell.recepcion.Cita;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RegistroCitasBasic extends JDialog {

    private JTextField txtPacienteNombre;
    private JTextField txtVeterinarioNombre;
    private JTextField txtMotivo;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JComboBox<String> cbEstado;

    private boolean saved = false;
    private Cita nuevaCita;

    public RegistroCitasBasic(Frame parent) {
        super(parent, "Nueva Cita", true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        
        formPanel.add(new JLabel("Nombre Paciente:"));
        txtPacienteNombre = new JTextField();
        formPanel.add(txtPacienteNombre);
        
        formPanel.add(new JLabel("Nombre Veterinario:"));
        txtVeterinarioNombre = new JTextField();
        formPanel.add(txtVeterinarioNombre);
        
        formPanel.add(new JLabel("Motivo:"));
        txtMotivo = new JTextField();
        formPanel.add(txtMotivo);
        
        formPanel.add(new JLabel("Fecha y Hora:"));
        
        JPanel dateTimePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); 
        
        dateTimePanel.add(dateChooser);
        dateTimePanel.add(timeSpinner);
        
        formPanel.add(dateTimePanel);
        
        formPanel.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "COMPLETADA", "CANCELADA", "URGENCIA"});
        formPanel.add(cbEstado);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Guardar");
        
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
        
        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);
        
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void guardar() {
        if (txtPacienteNombre.getText().trim().isEmpty() || dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Nombre del paciente y Fecha son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date selectedDate = dateChooser.getDate();
        Date selectedTime = (Date) timeSpinner.getValue();
        
        LocalDate datePart = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime timePart = selectedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDateTime fechaHora = LocalDateTime.of(datePart, timePart);

        if (nuevaCita == null) {
            nuevaCita = new Cita();
            nuevaCita.setId(new Random().nextInt(10000));
        }

        nuevaCita.setPacienteNombre(txtPacienteNombre.getText().trim());
        nuevaCita.setVeterinarioNombre(txtVeterinarioNombre.getText().trim());
        nuevaCita.setMotivo(txtMotivo.getText().trim());
        nuevaCita.setFechaHora(fechaHora);
        nuevaCita.setEstado(Cita.EstadoCita.valueOf(cbEstado.getSelectedItem().toString()));

        saved = true;
        dispose();
    }

    public void setCitaToEdit(Cita c) {
        this.nuevaCita = c;
        setTitle("Editar Cita");
        
        txtPacienteNombre.setText(c.getPacienteNombre());
        txtVeterinarioNombre.setText(c.getVeterinarioNombre());
        txtMotivo.setText(c.getMotivo());
        
        if (c.getFechaHora() != null) {
            Date out = Date.from(c.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(out);
            timeSpinner.setValue(out);
        }
        if (c.getEstado() != null) {
            cbEstado.setSelectedItem(c.getEstado().name());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Cita getNuevaCita() {
        return nuevaCita;
    }
}
