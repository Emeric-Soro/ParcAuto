package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Privilege;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux privilèges
 */
public interface IPrivilegeDAO extends IBaseDAO<Privilege> {

    /**
     * Recherche un privilège par son libellé
     * @param libelle Libellé du privilège
     * @return Le privilège correspondant ou null si inexistant
     * @throws SQLException En cas d'erreur SQL
     */
    Privilege findByLibelle(String libelle) throws SQLException;

    /**
     * Vérifie l’existence d’un privilège par son libellé
     * @param libelle Libellé à vérifier
     * @return true si le privilège existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLibelle(String libelle) throws SQLException;

    /**
     * Supprime un privilège par son libellé
     * @param libelle Libellé du privilège à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean deleteByLibelle(String libelle) throws SQLException;

    /**
     * Met à jour le libellé d’un privilège
     * @param idPrivilege Identifiant du privilège
     * @param nouveauLibelle Nouveau libellé à attribuer
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d’erreur SQL
     */
    boolean updateLibelle(String idPrivilege, String nouveauLibelle) throws SQLException;

    boolean updateLibelle(int id, String nouveauLibelle) throws SQLException;
}
