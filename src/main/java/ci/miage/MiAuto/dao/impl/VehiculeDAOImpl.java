package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.config.DatabaseConnection;
import main.java.ci.miage.MiAuto.dao.interfaces.IVehiculeDAO;
import main.java.ci.miage.MiAuto.models.EtatVoiture;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiculeDAOImpl extends BaseDAOImpl<Vehicule> implements IVehiculeDAO {

    /**
     * Constructeur
     */
    public VehiculeDAOImpl() {
        super();
    }

    @Override
    public Vehicule findById(int id) throws SQLException {
        Vehicule vehicule = null;
        String requeteTrouverVehicule = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle " +
                "FROM vehicules v LEFT JOIN etat_voiture e " +
                "ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.id_vehicule = 1";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(requeteTrouverVehicule)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vehicule = mapResultSetToVehicule(rs);
                }
            }
        }
        return vehicule;
    }

    @Override
    public List<Vehicule> findAll() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String requeteSelectionDeTout = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle " +
                "FROM vehicules v LEFT JOIN etat_voiture e " +
                "ON v.id_etat_voiture = e.id_etat_voiture ";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(requeteSelectionDeTout)) {

            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        }
        return vehicules;
    }

    @Override
    public Vehicule save(Vehicule vehicule) throws SQLException {
        String requeteInsertionVehicule = "INSERT INTO vehicule (id_etat_voiture, numero_chassi, immatriculation, marque, " +
                "modele, nb_places, energie, date_acquisition, date_ammortissement, date_mise_en_service, " +
                "puissance, couleur, prix_vehicule, date_etat, kilometrage, date_derniere_visite, " +
                "date_prochain_visite, statut_attribution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(requeteInsertionVehicule, Statement.RETURN_GENERATED_KEYS)) {
            // Paramètres obligatoires
            stmt.setInt(1, vehicule.getIdEtatVoiture());
            stmt.setString(2, vehicule.getNumeroChassi());
            stmt.setString(3, vehicule.getImmatriculation());
            stmt.setString(4, vehicule.getMarque());
            stmt.setString(5, vehicule.getModele());

            // Paramètres optionnels
            stmt.setInt(6, vehicule.getNbPlaces());
            stmt.setString(7, vehicule.getEnergie());

            if (vehicule.getDateAcquisition() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(vehicule.getDateAcquisition()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }

            if (vehicule.getDateAmmortissement() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(vehicule.getDateAmmortissement()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            if (vehicule.getDateMiseEnService() != null) {
                stmt.setTimestamp(10, Timestamp.valueOf(vehicule.getDateMiseEnService()));
            } else {
                stmt.setNull(10, Types.TIMESTAMP);
            }
            stmt.setInt(11, vehicule.getPuissance());
            stmt.setString(12, vehicule.getCouleur());
            stmt.setInt(13, vehicule.getPrixVehicule());

            if (vehicule.getDateEtat() != null) {
                stmt.setTimestamp(14, Timestamp.valueOf(vehicule.getDateEtat()));
            } else {
                stmt.setTimestamp(14, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setInt(15, vehicule.getKilometrage());

            if (vehicule.getDateDerniereVisite() != null) {
                stmt.setTimestamp(16, Timestamp.valueOf(vehicule.getDateDerniereVisite()));
            } else {
                stmt.setNull(16, Types.TIMESTAMP);
            }

            if (vehicule.getDateProchainVisite() != null) {
                stmt.setTimestamp(17, Timestamp.valueOf(vehicule.getDateProchainVisite()));
            } else {
                stmt.setNull(17, Types.TIMESTAMP);
            }

            stmt.setBoolean(18, vehicule.isStatutAttribution());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicule.setIdVehicule(generatedKeys.getInt(1));
                        return vehicule;
                    }
                }
            }
        }

        return null;

    }
}
