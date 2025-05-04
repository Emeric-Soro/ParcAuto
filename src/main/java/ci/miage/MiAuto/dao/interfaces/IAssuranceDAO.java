package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Assurance;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux assurances
 */
public interface IAssuranceDAO extends IBaseDAO<Assurance> {

    /**
     * Recherche les assurances par agence
     * @param agence Nom de l'agence d'assurance
     * @return Liste des assurances de cette agence
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findByAgence(String agence) throws SQLException;

    /**
     * Recherche les assurances valides (non expirées)
     * @return Liste des assurances valides
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findValides() throws SQLException;

    /**
     * Recherche les assurances expirées
     * @return Liste des assurances expirées
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findExpirees() throws SQLException;

    /**
     * Recherche les assurances qui expirent bientôt
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des assurances proches de l'expiration
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findProchesExpiration(int joursAvantExpiration) throws SQLException;

    /**
     * Recherche les assurances par période
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des assurances dans la période
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException;

    /**
     * Recherche les assurances par date d'expiration
     * @param dateExpiration Date d'expiration à rechercher
     * @return Liste des assurances correspondantes
     * @throws SQLException En cas d'erreur SQL
     */
    List<Assurance> findByDateExpiration(LocalDateTime dateExpiration) throws SQLException;

    /**
     * Calcule le coût total des assurances sur une période
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Coût total des assurances sur la période
     * @throws SQLException En cas d'erreur SQL
     */
    int calculerCoutTotal(LocalDateTime debut, LocalDateTime fin) throws SQLException;
}