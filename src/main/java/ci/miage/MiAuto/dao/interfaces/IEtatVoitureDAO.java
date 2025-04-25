package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.EtatVoiture;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux états de voiture
 */
public interface IEtatVoitureDAO extends IBaseDAO<EtatVoiture> {

    /**
     * Recherche un état de voiture par son libellé
     * @param libelle Libellé de l'état
     * @return L'état correspondant ou null s'il n'existe pas
     * @throws SQLException En cas d'erreur SQL
     */
    EtatVoiture findByLibelle(String libelle) throws SQLException;

    /**
     * Vérifie si un état existe par son libellé
     * @param libelle Libellé de l'état
     * @return true si l'état existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLibelle(String libelle) throws SQLException;

    /**
     * Supprime un état de voiture par son libellé
     * @param libelle Libellé de l'état à supprimer
     * @return true si la suppression a été effectuée
     * @throws SQLException En cas d'erreur SQL
     */
    boolean deleteByLibelle(String libelle) throws SQLException;

    /**
     * Met à jour le libellé d’un état
     * @param id Identifiant de l’état
     * @param nouveauLibelle Nouveau libellé
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d’erreur SQL
     */
    boolean updateLibelle(int id, String nouveauLibelle) throws SQLException;
}
