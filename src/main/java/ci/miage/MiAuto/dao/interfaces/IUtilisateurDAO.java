package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour les opérations liées aux utilisateurs de la plateforme
 */
public interface IUtilisateurDAO extends IBaseDAO<Utilisateur> {

    /**
     * Recherche un utilisateur par son email
     * @param email Email de l'utilisateur
     * @return Utilisateur correspondant ou null si inexistant
     * @throws SQLException En cas d'erreur SQL
     */
    Utilisateur findByEmail(String email) throws SQLException;

    /**
     * Recherche un utilisateur par son login
     * @param login Login à rechercher
     * @return Utilisateur trouvé ou null
     * @throws SQLException En cas d'erreur SQL
     */
    Utilisateur findByLogin(String login) throws SQLException;


    boolean updateLastConnection(int idUtilisateur, LocalDateTime dateConnexion) throws SQLException;

    /**
     * Recherche des utilisateurs par rôle
     * @param idRole ID du rôle
     * @return Liste des utilisateurs avec ce rôle
     * @throws SQLException En cas d'erreur SQL
     */
    List<Utilisateur> findByRole(int idRole) throws SQLException;


    /**
     * Vérifie si un login existe déjà
     * @param login Login à vérifier
     * @return true si le login existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByLogin(String login) throws SQLException;


    /**
     * Désactive un utilisateur
     * @param idUtilisateur ID de l'utilisateur à désactiver
     * @return true si la désactivation a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean desactiver(int idUtilisateur) throws SQLException;


    /**
     * Active un utilisateur
     * @param idUtilisateur ID de l'utilisateur à activer
     * @return true si l'activation a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean activer(int idUtilisateur) throws SQLException;


    /**
     * Met à jour le mot de passe d'un utilisateur
     * @param idUtilisateur ID de l'utilisateur
     * @param newPassword Nouveau mot de passe (déjà haché)
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updatePassword(int idUtilisateur, String newPassword) throws SQLException;



    /**
     * Vérifie l'existence d'un utilisateur par email
     * @param email Email à vérifier
     * @return true si l'utilisateur existe
     * @throws SQLException En cas d'erreur SQL
     */
    boolean existsByEmail(String email) throws SQLException;

    /**
     * Vérifie les identifiants de connexion d'un utilisateur
     * @param email Email de connexion
     * @param motDePasse Mot de passe
     * @return true si les identifiants sont valides
     * @throws SQLException En cas d'erreur SQL
     */
    boolean authenticate(String email, String motDePasse) throws SQLException;

    /**
     * Active ou désactive un compte utilisateur
     * @param idUtilisateur ID de l'utilisateur
     * @param actif true pour activer, false pour désactiver
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean setActivationStatus(String idUtilisateur, boolean actif) throws SQLException;

    /**
     * Met à jour le mot de passe d'un utilisateur
     * @param idUtilisateur ID de l'utilisateur
     * @param nouveauMotDePasse Nouveau mot de passe
     * @return true si la mise à jour a réussi
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateMotDePasse(String idUtilisateur, String nouveauMotDePasse) throws SQLException;

    /**
     * Recherche tous les utilisateurs associés à un rôle
     * @param idRole ID du rôle
     * @return Liste des utilisateurs ayant ce rôle
     * @throws SQLException En cas d'erreur SQL
     */
    List<Utilisateur> findByRole(String idRole) throws SQLException;

    void delete(String id) throws SQLException;

    Utilisateur findById(String id) throws SQLException;

    /**
     * Vérifie si un login existe déjà
     * @param login Login à vérifier
     * @param idUtilisateur ID de l'utilisateur à exclure (0 pour un nouvel utilisateur)
     * @return true si le login existe déjà, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean loginExists(String login, int idUtilisateur) throws SQLException;

    /**
     * Met à jour le statut d'un utilisateur (actif/inactif)
     * @param idUtilisateur ID de l'utilisateur
     * @param statut Nouveau statut
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean updateStatus(int idUtilisateur, boolean statut) throws SQLException;

    /**
     * Recherche les utilisateurs actifs
     * @return Liste des utilisateurs actifs
     * @throws SQLException En cas d'erreur SQL
     */
    List<Utilisateur> findActive() throws SQLException;

    /**
     * Recherche les utilisateurs inactifs
     * @return Liste des utilisateurs inactifs
     * @throws SQLException En cas d'erreur SQL
     */
    List<Utilisateur> findInactive() throws SQLException;
}