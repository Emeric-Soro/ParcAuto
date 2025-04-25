package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IMissionDAO;
import main.java.ci.miage.MiAuto.models.Mission;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DAO pour les missions
 */
public class MissionDAOImpl extends BaseDAOImpl<Mission> implements IMissionDAO {

    /**
     * Constructeur par défaut
     */
    public MissionDAOImpl() {
        super();
    }

    @Override
    public Mission findById(int id) throws SQLException {
        String query = "SELECT * FROM mission WHERE ID_MISSION = ?";

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
    public List<Mission> findAll() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission ORDER BY DATE_DEBUT_MISSION DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                missions.add(mapResultSetToEntity(rs));
            }
        }

        return missions;
    }

    @Override
    public Mission save(Mission mission) throws SQLException {
        String query = "INSERT INTO mission (ID_VEHICULE, LIB_MISSION, DATE_DEBUT_MISSION, DATE_FIN_MISSION, COUT_MISSION, COUT_CARBURANT, OBSERVATION_MISSION, CIRCUIT_MISSION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, mission.getIdVehicule());
            stmt.setString(2, mission.getLibMission());

            if (mission.getDateDebutMission() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(mission.getDateDebutMission()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            if (mission.getDateFinMission() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(mission.getDateFinMission()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setDouble(5, mission.getCoutMission());
            stmt.setDouble(6, mission.getCoutCarburant());
            stmt.setString(7, mission.getObservationMission());
            stmt.setString(8, mission.getCircuitMission());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mission.setIdMission(generatedKeys.getInt(1));
                        return mission;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(Mission mission) throws SQLException {
        String query = "UPDATE mission SET ID_VEHICULE = ?, LIB_MISSION = ?, DATE_DEBUT_MISSION = ?, " +
                "DATE_FIN_MISSION = ?, COUT_MISSION = ?, COUT_CARBURANT = ?, " +
                "OBSERVATION_MISSION = ?, CIRCUIT_MISSION = ? WHERE ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, mission.getIdVehicule());
            stmt.setString(2, mission.getLibMission());

            if (mission.getDateDebutMission() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(mission.getDateDebutMission()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            if (mission.getDateFinMission() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(mission.getDateFinMission()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setDouble(5, mission.getCoutMission());
            stmt.setDouble(6, mission.getCoutCarburant());
            stmt.setString(7, mission.getObservationMission());
            stmt.setString(8, mission.getCircuitMission());
            stmt.setInt(9, mission.getIdMission());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM mission WHERE ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Mission> findByVehicule(String idVehicule) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        int vehiculeId = Integer.parseInt(idVehicule);

        String query = "SELECT * FROM mission WHERE ID_VEHICULE = ? ORDER BY DATE_DEBUT_MISSION DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehiculeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToEntity(rs));
                }
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findByPersonnel(String idPersonnel) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        int personnelId = Integer.parseInt(idPersonnel);

        String query = "SELECT m.* FROM mission m " +
                "JOIN participer p ON m.ID_MISSION = p.ID_MISSION " +
                "WHERE p.ID_PERSONNEL = ? ORDER BY m.DATE_DEBUT_MISSION DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, personnelId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToEntity(rs));
                }
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException {
        List<Mission> missions = new ArrayList<>();

        String query = "SELECT * FROM mission " +
                "WHERE (DATE(DATE_DEBUT_MISSION) BETWEEN ? AND ?) OR " +
                "(DATE(DATE_FIN_MISSION) BETWEEN ? AND ?) OR " +
                "(DATE_DEBUT_MISSION <= ? AND DATE_FIN_MISSION >= ?) " +
                "ORDER BY DATE_DEBUT_MISSION";

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
                    missions.add(mapResultSetToEntity(rs));
                }
            }
        }

        return missions;
    }

    @Override
    public boolean updateEtat(String idMission, String nouvelEtat) throws SQLException {
        // Note: Dans votre modèle actuel, il n'y a pas de colonne d'état pour les missions
        // Si vous souhaitez ajouter cette fonctionnalité, vous devriez d'abord modifier votre schéma de base de données
        // Pour l'instant, cette méthode retourne false
        return false;
    }

    @Override
    public int deleteByVehicule(String idVehicule) throws SQLException {
        int vehiculeId = Integer.parseInt(idVehicule);
        String query = "DELETE FROM mission WHERE ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehiculeId);

            return stmt.executeUpdate();
        }
    }

    /**
     * Recherche les missions en cours à une date donnée
     *
     * @param date Date de référence
     * @return Liste des missions en cours à cette date
     * @throws SQLException En cas d'erreur SQL
     */
    @Override
    public List<Mission> findMissionsEnCours(LocalDateTime date) throws SQLException {
        List<Mission> missions = new ArrayList<>();

        String query = "SELECT * FROM mission " +
                "WHERE DATE_DEBUT_MISSION <= ? AND (DATE_FIN_MISSION >= ? OR DATE_FIN_MISSION IS NULL)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            Timestamp timestamp = Timestamp.valueOf(date);

            stmt.setTimestamp(1, timestamp);
            stmt.setTimestamp(2, timestamp);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToEntity(rs));
                }
            }
        }

        return missions;
    }

    @Override
    protected Mission mapResultSetToEntity(ResultSet rs) throws SQLException {
        Mission mission = new Mission();

        mission.setIdMission(rs.getInt("ID_MISSION"));
        mission.setIdVehicule(rs.getInt("ID_VEHICULE"));
        mission.setLibMission(rs.getString("LIB_MISSION"));

        Timestamp dateDebut = rs.getTimestamp("DATE_DEBUT_MISSION");
        if (dateDebut != null) {
            mission.setDateDebutMission(dateDebut.toLocalDateTime());
        }

        Timestamp dateFin = rs.getTimestamp("DATE_FIN_MISSION");
        if (dateFin != null) {
            mission.setDateFinMission(dateFin.toLocalDateTime());
        }

        mission.setCoutMission(rs.getInt("COUT_MISSION"));
        mission.setCoutCarburant(rs.getInt("COUT_CARBURANT"));
        mission.setObservationMission(rs.getString("OBSERVATION_MISSION"));
        mission.setCircuitMission(rs.getString("CIRCUIT_MISSION"));

        return mission;
    }
}