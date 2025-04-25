package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Personnel;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux personnels
 */
public interface IPersonnelDAO extends IBaseDAO<Personnel> {

    /**
     * Recherche un personnel par email
     * @param email Email à rechercher
     * @return Personnel correspondant ou null si non trouvé
     * @throws SQLException En cas d'erreur SQL
     */
    Personnel findByEmail(String email) throws SQLException;

    /**
     * Vérifie l'existence d'un personnel par email
     * @param email Email à vérifier
     * @return true si le personnel existe
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByEmail(String email) throws SQLException;

    /**
     * Recherche les personnels par fonction
     * @param idFonction ID de la fonction
     * @return Liste des personnels exerçant cette fonction
     * @throws SQLException En cas d'erreur SQL
     */
    List<Personnel> findByFonction(String idFonction) throws SQLException;

    /**
     * Recherche les personnels par service
     * @param idService ID du service
     * @return Liste des personnels du service
     * @throws SQLException En cas d'erreur SQL
     */
    List<Personnel> findByService(String idService) throws SQLException;

    /**
     * Met à jour le service d’un personnel
     * @param idPersonnel ID du personnel
     * @param idNouveauService ID du nouveau service
     * @return true si la mise à jour a été effectuée
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateService(String idPersonnel, String idNouveauService) throws SQLException;
}
