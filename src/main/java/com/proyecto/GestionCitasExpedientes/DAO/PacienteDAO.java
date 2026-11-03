package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class PacienteDAO {
    private final DataSource ds;

    public PacienteDAO(DataSource ds) {
        this.ds = ds;
    }
    
    public Paciente insertar(Paciente p) {
        String sqlPersona = "INSERT INTO persona(nombre,direccion,telefono,email) VALUES (?,?,?,?)";
        String sqlPaciente = "INSERT INTO paciente(id_paciente,tipo_sangre,genero,edad,historial) VALUES (?,?,?,?,?)";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDireccion());
                ps.setString(3, p.getTelefono());
                ps.setString(4, p.getEmail());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) p.setIdPersona(rs.getInt(1));
                    else throw new SQLException("No se obtuvo id persona");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPaciente)) {
                ps2.setInt(1, p.getIdPersona());
                ps2.setString(2, p.getTipoSangre());
                ps2.setString(3, p.getGenero());
                if (p.getEdad() != null) ps2.setInt(4, p.getEdad()); else ps2.setNull(4, Types.INTEGER);
                ps2.setString(5, p.getHistorial());
                ps2.executeUpdate();
            }
            conn.commit();
            return p;
        } catch (SQLException ex) {
            throw new RuntimeException("Error crear paciente: " + ex.getMessage(), ex);
        }
    }
    public List<Paciente> listar() {
        String sql = "SELECT pr.id_persona, pr.nombre, pr.direccion, pr.telefono, pr.email, " +
                     "pc.tipo_sangre, pc.genero, pc.edad, pc.historial " +
                     "FROM persona pr JOIN paciente pc ON pr.id_persona = pc.id_paciente";
        List<Paciente> lista = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Error listar pacientes: " + ex.getMessage(), ex);
        }
    }
    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setIdPersona(rs.getInt("id_persona"));
        p.setNombre(rs.getString("nombre"));
        p.setDireccion(rs.getString("direccion"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setTipoSangre(rs.getString("tipo_sangre"));
        p.setGenero(rs.getString("genero"));
        int edad = rs.getInt("edad");
        if (!rs.wasNull()) p.setEdad(edad);
        p.setHistorial(rs.getString("historial"));
        return p;
    }
    
    public Optional<Paciente> buscarPorId(int id) {
        String sql = "SELECT pr.id_persona, pr.nombre, pr.direccion, pr.telefono, pr.email, " +
                     "pc.tipo_sangre, pc.genero, pc.edad, pc.historial " +
                     "FROM persona pr JOIN paciente pc ON pr.id_persona = pc.id_paciente WHERE pr.id_persona = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar paciente: " + ex.getMessage(), ex);
        }
    }
    
    public Paciente actualizar(Paciente p) {
        String sqlPersona = "UPDATE persona SET nombre=?, direccion=?, telefono=?, email=? WHERE id_persona=?";
        String sqlPaciente = "UPDATE paciente SET tipo_sangre=?, genero=?, edad=?, historial=? WHERE id_paciente=?";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPersona)) {
                ps1.setString(1, p.getNombre());
                ps1.setString(2, p.getDireccion());
                ps1.setString(3, p.getTelefono());
                ps1.setString(4, p.getEmail());
                ps1.setInt(5, p.getIdPersona());
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPaciente)) {
                ps2.setString(1, p.getTipoSangre());
                ps2.setString(2, p.getGenero());
                if (p.getEdad() != null) ps2.setInt(3, p.getEdad()); else ps2.setNull(3, Types.INTEGER);
                ps2.setString(4, p.getHistorial());
                ps2.setInt(5, p.getIdPersona());
                ps2.executeUpdate();
            }
            conn.commit();
            return p;
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizar paciente: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sqlPaciente = "DELETE FROM paciente WHERE id_paciente = ?";
        String sqlPersona = "DELETE FROM persona WHERE id_persona = ?";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPaciente)) {
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
            throw new RuntimeException("Error eliminar paciente: " + ex.getMessage(), ex);
        }
    }
}
