package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.AssuranceDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Assurance;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service pour la gestion des assurances
 */
public class AssuranceService {

    private AssuranceDAOImpl assuranceDAO;
    private VehiculeDAOImpl vehiculeDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    /**
     * Constructeur par défaut
     */
    public AssuranceService() {
        this.assuranceDAO = new AssuranceDAOImpl();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère toutes les assurances
     * @return Liste de toutes les assurances
     */
    public List<Assurance> getAllAssurances() {
        try {
            return assuranceDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des assurances: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère une assurance par son numéro de carte
     * @param numCarteAssurance Numéro de carte d'assurance
     * @return L'assurance trouvée ou null
     */
    public Assurance getAssuranceById(int numCarteAssurance) {
        try {
            return assuranceDAO.findById(numCarteAssurance);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'assurance: " + e.getMessage());
            return null;
        }
    }

    /**
     * Ajoute une nouvelle assurance
     * @param assurance Assurance à ajouter
     * @return L'assurance ajoutée avec son numéro généré, ou null
     */
    public Assurance addAssurance(Assurance assurance) {
        try {
            Assurance nouvelleAssurance = assuranceDAO.save(assurance);

            if (nouvelleAssurance != null) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("CREATION");
                log.setTypeReference("ASSURANCE");
                log.setIdReference(nouvelleAssurance.getNumCarteAssurance());
                log.setDescription("Nouvelle assurance créée: " + nouvelleAssurance.getAgence());
                activiteLogDAO.save(log);
            }

            return nouvelleAssurance;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'assurance: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour une assurance existante
     * @param assurance Assurance à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateAssurance(Assurance assurance) {
        try {
            boolean updated = assuranceDAO.update(assurance);

            if (updated) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("MODIFICATION");
                log.setTypeReference("ASSURANCE");
                log.setIdReference(assurance.getNumCarteAssurance());
                log.setDescription("Assurance modifiée: " + assurance.getAgence());
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'assurance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime une assurance
     * @param numCarteAssurance Numéro de carte de l'assurance à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteAssurance(int numCarteAssurance) {
        try {
            Assurance assurance = assuranceDAO.findById(numCarteAssurance);
            if (assurance == null) {
                return false;
            }

            boolean deleted = assuranceDAO.delete(numCarteAssurance);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION");
                log.setTypeReference("ASSURANCE");
                log.setIdReference(numCarteAssurance);
                log.setDescription("Assurance supprimée: " + assurance.getAgence());
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'assurance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche des assurances par agence
     * @param agence Agence à rechercher
     * @return Liste des assurances correspondantes
     */
    public List<Assurance> findByAgence(String agence) {
        try {
            return assuranceDAO.findByAgence(agence);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des assurances par agence: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des assurances valides (non expirées)
     * @return Liste des assurances valides
     */
    public List<Assurance> findValides() {
        try {
            return assuranceDAO.findValides();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des assurances valides: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des assurances expirées
     * @return Liste des assurances expirées
     */
    public List<Assurance> findExpirees() {
        try {
            return assuranceDAO.findExpirees();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des assurances expirées: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des assurances qui expirent bientôt
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des assurances proches de l'expiration
     */
    public List<Assurance> findProchesExpiration(int joursAvantExpiration) {
        try {
            return assuranceDAO.findProchesExpiration(joursAvantExpiration);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des assurances proches de l'expiration: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des assurances par date d'expiration
     * @param dateExpiration Date d'expiration à rechercher
     * @return Liste des assurances correspondantes
     */
    public List<Assurance> findByDateExpiration(LocalDateTime dateExpiration) {
        try {
            return assuranceDAO.findByDateExpiration(dateExpiration);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des assurances par date d'expiration: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcule le coût total des assurances sur une période
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Coût total des assurances sur la période
     */
    public int calculerCoutTotal(LocalDateTime debut, LocalDateTime fin) {
        try {
            return assuranceDAO.calculerCoutTotal(debut, fin);
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du coût total des assurances: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Assigne une assurance à un véhicule
     * @param numCarteAssurance Numéro de carte d'assurance
     * @param idVehicule ID du véhicule
     * @return true si l'assignation a réussi, false sinon
     */
    public boolean assignerVehicule(int numCarteAssurance, int idVehicule) {
        try {
            boolean assigned = assuranceDAO.assignerVehicule(numCarteAssurance, idVehicule);

            if (assigned) {
                Assurance assurance = assuranceDAO.findById(numCarteAssurance);
                Vehicule vehicule = vehiculeDAO.findById(idVehicule);

                if (assurance != null && vehicule != null) {
                    // Enregistrer l'activité
                    ActiviteLog log = new ActiviteLog();
                    log.setTypeActivite("ASSURANCE");
                    log.setTypeReference("VEHICULE");
                    log.setIdReference(idVehicule);
                    log.setDescription("Assurance " + assurance.getAgence() + " (N°" + assurance.getNumCarteAssurance() +
                            ") assignée au véhicule " + vehicule.getMarque() + " " + vehicule.getModele() +
                            " (" + vehicule.getImmatriculation() + ")");
                    activiteLogDAO.save(log);
                }
            }

            return assigned;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation de l'assurance au véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retire l'assignation d'une assurance à un véhicule
     * @param numCarteAssurance Numéro de carte d'assurance
     * @param idVehicule ID du véhicule (0 pour retirer toutes les assignations)
     * @return true si l'opération a réussi, false sinon
     */
    public boolean retirerAssignationVehicule(int numCarteAssurance, int idVehicule) {
        try {
            // Si on va retirer une assignation spécifique, on enregistre l'activité avant
            if (idVehicule > 0) {
                Assurance assurance = assuranceDAO.findById(numCarteAssurance);
                Vehicule vehicule = vehiculeDAO.findById(idVehicule);

                if (assurance != null && vehicule != null) {
                    boolean removed = assuranceDAO.supprimerAssignationVehicule(numCarteAssurance, idVehicule);

                    if (removed) {
                        // Enregistrer l'activité
                        ActiviteLog log = new ActiviteLog();
                        log.setTypeActivite("RETRAIT_ASSURANCE");
                        log.setTypeReference("VEHICULE");
                        log.setIdReference(idVehicule);
                        log.setDescription("Assurance " + assurance.getAgence() + " (N°" + assurance.getNumCarteAssurance() +
                                ") retirée du véhicule " + vehicule.getMarque() + " " + vehicule.getModele() +
                                " (" + vehicule.getImmatriculation() + ")");
                        activiteLogDAO.save(log);
                    }

                    return removed;
                }
            }

            // Retirer toutes les assignations ou cas non traité ci-dessus
            return assuranceDAO.supprimerAssignationVehicule(numCarteAssurance, idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors du retrait de l'assignation de l'assurance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les véhicules associés à une assurance
     * @param numCarteAssurance Numéro de carte d'assurance
     * @return Liste des véhicules associés
     */
    public List<Vehicule> getVehiculesForAssurance(int numCarteAssurance) {
        try {
            List<Integer> vehiculeIds = assuranceDAO.getVehiculesIdsForAssurance(numCarteAssurance);
            List<Vehicule> vehicules = new ArrayList<>();

            for (Integer id : vehiculeIds) {
                Vehicule vehicule = vehiculeDAO.findById(id);
                if (vehicule != null) {
                    vehicules.add(vehicule);
                }
            }

            return vehicules;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules pour l'assurance: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les assurances associées à un véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des assurances associées
     */
    public List<Assurance> getAssurancesForVehicule(int idVehicule) {
        try {
            List<Integer> assuranceIds = assuranceDAO.getAssuranceIdsForVehicule(idVehicule);
            List<Assurance> assurances = new ArrayList<>();

            for (Integer id : assuranceIds) {
                Assurance assurance = assuranceDAO.findById(id);
                if (assurance != null) {
                    assurances.add(assurance);
                }
            }

            return assurances;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des assurances pour le véhicule: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Compte le nombre d'assurances valides, expirées et proches de l'expiration
     * @return Un tableau associatif avec les compteurs
     */
    public Map<String, Integer> getStatistiquesAssurances() {
        Map<String, Integer> stats = new HashMap<>();

        try {
            List<Assurance> toutes = assuranceDAO.findAll();
            List<Assurance> valides = assuranceDAO.findValides();
            List<Assurance> expirees = assuranceDAO.findExpirees();
            List<Assurance> proches = assuranceDAO.findProchesExpiration(30);

            stats.put("total", toutes.size());
            stats.put("valides", valides.size());
            stats.put("expirees", expirees.size());
            stats.put("prochesExpiration", proches.size());
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des statistiques d'assurances: " + e.getMessage());

            // Valeurs par défaut
            stats.put("total", 0);
            stats.put("valides", 0);
            stats.put("expirees", 0);
            stats.put("prochesExpiration", 0);
        }

        return stats;
    }
}