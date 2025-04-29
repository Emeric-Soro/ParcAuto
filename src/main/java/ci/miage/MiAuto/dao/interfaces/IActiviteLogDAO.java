package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.ActiviteLog;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les logs d'activités
 */
public interface IActiviteLogDAO extends IBaseDAO<ActiviteLog> {

    /**
     * Récupère les n activités les plus récentes
     * @param limit Nombre d'activités à récupérer
     * @return Liste des activités récentes
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findRecent(int limit) throws SQLException;

    /**
     * Recherche les activités par type
     * @param typeActivite Type d'activité à rechercher
     * @return Liste des activités du type spécifié
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findByType(String typeActivite) throws SQLException;

    /**
     * Recherche les activités liées à une entité spécifique
     * @param idReference ID de l'entité
     * @param typeReference Type de l'entité
     * @return Liste des activités liées à cette entité
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findByReference(int idReference, String typeReference) throws SQLException;
}