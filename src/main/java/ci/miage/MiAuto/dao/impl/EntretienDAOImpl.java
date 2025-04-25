package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IEntretienDAO;
import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DAO pour les entretiens
 */
public class EntretienDAOImpl extends BaseDAOImpl<Entretien> implements IEntretienDAO {

    /**
     * Constructeur par défaut
     */
    public EntretienDAOImpl() {
        super();
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
    public Entretien save(Entretien entretien) throws SQLException {
        String query = "INSERT INTO entretien (ID_VEHICULE, DATE_ENTREE_ENTR, DATE_SORTIE_ENTR, MOTIF_ENTR, OBSERVATION, COUT_ENTR, LIEU_ENTR) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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
            stmt.setDouble(6, entretien.getCoutEntr());
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
            stmt.setDouble(6, entretien.getCoutEntr());
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
    public List<Entretien> findByVehicule(String idVehicule) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        int vehiculeId = Integer.parseInt(idVehicule);

        String query = "SELECT * FROM entretien WHERE ID_VEHICULE = ? ORDER BY DATE_ENTREE_ENTR DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehiculeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntity(rs));
                }
            }
        }

        return entretiens;
    }

    @Override
    public List<Entretien> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();

        String query = "SELECT * FROM entretien " +
                "WHERE (DATE(DATE_ENTREE_ENTR) BETWEEN ? AND ?) OR " +
                "(DATE(DATE_SORTIE_ENTR) BETWEEN ? AND ?) OR " +
                "(DATE_ENTREE_ENTR <= ? AND (DATE_SORTIE_ENTR >= ? OR DATE_SORTIE_ENTR IS NULL)) " +
                "ORDER BY DATE_ENTREE_ENTR";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            java.sql.Date sqlDebut = java.sql.Date.valueOf(debut);
            java.sql.Date sqlFin = java.sql.Date.valueOf(fin);

            stmt.setDate(1, sqlDebut);
            stmt.setDate(2, sqlFin);
            stmt.setDate(3, sqlDebut);
            stmt.setDate(4, sqlFin);
            stmt.setDate(5, sqlDebut);
            stmt.setDate(6, sqlFin);

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
    public boolean updateCout(String idEntretien, double nouveauCout) throws SQLException {
        int entretienId = Integer.parseInt(idEntretien);
        String query = "UPDATE entretien SET COUT_ENTR = ? WHERE ID_ENTRETIEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, nouveauCout);
            stmt.setInt(2, entretienId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public int deleteByVehicule(String idVehicule) throws SQLException {
        int vehiculeId = Integer.parseInt(idVehicule);
        String query = "DELETE FROM entretien WHERE ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehiculeId);

            return stmt.executeUpdate();
        }
    }

    /**
     * Trouve tous les entretiens en cours (date d'entrée existe mais pas de date de sortie)
     * @return Liste des entretiens en cours
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Entretien> findEntretiensEnCours() throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();

        String query = "SELECT * FROM entretien WHERE DATE_ENTREE_ENTR IS NOT NULL AND DATE_SORTIE_ENTR IS NULL ORDER BY DATE_ENTREE_ENTR";

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

        Timestamp dateEntree = rs.getTimestamp("DATE_ENTREE_ENTR");
        if (dateEntree != null) {
            entretien.setDateEntreeEntr(dateEntree.toLocalDateTime());
        }

        Timestamp dateSortie = rs.getTimestamp("DATE_SORTIE_ENTR");
        if (dateSortie != null) {
            entretien.setDateSortieEntr(dateSortie.toLocalDateTime());
        }

        entretien.setMotifEntr(rs.getString("MOTIF_ENTR"));
        entretien.setObservation(rs.getString("OBSERVATION"));
        entretien.setCoutEntr(rs.getInt("COUT_ENTR"));
        entretien.setLieuEntr(rs.getString("LIEU_ENTR"));

        return entretien;
    }
}