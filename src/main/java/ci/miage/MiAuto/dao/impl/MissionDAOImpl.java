package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IMissionDAO;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionDAOImpl extends BaseDAOImpl<Mission> implements IMissionDAO {

    private VehiculeDAOImpl vehiculeDAO;
    private PersonnelDAOImpl personnelDAO;

    public MissionDAOImpl() {
        super();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.personnelDAO = new PersonnelDAOImpl();
    }

    @Override
    public Mission save(Mission mission) throws SQLException {
        String query = "INSERT INTO mission (ID_VEHICULE, LIB_MISSION, DATE_DEBUT_MISSION, DATE_FIN_MISSION, " +
                "COUT_MISSION, COUT_CARBURANT, OBSERVATION_MISSION, CIRCUIT_MISSION) " +
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

            stmt.setInt(5, mission.getCoutMission());
            stmt.setInt(6, mission.getCoutCarburant());
            stmt.setString(7, mission.getObservationMission());
            stmt.setString(8, mission.getCircuitMission());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mission.setIdMission(generatedKeys.getInt(1));

                        // Ajouter les participants si présents
                        if (mission.getParticipants() != null && !mission.getParticipants().isEmpty()) {
                            for (Personnel participant : mission.getParticipants()) {
                                addParticipant(mission.getIdMission(), participant.getIdPersonnel());
                            }
                        }

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
                "DATE_FIN_MISSION = ?, COUT_MISSION = ?, COUT_CARBURANT = ?, OBSERVATION_MISSION = ?, " +
                "CIRCUIT_MISSION = ? WHERE ID_MISSION = ?";

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

            stmt.setInt(5, mission.getCoutMission());
            stmt.setInt(6, mission.getCoutCarburant());
            stmt.setString(7, mission.getObservationMission());
            stmt.setString(8, mission.getCircuitMission());
            stmt.setInt(9, mission.getIdMission());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        // Supprimer d'abord les participants
        String deleteParticipantsQuery = "DELETE FROM participer WHERE ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmtParticipants = conn.prepareStatement(deleteParticipantsQuery)) {

            stmtParticipants.setInt(1, id);
            stmtParticipants.executeUpdate();
        }

        // Puis supprimer la mission
        String query = "DELETE FROM mission WHERE ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Mission findById(int id) throws SQLException {
        String query = "SELECT * FROM mission WHERE ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Mission mission = mapResultSetToEntity(rs);

                    // Charger le véhicule
                    Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                    mission.setVehicule(vehicule);

                    // Charger les participants
                    List<Personnel> participants = findParticipants(mission.getIdMission());
                    mission.setParticipants(participants);

                    return mission;
                }
            }
        }

        return null;
    }

    @Override
    public List<Mission> findAll() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Mission mission = mapResultSetToEntity(rs);

                // Charger le véhicule
                Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                mission.setVehicule(vehicule);

                // Charger les participants
                List<Personnel> participants = findParticipants(mission.getIdMission());
                mission.setParticipants(participants);

                missions.add(mission);
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findByVehicule(int idVehicule) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mission mission = mapResultSetToEntity(rs);

                    // Charger les participants
                    List<Personnel> participants = findParticipants(mission.getIdMission());
                    mission.setParticipants(participants);

                    missions.add(mission);
                }
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findEnCours() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE DATE_DEBUT_MISSION <= NOW() AND " +
                "(DATE_FIN_MISSION IS NULL OR DATE_FIN_MISSION >= NOW())";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Mission mission = mapResultSetToEntity(rs);

                // Charger le véhicule
                Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                mission.setVehicule(vehicule);

                // Charger les participants
                List<Personnel> participants = findParticipants(mission.getIdMission());
                mission.setParticipants(participants);

                missions.add(mission);
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findAVenir() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE DATE_DEBUT_MISSION > NOW()";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Mission mission = mapResultSetToEntity(rs);

                // Charger le véhicule
                Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                mission.setVehicule(vehicule);

                // Charger les participants
                List<Personnel> participants = findParticipants(mission.getIdMission());
                mission.setParticipants(participants);

                missions.add(mission);
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findTerminees() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE DATE_FIN_MISSION IS NOT NULL AND DATE_FIN_MISSION < NOW()";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Mission mission = mapResultSetToEntity(rs);

                // Charger le véhicule
                Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                mission.setVehicule(vehicule);

                // Charger les participants
                List<Personnel> participants = findParticipants(mission.getIdMission());
                mission.setParticipants(participants);

                missions.add(mission);
            }
        }

        return missions;
    }

    @Override
    public List<Mission> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE " +
                "(DATE_DEBUT_MISSION BETWEEN ? AND ?) OR " +
                "(DATE_FIN_MISSION BETWEEN ? AND ?) OR " +
                "(DATE_DEBUT_MISSION <= ? AND (DATE_FIN_MISSION >= ? OR DATE_FIN_MISSION IS NULL))";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));
            stmt.setTimestamp(3, Timestamp.valueOf(debut));
            stmt.setTimestamp(4, Timestamp.valueOf(fin));
            stmt.setTimestamp(5, Timestamp.valueOf(debut));
            stmt.setTimestamp(6, Timestamp.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mission mission = mapResultSetToEntity(rs);

                    // Charger le véhicule
                    Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
                    mission.setVehicule(vehicule);

                    // Charger les participants
                    List<Personnel> participants = findParticipants(mission.getIdMission());
                    mission.setParticipants(participants);

                    missions.add(mission);
                }
            }
        }

        return missions;
    }

    @Override
    public boolean addParticipant(int idMission, int idPersonnel) throws SQLException {
        String query = "INSERT INTO participer (ID_MISSION, ID_PERSONNEL) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idMission);
            stmt.setInt(2, idPersonnel);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Si le participant existe déjà, ignorer l'erreur
            if (e.getErrorCode() == 1062) { // Code d'erreur MySQL pour clé dupliquée
                return true;
            }
            throw e;
        }
    }

    @Override
    public boolean removeParticipant(int idMission, int idPersonnel) throws SQLException {
        String query = "DELETE FROM participer WHERE ID_MISSION = ? AND ID_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idMission);
            stmt.setInt(2, idPersonnel);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Personnel> findParticipants(int idMission) throws SQLException {
        List<Personnel> participants = new ArrayList<>();
        String query = "SELECT p.* FROM personnel p " +
                "JOIN participer pa ON p.ID_PERSONNEL = pa.ID_PERSONNEL " +
                "WHERE pa.ID_MISSION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idMission);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Personnel personnel = personnelDAO.mapResultSetToEntity(rs);
                    participants.add(personnel);
                }
            }
        }

        return participants;
    }

    @Override
    protected Mission mapResultSetToEntity(ResultSet rs) throws SQLException {
        Mission mission = new Mission();

        mission.setIdMission(rs.getInt("ID_MISSION"));
        mission.setIdVehicule(rs.getInt("ID_VEHICULE"));
        mission.setLibMission(rs.getString("LIB_MISSION"));

        Timestamp tsDateDebut = rs.getTimestamp("DATE_DEBUT_MISSION");
        if (tsDateDebut != null) {
            mission.setDateDebutMission(tsDateDebut.toLocalDateTime());
        }

        Timestamp tsDateFin = rs.getTimestamp("DATE_FIN_MISSION");
        if (tsDateFin != null) {
            mission.setDateFinMission(tsDateFin.toLocalDateTime());
        }

        mission.setCoutMission(rs.getInt("COUT_MISSION"));
        mission.setCoutCarburant(rs.getInt("COUT_CARBURANT"));
        mission.setObservationMission(rs.getString("OBSERVATION_MISSION"));
        mission.setCircuitMission(rs.getString("CIRCUIT_MISSION"));

        return mission;
    }
}