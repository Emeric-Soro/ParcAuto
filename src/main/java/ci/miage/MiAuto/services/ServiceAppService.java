package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.ServiceDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des services de l'entreprise
 * Note: Renommé en ServiceAppService pour éviter la confusion avec la classe Service
 */
public class ServiceAppService {

    private ServiceDAOImpl serviceDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    /**
     * Constructeur par défaut
     */
    public ServiceAppService() {
        this.serviceDAO = new ServiceDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère un service par son ID
     * @param idService ID du service
     * @return Le service trouvé ou null
     */
    public Service getServiceById(int idService) {
        try {
            return serviceDAO.findById(idService);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du service: " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère tous les services
     * @return Liste de tous les services
     */
    public List<Service> getAllServices() {
        try {
            return serviceDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des services: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ajoute un nouveau service
     * @param service Service à ajouter
     * @return Le service ajouté avec son ID généré, ou null
     */
    public Service addService(Service service) {
        try {
            Service nouveauService = serviceDAO.save(service);

            if (nouveauService != null) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("CREATION");
                log.setTypeReference("SERVICE");
                log.setIdReference(nouveauService.getIdService());
                log.setDescription("Nouveau service créé: " + nouveauService.getLibelleService() +
                        (nouveauService.getLocalisationService() != null ?
                                " (" + nouveauService.getLocalisationService() + ")" : ""));
                activiteLogDAO.save(log);
            }

            return nouveauService;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du service: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour un service existant
     * @param service Service à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateService(Service service) {
        try {
            boolean updated = serviceDAO.update(service);

            if (updated) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("MODIFICATION");
                log.setTypeReference("SERVICE");
                log.setIdReference(service.getIdService());
                log.setDescription("Service modifié: " + service.getLibelleService() +
                        (service.getLocalisationService() != null ?
                                " (" + service.getLocalisationService() + ")" : ""));
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un service
     * @param idService ID du service à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteService(int idService) {
        try {
            Service service = serviceDAO.findById(idService);
            if (service == null) {
                return false;
            }

            boolean deleted = serviceDAO.delete(idService);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION");
                log.setTypeReference("SERVICE");
                log.setIdReference(idService);
                log.setDescription("Service supprimé: " + service.getLibelleService() +
                        (service.getLocalisationService() != null ?
                                " (" + service.getLocalisationService() + ")" : ""));
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche un service par libellé
     * @param libelle Libellé à rechercher
     * @return Le service trouvé ou null
     */
    public Service findServiceByLibelle(String libelle) {
        try {
            return serviceDAO.findByLibelle(libelle);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du service par libellé: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recherche des services par localisation
     * @param localisation Localisation à rechercher
     * @return Liste des services correspondants
     */
    public List<Service> searchServicesByLocalisation(String localisation) {
        try {
            return serviceDAO.findByLocalisation(localisation);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des services par localisation: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Vérifie si un service existe avec le libellé donné
     * @param libelle Libellé à vérifier
     * @return true si le service existe, false sinon
     */
    public boolean serviceExistsByLibelle(String libelle) {
        try {
            return serviceDAO.existsByLibelle(libelle);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence du service: " + e.getMessage());
            return false;
        }
    }
}