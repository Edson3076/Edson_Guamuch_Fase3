package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.Consulta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultaDao {
    private final DataSource ds;

    public ConsultaDao(DataSource ds) {
        this.ds = ds;
    }
    
    public Consulta insertar(Consulta c) {
        String sql = "INSERT INTO consulta(cita_id, expediente_id, sintomas, diagnostico, tratamiento, fecha_consulta) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getCita().getIdCita());
            ps.setInt(2, c.getExpediente().getIdExpediente());
            ps.setString(3, c.getSintomas());
            ps.setString(4, c.getDiagnostico());
            ps.setString(5, c.getTratamiento());
            ps.setTimestamp(6, Timestamp.valueOf(c.getFechaConsulta() != null ? c.getFechaConsulta() : LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setIdConsulta(rs.getInt(1));
            }
            return c;
        } catch (SQLException ex) {
            throw new RuntimeException("Error insertar consulta: " + ex.getMessage(), ex);
        }
    }
    
    public List<Consulta> listarPorExpediente(int idExpediente) {
        String sql = "SELECT id_consulta, cita_id, expediente_id, sintomas, diagnostico, tratamiento, fecha_consulta FROM consulta WHERE expediente_id = ?";
        List<Consulta> lista = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idExpediente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = new Consulta();
                    c.setIdConsulta(rs.getInt("id_consulta"));
                    // solo setear ids de relaciones; cargar entidades completas si es necesario
                    // c.setCita(new Cita(...));
                    c.setSintomas(rs.getString("sintomas"));
                    c.setDiagnostico(rs.getString("diagnostico"));
                    c.setTratamiento(rs.getString("tratamiento"));
                    c.setFechaConsulta(rs.getTimestamp("fecha_consulta").toLocalDateTime());
                    lista.add(c);
                }
                return lista;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error listar consultas: " + ex.getMessage(), ex);
        }
    }
    
    public Optional<Consulta> buscarPorId(int id) {
        String sql = "SELECT id_consulta, cita_id, expediente_id, sintomas, diagnostico, tratamiento, fecha_consulta FROM consulta WHERE id_consulta = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Consulta c = new Consulta();
                    c.setIdConsulta(rs.getInt("id_consulta"));
                    c.setSintomas(rs.getString("sintomas"));
                    c.setDiagnostico(rs.getString("diagnostico"));
                    c.setTratamiento(rs.getString("tratamiento"));
                    c.setFechaConsulta(rs.getTimestamp("fecha_consulta").toLocalDateTime());
                    return Optional.of(c);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar consulta: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminar consulta: " + ex.getMessage(), ex);
        }
    }
}
