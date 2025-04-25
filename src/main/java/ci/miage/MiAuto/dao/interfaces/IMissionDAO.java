package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Mission;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux missions
 */
public interface IMissionDAO extends IBaseDAO<Mission> {

    /**
     * Recherche les missions par véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des missions affectées à ce véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findByVehicule(String idVehicule) throws SQLException;

    /**
     * Recherche les missions par personnel
     * @param idPersonnel ID du personnel
     * @return Liste des missions confiées à ce personnel
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findByPersonnel(String idPersonnel) throws SQLException;

    /**
     * Recherche les missions dans une période donnée
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des missions dans l'intervalle
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException;

    /**
     * Met à jour l’état d’une mission (ex: planifiée, en cours, terminée)
     * @param idMission ID de la mission
     * @param nouvelEtat Nouveau libellé d’état
     * @return true si la mise à jour a été effectuée
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateEtat(String idMission, String nouvelEtat) throws SQLException;

    /**
     * Supprime toutes les missions liées à un véhicule
     * @param idVehicule ID du véhicule
     * @return Nombre de lignes supprimées
     * @throws SQLException En cas d'erreur SQL
     */
    int deleteByVehicule(String idVehicule) throws SQLException;
}
