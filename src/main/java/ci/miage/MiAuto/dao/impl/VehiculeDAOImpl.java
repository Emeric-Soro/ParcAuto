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

/**
 * Implémentation de l'interface DAO pour les opérations liées aux véhicules
 */
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
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.id_vehicule = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vehicule = mapResultSetToEntity(rs);
                }
            }
        }

        return vehicule;
    }

    @Override
    public List<Vehicule> findAll() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                vehicules.add(mapResultSetToEntity(rs));
            }
        }

        return vehicules;
    }

    @Override
    public Vehicule save(Vehicule vehicule) throws SQLException {
        String query = "INSERT INTO vehicules (id_etat_voiture, numero_chassi, immatriculation, marque, " +
                "modele, nb_places, energie, date_acquisition, date_ammortissement, date_mise_en_service, " +
                "puissance, couleur, prix_vehicule, date_etat, kilometrage, date_derniere_visite, " +
                "DATE_PROCHAINE_VISITE, statut_attribution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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

    @Override
    public boolean update(Vehicule vehicule) throws SQLException {
        String query = "UPDATE vehicules SET id_etat_voiture = ?, numero_chassi = ?, immatriculation = ?, " +
                "marque = ?, modele = ?, nb_places = ?, energie = ?, date_acquisition = ?, " +
                "date_ammortissement = ?, date_mise_en_service = ?, puissance = ?, couleur = ?, " +
                "prix_vehicule = ?, date_etat = ?, kilometrage = ?, date_derniere_visite = ?, " +
                "DATE_PROCHAINE_VISITE = ?, statut_attribution = ? WHERE id_vehicule = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehicule.getIdEtatVoiture());
            stmt.setString(2, vehicule.getNumeroChassi());
            stmt.setString(3, vehicule.getImmatriculation());
            stmt.setString(4, vehicule.getMarque());
            stmt.setString(5, vehicule.getModele());
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
            stmt.setInt(19, vehicule.getIdVehicule());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM vehicules WHERE id_vehicule = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Vehicule> findByEtat(String libEtatVoiture) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.ID_ETAT_VOITURE = e.ID_ETAT_VOITURE " +
                "WHERE LOWER(e.LIB_ETAT_VOITURE) LIKE LOWER(?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Recherche partielle et insensible à la casse
            stmt.setString(1, "%" + libEtatVoiture + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToEntity(rs));
                }
            }
        }

        return vehicules;
    }
    // Ajoutez une méthode pour récupérer tous les états
    public List<EtatVoiture> getAllEtats() throws SQLException {
        List<EtatVoiture> etats = new ArrayList<>();
        String query = "SELECT * FROM etat_voiture ORDER BY ID_ETAT_VOITURE";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                EtatVoiture etat = new EtatVoiture();
                etat.setIdEtatVoiture(rs.getInt("ID_ETAT_VOITURE"));
                etat.setLibEtatVoiture(rs.getString("LIB_ETAT_VOITURE"));
                etats.add(etat);
            }
        }

        return etats;
    }

    @Override
    public List<Vehicule> findDisponibles() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();

        // Supposons que l'état 1 correspond à "Disponible"
        // Adaptez selon votre modèle de données
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.id_etat_voiture = 1 AND v.statut_attribution = 0";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                vehicules.add(mapResultSetToEntity(rs));
            }
        }

        return vehicules;
    }

    @Override
    public List<Vehicule> findByMarque(String marque) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.marque LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + marque + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToEntity(rs));
                }
            }
        }

        return vehicules;
    }

    @Override
    public boolean updateEtat(int idVehicule, int idEtatVoiture) throws SQLException {
        String query = "UPDATE vehicules SET id_etat_voiture = ?, date_etat = ? WHERE id_vehicule = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idEtatVoiture);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, idVehicule);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Vehicule> findVisiteTechniqueExpiration(int joursAvantExpiration) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();

        // Récupère les véhicules dont la date de prochaine visite est dans les X prochains jours
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.DATE_PROCHAINE_VISITE IS NOT NULL " +
                "AND v.DATE_PROCHAINE_VISITE <= DATE_ADD(NOW(), INTERVAL ? DAY)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, joursAvantExpiration);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToEntity(rs));
                }
            }
        }

        return vehicules;
    }

    @Override
    public List<Vehicule> findByImmatriculation(String immatriculation) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v " +
                "LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture " +
                "WHERE v.immatriculation LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + immatriculation + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToEntity(rs));
                }
            }
        }

        return vehicules;
    }

    @Override
    public Map<Integer, Integer> countByEtat() throws SQLException {
        Map<Integer, Integer> countMap = new HashMap<>();
        String query = "SELECT id_etat_voiture, COUNT(*) as count FROM vehicules GROUP BY id_etat_voiture";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idEtat = rs.getInt("id_etat_voiture");
                int count = rs.getInt("count");
                countMap.put(idEtat, count);
            }
        }

        return countMap;
    }

    @Override
    public boolean updateKilometrage(int idVehicule, int kilometrage) throws SQLException {
        String query = "UPDATE vehicule SET kilometrage = ? WHERE id_vehicule = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, kilometrage);
            stmt.setInt(2, idVehicule);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Convertit un ResultSet en objet Vehicule
     *
     * @param rs ResultSet à convertir
     * @return Objet Vehicule
     * @throws SQLException En cas d'erreur SQL
     */
    @Override
    protected Vehicule mapResultSetToEntity(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();

        vehicule.setIdVehicule(rs.getInt("id_vehicule"));
        vehicule.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
        vehicule.setNumeroChassi(rs.getString("numero_chassi"));
        vehicule.setImmatriculation(rs.getString("immatriculation"));
        vehicule.setMarque(rs.getString("marque"));
        vehicule.setModele(rs.getString("modele"));
        vehicule.setNbPlaces(rs.getInt("nb_places"));
        vehicule.setEnergie(rs.getString("energie"));

        Timestamp tsAcquisition = rs.getTimestamp("date_acquisition");
        if (tsAcquisition != null) {
            vehicule.setDateAcquisition(tsAcquisition.toLocalDateTime());
        }

        Timestamp tsAmmortissement = rs.getTimestamp("date_ammortissement");
        if (tsAmmortissement != null) {
            vehicule.setDateAmmortissement(tsAmmortissement.toLocalDateTime());
        }

        Timestamp tsMiseEnService = rs.getTimestamp("date_mise_en_service");
        if (tsMiseEnService != null) {
            vehicule.setDateMiseEnService(tsMiseEnService.toLocalDateTime());
        }

        vehicule.setPuissance(rs.getInt("puissance"));
        vehicule.setCouleur(rs.getString("couleur"));
        vehicule.setPrixVehicule(rs.getInt("prix_vehicule"));

        Timestamp tsEtat = rs.getTimestamp("date_etat");
        if (tsEtat != null) {
            vehicule.setDateEtat(tsEtat.toLocalDateTime());
        }

        vehicule.setKilometrage(rs.getInt("kilometrage"));

        Timestamp tsDerniereVisite = rs.getTimestamp("date_derniere_visite");
        if (tsDerniereVisite != null) {
            vehicule.setDateDerniereVisite(tsDerniereVisite.toLocalDateTime());
        }

        Timestamp tsProchainVisite = rs.getTimestamp("DATE_PROCHAINE_VISITE");
        if (tsProchainVisite != null) {
            vehicule.setDateProchainVisite(tsProchainVisite.toLocalDateTime());
        }

        vehicule.setStatutAttribution(rs.getBoolean("statut_attribution"));

        // Ajouter l'état de la voiture s'il est présent
        try {
            String etatLibelle = rs.getString("etat_libelle");
            if (etatLibelle != null) {
                EtatVoiture etatVoiture = new EtatVoiture();
                etatVoiture.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
                etatVoiture.setLibEtatVoiture(etatLibelle);
                vehicule.setEtatVoiture(etatVoiture);
            }
        } catch (SQLException e) {
            // Ignorer cette erreur car la colonne peut ne pas exister dans certaines requêtes
        }

        return vehicule;
    }

    /**
     * Méthode de recherche avancée combinant plusieurs critères
     * @param marque Marque du véhicule (peut être null ou vide)
     * @param modele Modèle du véhicule (peut être null ou vide)
     * @param idEtat État du véhicule (0 pour ignorer ce critère)
     * @param disponible Si true, ne retourne que les véhicules disponibles
     * @return Liste des véhicules correspondant aux critères
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Vehicule> rechercheAvancee(String marque, String modele, int idEtat, boolean disponible) throws SQLException {
        try {
            // Construire la requête SQL dynamiquement en fonction des critères fournis
            StringBuilder queryBuilder = new StringBuilder("SELECT v.*, e.LIB_ETAT_VOITURE AS etat_libelle FROM vehicules v ");
            queryBuilder.append("LEFT JOIN etat_voiture e ON v.id_etat_voiture = e.id_etat_voiture WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            if (marque != null && !marque.trim().isEmpty()) {
                queryBuilder.append("AND v.marque LIKE ? ");
                params.add("%" + marque + "%");
            }

            if (modele != null && !modele.trim().isEmpty()) {
                queryBuilder.append("AND v.modele LIKE ? ");
                params.add("%" + modele + "%");
            }

            if (idEtat > 0) {
                queryBuilder.append("AND v.id_etat_voiture = ? ");
                params.add(idEtat);
            }

            if (disponible) {
                queryBuilder.append("AND v.statut_attribution = 0 ");
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

                // Définir les paramètres
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                List<Vehicule> resultat = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultat.add(mapResultSetToEntity(rs));
                    }
                }

                return resultat;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche avancée: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Vérifie si un véhicule avec l'immatriculation donnée existe déjà
     * @param immatriculation Immatriculation à vérifier
     * @param idVehicule ID du véhicule à exclure (pour les mises à jour)
     * @return true si l'immatriculation existe déjà, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean immatriculationExiste(String immatriculation, int idVehicule) throws SQLException {
        String query = "SELECT id_vehicule FROM vehicules WHERE immatriculation = ? AND id_vehicule != ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, immatriculation);
            stmt.setInt(2, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retourne true si un résultat existe
            }
        }
    }

    /**
     * Vérifie si un véhicule avec le numéro de châssis donné existe déjà
     * @param numeroChassi Numéro de châssis à vérifier
     * @param idVehicule ID du véhicule à exclure (pour les mises à jour)
     * @return true si le numéro de châssis existe déjà, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean numeroChassiExiste(String numeroChassi, int idVehicule) throws SQLException {
        String query = "SELECT id_vehicule FROM vehicules WHERE numero_chassi = ? AND id_vehicule != ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroChassi);
            stmt.setInt(2, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retourne true si un résultat existe
            }
        }
    }
}