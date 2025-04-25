package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Entretien;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux entretiens
 */
public interface IEntretienDAO extends IBaseDAO<Entretien> {

    /**
     * Recherche les entretiens d'un véhicule donné
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens du véhicule
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findByVehicule(String idVehicule) throws SQLException;

    /**
     * Recherche les entretiens entre deux dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des entretiens dans l'intervalle
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findBetweenDates(LocalDate debut, LocalDate fin) throws SQLException;

    /**
     * Recherche les entretiens par lieu
     * @param lieu Lieu recherché
     * @return Liste des entretiens ayant eu lieu à ce lieu
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findByLieu(String lieu) throws SQLException;

    /**
     * Met à jour le coût d'un entretien
     * @param idEntretien ID de l'entretien
     * @param nouveauCout Nouveau coût
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateCout(String idEntretien, double nouveauCout) throws SQLException;

    /**
     * Supprime tous les entretiens associés à un véhicule
     * @param idVehicule ID du véhicule
     * @return nombre de lignes supprimées
     * @throws SQLException En cas d'erreur SQL
     */
    int deleteByVehicule(String idVehicule) throws SQLException;

    /**
     * Trouve tous les entretiens en cours (date d'entrée existe mais pas de date de sortie)
     * @return Liste des entretiens en cours
     * @throws SQLException En cas d'erreur SQL
     */
    List<Entretien> findEntretiensEnCours() throws SQLException;
}
