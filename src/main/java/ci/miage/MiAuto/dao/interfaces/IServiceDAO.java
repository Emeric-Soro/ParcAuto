package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux services
 */
public interface IServiceDAO extends IBaseDAO<Service> {

    /**
     * Recherche un service par son libellé
     * @param libelle Libellé du service
     * @return Le service correspondant ou null
     * @throws SQLException En cas d'erreur SQL
     */
    Service findByLibelle(String libelle) throws SQLException;

    /**
     * Vérifie l'existence d'un service
     * @param libelle Libellé du service
     * @return true si le service existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLibelle(String libelle) throws SQLException;

    /**
     * Met à jour le libellé d’un service
     * @param id Identifiant du service
     * @param nouveauLibelle Nouveau libellé
     * @return true si la mise à jour a été faite
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateLibelle(String id, String nouveauLibelle) throws SQLException;

    /**
     * Supprime un service par son libellé
     * @param libelle Libellé du service à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean deleteByLibelle(String libelle) throws SQLException;

    /**
     * Recherche tous les personnels liés à un service
     * @param idService ID du service
     * @return Liste des ID des personnels du service
     * @throws SQLException En cas d'erreur SQL
     */
    List<String> findPersonnelsByService(String idService) throws SQLException;

    boolean updateLibelle(int id, String nouveauLibelle) throws SQLException;
}
