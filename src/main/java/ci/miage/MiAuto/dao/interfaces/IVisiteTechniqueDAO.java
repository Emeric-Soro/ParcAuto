package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.VisiteTechnique;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux visites techniques
 */
public interface IVisiteTechniqueDAO extends IBaseDAO<VisiteTechnique> {

    /**
     * Recherche les visites techniques d’un véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des visites techniques du véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findByVehicule(String idVehicule) throws SQLException;

    /**
     * Recherche les visites techniques à venir dans un intervalle de jours
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return Liste des visites proches de l’expiration
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findProchainesEcheances(int joursAvantExpiration) throws SQLException;

    /**
     * Recherche les visites techniques réalisées dans un intervalle de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des visites techniques effectuées dans cet intervalle
     * @throws SQLException En cas d'erreur SQL
     */
    List<VisiteTechnique> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException;

    /**
     * Supprime toutes les visites techniques d’un véhicule
     * @param idVehicule ID du véhicule
     * @return nombre de lignes supprimées
     * @throws SQLException En cas d'erreur SQL
     */
    int deleteByVehicule(String idVehicule) throws SQLException;
}
