package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IMissionDAO;
import main.java.ci.miage.MiAuto.models.Mission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionDAOImpl extends BaseDAOImpl<Mission> implements IMissionDAO {

    public MissionDAOImpl() {
        super();
    }

    @Override
    public Mission findById(int id) throws SQLException {
        String query = "SELECT * FROM mission WHERE id_mission = ?";
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
    public List<Mission> findAll() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) missions.add(mapResultSetToEntity(rs));
        }
        return missions;
    }

    @Override
    public Mission save(Mission m) throws SQLException {
        String query = "INSERT INTO mission (titre, description, date_debut, date_fin, lieu, id_personnel, id_vehicule, etat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getTitre());
            stmt.setString(2, m.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(m.getDateDebutMission()));
            stmt.setTimestamp(4, Timestamp.valueOf(m.getDateFinMission()));
            stmt.setString(5, m.getLieu());
            stmt.setInt(6, m.getIdPersonnel());
            stmt.setInt(7, m.getIdVehicule());
            stmt.setString(8, m.getEtat());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        m.setIdMission(keys.getInt(1));
                        return m;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Mission m) throws SQLException {
        String query = "UPDATE mission SET titre = ?, description = ?, date_debut = ?, date_fin = ?, lieu = ?, id_personnel = ?, id_vehicule = ?, etat = ? " +
                "WHERE id_mission = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, m.getTitre());
            stmt.setString(2, m.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(m.getDateDebut()));
            stmt.setTimestamp(4, Timestamp.valueOf(m.getDateFin()));
            stmt.setString(5, m.getLieu());
            stmt.setInt(6, m.getIdPersonnel());
            stmt.setInt(7, m.getIdVehicule());
            stmt.setString(8, m.getEtat());
            stmt.setInt(9, m.getIdMission());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM mission WHERE id_mission = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Mission mapResultSetToEntity(ResultSet rs) throws SQLException {
        Mission m = new Mission();
        m.setIdMission(rs.getInt("id_mission"));
        m.setTitre(rs.getString("titre"));
        m.setDescription(rs.getString("description"));
        m.setDateDebut(rs.getTimestamp("date_debut").toLocalDateTime());
        m.setDateFin(rs.getTimestamp("date_fin").toLocalDateTime());
        m.setLieu(rs.getString("lieu"));
        m.setIdPersonnel(rs.getInt("id_personnel"));
        m.setIdVehicule(rs.getInt("id_vehicule"));
        m.setEtat(rs.getString("etat"));
        return m;
    }
}
