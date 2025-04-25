package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux rôles
 */
public interface IRoleDAO extends IBaseDAO<Role> {

    /**
     * Recherche un rôle par son libellé
     * @param libelle Libellé du rôle
     * @return Le rôle correspondant ou null si inexistant
     * @throws SQLException En cas d'erreur SQL
     */
    Role findByLibelle(String libelle) throws SQLException;

    /**
     * Vérifie l’existence d’un rôle
     * @param libelle Libellé du rôle
     * @return true si le rôle existe
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLibelle(String libelle) throws SQLException;

    /**
     * Supprime un rôle par son libellé
     * @param libelle Libellé du rôle à supprimer
     * @return true si la suppression a été faite
     * @throws SQLException En cas d'erreur SQL
     */
    boolean deleteByLibelle(String libelle) throws SQLException;

    /**
     * Met à jour le libellé d’un rôle
     * @param id Identifiant du rôle
     * @param nouveauLibelle Nouveau libellé
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d’erreur SQL
     */
    boolean updateLibelle(String id, String nouveauLibelle) throws SQLException;

    /**
     * Récupère tous les privilèges associés à un rôle donné
     * @param idRole ID du rôle
     * @return Liste des privilèges associés
     * @throws SQLException En cas d'erreur SQL
     */
    List<String> findPrivilegesByRole(String idRole) throws SQLException;
}
