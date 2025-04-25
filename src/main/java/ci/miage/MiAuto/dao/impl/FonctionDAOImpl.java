package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IFonctionDAO;
import main.java.ci.miage.MiAuto.models.Fonction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FonctionDAOImpl extends BaseDAOImpl<Fonction> implements IFonctionDAO {

    public FonctionDAOImpl() {
        super();
    }

    @Override
    public Fonction findById(int id) throws SQLException {
        String query = "SELECT * FROM fonction WHERE id_fonction = ?";
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
    public List<Fonction> findAll() throws SQLException {
        List<Fonction> fonctions = new ArrayList<>();
        String query = "SELECT * FROM fonction";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) fonctions.add(mapResultSetToEntity(rs));
        }
        return fonctions;
    }

    @Override
    public Fonction save(Fonction f) throws SQLException {
        String query = "INSERT INTO fonction (nom_fonction) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, f.getLibelleFonction());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        f.setIdFonction(keys.getInt(1));
                        return f;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Fonction f) throws SQLException {
        String query = "UPDATE fonction SET nom_fonction = ? WHERE id_fonction = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, f.getLibelleFonction());
            stmt.setInt(2, f.getIdFonction());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM fonction WHERE id_fonction = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Fonction findByLibelle(String libelle) throws SQLException {
        String query = "SELECT * FROM fonction WHERE LOWER(nom_fonction) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    @Override
    public boolean existsByLibelle(String libelle) throws SQLException {
        String query = "SELECT COUNT(*) FROM fonction WHERE LOWER(nom_fonction) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean deleteByLibelle(String libelle) throws SQLException {
        String query = "DELETE FROM fonction WHERE LOWER(nom_fonction) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateLibelle(String id, String nouveauLibelle) throws SQLException {
        return false;
    }

    @Override
    public boolean updateLibelle(int id, String nouveauLibelle) throws SQLException {
        String query = "UPDATE fonction SET nom_fonction = ? WHERE id_fonction = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nouveauLibelle);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Fonction mapResultSetToEntity(ResultSet rs) throws SQLException {
        Fonction f = new Fonction();
        f.setIdFonction(rs.getInt("id_fonction"));
        f.setLibelleFonction(rs.getString("nom_fonction"));
        return f;
    }
}
