package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorConsultas {

    @FXML
    private ComboBox<Cita> cmbCitasConfirmadas;

    @FXML
    private Label lblPacienteInfo;

    @FXML
    private Label lblMedicoInfo;

    @FXML
    private Label lblExpedienteInfo;

    @FXML
    private Label lblDatosClinicosInfo;

    @FXML
    private TextArea txtSintomas;

    @FXML
    private TextArea txtDiagnostico;

    @FXML
    private TextArea txtTratamiento;

    @FXML
    private Button btnGuardarConsulta;

    @FXML
    private TableView<Consulta> tblHistorialConsultas;

    @FXML
    private TableColumn<Consulta, String> colConId;

    @FXML
    private TableColumn<Consulta, String> colConFecha;

    @FXML
    private TableColumn<Consulta, String> colConSintomas;

    @FXML
    private TableColumn<Consulta, String> colConDiagnostico;

    @FXML
    private TableColumn<Consulta, String> colConTratamiento;

    @FXML
    private Label lblConteoConsultas;

    private final ObservableList<Cita> listaCitasDisponibles = FXCollections.observableArrayList();
    private final ObservableList<Consulta> listaHistorialConsultas = FXCollections.observableArrayList();

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        configurarColumnasTabla();
        configurarComboCitas();

        cmbCitasConfirmadas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarDatosDeCita(newVal);
            } else {
                limpiarFichaPaciente();
            }
        });

        recargarCitas();
    }

    private void configurarColumnasTabla() {
        colConId.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getIdConsulta() != null ? "#" + d.getValue().getIdConsulta() : "-"
        ));
        colConFecha.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFechaConsulta() != null ? d.getValue().getFechaConsulta().format(dtf) : "-"
        ));
        colConSintomas.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getSintomas() != null ? d.getValue().getSintomas() : ""
        ));
        colConDiagnostico.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDiagnostico() != null ? d.getValue().getDiagnostico() : ""
        ));
        colConTratamiento.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTratamiento() != null ? d.getValue().getTratamiento() : ""
        ));
        tblHistorialConsultas.setItems(listaHistorialConsultas);
    }

    private void configurarComboCitas() {
        cmbCitasConfirmadas.setConverter(new StringConverter<Cita>() {
            @Override
            public String toString(Cita c) {
                if (c == null) return "";
                String pac = c.getPaciente() != null ? c.getPaciente().getNombre() : "Sin Paciente";
                String med = c.getMedico() != null ? c.getMedico().getNombre() : "Sin Médico";
                return "Cita #" + c.getIdCita() + " - " + pac + " con " + med + " (" + c.getFecha() + " " + c.getHora() + ")";
            }

            @Override
            public Cita fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    public void recargarCitas() {
        List<Cita> todas = ServicioDatos.getInstancia().listarCitas();
        listaCitasDisponibles.clear();
        for (Cita c : todas) {
            // Solo citas confirmadas pueden atenderse
            if (c.getConfirmada() != null && c.getConfirmada()) {
                listaCitasDisponibles.add(c);
            }
        }
        cmbCitasConfirmadas.setItems(listaCitasDisponibles);
        if (!listaCitasDisponibles.isEmpty()) {
            cmbCitasConfirmadas.getSelectionModel().selectFirst();
        }
    }

    private void cargarDatosDeCita(Cita c) {
        Paciente pac = c.getPaciente();
        if (pac != null) {
            lblPacienteInfo.setText(pac.getNombre() + " (ID: #" + pac.getIdPersona() + ")");
            String edad = pac.getEdad() != null ? pac.getEdad() + " años" : "N/D";
            String sangre = pac.getTipoSangre() != null ? pac.getTipoSangre() : "N/D";
            String genero = pac.getGenero() != null ? pac.getGenero() : "N/D";
            lblDatosClinicosInfo.setText("Edad: " + edad + " | Sangre: " + sangre + " | Género: " + genero);

            ExpedienteMedico exp = pac.getExpediente();
            if (exp != null) {
                lblExpedienteInfo.setText("EXP-" + exp.getIdExpediente() + " (Apertura: " + exp.getFechaCreacion() + ")");
                recargarHistorialConsultas(exp.getIdExpediente());
            } else {
                lblExpedienteInfo.setText("Sin expediente previo");
                listaHistorialConsultas.clear();
                actualizarConteoHistorial();
            }
        }

        if (c.getMedico() != null) {
            String esp = c.getMedico().getEspecialidad() != null ? c.getMedico().getEspecialidad().getNombre() : "General";
            lblMedicoInfo.setText(c.getMedico().getNombre() + " (" + esp + ")");
        } else {
            lblMedicoInfo.setText("-");
        }
    }

    private void recargarHistorialConsultas(Integer idExpediente) {
        List<Consulta> historial = ServicioDatos.getInstancia().listarConsultasPorExpediente(idExpediente);
        listaHistorialConsultas.setAll(historial);
        actualizarConteoHistorial();
    }

    private void actualizarConteoHistorial() {
        lblConteoConsultas.setText("Total consultas en expediente: " + listaHistorialConsultas.size());
    }

    private void limpiarFichaPaciente() {
        lblPacienteInfo.setText("Seleccione una cita arriba");
        lblMedicoInfo.setText("-");
        lblExpedienteInfo.setText("-");
        lblDatosClinicosInfo.setText("Edad: - | Sangre: -");
        listaHistorialConsultas.clear();
        actualizarConteoHistorial();
    }

    @FXML
    public void handleLimpiarFormulario() {
        txtSintomas.clear();
        txtDiagnostico.clear();
        txtTratamiento.clear();
    }

    @FXML
    public void handleRegistrarConsulta() {
        Cita cita = cmbCitasConfirmadas.getValue();
        if (cita == null) {
            Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Cita requerida", "Debe seleccionar una cita médica a atender.");
            cmbCitasConfirmadas.requestFocus();
            return;
        }

        String sintomas = txtSintomas.getText() != null ? txtSintomas.getText().trim() : "";
        String diagnostico = txtDiagnostico.getText() != null ? txtDiagnostico.getText().trim() : "";
        String tratamiento = txtTratamiento.getText() != null ? txtTratamiento.getText().trim() : "";

        if (sintomas.isBlank()) {
            Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Síntomas requeridos", "Debe ingresar los síntomas del paciente.");
            txtSintomas.requestFocus();
            return;
        }
        if (diagnostico.isBlank()) {
            Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Diagnóstico requerido", "Debe registrar la conclusión diagnóstica.");
            txtDiagnostico.requestFocus();
            return;
        }
        if (tratamiento.isBlank()) {
            Navegador.mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Tratamiento requerido", "Debe ingresar el tratamiento o receta prescrita.");
            txtTratamiento.requestFocus();
            return;
        }

        try {
            Consulta nueva = new Consulta();
            nueva.setCita(cita);
            nueva.setSintomas(sintomas);
            nueva.setDiagnostico(diagnostico);
            nueva.setTratamiento(tratamiento);
            nueva.setFechaConsulta(LocalDateTime.now());

            Consulta registrada = ServicioDatos.getInstancia().registrarConsulta(nueva);

            Navegador.mostrarAlerta(Alert.AlertType.INFORMATION, "Consulta Completada", "Registro exitoso",
                    "Consulta #" + registrada.getIdConsulta() + " asentada en el expediente médico del paciente " +
                            cita.getPaciente().getNombre() + " exitosamente.");

            handleLimpiarFormulario();

            // Refrescar historial del expediente
            if (cita.getPaciente() != null && cita.getPaciente().getExpediente() != null) {
                recargarHistorialConsultas(cita.getPaciente().getExpediente().getIdExpediente());
            }
        } catch (Exception e) {
            Navegador.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar la consulta médica", e.getMessage());
        }
    }
}
