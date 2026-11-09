package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class ControladorMedicos {

    @FXML
    private TabPane tabPanePrincipal;

    // --- PESTAÑA MÉDICOS ---
    @FXML
    private TableView<Medico> tblMedicos;

    @FXML
    private TableColumn<Medico, String> colMedId;

    @FXML
    private TableColumn<Medico, String> colMedColegiado;

    @FXML
    private TableColumn<Medico, String> colMedNombre;

    @FXML
    private TableColumn<Medico, String> colMedEspecialidad;

    @FXML
    private TableColumn<Medico, String> colMedHorario;

    @FXML
    private TableColumn<Medico, String> colMedTelefono;

    @FXML
    private TableColumn<Medico, String> colMedEmail;

    // Formulario Médico
    @FXML
    private Label lblTituloFormMedico;

    @FXML
    private TextField txtMedNombre;

    @FXML
    private TextField txtMedColegiado;

    @FXML
    private TextField txtMedTelefono;

    @FXML
    private ComboBox<Especialidad> cmbMedEspecialidad;

    @FXML
    private TextField txtMedHorario;

    @FXML
    private TextField txtMedEmail;

    @FXML
    private TextField txtMedDireccion;

    @FXML
    private Button btnGuardarMedico;

    @FXML
    private Button btnEliminarMedico;

    private Medico medicoSeleccionado = null;
    private final ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();

    // --- PESTAÑA ESPECIALIDADES ---
    @FXML
    private TableView<Especialidad> tblEspecialidades;

    @FXML
    private TableColumn<Especialidad, String> colEspId;

    @FXML
    private TableColumn<Especialidad, String> colEspNombre;

    @FXML
    private TableColumn<Especialidad, String> colEspCosto;

    @FXML
    private TableColumn<Especialidad, String> colEspDescripcion;

    // Formulario Especialidad
    @FXML
    private Label lblTituloFormEsp;

    @FXML
    private TextField txtEspNombre;

    @FXML
    private TextField txtEspCosto;

    @FXML
    private TextArea txtEspDescripcion;

    @FXML
    private Button btnGuardarEsp;

    @FXML
    private Button btnEliminarEsp;

    private Especialidad especialidadSeleccionada = null;
    private final ObservableList<Especialidad> listaEspecialidades = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnasMedicos();
        configurarColumnasEspecialidades();
        configurarComboEspecialidades();

        tblMedicos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarMedicoEnFormulario(newVal);
            }
        });

        tblEspecialidades.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarEspecialidadEnFormulario(newVal);
            }
        });

        recargarEspecialidades();
        recargarMedicos();

        btnEliminarMedico.setDisable(true);
        btnEliminarEsp.setDisable(true);
    }

    private void configurarColumnasMedicos() {
        colMedId.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getIdPersona() != null ? "#" + d.getValue().getIdPersona() : "-"
        ));
        colMedColegiado.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getColegiado() != null ? d.getValue().getColegiado() : "-"
        ));
        colMedNombre.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNombre() != null ? d.getValue().getNombre() : ""
        ));
        colMedEspecialidad.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEspecialidad() != null ? d.getValue().getEspecialidad().getNombre() : "General"
        ));
        colMedHorario.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getHorarioAtencion() != null ? d.getValue().getHorarioAtencion() : "-"
        ));
        colMedTelefono.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTelefono() != null ? d.getValue().getTelefono() : "-"
        ));
        colMedEmail.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEmail() != null ? d.getValue().getEmail() : "-"
        ));
        tblMedicos.setItems(listaMedicos);
    }

    private void configurarColumnasEspecialidades() {
        colEspId.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getIdEspecialidad() != null ? "#" + d.getValue().getIdEspecialidad() : "-"
        ));
        colEspNombre.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNombre() != null ? d.getValue().getNombre() : ""
        ));
        colEspCosto.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("Q%.2f", d.getValue().getCostoConsulta())
        ));
        colEspDescripcion.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDescripcion() != null ? d.getValue().getDescripcion() : ""
        ));
        tblEspecialidades.setItems(listaEspecialidades);
    }

    private void configurarComboEspecialidades() {
        cmbMedEspecialidad.setConverter(new StringConverter<Especialidad>() {
            @Override
            public String toString(Especialidad esp) {
                return esp != null ? esp.getNombre() + " (Q" + esp.getCostoConsulta() + ")" : "";
            }

            @Override
            public Especialidad fromString(String string) {
                return null;
            }
        });
        cmbMedEspecialidad.setItems(listaEspecialidades);
    }

    // --- ACCIONES MÉDICOS ---
    @FXML
    public void recargarMedicos() {
        listaMedicos.setAll(ServicioDatos.getInstancia().listarMedicos());
    }

    private void cargarMedicoEnFormulario(Medico m) {
        this.medicoSeleccionado = m;
        lblTituloFormMedico.setText("Editar Médico #" + m.getIdPersona());
        txtMedNombre.setText(m.getNombre());
        txtMedColegiado.setText(m.getColegiado());
        txtMedTelefono.setText(m.getTelefono() != null ? m.getTelefono() : "");
        txtMedHorario.setText(m.getHorarioAtencion() != null ? m.getHorarioAtencion() : "");
        txtMedEmail.setText(m.getEmail() != null ? m.getEmail() : "");
        txtMedDireccion.setText(m.getDireccion() != null ? m.getDireccion() : "");

        if (m.getEspecialidad() != null) {
            for (Especialidad esp : listaEspecialidades) {
                if (esp.getIdEspecialidad().equals(m.getEspecialidad().getIdEspecialidad())) {
                    cmbMedEspecialidad.getSelectionModel().select(esp);
                    break;
                }
            }
        }
        btnGuardarMedico.setText("💾 Actualizar");
        btnEliminarMedico.setDisable(false);
    }

    @FXML
    public void handleNuevoMedico() {
        medicoSeleccionado = null;
        tblMedicos.getSelectionModel().clearSelection();
        lblTituloFormMedico.setText("Registrar Nuevo Médico");
        txtMedNombre.clear();
        txtMedColegiado.clear();
        txtMedTelefono.clear();
        txtMedHorario.setText("08:00-12:00;14:00-18:00");
        txtMedEmail.clear();
        txtMedDireccion.clear();
        cmbMedEspecialidad.getSelectionModel().clearSelection();
        btnGuardarMedico.setText("💾 Guardar");
        btnEliminarMedico.setDisable(true);
        txtMedNombre.requestFocus();
    }

    @FXML
    public void handleGuardarMedico() {
        try {
            String nombre = txtMedNombre.getText() != null ? txtMedNombre.getText().trim() : "";
            String colegiado = txtMedColegiado.getText() != null ? txtMedColegiado.getText().trim() : "";
            Especialidad esp = cmbMedEspecialidad.getValue();

            if (nombre.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "El nombre del médico es requerido.");
                txtMedNombre.requestFocus();
                return;
            }
            if (colegiado.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "El número de colegiado es requerido.");
                txtMedColegiado.requestFocus();
                return;
            }
            if (esp == null) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "Debe seleccionar una especialidad.");
                cmbMedEspecialidad.requestFocus();
                return;
            }

            Medico m = medicoSeleccionado != null ? medicoSeleccionado : new Medico();
            m.setNombre(nombre);
            m.setColegiado(colegiado);
            m.setEspecialidad(esp);
            m.setTelefono(txtMedTelefono.getText() != null ? txtMedTelefono.getText().trim() : "");
            m.setHorarioAtencion(txtMedHorario.getText() != null ? txtMedHorario.getText().trim() : "08:00-16:00");
            m.setEmail(txtMedEmail.getText() != null ? txtMedEmail.getText().trim() : "");
            m.setDireccion(txtMedDireccion.getText() != null ? txtMedDireccion.getText().trim() : "");

            ServicioDatos.getInstancia().guardarMedico(m);

            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Operación completada",
                    "Médico " + (medicoSeleccionado != null ? "actualizado" : "registrado") + " correctamente.");

            recargarMedicos();
            handleNuevoMedico();
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el médico", e.getMessage());
        }
    }

    @FXML
    public void handleEliminarMedico() {
        if (medicoSeleccionado == null) return;
        boolean confirmar = Navegador.confirmar(
                "Eliminar Médico",
                "¿Desea eliminar al médico " + medicoSeleccionado.getNombre() + "?",
                "Esta acción quitará al médico del directorio activo."
        );
        if (confirmar) {
            try {
                ServicioDatos.getInstancia().eliminarMedico(medicoSeleccionado.getIdPersona());
                Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Médico Eliminado", "Operación exitosa", "El médico ha sido retirado.");
                recargarMedicos();
                handleNuevoMedico();
            } catch (Exception e) {
                Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el médico", e.getMessage());
            }
        }
    }

    // --- ACCIONES ESPECIALIDADES ---
    @FXML
    public void recargarEspecialidades() {
        listaEspecialidades.setAll(ServicioDatos.getInstancia().listarEspecialidades());
    }

    private void cargarEspecialidadEnFormulario(Especialidad e) {
        this.especialidadSeleccionada = e;
        lblTituloFormEsp.setText("Editar Especialidad #" + e.getIdEspecialidad());
        txtEspNombre.setText(e.getNombre());
        txtEspCosto.setText(String.valueOf(e.getCostoConsulta()));
        txtEspDescripcion.setText(e.getDescripcion() != null ? e.getDescripcion() : "");
        btnGuardarEsp.setText("💾 Actualizar");
        btnEliminarEsp.setDisable(false);
    }

    @FXML
    public void handleNuevaEspecialidad() {
        especialidadSeleccionada = null;
        tblEspecialidades.getSelectionModel().clearSelection();
        lblTituloFormEsp.setText("Nueva Especialidad");
        txtEspNombre.clear();
        txtEspCosto.clear();
        txtEspDescripcion.clear();
        btnGuardarEsp.setText("💾 Guardar");
        btnEliminarEsp.setDisable(true);
        txtEspNombre.requestFocus();
    }

    @FXML
    public void handleGuardarEspecialidad() {
        try {
            String nombre = txtEspNombre.getText() != null ? txtEspNombre.getText().trim() : "";
            String costoStr = txtEspCosto.getText() != null ? txtEspCosto.getText().trim() : "";

            if (nombre.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Campo obligatorio", "El nombre de la especialidad es requerido.");
                txtEspNombre.requestFocus();
                return;
            }

            double costo;
            try {
                costo = Double.parseDouble(costoStr);
                if (costo < 0) throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Costo Inválido", "Ingrese un valor numérico válido mayor o igual a 0.");
                txtEspCosto.requestFocus();
                return;
            }

            Especialidad e = especialidadSeleccionada != null ? especialidadSeleccionada : new Especialidad();
            e.setNombre(nombre);
            e.setCostoConsulta(costo);
            e.setDescripcion(txtEspDescripcion.getText() != null ? txtEspDescripcion.getText().trim() : "");

            ServicioDatos.getInstancia().guardarEspecialidad(e);

            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Operación completada",
                    "Especialidad " + (especialidadSeleccionada != null ? "actualizada" : "creada") + " correctamente.");

            recargarEspecialidades();
            recargarMedicos();
            handleNuevaEspecialidad();
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la especialidad", e.getMessage());
        }
    }

    @FXML
    public void handleEliminarEspecialidad() {
        if (especialidadSeleccionada == null) return;
        boolean confirmar = Navegador.confirmar(
                "Eliminar Especialidad",
                "¿Desea eliminar la especialidad " + especialidadSeleccionada.getNombre() + "?",
                "No se puede eliminar una especialidad si cuenta con médicos asociados."
        );
        if (confirmar) {
            try {
                ServicioDatos.getInstancia().eliminarEspecialidad(especialidadSeleccionada.getIdEspecialidad());
                Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Operación exitosa", "Especialidad eliminada correctamente.");
                recargarEspecialidades();
                recargarMedicos();
                handleNuevaEspecialidad();
            } catch (Exception e) {
                Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Validación de Integridad", "No se puede eliminar", e.getMessage());
            }
        }
    }
}
