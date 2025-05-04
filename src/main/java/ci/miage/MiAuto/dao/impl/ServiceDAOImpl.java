package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IServiceDAO;
import main.java.ci.miage.MiAuto.models.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImpl extends BaseDAOImpl<Service> implements IServiceDAO {

    public ServiceDAOImpl() {
        super();
    }

    @Override
    public Service findById(int id) throws SQLException {
        String query = "SELECT * FROM service WHERE id_service = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    @Override
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM service";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) services.add(mapResultSetToEntity(rs));
        }
        return services;
    }

    @Override
    public Service save(Service s) throws SQLException {
        String query = "INSERT INTO service (lib_service, localisation_service) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, s.getLibelleService());
            stmt.setString(2, s.getLocalisationService());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        s.setIdService(keys.getInt(1));
                        return s;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Service s) throws SQLException {
        String query = "UPDATE service SET lib_service = ?, localisation_service = ? WHERE id_service = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, s.getLibelleService());
            stmt.setString(2, s.getLocalisationService());
            stmt.setInt(3, s.getIdService());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM service WHERE id_service = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }


    @Override
    public boolean existsByLibelle(String libelle) throws SQLException {
        String query = "SELECT COUNT(*) FROM service WHERE LOWER(lib_service) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean updateLibelle(String id, String nouveauLibelle) throws SQLException {
        return false;
    }

    @Override
    public boolean deleteByLibelle(String libelle) throws SQLException {
        String query = "DELETE FROM service WHERE LOWER(lib_service) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> findPersonnelsByService(String idService) throws SQLException {
        return List.of();
    }

    @Override
    public boolean updateLibelle(int id, String nouveauLibelle) throws SQLException {
        String query = "UPDATE service SET lib_service = ? WHERE id_service = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nouveauLibelle);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Service mapResultSetToEntity(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setIdService(rs.getInt("id_service"));
        s.setLibelleService(rs.getString("lib_service"));
        s.setLocalisationService(rs.getString("localisation_service"));
        return s;
    }


    public List<Service> findByLocalisation(String localisation) throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM service WHERE LOWER(localisation_service) LIKE LOWER(?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + localisation + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToEntity(rs));
                }
            }
        }
        return services;
    }

    @Override
    public Service findByLibelle(String libelle) throws SQLException {
        // Ajoutez cette méthode manquante
        String query = "SELECT * FROM service WHERE LIB_SERVICE = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }
}
