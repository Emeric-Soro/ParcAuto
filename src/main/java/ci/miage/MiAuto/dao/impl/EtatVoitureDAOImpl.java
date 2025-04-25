package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IEtatVoitureDAO;
import main.java.ci.miage.MiAuto.models.EtatVoiture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtatVoitureDAOImpl extends BaseDAOImpl<EtatVoiture> implements IEtatVoitureDAO {

    public EtatVoitureDAOImpl() {
        super();
    }

    @Override
    public EtatVoiture findById(int id) throws SQLException {
        String query = "SELECT * FROM etat_voiture WHERE id_etat_voiture = ?";
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
    public List<EtatVoiture> findAll() throws SQLException {
        List<EtatVoiture> etats = new ArrayList<>();
        String query = "SELECT * FROM etat_voiture";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) etats.add(mapResultSetToEntity(rs));
        }
        return etats;
    }

    @Override
    public EtatVoiture save(EtatVoiture etat) throws SQLException {
        String query = "INSERT INTO etat_voiture (lib_etat_voiture) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, etat.getLibEtatVoiture());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        etat.setIdEtatVoiture(generatedKeys.getInt(1));
                        return etat;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(EtatVoiture etat) throws SQLException {
        String query = "UPDATE etat_voiture SET lib_etat_voiture = ? WHERE id_etat_voiture = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, etat.getLibEtatVoiture());
            stmt.setInt(2, etat.getIdEtatVoiture());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM etat_voiture WHERE id_etat_voiture = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public EtatVoiture findByLibelle(String libelle) throws SQLException {
        String query = "SELECT * FROM etat_voiture WHERE LOWER(lib_etat_voiture) = LOWER(?)";
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
        String query = "SELECT COUNT(*) FROM etat_voiture WHERE LOWER(lib_etat_voiture) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean deleteByLibelle(String libelle) throws SQLException {
        String query = "DELETE FROM etat_voiture WHERE LOWER(lib_etat_voiture) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateLibelle(int id, String nouveauLibelle) throws SQLException {
        String query = "UPDATE etat_voiture SET lib_etat_voiture = ? WHERE id_etat_voiture = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nouveauLibelle);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected EtatVoiture mapResultSetToEntity(ResultSet rs) throws SQLException {
        EtatVoiture e = new EtatVoiture();
        e.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
        e.setLibEtatVoiture(rs.getString("lib_etat_voiture"));
        return e;
    }
}
