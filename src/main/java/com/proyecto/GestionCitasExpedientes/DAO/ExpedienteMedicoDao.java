package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.ExpedienteMedico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class ExpedienteMedicoDao {
    private final DataSource ds;

    public ExpedienteMedicoDao(DataSource ds) {
        this.ds = ds;
    }
    
    public ExpedienteMedico crearParaPaciente(Paciente p) {
        String sql = "INSERT INTO expediente_medico(paciente_id, fecha_creacion, antecedente) VALUES (?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdPersona());
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setString(3, p.getHistorial());
            ps.executeUpdate();
            ExpedienteMedico exp = new ExpedienteMedico();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) exp.setIdExpediente(rs.getInt(1));
            }
            exp.setPaciente(p);
            exp.setFechaCreacion(LocalDate.now());
            exp.setAntecedente(p.getHistorial());
            return exp;
        } catch (SQLException ex) {
            throw new RuntimeException("Error crear expediente: " + ex.getMessage(), ex);
        }
    }
    
    public Optional<ExpedienteMedico> buscarPorId(int id) {
        String sql = "SELECT id_expediente, paciente_id, fecha_creacion, antecedente FROM expediente_medico WHERE id_expediente = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExpedienteMedico e = new ExpedienteMedico();
                    e.setIdExpediente(rs.getInt("id_expediente"));
                    // paciente se puede cargar por separado si se necesita
                    e.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
                    e.setAntecedente(rs.getString("antecedente"));
                    return Optional.of(e);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar expediente: " + ex.getMessage(), ex);
        }
    }
    
    public ExpedienteMedico actualizarAntecedente(int id, String antecedente) {
        String sql = "UPDATE expediente_medico SET antecedente = ? WHERE id_expediente = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, antecedente);
            ps.setInt(2, id);
            ps.executeUpdate();
            return buscarPorId(id).orElseThrow(() -> new RuntimeException("Expediente no encontrado tras actualizar"));
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizar expediente: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM expediente_medico WHERE id_expediente = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminar expediente: " + ex.getMessage(), ex);
        }
    }
}
