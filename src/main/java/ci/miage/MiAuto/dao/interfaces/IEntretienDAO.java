package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux entretiens
 */
public interface IEntretienDAO extends IBaseDAO<Entretien> {

    /**
     * Recherche les entretiens d'un véhicule spécifique
     * @param idVehicule ID du véhicule recherché
     * @return Liste des entretiens du véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findByVehicule(int idVehicule) throws SQLException;

    /**
     * Recherche les entretiens en cours (sans date de sortie)
     * @return Liste des entretiens en cours
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findEnCours() throws SQLException;

    /**
     * Recherche les entretiens terminés (avec date de sortie)
     * @return Liste des entretiens terminés
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findTermines() throws SQLException;

    /**
     * Recherche les entretiens par motif (recherche partielle)
     * @param motif Motif à rechercher
     * @return Liste des entretiens correspondants
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findByMotif(String motif) throws SQLException;

    /**
     * Recherche les entretiens par lieu (recherche partielle)
     * @param lieu Lieu à rechercher
     * @return Liste des entretiens correspondants
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findByLieu(String lieu) throws SQLException;

    /**
     * Met à jour uniquement la date de sortie d'un entretien
     * @param idEntretien ID de l'entretien
     * @param dateSortie Nouvelle date de sortie
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateDateSortie(int idEntretien, java.time.LocalDateTime dateSortie) throws SQLException;
}