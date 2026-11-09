package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ControladorPacientes {

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblConteoPacientes;

    @FXML
    private TableView<Paciente> tblPacientes;

    @FXML
    private TableColumn<Paciente, String> colId;

    @FXML
    private TableColumn<Paciente, String> colNombre;

    @FXML
    private TableColumn<Paciente, String> colEdad;

    @FXML
    private TableColumn<Paciente, String> colGenero;

    @FXML
    private TableColumn<Paciente, String> colTelefono;

    @FXML
    private TableColumn<Paciente, String> colEmail;

    @FXML
    private TableColumn<Paciente, String> colTipoSangre;

    @FXML
    private TableColumn<Paciente, String> colExpediente;

    // Formulario
    @FXML
    private Label lblTituloFormulario;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private Spinner<Integer> spEdad;

    @FXML
    private ComboBox<String> cmbGenero;

    @FXML
    private ComboBox<String> cmbTipoSangre;

    @FXML
    private TextArea txtHistorial;

    @FXML
    private Label lblNoExpediente;

    @FXML
    private Label lblFechaApertura;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnEliminar;

    private ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private FilteredList<Paciente> listaFiltrada;
    private Paciente pacienteSeleccionado = null;

    @FXML
    public void initialize() {
        // Inicializar combos y spinners
        spEdad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 120, 25));
        cmbGenero.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        cmbGenero.getSelectionModel().selectFirst();

        cmbTipoSangre.setItems(FXCollections.observableArrayList("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"));
        cmbTipoSangre.getSelectionModel().selectFirst();

        configurarColumnas();
        configurarFiltroBusqueda();

        // Listener de selección en tabla
        tblPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarPacienteEnFormulario(newVal);
            }
        });

        recargarTabla();
        btnEliminar.setDisable(true);
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getIdPersona() != null ? "#" + d.getValue().getIdPersona() : "-"
        ));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNombre() != null ? d.getValue().getNombre() : ""
        ));
        colEdad.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEdad() != null ? d.getValue().getEdad().toString() : "-"
        ));
        colGenero.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getGenero() != null ? d.getValue().getGenero() : "-"
        ));
        colTelefono.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTelefono() != null ? d.getValue().getTelefono() : "-"
        ));
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEmail() != null ? d.getValue().getEmail() : "-"
        ));
        colTipoSangre.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTipoSangre() != null ? d.getValue().getTipoSangre() : "-"
        ));
        colExpediente.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getExpediente() != null
                        ? "EXP-" + d.getValue().getExpediente().getIdExpediente()
                        : "Sin expediente"
        ));
    }

    private void configurarFiltroBusqueda() {
        listaFiltrada = new FilteredList<>(listaPacientes, p -> true);
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(paciente -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filtro = newVal.toLowerCase().trim();
                boolean coincideNombre = paciente.getNombre() != null && paciente.getNombre().toLowerCase().contains(filtro);
                boolean coincideEmail = paciente.getEmail() != null && paciente.getEmail().toLowerCase().contains(filtro);
                boolean coincideTel = paciente.getTelefono() != null && paciente.getTelefono().contains(filtro);
                return coincideNombre || coincideEmail || coincideTel;
            });
            actualizarConteo();
        });
        tblPacientes.setItems(listaFiltrada);
    }

    @FXML
    public void recargarTabla() {
        listaPacientes.setAll(ServicioDatos.getInstancia().listarPacientes());
        actualizarConteo();
    }

    private void actualizarConteo() {
        lblConteoPacientes.setText("Mostrando " + listaFiltrada.size() + " de " + listaPacientes.size() + " pacientes");
    }

    private void cargarPacienteEnFormulario(Paciente p) {
        this.pacienteSeleccionado = p;
        lblTituloFormulario.setText("Editar Paciente #" + p.getIdPersona());
        txtNombre.setText(p.getNombre());
        txtEmail.setText(p.getEmail());
        txtTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "");
        txtDireccion.setText(p.getDireccion() != null ? p.getDireccion() : "");
        if (p.getEdad() != null) {
            spEdad.getValueFactory().setValue(p.getEdad());
        }
        if (p.getGenero() != null) {
            cmbGenero.getSelectionModel().select(p.getGenero());
        }
        if (p.getTipoSangre() != null) {
            cmbTipoSangre.getSelectionModel().select(p.getTipoSangre());
        }
        txtHistorial.setText(p.getHistorial() != null ? p.getHistorial() : "");

        ExpedienteMedico exp = p.getExpediente();
        if (exp != null) {
            lblNoExpediente.setText("Expediente Clínico: EXP-" + exp.getIdExpediente());
            lblFechaApertura.setText("Fecha de Apertura: " + (exp.getFechaCreacion() != null ? exp.getFechaCreacion() : "N/D"));
        } else {
            lblNoExpediente.setText("Expediente Clínico: No asignado");
            lblFechaApertura.setText("Fecha de Apertura: N/D");
        }

        btnGuardar.setText("💾 Actualizar");
        btnEliminar.setDisable(false);
    }

    @FXML
    public void handleNuevo() {
        pacienteSeleccionado = null;
        tblPacientes.getSelectionModel().clearSelection();
        lblTituloFormulario.setText("Nuevo Registro de Paciente");
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        spEdad.getValueFactory().setValue(25);
        cmbGenero.getSelectionModel().selectFirst();
        cmbTipoSangre.getSelectionModel().selectFirst();
        txtHistorial.clear();
        lblNoExpediente.setText("Expediente: Automático al registrar");
        lblFechaApertura.setText("Fecha de Apertura: Registro del día");
        btnGuardar.setText("💾 Guardar");
        btnEliminar.setDisable(true);
        txtNombre.requestFocus();
    }

    @FXML
    public void handleGuardar() {
        try {
            String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
            String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";

            if (nombre.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "El nombre del paciente es requerido.");
                txtNombre.requestFocus();
                return;
            }

            if (email.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "El correo electrónico es requerido.");
                txtEmail.requestFocus();
                return;
            }

            Paciente p = pacienteSeleccionado != null ? pacienteSeleccionado : new Paciente();
            p.setNombre(nombre);
            p.setEmail(email);
            p.setTelefono(txtTelefono.getText() != null ? txtTelefono.getText().trim() : "");
            p.setDireccion(txtDireccion.getText() != null ? txtDireccion.getText().trim() : "");
            p.setEdad(spEdad.getValue());
            p.setGenero(cmbGenero.getValue());
            p.setTipoSangre(cmbTipoSangre.getValue());
            p.setHistorial(txtHistorial.getText() != null ? txtHistorial.getText().trim() : "");

            ServicioDatos.getInstancia().guardarPaciente(p);

            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Operación completada",
                    "Paciente " + (pacienteSeleccionado != null ? "actualizado" : "registrado") + " correctamente.");

            recargarTabla();
            handleNuevo();
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", "No se pudo registrar el paciente", e.getMessage());
        }
    }

    @FXML
    public void handleEliminar() {
        if (pacienteSeleccionado == null) return;

        boolean confirmar = Navegador.confirmar(
                "Eliminar Paciente",
                "¿Eliminar al paciente " + pacienteSeleccionado.getNombre() + "?",
                "Esta acción no se puede deshacer. (No se permite eliminar pacientes con consultas registradas)."
        );

        if (confirmar) {
            try {
                ServicioDatos.getInstancia().eliminarPaciente(pacienteSeleccionado.getIdPersona());
                Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Paciente Eliminado", "Operación exitosa", "El paciente ha sido retirado del sistema.");
                recargarTabla();
                handleNuevo();
            } catch (Exception e) {
                Navegador.mostrarAlerta(Alert.AlertType.ERROR, "No se puede eliminar", "Validación de Integridad", e.getMessage());
            }
        }
    }
}
