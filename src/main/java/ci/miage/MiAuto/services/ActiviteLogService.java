package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.interfaces.IActiviteLogDAO;
import main.java.ci.miage.MiAuto.models.ActiviteLog;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des logs d'activité
 */
public class ActiviteLogService {

    private IActiviteLogDAO activiteLogDAO;

    /**
     * Constructeur par défaut
     */
    public ActiviteLogService() {
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Constructeur avec DAO injecté (utile pour les tests)
     * @param activiteLogDAO DAO à utiliser
     */
    public ActiviteLogService(IActiviteLogDAO activiteLogDAO) {
        this.activiteLogDAO = activiteLogDAO;
    }

    /**
     * Ajoute un nouveau log d'activité
     * @param activiteLog L'activité à enregistrer
     * @return L'activité enregistrée avec son ID généré, ou null en cas d'erreur
     */
    public ActiviteLog addActiviteLog(ActiviteLog activiteLog) {
        try {
            return activiteLogDAO.save(activiteLog);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du log d'activité: " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère toutes les activités récentes
     * @param limit Nombre d'activités à récupérer
     * @return Liste des activités récentes
     */
    public List<ActiviteLog> getActivitesRecentes(int limit) {
        try {
            return activiteLogDAO.findRecent(limit);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des activités récentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère toutes les activités d'un certain type
     * @param typeActivite Type d'activité
     * @return Liste des activités du type spécifié
     */
    public List<ActiviteLog> getActivitesByType(String typeActivite) {
        try {
            return activiteLogDAO.findByType(typeActivite);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des activités par type: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère toutes les activités liées à une entité spécifique
     * @param idReference ID de l'entité
     * @param typeReference Type de l'entité
     * @return Liste des activités liées à cette entité
     */
    public List<ActiviteLog> getActivitesByReference(int idReference, String typeReference) {
        try {
            return activiteLogDAO.findByReference(idReference, typeReference);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des activités par référence: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Formate la date d'activité en texte relatif (il y a X minutes, heures, etc.)
     * @param dateActivite Date de l'activité
     * @return Texte formaté
     */
    public String formatDateRelative(LocalDateTime dateActivite) {
        if (dateActivite == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateActivite, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) {
            return "À l'instant";
        } else if (minutes < 60) {
            return "Il y a " + minutes + (minutes == 1 ? " minute" : " minutes");
        } else if (hours < 24) {
            return "Il y a " + hours + (hours == 1 ? " heure" : " heures");
        } else if (days < 7) {
            return "Il y a " + days + (days == 1 ? " jour" : " jours");
        } else {
            // Format date (ex: "25 Avril 2025, 14:30")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
            return dateActivite.format(formatter);
        }
    }
}