package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.VisiteTechnique;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux visites techniques
 */
public interface IVisiteTechniqueDAO extends IBaseDAO<VisiteTechnique> {

    /**
     * Recherche les visites techniques d'un véhicule spécifique
     * @param idVehicule ID du véhicule recherché
     * @return Liste des visites techniques du véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findByVehicule(int idVehicule) throws SQLException;

    /**
     * Recherche les visites techniques valides (non expirées)
     * @return Liste des visites techniques valides
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findValides() throws SQLException;

    /**
     * Recherche les visites techniques expirées
     * @return Liste des visites techniques expirées
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findExpirees() throws SQLException;

    /**
     * Recherche les visites techniques qui expirent bientôt
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des visites techniques proches de l'expiration
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findProchesExpiration(int joursAvantExpiration) throws SQLException;

    /**
     * Recherche les visites techniques effectuées dans une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des visites techniques dans la période
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException;

    /**
     * Recherche les visites techniques par résultat
     * @param resultat Résultat recherché
     * @return Liste des visites techniques avec le résultat spécifié
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findByResultat(String resultat) throws SQLException;

    /**
     * Recherche les visites techniques par centre
     * @param centre Centre de visite recherché
     * @return Liste des visites techniques effectuées dans le centre spécifié
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findByCentre(String centre) throws SQLException;
}