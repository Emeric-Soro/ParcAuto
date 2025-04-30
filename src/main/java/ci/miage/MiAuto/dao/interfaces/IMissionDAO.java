package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface IMissionDAO extends IBaseDAO<Mission> {

    /**
     * Trouve les missions par véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des missions du véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findByVehicule(int idVehicule) throws SQLException;

    /**
     * Trouve les missions en cours (date de début <= aujourd'hui et date de fin >= aujourd'hui ou null)
     * @return Liste des missions en cours
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findEnCours() throws SQLException;

    /**
     * Trouve les missions à venir (date de début > aujourd'hui)
     * @return Liste des missions à venir
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findAVenir() throws SQLException;

    /**
     * Trouve les missions terminées (date de fin < aujourd'hui)
     * @return Liste des missions terminées
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findTerminees() throws SQLException;

    /**
     * Trouve les missions qui ont lieu dans une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des missions dans la période
     * @throws SQLException En cas d'erreur SQL
     */
    List<Mission> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException;

    /**
     * Ajoute un participant à une mission
     * @param idMission ID de la mission
     * @param idPersonnel ID du personnel
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean addParticipant(int idMission, int idPersonnel) throws SQLException;

    /**
     * Supprime un participant d'une mission
     * @param idMission ID de la mission
     * @param idPersonnel ID du personnel
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean removeParticipant(int idMission, int idPersonnel) throws SQLException;

    /**
     * Récupère tous les participants d'une mission
     * @param idMission ID de la mission
     * @return Liste des participants
     * @throws SQLException En cas d'erreur SQL
     */
    List<Personnel> findParticipants(int idMission) throws SQLException;
}