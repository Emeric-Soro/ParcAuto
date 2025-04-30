package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.ActiviteLog;

import java.sql.SQLException;
import java.util.List;

public interface IActiviteLogDAO extends IBaseDAO<ActiviteLog> {

    /**
     * Trouve toutes les activités d'un type spécifique
     * @param typeActivite Type d'activité à rechercher
     * @return Liste des activités correspondantes
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findByType(String typeActivite) throws SQLException;

    /**
     * Trouve toutes les activités liées à une référence spécifique
     * @param typeReference Type de référence (ex: "VEHICULE")
     * @param idReference ID de la référence
     * @return Liste des activités correspondantes
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findByReference(String typeReference, int idReference) throws SQLException;

    /**
     * Trouve les activités les plus récentes
     * @param limit Nombre d'activités à retourner
     * @return Liste des activités les plus récentes
     * @throws SQLException En cas d'erreur SQL
     */
    List<ActiviteLog> findRecent(int limit) throws SQLException;
}