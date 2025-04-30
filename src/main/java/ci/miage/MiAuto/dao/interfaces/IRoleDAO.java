package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Role;

import java.sql.SQLException;
import java.util.List;

public interface IRoleDAO extends IBaseDAO<Role> {

    /**
     * Trouve un rôle par son nom
     * @param nomRole Nom du rôle à rechercher
     * @return Rôle correspondant ou null si non trouvé
     * @throws SQLException En cas d'erreur SQL
     */
    Role findByName(String nomRole) throws SQLException;

    /**
     * Ajoute un privilège à un rôle
     * @param idRole ID du rôle
     * @param idPrivilege ID du privilège
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean addPrivilegeToRole(int idRole, int idPrivilege) throws SQLException;

    /**
     * Retire un privilège d'un rôle
     * @param idRole ID du rôle
     * @param idPrivilege ID du privilège
     * @return true si le retrait a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean removePrivilegeFromRole(int idRole, int idPrivilege) throws SQLException;
}