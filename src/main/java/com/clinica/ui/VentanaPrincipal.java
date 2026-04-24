package com.clinica.ui;

import com.clinica.modelo.Paciente;
import com.clinica.modelo.Turno;
import com.clinica.servicio.PacienteService;
import com.clinica.servicio.TurnoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana principal del sistema.
 * Implementa las funcionalidades de registro de pacientes (CU-01),
 * asignación de turnos (CU-02) y consulta de historial (CU-04).
 *
 * @author Fiume, Agustín - VINF016173
 */
public class VentanaPrincipal extends JFrame {

    private final PacienteService pacienteService = new PacienteService();
    private final TurnoService    turnoService    = new TurnoService();

    private JTabbedPane pestanias;

    // ── Pestaña: Pacientes ────────────────────────────────────
    private JTextField txtBuscarPaciente;
    private JTable     tablaPacientes;
    private DefaultTableModel modeloPacientes;

    // ── Pestaña: Nuevo Paciente ───────────────────────────────
    private JTextField txtNombre, txtApellido, txtDni, txtFechaNac, txtTelefono, txtEmail;
    private JComboBox<String> cmbObraSocial;

    // ── Pestaña: Turnos ───────────────────────────────────────
    private JTextField txtDniTurno, txtFechaHora;
    private JComboBox<String> cmbProfesional;
    private JTextField txtMotivo;
    private JTable     tablaTurnos;
    private DefaultTableModel modeloTurnos;

    public VentanaPrincipal() {
        super("Sistema de Gestión de Turnos — Clínica Salud Integral");
        inicializar();
    }

    private void inicializar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        pestanias = new JTabbedPane();
        pestanias.addTab("Pacientes",      crearPanelPacientes());
        pestanias.addTab("Nuevo Paciente", crearPanelNuevoPaciente());
        pestanias.addTab("Turnos",         crearPanelTurnos());

        add(pestanias);
        cargarTablaPacientes("");
    }

    // ── Panel: Pacientes ──────────────────────────────────────
    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra de búsqueda
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.add(new JLabel("Buscar:"));
        txtBuscarPaciente = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarTablaPacientes(txtBuscarPaciente.getText()));
        panelBuscar.add(txtBuscarPaciente);
        panelBuscar.add(btnBuscar);

        // Tabla
        String[] columnas = { "ID", "Apellido y Nombre", "DNI", "Teléfono", "Email" };
        modeloPacientes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPacientes = new JTable(modeloPacientes);
        tablaPacientes.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaPacientes.getColumnModel().getColumn(1).setPreferredWidth(200);

        // Botón ver historial
        JButton btnHistorial = new JButton("Ver historial del paciente seleccionado");
        btnHistorial.addActionListener(e -> verHistorial());

        panel.add(panelBuscar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);
        panel.add(btnHistorial, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarTablaPacientes(String texto) {
        modeloPacientes.setRowCount(0);
        try {
            List<Paciente> lista = pacienteService.buscar(texto);
            for (Paciente p : lista) {
                modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombreCompleto(), p.getDni(),
                    p.getTelefono(), p.getEmail()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar pacientes: " + ex.getMessage());
        }
    }

    private void verHistorial() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un paciente de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaciente = (int) modeloPacientes.getValueAt(fila, 0);
        String nombre  = (String) modeloPacientes.getValueAt(fila, 1);

        try {
            List<Turno> historial = turnoService.obtenerHistorial(idPaciente);
            StringBuilder sb = new StringBuilder("Historial de " + nombre + ":\n\n");
            if (historial.isEmpty()) {
                sb.append("No hay consultas registradas.");
            } else {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (Turno t : historial) {
                    sb.append("• ").append(t.getFechaHora().format(fmt))
                      .append(" — ").append(t.getNombreProfesional())
                      .append(" [").append(t.getEstado()).append("]\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Historial del paciente", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarError("Error al cargar historial: " + ex.getMessage());
        }
    }

    // ── Panel: Nuevo Paciente ─────────────────────────────────
    private JPanel crearPanelNuevoPaciente() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtNombre    = new JTextField(20);
        txtApellido  = new JTextField(20);
        txtDni       = new JTextField(10);
        txtFechaNac  = new JTextField(10);
        txtTelefono  = new JTextField(15);
        txtEmail     = new JTextField(25);
        cmbObraSocial = new JComboBox<>(new String[]{"1 - OSDE", "2 - Swiss Medical", "3 - PAMI", "4 - Particular"});

        agregarCampo(panel, gbc, 0, "Nombre:",           txtNombre);
        agregarCampo(panel, gbc, 1, "Apellido:",         txtApellido);
        agregarCampo(panel, gbc, 2, "DNI:",              txtDni);
        agregarCampo(panel, gbc, 3, "Fecha nac. (yyyy-MM-dd):", txtFechaNac);
        agregarCampo(panel, gbc, 4, "Teléfono:",         txtTelefono);
        agregarCampo(panel, gbc, 5, "Email:",            txtEmail);
        agregarCampo(panel, gbc, 6, "Obra social:",      cmbObraSocial);

        JButton btnGuardar = new JButton("Registrar Paciente");
        btnGuardar.addActionListener(e -> registrarPaciente());
        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void registrarPaciente() {
        try {
            Paciente p = new Paciente();
            p.setNombre(txtNombre.getText().trim());
            p.setApellido(txtApellido.getText().trim());
            p.setDni(txtDni.getText().trim());
            p.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText().trim()));
            p.setTelefono(txtTelefono.getText().trim());
            p.setEmail(txtEmail.getText().trim());
            p.setIdObraSocial(cmbObraSocial.getSelectedIndex() + 1);

            pacienteService.registrar(p);
            JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos y actualizar tabla
            txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
            txtFechaNac.setText(""); txtTelefono.setText(""); txtEmail.setText("");
            cargarTablaPacientes("");
            pestanias.setSelectedIndex(0);

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha incorrecto. Usá el formato yyyy-MM-dd (ej: 1990-05-21).");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error al registrar el paciente: " + ex.getMessage());
        }
    }

    // ── Panel: Turnos ─────────────────────────────────────────
    private JPanel crearPanelTurnos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario de asignación
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Asignar nuevo turno"));

        txtDniTurno  = new JTextField();
        txtFechaHora = new JTextField("2025-08-01 09:00");
        txtMotivo    = new JTextField();
        cmbProfesional = new JComboBox<>(new String[]{
            "1 - González, María (Clínica Médica)",
            "2 - Rodríguez, Carlos (Pediatría)",
            "3 - Martínez, Laura (Cardiología)"
        });

        form.add(new JLabel("DNI del paciente:"));     form.add(txtDniTurno);
        form.add(new JLabel("Profesional:"));           form.add(cmbProfesional);
        form.add(new JLabel("Fecha y hora (yyyy-MM-dd HH:mm):")); form.add(txtFechaHora);
        form.add(new JLabel("Motivo de consulta:"));    form.add(txtMotivo);

        JButton btnAsignar = new JButton("Asignar Turno");
        btnAsignar.addActionListener(e -> asignarTurno());
        form.add(new JLabel()); form.add(btnAsignar);

        // Tabla de turnos del día
        String[] cols = { "ID", "Paciente", "Profesional", "Fecha y hora", "Estado" };
        modeloTurnos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTurnos = new JTable(modeloTurnos);

        JButton btnCancelar = new JButton("Cancelar turno seleccionado");
        btnCancelar.addActionListener(e -> cancelarTurno());

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaTurnos), BorderLayout.CENTER);
        panel.add(btnCancelar, BorderLayout.SOUTH);
        return panel;
    }

    private void asignarTurno() {
        try {
            String dni = txtDniTurno.getText().trim();
            Paciente paciente = pacienteService.buscarPorDni(dni);
            if (paciente == null) {
                mostrarError("No se encontró ningún paciente con DNI: " + dni);
                return;
            }

            int idProfesional   = cmbProfesional.getSelectedIndex() + 1;
            LocalDateTime fecha = LocalDateTime.parse(
                txtFechaHora.getText().trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );

            turnoService.asignar(paciente.getIdPaciente(), idProfesional, fecha, txtMotivo.getText().trim());
            JOptionPane.showMessageDialog(this, "Turno asignado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar tabla con los turnos del profesional para ese día
            modeloTurnos.setRowCount(0);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Turno t : turnoService.obtenerAgendaDia(idProfesional, fecha.toLocalDate())) {
                modeloTurnos.addRow(new Object[]{
                    t.getIdTurno(), t.getNombrePaciente(), t.getNombreProfesional(),
                    t.getFechaHora().format(fmt), t.getEstado()
                });
            }
            txtDniTurno.setText(""); txtMotivo.setText("");

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha incorrecto. Usá el formato yyyy-MM-dd HH:mm (ej: 2025-08-01 09:00).");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error al asignar el turno: " + ex.getMessage());
        }
    }

    private void cancelarTurno() {
        int fila = tablaTurnos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un turno de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idTurno = (int) modeloTurnos.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Confirmar la cancelación del turno #" + idTurno + "?",
            "Confirmar cancelación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                turnoService.cancelar(idTurno);
                modeloTurnos.removeRow(fila);
                JOptionPane.showMessageDialog(this, "Turno cancelado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                mostrarError("Error al cancelar el turno: " + ex.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Entry point ───────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new VentanaPrincipal().setVisible(true);
        });
    }
}
