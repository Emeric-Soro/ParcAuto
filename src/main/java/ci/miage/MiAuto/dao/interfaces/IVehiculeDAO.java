package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux véhicules
 */
public interface IVehiculeDAO extends IBaseDAO<Vehicule> {

    /**
     * Recherche les véhicules par état
     * @param idEtatVoiture ID de l'état recherché
     * @return Liste des véhicules dans l'état spécifié
     * @throws SQLException En cas d'erreur SQL
     */
    List<Vehicule> findByEtat(int idEtatVoiture) throws SQLException;

    /**
     * Recherche les véhicules disponibles (non attribués et en état disponible)
     * @return Liste des véhicules disponibles
     * @throws SQLException En cas d'erreur SQL
     */
    List<Vehicule> findDisponibles() throws SQLException;

    /**
     * Recherche les véhicules par marque
     * @param marque Marque recherchée
     * @return Liste des véhicules de la marque spécifiée
     * @throws SQLException En cas d'erreur SQL
     */
    List<Vehicule> findByMarque(String marque) throws SQLException;

    /**
     * Met à jour l'état d'un véhicule
     * @param idVehicule ID du véhicule
     * @param idEtatVoiture Nouvel état du véhicule
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateEtat(int idVehicule, int idEtatVoiture) throws SQLException;

    /**
     * Recherche les véhicules dont la visite technique est expirée ou va expirer dans les jours à venir
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des véhicules dont la visite technique est proche de l'expiration
     * @throws SQLException En cas d'erreur SQL
     */
    List<Vehicule> findVisiteTechniqueExpiration(int joursAvantExpiration) throws SQLException;

    /**
     * Recherche les véhicules par immatriculation (partielle ou complète)
     * @param immatriculation Immatriculation recherchée
     * @return Liste des véhicules correspondant au critère
     * @throws SQLException En cas d'erreur SQL
     */
    List<Vehicule> findByImmatriculation(String immatriculation) throws SQLException;

    /**
     * Compte le nombre de véhicules par état
     * @return Un tableau associatif (Map) contenant le nombre de véhicules par état
     * @throws SQLException En cas d'erreur SQL
     */
    java.util.Map<Integer, Integer> countByEtat() throws SQLException;

    /**
     * Met à jour le kilométrage d'un véhicule
     * @param idVehicule ID du véhicule
     * @param kilometrage Nouveau kilométrage
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateKilometrage(int idVehicule, int kilometrage) throws SQLException;
}