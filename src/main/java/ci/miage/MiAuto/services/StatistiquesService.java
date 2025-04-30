package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.EntretienDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesService {

    private VehiculeDAOImpl vehiculeDAO;
    private EntretienDAOImpl entretienDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    public StatistiquesService() {
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.entretienDAO = new EntretienDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère le nombre total de véhicules
     * @return Nombre total de véhicules
     */
    public int getTotalVehicules() {
        try {
            return vehiculeDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des véhicules: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules par état
     * @return Map contenant le nombre de véhicules par état
     */
    public Map<Integer, Integer> getVehiculesByEtat() {
        try {
            return vehiculeDAO.countByEtat();
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des véhicules par état: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Récupère le nombre de véhicules disponibles
     * @return Nombre de véhicules disponibles
     */
    public int getVehiculesDisponibles() {
        try {
            Map<Integer, Integer> countByEtat = vehiculeDAO.countByEtat();
            // Supposons que l'ID 1 correspond à "Disponible"
            return countByEtat.getOrDefault(1, 0);
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des véhicules disponibles: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules en mission
     * @return Nombre de véhicules en mission
     */
    public int getVehiculesEnMission() {
        try {
            Map<Integer, Integer> countByEtat = vehiculeDAO.countByEtat();
            // Supposons que l'ID 2 correspond à "En mission"
            return countByEtat.getOrDefault(2, 0);
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des véhicules en mission: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre de véhicules dont la visite technique est à planifier
     * @param joursAvantExpiration Nombre de jours avant expiration à considérer
     * @return Nombre de véhicules à planifier
     */
    public int getVehiculesVisiteAPlanifier(int joursAvantExpiration) {
        try {
            return vehiculeDAO.findVisiteTechniqueExpiration(joursAvantExpiration).size();
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des véhicules à planifier: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère le nombre d'entretiens en cours
     * @return Nombre d'entretiens en cours
     */
    public int getEntretiensEnCours() {
        try {
            List<Entretien> entretiens = entretienDAO.findAll();
            long count = entretiens.stream()
                    .filter(e -> e.getDateSortieEntr() == null) // Entretien sans date de sortie = en cours
                    .count();
            return (int) count;
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des entretiens en cours: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Récupère les activités récentes
     * @param limit Nombre maximum d'activités à récupérer
     * @return Liste des activités récentes
     */
    public List<ActiviteLog> getActivitesRecentes(int limit) {
        try {
            return activiteLogDAO.findRecent(limit);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des activités récentes: " + e.getMessage());
            return List.of();
        }
    }
}