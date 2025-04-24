package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.config.DatabaseConnection;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.dao.interfaces.IVehiculeDAO;
import main.java.ci.miage.MiAuto.models.EtatVoiture;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service pour la gestion des véhicules du parc automobile
 */
public class VehiculeService {

    private IVehiculeDAO vehiculeDAO;

    /**
     * Constructeur par défaut
     */
    public VehiculeService() {
        this.vehiculeDAO = new VehiculeDAOImpl();
    }

    /**
     * Constructeur avec DAO injecté (utile pour les tests)
     * @param vehiculeDAO DAO à utiliser
     */
    public VehiculeService(IVehiculeDAO vehiculeDAO) {
        this.vehiculeDAO = vehiculeDAO;
    }

    /**
     * Récupère un véhicule par son ID
     * @param id ID du véhicule
     * @return Le véhicule trouvé ou null
     */
    public Vehicule getVehiculeById(int id) {
        try {
            return vehiculeDAO.findById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du véhicule: " + e.getMessage());
            return null;
        }
    }


    /**
     * Récupère tous les véhicules
     * @return Liste de tous les véhicules
     */
    public List<Vehicule> getAllVehicules() {
        try {
            return vehiculeDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ajoute un nouveau véhicule
     * @param vehicule Véhicule à ajouter
     * @return Le véhicule ajouté avec son ID généré, ou null en cas d'erreur
     */
    public Vehicule addVehicule(Vehicule vehicule) {
        try {
            // Date actuelle pour l'état
            if (vehicule.getDateEtat() == null) {
                vehicule.setDateEtat(LocalDateTime.now());
            }

            return vehiculeDAO.save(vehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du véhicule: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour un véhicule existant
     * @param vehicule Véhicule à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateVehicule(Vehicule vehicule) {
        try {
            return vehiculeDAO.update(vehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un véhicule
     * @param id ID du véhicule à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteVehicule(int id) {
        try {
            return vehiculeDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les véhicules selon un état spécifique
     * @param libEtatVoiture ID de l'état recherché
     * @return Liste des véhicules dans l'état spécifié
     */
    public List<Vehicule> getVehiculesByEtat(String libEtatVoiture) {
        try {
            return vehiculeDAO.findByEtat(libEtatVoiture);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules par état: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère tous les états de véhicule disponibles
     * @return Liste de tous les états
     */
    public List<EtatVoiture> getAllEtats() {
        // Créez une interface et une implémentation pour EtatVoitureDAO
        // Ou utilisez une requête directe si vous préférez
        try {
            // Par exemple, en utilisant le VehiculeDAOImpl
            return ((VehiculeDAOImpl)vehiculeDAO).getAllEtats();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des états: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les véhicules disponibles
     * @return Liste des véhicules disponibles
     */
    public List<Vehicule> getVehiculesDisponibles() {
        try {
            return vehiculeDAO.findDisponibles();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les véhicules par marque
     * @param marque Marque recherchée
     * @return Liste des véhicules de la marque spécifiée
     */
    public List<Vehicule> getVehiculesByMarque(String marque) {
        try {
            return vehiculeDAO.findByMarque(marque);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules par marque: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Change l'état d'un véhicule
     * @param idVehicule ID du véhicule
     * @param idEtatVoiture Nouvel état du véhicule
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean changeEtatVehicule(int idVehicule, int idEtatVoiture) {
        try {
            return vehiculeDAO.updateEtat(idVehicule, idEtatVoiture);
        } catch (SQLException e) {
            System.err.println("Erreur lors du changement d'état du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les véhicules dont la visite technique est sur le point d'expirer
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des véhicules concernés
     */
    public List<Vehicule> getVehiculesVisiteTechniqueExpiration(int joursAvantExpiration) {
        try {
            return vehiculeDAO.findVisiteTechniqueExpiration(joursAvantExpiration);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules par expiration de visite: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des véhicules par immatriculation
     * @param immatriculation Immatriculation (partielle ou complète) à rechercher
     * @return Liste des véhicules correspondants
     */
    public List<Vehicule> getVehiculesByImmatriculation(String immatriculation) {
        try {
            return vehiculeDAO.findByImmatriculation(immatriculation);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des véhicules par immatriculation: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtient des statistiques sur le nombre de véhicules par état
     * @return Map contenant le nombre de véhicules par état
     */
    public Map<Integer, Integer> getStatistiquesEtats() {
        try {
            return vehiculeDAO.countByEtat();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques par état: " + e.getMessage());
            return Map.of();
        }
    }

    /**
     * Met à jour le kilométrage d'un véhicule
     * @param idVehicule ID du véhicule
     * @param kilometrage Nouveau kilométrage
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateKilometrageVehicule(int idVehicule, int kilometrage) {
        try {
            return vehiculeDAO.updateKilometrage(idVehicule, kilometrage);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du kilométrage: " + e.getMessage());
            return false;
        }
    }

    /**
     * Méthode de recherche avancée combinant plusieurs critères
     * @param marque Marque du véhicule (peut être null ou vide)
     * @param modele Modèle du véhicule (peut être null ou vide)
     * @param idEtat État du véhicule (0 pour ignorer ce critère)
     * @param disponible Si true, ne retourne que les véhicules disponibles
     * @return Liste des véhicules correspondant aux critères
     */
    public List<Vehicule> rechercheAvancee(String marque, String modele, int idEtat, boolean disponible) {
        try {
            return ((VehiculeDAOImpl)vehiculeDAO).rechercheAvancee(marque, modele, idEtat, disponible);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche avancée: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Vérifie si un véhicule avec l'immatriculation donnée existe déjà
     * @param immatriculation Immatriculation à vérifier
     * @param idVehicule ID du véhicule à exclure (pour les mises à jour)
     * @return true si l'immatriculation existe déjà, false sinon
     */
    public boolean immatriculationExiste(String immatriculation, int idVehicule) {
        try {
            return ((VehiculeDAOImpl)vehiculeDAO).immatriculationExiste(immatriculation, idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification d'immatriculation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si un véhicule avec le numéro de châssis donné existe déjà
     * @param numeroChassi Numéro de châssis à vérifier
     * @param idVehicule ID du véhicule à exclure (pour les mises à jour)
     * @return true si le numéro de châssis existe déjà, false sinon
     */
    public boolean numeroChassiExiste(String numeroChassi, int idVehicule) {
        try {
            return ((VehiculeDAOImpl)vehiculeDAO).numeroChassiExiste(numeroChassi, idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du numéro de châssis: " + e.getMessage());
            return false;
        }
    }

    /**
     * Permet d'attribuer un véhicule (changer son statut d'attribution)
     * @param idVehicule ID du véhicule
     * @param attribue true si le véhicule est attribué, false sinon
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean attribuerVehicule(int idVehicule, boolean attribue) {
        try {
            Vehicule vehicule = vehiculeDAO.findById(idVehicule);
            if (vehicule != null) {
                vehicule.setStatutAttribution(attribue);
                return vehiculeDAO.update(vehicule);
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'attribution du véhicule: " + e.getMessage());
            return false;
        }
    }
}