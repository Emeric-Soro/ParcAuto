package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IEntretienDAO;
import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les opérations liées aux entretiens
 */
public class EntretienDAOImpl extends BaseDAOImpl<Entretien> implements IEntretienDAO {

    /**
     * Constructeur par défaut
     */
    public EntretienDAOImpl() {
        super();
    }

    @Override
    public Entretien save(Entretien entretien) throws SQLException {
        String query = "INSERT INTO entretien (ID_VEHICULE, DATE_ENTREE_ENTR, DATE_SORTIE_ENTR, MOTIF_ENTR, OBSERVATION, COUT_ENTR, LIEU_ENTR) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entretien.getIdVehicule());

            if (entretien.getDateEntreeEntr() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(entretien.getDateEntreeEntr()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            }

            if (entretien.getDateSortieEntr() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(entretien.getDateSortieEntr()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setString(4, entretien.getMotifEntr());
            stmt.setString(5, entretien.getObservation());
            stmt.setInt(6, entretien.getCoutEntr());
            stmt.setString(7, entretien.getLieuEntr());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entretien.setIdEntretien(generatedKeys.getInt(1));
                        return entretien;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(Entretien entretien) throws SQLException {
        String query = "UPDATE entretien SET ID_VEHICULE = ?, DATE_ENTREE_ENTR = ?, DATE_SORTIE_ENTR = ?, " +
                "MOTIF_ENTR = ?, OBSERVATION = ?, COUT_ENTR = ?, LIEU_ENTR = ? WHERE ID_ENTRETIEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, entretien.getIdVehicule());

            if (entretien.getDateEntreeEntr() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(entretien.getDateEntreeEntr()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            if (entretien.getDateSortieEntr() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(entretien.getDateSortieEntr()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setString(4, entretien.getMotifEntr());
            stmt.setString(5, entretien.getObservation());
            stmt.setInt(6, entretien.getCoutEntr());
            stmt.setString(7, entretien.getLieuEntr());
            stmt.setInt(8, entretien.getIdEntretien());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM entretien WHERE ID_ENTRETIEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Entretien findById(int id) throws SQLException {
        String query = "SELECT * FROM entretien WHERE ID_ENTRETIEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Entretien> findAll() throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                entretiens.add(mapResultSetToEntity(rs));
            }
        }

        return entretiens;
    }

    @Override
    protected Entretien mapResultSetToEntity(ResultSet rs) throws SQLException {
        Entretien entretien = new Entretien();

        entretien.setIdEntretien(rs.getInt("ID_ENTRETIEN"));
        entretien.setIdVehicule(rs.getInt("ID_VEHICULE"));

        Timestamp tsDateEntree = rs.getTimestamp("DATE_ENTREE_ENTR");
        if (tsDateEntree != null) {
            entretien.setDateEntreeEntr(tsDateEntree.toLocalDateTime());
        }

        Timestamp tsDateSortie = rs.getTimestamp("DATE_SORTIE_ENTR");
        if (tsDateSortie != null) {
            entretien.setDateSortieEntr(tsDateSortie.toLocalDateTime());
        }

        entretien.setMotifEntr(rs.getString("MOTIF_ENTR"));
        entretien.setObservation(rs.getString("OBSERVATION"));
        entretien.setCoutEntr(rs.getInt("COUT_ENTR"));
        entretien.setLieuEntr(rs.getString("LIEU_ENTR"));

        return entretien;
    }

    @Override
    public List<Entretien> findByVehicule(int idVehicule) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien WHERE ID_VEHICULE = ? ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntity(rs));
                }
            }
        }

        return entretiens;
    }

    @Override
    public List<Entretien> findEnCours() throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien WHERE DATE_SORTIE_ENTR IS NULL ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                entretiens.add(mapResultSetToEntity(rs));
            }
        }

        return entretiens;
    }

    @Override
    public List<Entretien> findTermines() throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien WHERE DATE_SORTIE_ENTR IS NOT NULL ORDER BY DATE_SORTIE_ENTR DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                entretiens.add(mapResultSetToEntity(rs));
            }
        }

        return entretiens;
    }

    @Override
    public List<Entretien> findByMotif(String motif) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien WHERE MOTIF_ENTR LIKE ? ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + motif + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntity(rs));
                }
            }
        }

        return entretiens;
    }

    @Override
    public List<Entretien> findByLieu(String lieu) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT * FROM entretien WHERE LIEU_ENTR LIKE ? ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + lieu + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntity(rs));
                }
            }
        }

        return entretiens;
    }

    @Override
    public boolean updateDateSortie(int idEntretien, LocalDateTime dateSortie) throws SQLException {
        String query = "UPDATE entretien SET DATE_SORTIE_ENTR = ? WHERE ID_ENTRETIEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (dateSortie != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(dateSortie));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            stmt.setInt(2, idEntretien);

            return stmt.executeUpdate() > 0;
        }
    }
}