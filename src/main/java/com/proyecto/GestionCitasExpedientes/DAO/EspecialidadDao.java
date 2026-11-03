package com.proyecto.GestionCitasExpedientes.DAO;

import com.proyecto.GestionCitasExpedientes.modelo.Especialidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class EspecialidadDao {
    private final DataSource ds;

    public EspecialidadDao(DataSource ds) {
        this.ds = ds;
    }
    
    public Especialidad insertar(Especialidad e) {
        String sql = "INSERT INTO especialidad(nombre, descripcion, costo_consulta) VALUES (?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setDouble(3, e.getCostoConsulta());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setIdEspecialidad(rs.getInt(1));
            }
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Error insertar especialidad: " + ex.getMessage(), ex);
        }
    }
    
     public List<Especialidad> listar() {
        String sql = "SELECT id_especialidad, nombre, descripcion, costo_consulta FROM especialidad";
        List<Especialidad> lista = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Especialidad e = new Especialidad();
                e.setIdEspecialidad(rs.getInt("id_especialidad"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                e.setCostoConsulta(rs.getDouble("costo_consulta"));
                lista.add(e);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Error listar especialidades: " + ex.getMessage(), ex);
        }
    }
     
    public Optional<Especialidad> buscarPorId(int id) {
        String sql = "SELECT id_especialidad, nombre, descripcion, costo_consulta FROM especialidad WHERE id_especialidad = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Especialidad e = new Especialidad();
                    e.setIdEspecialidad(rs.getInt("id_especialidad"));
                    e.setNombre(rs.getString("nombre"));
                    e.setDescripcion(rs.getString("descripcion"));
                    e.setCostoConsulta(rs.getDouble("costo_consulta"));
                    return Optional.of(e);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscar especialidad: " + ex.getMessage(), ex);
        }
    }
    
    public Especialidad actualizar(Especialidad e) {
        String sql = "UPDATE especialidad SET nombre=?, descripcion=?, costo_consulta=? WHERE id_especialidad=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setDouble(3, e.getCostoConsulta());
            ps.setInt(4, e.getIdEspecialidad());
            ps.executeUpdate();
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizar especialidad: " + ex.getMessage(), ex);
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM especialidad WHERE id_especialidad = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminar especialidad: " + ex.getMessage(), ex);
        }
    }
}
