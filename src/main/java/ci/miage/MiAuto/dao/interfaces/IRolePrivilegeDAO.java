package main.java.ci.miage.MiAuto.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des relations entre rôles et privilèges
 */
public interface IRolePrivilegeDAO {

    /**
     * Ajoute un privilège à un rôle
     * @param idRole ID du rôle
     * @param idPrivilege ID du privilège
     * @return true si l'ajout a été effectué avec succès
     * @throws SQLException En cas d'erreur SQL
     */
    boolean addPrivilegeToRole(String idRole, String idPrivilege) throws SQLException;

    /**
     * Supprime un privilège d’un rôle
     * @param idRole ID du rôle
     * @param idPrivilege ID du privilège
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean removePrivilegeFromRole(String idRole, String idPrivilege) throws SQLException;

    /**
     * Supprime tous les privilèges associés à un rôle
     * @param idRole ID du rôle
     * @return Nombre de privilèges supprimés
     * @throws SQLException En cas d'erreur SQL
     */
    int removeAllPrivilegesFromRole(String idRole) throws SQLException;

    /**
     * Liste des privilèges associés à un rôle
     * @param idRole ID du rôle
     * @return Liste des ID de privilèges
     * @throws SQLException En cas d'erreur SQL
     */
    List<String> findPrivilegesIdsByRole(String idRole) throws SQLException;

    /**
     * Vérifie si une relation rôle-privilège existe
     * @param idRole ID du rôle
     * @param idPrivilege ID du privilège
     * @return true si la relation existe
     * @throws SQLException En cas d'erreur SQL
     */
    boolean exists(String idRole, String idPrivilege) throws SQLException;
}
