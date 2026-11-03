package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.Cita;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class CitaDao {
    private final DataSource ds;

    public CitaDao(DataSource ds) {
        this.ds = ds;
    }
    
    public Cita insertar(Cita c) {
        String sql = "INSERT INTO cita(paciente_id, medico_id, fecha, hora, confirmada, motivo_cancelacion, costo) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getPaciente().getIdPersona());
            ps.setInt(2, c.getMedico().getIdPersona());
            ps.setDate(3, Date.valueOf(c.getFecha()));
            ps.setTime(4, Time.valueOf(c.getHora()));
            ps.setBoolean(5, c.getConfirmada() != null ? c.getConfirmada() : true);
            ps.setString(6, c.getMotivoCancelacion());
            if (c.getCosto() != null) ps.setDouble(7, c.getCosto()); else ps.setNull(7, Types.DOUBLE);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setIdCita(rs.getInt(1));
            }
            return c;
        } catch (SQLException ex) {
            throw new RuntimeException("Error insertar cita: " + ex.getMessage(), ex);
        }
    }
    
    public Optional<Cita> buscarPorId(int id) {
        String sql = "SELECT id_cita, paciente_id, medico_id, fecha, hora, confirmada, motivo_cancelacion, costo FROM cita WHERE id_cita = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    Paciente p = new Paciente(); p.setIdPersona(rs.getInt("paciente_id"));
                    Medico m = new Medico(); m.setIdPersona(rs.getInt("medico_id"));
                    c.setPaciente(p); c.setMedico(m);
                    c.setFecha(rs.getDate("fecha").toLocalDate());
                    c.setHora(rs.getTime("hora").toLocalTime());
                    c.setConfirmada(rs.getBoolean("confirmada"));
                    c.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                    c.setCosto(rs.getDouble("costo"));
                    return Optional.of(c);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar cita: " + ex.getMessage(), ex);
        }
    }
    
    public List<Cita> listarPorMedicoYFecha(int idMedico, LocalDate fecha) {
        String sql = "SELECT id_cita, paciente_id, medico_id, fecha, hora, confirmada, motivo_cancelacion, costo FROM cita WHERE medico_id = ? AND fecha = ?";
        List<Cita> lista = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            ps.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    Paciente p = new Paciente(); p.setIdPersona(rs.getInt("paciente_id"));
                    Medico m = new Medico(); m.setIdPersona(rs.getInt("medico_id"));
                    c.setPaciente(p); c.setMedico(m);
                    c.setFecha(rs.getDate("fecha").toLocalDate());
                    c.setHora(rs.getTime("hora").toLocalTime());
                    c.setConfirmada(rs.getBoolean("confirmada"));
                    c.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                    c.setCosto(rs.getDouble("costo"));
                    lista.add(c);
                }
                return lista;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error listar citas: " + ex.getMessage(), ex);
        }
    }
    
    public Cita actualizar(Cita c) {
        String sql = "UPDATE cita SET paciente_id=?, medico_id=?, fecha=?, hora=?, confirmada=?, motivo_cancelacion=?, costo=? WHERE id_cita=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPaciente().getIdPersona());
            ps.setInt(2, c.getMedico().getIdPersona());
            ps.setDate(3, Date.valueOf(c.getFecha()));
            ps.setTime(4, Time.valueOf(c.getHora()));
            ps.setBoolean(5, c.getConfirmada() != null ? c.getConfirmada() : true);
            ps.setString(6, c.getMotivoCancelacion());
            if (c.getCosto() != null) ps.setDouble(7, c.getCosto()); else ps.setNull(7, Types.DOUBLE);
            ps.setInt(8, c.getIdCita());
            ps.executeUpdate();
            return c;
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizar cita: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cita WHERE id_cita = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminar cita: " + ex.getMessage(), ex);
        }
    }
}
