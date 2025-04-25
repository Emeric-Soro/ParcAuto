package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IAssuranceVehiculeDAO;
import main.java.ci.miage.MiAuto.models.AssuranceVehicule;
import main.java.ci.miage.MiAuto.models.Assurance;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuranceVehiculeDAOImpl extends BaseDAOImpl<AssuranceVehicule> implements IAssuranceVehiculeDAO {

    public AssuranceVehiculeDAOImpl() {
        super();
    }

    @Override
    public boolean associer(int idVehicule, int numCarteAssurance) throws SQLException {
        String query = "INSERT INTO couvrir (ID_VEHICULE, NUM_CARTE_ASSURANCE) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);
            stmt.setInt(2, numCarteAssurance);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean dissocier(int idVehicule, int numCarteAssurance) throws SQLException {
        String query = "DELETE FROM couvrir WHERE ID_VEHICULE = ? AND NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);
            stmt.setInt(2, numCarteAssurance);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<AssuranceVehicule> findByVehicule(int idVehicule) throws SQLException {
        List<AssuranceVehicule> associations = new ArrayList<>();

        String query = "SELECT c.*, a.*, v.* FROM couvrir c " +
                "JOIN assurance a ON c.NUM_CARTE_ASSURANCE = a.NUM_CARTE_ASSURANCE " +
                "JOIN vehicules v ON c.ID_VEHICULE = v.ID_VEHICULE " +
                "WHERE c.ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    associations.add(mapResultSetToEntity(rs));
                }
            }
        }

        return associations;
    }

    @Override
    public List<AssuranceVehicule> findByAssurance(int numCarteAssurance) throws SQLException {
        List<AssuranceVehicule> associations = new ArrayList<>();

        String query = "SELECT c.*, a.*, v.* FROM couvrir c " +
                "JOIN assurance a ON c.NUM_CARTE_ASSURANCE = a.NUM_CARTE_ASSURANCE " +
                "JOIN vehicules v ON c.ID_VEHICULE = v.ID_VEHICULE " +
                "WHERE c.NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, numCarteAssurance);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    associations.add(mapResultSetToEntity(rs));
                }
            }
        }

        return associations;
    }

    @Override
    protected AssuranceVehicule mapResultSetToEntity(ResultSet rs) throws SQLException {
        AssuranceVehicule av = new AssuranceVehicule();

        av.setIdVehicule(rs.getInt("ID_VEHICULE"));
        av.setNumCarteAssurance(rs.getInt("NUM_CARTE_ASSURANCE"));

        // Optionnel: créer et remplir l'objet Assurance
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(rs.getInt("NUM_CARTE_ASSURANCE"));
        assurance.setAgence(rs.getString("AGENCE"));

        Timestamp dateDebut = rs.getTimestamp("DATE_DEBUT_ASSURANCE");
        if (dateDebut != null) {
            assurance.setDateDebutAssurance(dateDebut.toLocalDateTime());
        }

        Timestamp dateFin = rs.getTimestamp("DATE_FIN_ASSURANCE");
        if (dateFin != null) {
            assurance.setDateFinAssurance(dateFin.toLocalDateTime());
        }

        assurance.setCoutAssurance(rs.getInt("COUT_ASSURANCE"));

        av.setAssurance(assurance);

        // Optionnel: créer et remplir l'objet Vehicule
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("ID_VEHICULE"));
        // Remplir les autres propriétés du véhicule...

        av.setVehicule(vehicule);

        return av;
    }
}