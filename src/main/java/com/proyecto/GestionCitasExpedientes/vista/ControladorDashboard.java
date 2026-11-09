package com.proyecto.GestionCitasExpedientes.vista;

import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ControladorDashboard {

    @FXML
    private Label lblTotalPacientes;

    @FXML
    private Label lblTotalMedicos;

    @FXML
    private Label lblTotalCitas;

    @FXML
    private Label lblTotalConsultas;

    @FXML
    private TableView<Cita> tblCitasRecientes;

    @FXML
    private TableColumn<Cita, String> colCitaId;

    @FXML
    private TableColumn<Cita, String> colCitaFecha;

    @FXML
    private TableColumn<Cita, String> colCitaHora;

    @FXML
    private TableColumn<Cita, String> colCitaPaciente;

    @FXML
    private TableColumn<Cita, String> colCitaMedico;

    @FXML
    private TableColumn<Cita, String> colCitaEspecialidad;

    @FXML
    private TableColumn<Cita, String> colCitaEstado;

    @FXML
    private VBox contenedorEspecialidadesResumen;

    @FXML
    public void initialize() {
        configurarColumnasTabla();
        actualizarDashboard();
    }

    private void configurarColumnasTabla() {
        colCitaId.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIdCita() != null ? "#" + data.getValue().getIdCita() : "-"
        ));
        colCitaFecha.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFecha() != null ? data.getValue().getFecha().toString() : "-"
        ));
        colCitaHora.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getHora() != null ? data.getValue().getHora().toString() : "-"
        ));
        colCitaPaciente.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPaciente() != null ? data.getValue().getPaciente().getNombre() : "No asignado"
        ));
        colCitaMedico.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMedico() != null ? data.getValue().getMedico().getNombre() : "No asignado"
        ));
        colCitaEspecialidad.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMedico() != null && data.getValue().getMedico().getEspecialidad() != null
                        ? data.getValue().getMedico().getEspecialidad().getNombre() : "General"
        ));

        colCitaEstado.setCellFactory(column -> new TableCell<Cita, String>() {
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
    }

    @FXML
    public void actualizarDashboard() {
        ServicioDatos sd = ServicioDatos.getInstancia();

        int totalPacientes = sd.listarPacientes().size();
        int totalMedicos = sd.listarMedicos().size();
        List<Cita> citas = sd.listarCitas();
        int totalCitas = citas.size();
        int totalConsultas = sd.listarConsultas().size();

        lblTotalPacientes.setText(String.valueOf(totalPacientes));
        lblTotalMedicos.setText(String.valueOf(totalMedicos));
        lblTotalCitas.setText(String.valueOf(totalCitas));
        lblTotalConsultas.setText(String.valueOf(totalConsultas));

        tblCitasRecientes.setItems(FXCollections.observableArrayList(citas));

        // Actualizar resumen de especialidades
        contenedorEspecialidadesResumen.getChildren().clear();
        List<Especialidad> especialidades = sd.listarEspecialidades();
        for (Especialidad e : especialidades) {
            HBox item = new HBox(8);
            item.setStyle("-fx-padding: 4 6; -fx-background-color: #f8fafc; -fx-background-radius: 4;");
            Label lblNombre = new Label("• " + e.getNombre());
            lblNombre.setStyle("-fx-font-weight: 600; -fx-text-fill: #334155;");
            Label lblCosto = new Label("Q" + String.format("%.2f", e.getCostoConsulta()));
            lblCosto.setStyle("-fx-text-fill: #0284c7; -fx-font-weight: bold;");
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            item.getChildren().addAll(lblNombre, spacer, lblCosto);
            contenedorEspecialidadesResumen.getChildren().add(item);
        }
    }

    @FXML
    private void irACitas() {
        if (Navegador.getControladorPrincipal() != null) {
            Navegador.getControladorPrincipal().navegarACitas();
        }
    }

    @FXML
    private void irAPacientes() {
        if (Navegador.getControladorPrincipal() != null) {
            Navegador.getControladorPrincipal().navegarAPacientes();
        }
    }

    @FXML
    private void irAConsultas() {
        if (Navegador.getControladorPrincipal() != null) {
            Navegador.getControladorPrincipal().navegarAConsultas();
        }
    }

    @FXML
    private void irAMedicos() {
        if (Navegador.getControladorPrincipal() != null) {
            Navegador.getControladorPrincipal().navegarAMedicos();
        }
    }
}
