package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IEntretienDAO;
import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntretienDAOImpl extends BaseDAOImpl<Entretien> implements IEntretienDAO {

    public EntretienDAOImpl() {
        super();
    }

    @Override
    public Entretien findById(int id) throws SQLException {
        String query = "SELECT * FROM entretien WHERE id_entretien = ?";
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
    public List<Entretien> findAll() throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) entretiens.add(mapResultSetToEntity(rs));
        }
        return entretiens;
    }

    @Override
    public Entretien save(Entretien e) throws SQLException {
        String query = "INSERT INTO entretien (id_vehicule, motif, date_entree, date_sortie, cout, lieu, observations) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, e.getIdVehicule());
            stmt.setString(2, e.getMotifEntr());
            stmt.setTimestamp(3, Timestamp.valueOf(e.getDateEntreeEntr()));
            stmt.setTimestamp(4, Timestamp.valueOf(e.getDateSortieEntr()));
            stmt.setInt(5, e.getCoutEntr());
            stmt.setString(6, e.getLieuEntr());
            stmt.setString(7, e.getObservation());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        e.setIdEntretien(keys.getInt(1));
                        return e;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Entretien e) throws SQLException {
        String query = "UPDATE entretien SET id_vehicule = ?, motif = ?, date_entree = ?, date_sortie = ?, cout = ?, lieu = ?, observations = ? " +
                "WHERE id_entretien = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, e.getIdVehicule());
            stmt.setString(2, e.getMotifEntr());
            stmt.setTimestamp(3, Timestamp.valueOf(e.getDateEntreeEntr()));
            stmt.setTimestamp(4, Timestamp.valueOf(e.getDateSortieEntr()));
            stmt.setInt(5, e.getCoutEntr());
            stmt.setString(6, e.getLieuEntr());
            stmt.setString(7, e.getObservation());
            stmt.setInt(8, e.getIdEntretien());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM entretien WHERE id_entretien = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Entretien mapResultSetToEntity(ResultSet rs) throws SQLException {
        Entretien e = new Entretien();
        e.setIdEntretien(rs.getInt("id_entretien"));
        e.setIdVehicule(rs.getInt("id_vehicule"));
        e.setMotifEntr(rs.getString("motif"));
        e.setDateEntreeEntr(rs.getTimestamp("date_entree").toLocalDateTime());
        e.setDateSortieEntr(rs.getTimestamp("date_sortie").toLocalDateTime());
        e.setCoutEntr(rs.getInt("cout"));
        e.setLieuEntr(rs.getString("lieu"));
        e.setObservation(rs.getString("observations"));
        return e;
    }

    @Override
    public List<Entretien> findByVehicule(String idVehicule) throws SQLException {
        return List.of();
    }

    @Override
    public List<Entretien> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException {
        return List.of();
    }

    @Override
    public List<Entretien> findByLieu(String lieu) throws SQLException {
        return List.of();
    }

    @Override
    public boolean updateCout(String idEntretien, double nouveauCout) throws SQLException {
        return false;
    }

    @Override
    public int deleteByVehicule(String idVehicule) throws SQLException {
        return 0;
    }
}
