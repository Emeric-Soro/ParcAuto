package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VisiteTechniqueDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.models.VisiteTechnique;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des visites techniques
 */
public class VisiteTechniqueService {

    private VisiteTechniqueDAOImpl visiteTechniqueDAO;
    private VehiculeDAOImpl vehiculeDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    /**
     * Constructeur par défaut
     */
    public VisiteTechniqueService() {
        this.visiteTechniqueDAO = new VisiteTechniqueDAOImpl();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère toutes les visites techniques
     * @return Liste de toutes les visites techniques
     */
    public List<VisiteTechnique> getAllVisitesTechniques() {
        try {
            return visiteTechniqueDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère une visite technique par son ID
     * @param idVisite ID de la visite technique
     * @return La visite technique trouvée ou null
     */
    public VisiteTechnique getVisiteTechniqueById(int idVisite) {
        try {
            return visiteTechniqueDAO.findById(idVisite);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la visite technique: " + e.getMessage());
            return null;
        }
    }

    /**
     * Ajoute une nouvelle visite technique
     * @param visiteTechnique Visite technique à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addVisiteTechnique(VisiteTechnique visiteTechnique) {
        try {
            // Vérifier que le véhicule existe
            Vehicule vehicule = vehiculeDAO.findById(visiteTechnique.getIdVehicule());
            if (vehicule == null) {
                return false;
            }

            // Sauvegarder la visite technique
            VisiteTechnique saved = visiteTechniqueDAO.save(visiteTechnique);

            if (saved != null) {
                // Mettre à jour la date de dernière et prochaine visite du véhicule
                vehicule.setDateDerniereVisite(visiteTechnique.getDateVisite());
                vehicule.setDateProchainVisite(visiteTechnique.getDateExpiration());
                vehiculeDAO.update(vehicule);

                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("VISITE_TECHNIQUE");
                log.setTypeReference("VEHICULE");
                log.setIdReference(visiteTechnique.getIdVehicule());
                log.setDescription("Visite technique effectuée pour le véhicule " + vehicule.getMarque() + " " +
                        vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")");
                activiteLogDAO.save(log);

                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la visite technique: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour une visite technique existante
     * @param visiteTechnique Visite technique à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateVisiteTechnique(VisiteTechnique visiteTechnique) {
        try {
            boolean updated = visiteTechniqueDAO.update(visiteTechnique);

            if (updated) {
                // Mettre à jour la date de dernière et prochaine visite du véhicule
                Vehicule vehicule = vehiculeDAO.findById(visiteTechnique.getIdVehicule());
                if (vehicule != null) {
                    vehicule.setDateDerniereVisite(visiteTechnique.getDateVisite());
                    vehicule.setDateProchainVisite(visiteTechnique.getDateExpiration());
                    vehiculeDAO.update(vehicule);

                    // Enregistrer l'activité
                    ActiviteLog log = new ActiviteLog();
                    log.setTypeActivite("MODIFICATION_VISITE");
                    log.setTypeReference("VEHICULE");
                    log.setIdReference(visiteTechnique.getIdVehicule());
                    log.setDescription("Modification de la visite technique pour le véhicule " + vehicule.getMarque() +
                            " " + vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")");
                    activiteLogDAO.save(log);
                }
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la visite technique: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime une visite technique
     * @param idVisite ID de la visite technique à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteVisiteTechnique(int idVisite) {
        try {
            // Récupérer la visite technique avant suppression pour référence
            VisiteTechnique visite = visiteTechniqueDAO.findById(idVisite);
            if (visite == null) {
                return false;
            }

            // Supprimer la visite technique
            boolean deleted = visiteTechniqueDAO.delete(idVisite);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION_VISITE");
                log.setTypeReference("VISITE");
                log.setIdReference(idVisite);
                log.setDescription("Suppression d'une visite technique (ID: " + idVisite + ")");
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la visite technique: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les visites techniques d'un véhicule spécifique
     * @param idVehicule ID du véhicule
     * @return Liste des visites techniques du véhicule
     */
    public List<VisiteTechnique> getVisitesByVehicule(int idVehicule) {
        try {
            return visiteTechniqueDAO.findByVehicule(idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques du véhicule: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les visites techniques valides (non expirées)
     * @return Liste des visites techniques valides
     */
    public List<VisiteTechnique> getVisitesValides() {
        try {
            return visiteTechniqueDAO.findValides();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques valides: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les visites techniques expirées
     * @return Liste des visites techniques expirées
     */
    public List<VisiteTechnique> getVisitesExpirees() {
        try {
            return visiteTechniqueDAO.findExpirees();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques expirées: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les visites techniques qui expirent bientôt
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des visites techniques proches de l'expiration
     */
    public List<VisiteTechnique> getVisitesARenouveler(int joursAvantExpiration) {
        try {
            return visiteTechniqueDAO.findProchesExpiration(joursAvantExpiration);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques à renouveler: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les visites techniques effectuées dans une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des visites techniques dans la période
     */
    public List<VisiteTechnique> getVisitesByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return visiteTechniqueDAO.findByPeriode(debut, fin);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites techniques par période: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les véhicules sans visite technique valide
     * @return Liste des véhicules sans visite technique valide
     */
    public List<Vehicule> getVehiculesSansVisiteValide() {
        try {
            List<Vehicule> vehicules = vehiculeDAO.findAll();
            List<Vehicule> vehiculesSansVisite = new ArrayList<>();

            for (Vehicule vehicule : vehicules) {
                // Vérifier si le véhicule a une visite technique valide
                List<VisiteTechnique> visites = visiteTechniqueDAO.findByVehicule(vehicule.getIdVehicule());
                boolean hasValidVisite = false;

                for (VisiteTechnique visite : visites) {
                    if (visite.isValide()) {
                        hasValidVisite = true;
                        break;
                    }
                }

                if (!hasValidVisite) {
                    vehiculesSansVisite.add(vehicule);
                }
            }

            return vehiculesSansVisite;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules sans visite technique valide: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}