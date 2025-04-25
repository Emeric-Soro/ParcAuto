package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.EntretienDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.MissionDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VisiteTechniqueDAOImpl;
import main.java.ci.miage.MiAuto.dao.interfaces.IEntretienDAO;
import main.java.ci.miage.MiAuto.dao.interfaces.IMissionDAO;
import main.java.ci.miage.MiAuto.dao.interfaces.IVehiculeDAO;
import main.java.ci.miage.MiAuto.dao.interfaces.IVisiteTechniqueDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Service pour la gestion des statistiques
 */
public class StatistiquesService {

    private IVehiculeDAO vehiculeDAO;
    private IMissionDAO missionDAO;
    private IEntretienDAO entretienDAO;
    private IVisiteTechniqueDAO visiteTechniqueDAO;

    /**
     * Constructeur par défaut
     */
    public StatistiquesService() {
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.missionDAO = new MissionDAOImpl();
        this.entretienDAO = new EntretienDAOImpl();
        this.visiteTechniqueDAO = new VisiteTechniqueDAOImpl();
    }

    /**
     * Récupère le nombre total de véhicules
     * @return Nombre total de véhicules
     */
    public int getNombreTotalVehicules() {
        try {
            return vehiculeDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre total de véhicules: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules en mission
     * @return Nombre de véhicules en mission
     */
    public int getNombreVehiculesEnMission() {
        try {
            // État 2 = En mission (selon la base de données)
            return vehiculeDAO.findByEtat("En mission").size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de véhicules en mission: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules disponibles
     * @return Nombre de véhicules disponibles
     */
    public int getNombreVehiculesDisponibles() {
        try {
            // État 1 = Disponible (selon la base de données)
            return vehiculeDAO.findByEtat("Disponible").size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de véhicules disponibles: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules nécessitant une visite technique prochainement
     * @return Nombre de véhicules nécessitant une visite
     */
    public int getNombreVisitesAPlanifier() {
        try {
            // Récupérer les véhicules dont la visite technique expire dans les 30 jours
            return vehiculeDAO.findVisiteTechniqueExpiration(30).size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de visites à planifier: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre d'entretiens en cours
     * @return Nombre d'entretiens en cours
     */
    public int getNombreEntretiensEnCours() {
        try {
            // Récupérer les entretiens qui ont une date d'entrée mais pas de date de sortie
            return entretienDAO.findEntretiensEnCours().size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre d'entretiens en cours: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de missions actuelles
     * @return Nombre de missions actuelles
     */
    public int getNombreMissionsActuelles() {
        try {
            // Récupérer les missions en cours (date début <= aujourd'hui && date fin >= aujourd'hui)
            LocalDateTime now = LocalDateTime.now();
            return missionDAO.findMissionsEnCours(now).size();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de missions actuelles: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules par état
     * @return Map contenant le nombre de véhicules par état
     */
    public Map<Integer, Integer> getNombreVehiculesByEtat() {
        try {
            return vehiculeDAO.countByEtat();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de véhicules par état: " + e.getMessage());
            return Map.of();
        }
    }

    /**
     * Récupère le nombre total de kilomètres parcourus par tous les véhicules
     * @return Nombre total de kilomètres
     */
    public int getTotalKilometrageParc() {
        try {
            return vehiculeDAO.findAll().stream()
                    .mapToInt(v -> v.getKilometrage())
                    .sum();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du kilométrage total: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère la répartition des véhicules par marque
     * @return Map contenant le nombre de véhicules par marque
     */
    public Map<String, Integer> getRepartitionVehiculesByMarque() {
        // Cette méthode nécessiterait une implémentation spécifique dans le DAO
        // Pour cet exemple, nous retournons une map vide
        return Map.of();
    }
}