package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class ControladorCitas {

    // --- FILTROS ---
    @FXML
    private DatePicker dpFiltroFecha;

    @FXML
    private ComboBox<Medico> cmbFiltroMedico;

    @FXML
    private ComboBox<String> cmbFiltroEstado;

    @FXML
    private Label lblConteoCitas;

    // --- TABLA DE CITAS ---
    @FXML
    private TableView<Cita> tblCitas;

    @FXML
    private TableColumn<Cita, String> colId;

    @FXML
    private TableColumn<Cita, String> colFecha;

    @FXML
    private TableColumn<Cita, String> colHora;

    @FXML
    private TableColumn<Cita, String> colPaciente;

    @FXML
    private TableColumn<Cita, String> colMedico;

    @FXML
    private TableColumn<Cita, String> colEspecialidad;

    @FXML
    private TableColumn<Cita, String> colCosto;

    @FXML
    private TableColumn<Cita, String> colEstado;

    @FXML
    private TableColumn<Cita, String> colMotivo;

    // Botones de acción sobre cita
    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnAtenderConsulta;

    // --- FORMULARIO NUEVA CITA ---
    @FXML
    private ComboBox<Paciente> cmbPaciente;

    @FXML
    private ComboBox<Medico> cmbMedico;

    @FXML
    private Label lblEspecialidadInfo;

    @FXML
    private Label lblCostoInfo;

    @FXML
    private Label lblHorarioMedicoInfo;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private ComboBox<String> cmbHora;

    private final ObservableList<Cita> listaCitas = FXCollections.observableArrayList();
    private FilteredList<Cita> listaFiltrada;
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private final ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();

    private Cita citaSeleccionada = null;

    @FXML
    public void initialize() {
        configurarColumnasTabla();
        configurarCombosFormulario();
        configurarFiltros();

        tblCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            citaSeleccionada = newVal;
            boolean seleccionada = newVal != null;
            btnConfirmar.setDisable(!seleccionada || (newVal.getConfirmada() != null && newVal.getConfirmada()));
            btnCancelar.setDisable(!seleccionada || (newVal.getConfirmada() != null && !newVal.getConfirmada()));
            btnAtenderConsulta.setDisable(!seleccionada || (newVal.getConfirmada() != null && !newVal.getConfirmada()));
        });

        dpFecha.setValue(LocalDate.now());
        btnConfirmar.setDisable(true);
        btnCancelar.setDisable(true);
        btnAtenderConsulta.setDisable(true);

        recargarCitas();
        recargarListas();
    }

    private void configurarColumnasTabla() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getIdCita() != null ? "#" + d.getValue().getIdCita() : "-"
        ));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFecha() != null ? d.getValue().getFecha().toString() : "-"
        ));
        colHora.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getHora() != null ? d.getValue().getHora().toString() : "-"
        ));
        colPaciente.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPaciente() != null ? d.getValue().getPaciente().getNombre() : "N/A"
        ));
        colMedico.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getMedico() != null ? d.getValue().getMedico().getNombre() : "N/A"
        ));
        colEspecialidad.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getMedico() != null && d.getValue().getMedico().getEspecialidad() != null
                        ? d.getValue().getMedico().getEspecialidad().getNombre() : "General"
        ));
        colCosto.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCosto() != null ? String.format("Q%.2f", d.getValue().getCosto()) : "Q0.00"
        ));

        colEstado.setCellFactory(col -> new TableCell<Cita, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Cita c = getTableRow().getItem();
                    Label pill = new Label();
                    if (c.getConfirmada() != null && c.getConfirmada()) {
                        pill.setText("Confirmada");
                        pill.getStyleClass().setAll("pill-confirmada");
                    } else {
                        pill.setText("Cancelada");
                        pill.getStyleClass().setAll("pill-cancelada");
                    }
                    setGraphic(pill);
                    setText(null);
                }
            }
        });

        colMotivo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getMotivoCancelacion() != null ? d.getValue().getMotivoCancelacion() : "-"
        ));
    }

    private void configurarCombosFormulario() {
        // Formato para combo pacientes
        cmbPaciente.setConverter(new StringConverter<Paciente>() {
            @Override
            public String toString(Paciente p) {
                return p != null ? p.getNombre() + " (ID: #" + p.getIdPersona() + ")" : "";
            }
            @Override
            public Paciente fromString(String string) { return null; }
        });
        cmbPaciente.setItems(listaPacientes);

        // Formato para combo médicos
        cmbMedico.setConverter(new StringConverter<Medico>() {
            @Override
            public String toString(Medico m) {
                if (m == null) return "";
                String esp = m.getEspecialidad() != null ? m.getEspecialidad().getNombre() : "General";
                return m.getNombre() + " - " + esp;
            }
            @Override
            public Medico fromString(String string) { return null; }
        });
        cmbMedico.setItems(listaMedicos);

        // Actualizar datos de especialidad y costo al elegir médico
        cmbMedico.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.getEspecialidad() != null) {
                    lblEspecialidadInfo.setText(newVal.getEspecialidad().getNombre());
                    lblCostoInfo.setText(String.format("Q%.2f", newVal.getEspecialidad().getCostoConsulta()));
                } else {
                    lblEspecialidadInfo.setText("Medicina General");
                    lblCostoInfo.setText("Q50.00");
                }
                lblHorarioMedicoInfo.setText("Horario: " + (newVal.getHorarioAtencion() != null ? newVal.getHorarioAtencion() : "08:00-16:00"));
            } else {
                lblEspecialidadInfo.setText("Seleccione un médico");
                lblCostoInfo.setText("Q0.00");
                lblHorarioMedicoInfo.setText("Horario: -");
            }
        });

        // Horarios disponibles cada 30 min
        ObservableList<String> horas = FXCollections.observableArrayList(
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00", "17:30"
        );
        cmbHora.setItems(horas);
        cmbHora.getSelectionModel().selectFirst();
    }

    private void configurarFiltros() {
        cmbFiltroEstado.setItems(FXCollections.observableArrayList("Todas", "Confirmadas", "Canceladas"));
        cmbFiltroEstado.getSelectionModel().select("Todas");

        cmbFiltroMedico.setConverter(new StringConverter<Medico>() {
            @Override
            public String toString(Medico m) {
                return m != null ? m.getNombre() : "Todos los Médicos";
            }
            @Override
            public Medico fromString(String string) { return null; }
        });

        listaFiltrada = new FilteredList<>(listaCitas, c -> true);

        // Listeners para refresco dinámico de filtros
        dpFiltroFecha.valueProperty().addListener((o, oldV, newV) -> aplicarFiltros());
        cmbFiltroMedico.valueProperty().addListener((o, oldV, newV) -> aplicarFiltros());
        cmbFiltroEstado.valueProperty().addListener((o, oldV, newV) -> aplicarFiltros());

        tblCitas.setItems(listaFiltrada);
    }

    private void aplicarFiltros() {
        LocalDate fecha = dpFiltroFecha.getValue();
        Medico med = cmbFiltroMedico.getValue();
        String estado = cmbFiltroEstado.getValue();

        listaFiltrada.setPredicate(c -> {
            if (fecha != null && !fecha.equals(c.getFecha())) {
                return false;
            }
            if (med != null && (c.getMedico() == null || !med.getIdPersona().equals(c.getMedico().getIdPersona()))) {
                return false;
            }
            if ("Confirmadas".equalsIgnoreCase(estado) && (c.getConfirmada() == null || !c.getConfirmada())) {
                return false;
            }
            if ("Canceladas".equalsIgnoreCase(estado) && (c.getConfirmada() != null && c.getConfirmada())) {
                return false;
            }
            return true;
        });

        lblConteoCitas.setText("Mostrando " + listaFiltrada.size() + " de " + listaCitas.size() + " citas");
    }

    @FXML
    public void handleLimpiarFiltros() {
        dpFiltroFecha.setValue(null);
        cmbFiltroMedico.setValue(null);
        cmbFiltroEstado.getSelectionModel().select("Todas");
        aplicarFiltros();
    }

    @FXML
    public void recargarCitas() {
        listaCitas.setAll(ServicioDatos.getInstancia().listarCitas());
        aplicarFiltros();
    }

    public void recargarListas() {
        listaPacientes.setAll(ServicioDatos.getInstancia().listarPacientes());
        listaMedicos.setAll(ServicioDatos.getInstancia().listarMedicos());
        cmbFiltroMedico.setItems(FXCollections.observableArrayList(listaMedicos));
    }

    @FXML
    public void handleProgramarCita() {
        try {
            Paciente pac = cmbPaciente.getValue();
            Medico med = cmbMedico.getValue();
            LocalDate fecha = dpFecha.getValue();
            String horaStr = cmbHora.getValue();

            if (pac == null) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Paciente requerido", "Debe seleccionar un paciente de la lista.");
                cmbPaciente.requestFocus();
                return;
            }
            if (med == null) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Médico requerido", "Debe seleccionar un médico.");
                cmbMedico.requestFocus();
                return;
            }
            if (fecha == null) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Fecha requerida", "Debe seleccionar una fecha para la cita.");
                dpFecha.requestFocus();
                return;
            }
            if (fecha.isBefore(LocalDate.now())) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Fecha no válida", "La cita no puede programarse en una fecha pasada.");
                dpFecha.requestFocus();
                return;
            }
            if (horaStr == null) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Hora requerida", "Seleccione la hora de la cita.");
                cmbHora.requestFocus();
                return;
            }

            LocalTime hora = LocalTime.parse(horaStr);

            Cita nueva = new Cita();
            nueva.setPaciente(pac);
            nueva.setMedico(med);
            nueva.setFecha(fecha);
            nueva.setHora(hora);

            ServicioDatos.getInstancia().programarCita(nueva);

            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Cita Programada", "Registro exitoso",
                    "Cita agendada para " + pac.getNombre() + " con " + med.getNombre() + " el " + fecha + " a las " + hora + ".");

            recargarCitas();
            handleLimpiarFormulario();
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "No se pudo programar la cita", "Validación de Regla", e.getMessage());
        }
    }

    @FXML
    public void handleLimpiarFormulario() {
        cmbPaciente.getSelectionModel().clearSelection();
        cmbMedico.getSelectionModel().clearSelection();
        dpFecha.setValue(LocalDate.now());
        cmbHora.getSelectionModel().selectFirst();
        lblEspecialidadInfo.setText("Seleccione un médico");
        lblCostoInfo.setText("Q0.00");
        lblHorarioMedicoInfo.setText("Horario: -");
    }

    @FXML
    public void handleConfirmarCita() {
        if (citaSeleccionada == null) return;
        try {
            ServicioDatos.getInstancia().confirmarCita(citaSeleccionada.getIdCita());
            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Cita Confirmada", "Éxito", "La cita #" + citaSeleccionada.getIdCita() + " ha sido confirmada.");
            recargarCitas();
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo confirmar la cita", e.getMessage());
        }
    }

    @FXML
    public void handleCancelarCita() {
        if (citaSeleccionada == null) return;

        TextInputDialog dialog = new TextInputDialog("Cancelación solicitada por el paciente");
        dialog.setTitle("Cancelar Cita Médica");
        dialog.setHeaderText("Cita #" + citaSeleccionada.getIdCita() + " - " + citaSeleccionada.getPaciente().getNombre());
        dialog.setContentText("Ingrese el motivo de la cancelación:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(motivo -> {
            if (motivo.isBlank()) {
                Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Motivo requerido", "Debe ingresar una justificación para la cancelación.");
                return;
            }
            try {
                String usuario = ServicioDatos.getInstancia().getUsuarioActual();
                ServicioDatos.getInstancia().cancelarCita(citaSeleccionada.getIdCita(), motivo, usuario);
                Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Cita Cancelada", "Operación exitosa", "La cita ha sido marcada como cancelada.");
                recargarCitas();
            } catch (Exception e) {
                Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cancelar la cita", e.getMessage());
            }
        });
    }

    @FXML
    public void handleAtenderConsulta() {
        if (citaSeleccionada == null) return;
        if (citaSeleccionada.getConfirmada() != null && !citaSeleccionada.getConfirmada()) {
            Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Cita Cancelada", "No se puede atender una cita médica cancelada.");
            return;
        }

        // Navegar a la pantalla de Consultas Médicas
        if (Navegador.getControladorPrincipal() != null) {
            Navegador.getControladorPrincipal().navegarAConsultas();
        }
    }
}
