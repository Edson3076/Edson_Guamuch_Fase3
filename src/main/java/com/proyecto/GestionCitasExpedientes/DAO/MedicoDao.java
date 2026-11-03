package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import com.proyecto.GestionCitasExpedientes.modelo.Medico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class MedicoDao {
    private final DataSource ds;

    public MedicoDao(DataSource ds) {
        this.ds = ds;
    }
    
    public Medico insertar(Medico m) {
        String sqlPersona = "INSERT INTO persona(nombre,direccion,telefono,email) VALUES (?,?,?,?)";
        String sqlMedico = "INSERT INTO medico(id_medico, colegiado, horario_atencion, especialidad_id) VALUES (?,?,?,?)";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNombre());
                ps.setString(2, m.getDireccion());
                ps.setString(3, m.getTelefono());
                ps.setString(4, m.getEmail());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) m.setIdPersona(rs.getInt(1));
                    else throw new SQLException("No id persona");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlMedico)) {
                ps2.setInt(1, m.getIdPersona());
                ps2.setString(2, m.getColegiado());
                ps2.setString(3, m.getHorarioAtencion());
                if (m.getEspecialidad() != null && m.getEspecialidad().getIdEspecialidad() != null)
                    ps2.setInt(4, m.getEspecialidad().getIdEspecialidad());
                else ps2.setNull(4, Types.INTEGER);
                ps2.executeUpdate();
            }
            conn.commit();
            return m;
        } catch (SQLException ex) {
            throw new RuntimeException("Error insertar medico: " + ex.getMessage(), ex);
        }
    }
    
    public List<Medico> listar() {
        String sql = "SELECT pr.id_persona, pr.nombre, pr.direccion, pr.telefono, pr.email, " +
                     "md.colegiado, md.horario_atencion, md.especialidad_id, esp.nombre as esp_nombre, esp.costo_consulta " +
                     "FROM persona pr JOIN medico md ON pr.id_persona = md.id_medico " +
                     "LEFT JOIN especialidad esp ON md.especialidad_id = esp.id_especialidad";
        List<Medico> lista = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medico m = new Medico();
                m.setIdPersona(rs.getInt("id_persona"));
                m.setNombre(rs.getString("nombre"));
                m.setDireccion(rs.getString("direccion"));
                m.setTelefono(rs.getString("telefono"));
                m.setEmail(rs.getString("email"));
                m.setColegiado(rs.getString("colegiado"));
                m.setHorarioAtencion(rs.getString("horario_atencion"));
                int idEsp = rs.getInt("especialidad_id");
                if (!rs.wasNull()) {
                    Especialidad e = new Especialidad();
                    e.setIdEspecialidad(idEsp);
                    e.setNombre(rs.getString("esp_nombre"));
                    e.setCostoConsulta(rs.getDouble("costo_consulta"));
                    m.setEspecialidad(e);
                }
                lista.add(m);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Error listar medicos: " + ex.getMessage(), ex);
        }
    }
    
    public Optional<Medico> buscarPorId(int id) {
        String sql = "SELECT pr.id_persona, pr.nombre, pr.direccion, pr.telefono, pr.email, " +
                     "md.colegiado, md.horario_atencion, md.especialidad_id " +
                     "FROM persona pr JOIN medico md ON pr.id_persona = md.id_medico WHERE pr.id_persona = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medico m = new Medico();
                    m.setIdPersona(rs.getInt("id_persona"));
                    m.setNombre(rs.getString("nombre"));
                    m.setDireccion(rs.getString("direccion"));
                    m.setTelefono(rs.getString("telefono"));
                    m.setEmail(rs.getString("email"));
                    m.setColegiado(rs.getString("colegiado"));
                    m.setHorarioAtencion(rs.getString("horario_atencion"));
                    int idEsp = rs.getInt("especialidad_id");
                    if (!rs.wasNull()) {
                        Especialidad e = new Especialidad();
                        e.setIdEspecialidad(idEsp);
                        m.setEspecialidad(e);
                    }
                    return Optional.of(m);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar medico: " + ex.getMessage(), ex);
        }
    }
    
    public Medico actualizar(Medico m) {
        String sqlPersona = "UPDATE persona SET nombre=?, direccion=?, telefono=?, email=? WHERE id_persona=?";
        String sqlMedico = "UPDATE medico SET colegiado=?, horario_atencion=?, especialidad_id=? WHERE id_medico=?";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPersona)) {
                ps1.setString(1, m.getNombre());
                ps1.setString(2, m.getDireccion());
                ps1.setString(3, m.getTelefono());
                ps1.setString(4, m.getEmail());
                ps1.setInt(5, m.getIdPersona());
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlMedico)) {
                ps2.setString(1, m.getColegiado());
                ps2.setString(2, m.getHorarioAtencion());
                if (m.getEspecialidad() != null && m.getEspecialidad().getIdEspecialidad() != null)
                    ps2.setInt(3, m.getEspecialidad().getIdEspecialidad());
                else ps2.setNull(3, Types.INTEGER);
                ps2.setInt(4, m.getIdPersona());
                ps2.executeUpdate();
            }
            conn.commit();
            return m;
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizar medico: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sqlMedico = "DELETE FROM medico WHERE id_medico = ?";
        String sqlPersona = "DELETE FROM persona WHERE id_persona = ?";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlMedico)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }
            int affected;
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPersona)) {
                ps2.setInt(1, id);
                affected = ps2.executeUpdate();
            }
            conn.commit();
            return affected > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminar medico: " + ex.getMessage(), ex);
        }
    }
}
