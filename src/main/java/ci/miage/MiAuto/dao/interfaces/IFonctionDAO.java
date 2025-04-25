package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Fonction;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux fonctions des personnels
 */
public interface IFonctionDAO extends IBaseDAO<Fonction> {

    /**
     * Recherche une fonction par son libellé
     * @param libelle Libellé de la fonction
     * @return La fonction correspondante ou null si non trouvée
     * @throws SQLException En cas d'erreur SQL
     */
    Fonction findByLibelle(String libelle) throws SQLException;

    /**
     * Vérifie l'existence d'une fonction
     * @param libelle Libellé à vérifier
     * @return true si la fonction existe
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLibelle(String libelle) throws SQLException;

    /**
     * Supprime une fonction par son libellé
     * @param libelle Libellé de la fonction à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean deleteByLibelle(String libelle) throws SQLException;

    /**
     * Met à jour le libellé d'une fonction
     * @param id Identifiant de la fonction
     * @param nouveauLibelle Nouveau libellé à attribuer
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateLibelle(String id, String nouveauLibelle) throws SQLException;

    boolean updateLibelle(int id, String nouveauLibelle) throws SQLException;
}
