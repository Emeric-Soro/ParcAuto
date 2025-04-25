package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.UtilisateurDAOImpl;
import main.java.ci.miage.MiAuto.dao.interfaces.IUtilisateurDAO;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.utils.SecurityUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Service pour l'authentification des utilisateurs
 */
public class AuthentificationService {

    private IUtilisateurDAO utilisateurDAO;

    /**
     * Constructeur par défaut
     */
    public AuthentificationService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
    }

    /**
     * Constructeur avec DAO injecté (utile pour les tests)
     * @param utilisateurDAO DAO à utiliser
     */
    public AuthentificationService(IUtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et mot de passe
     * @param login Nom d'utilisateur
     * @param password Mot de passe
     * @return Utilisateur authentifié ou null si l'authentification échoue
     */
    public Utilisateur authentifier(String login, String password) {
        try {
            // Rechercher l'utilisateur par son login
            Utilisateur utilisateur = utilisateurDAO.findByLogin(login);

            if (utilisateur != null) {
                // Vérifier si l'utilisateur est actif
                if (!utilisateur.isStatut()) {
                    System.err.println("Tentative de connexion avec un compte désactivé: " + login);
                    return null;
                }

                // Vérifier le mot de passe (le mot de passe en base est supposé être haché)
                if (verifierMotDePasse(password, utilisateur.getMotDePasse())) {
                    // Mettre à jour la date de dernière connexion
                    utilisateur.setDerniereConnexion(LocalDateTime.now());
                    utilisateurDAO.update(utilisateur);

                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }

        return null;
    }

    /**
     * Vérifie si le mot de passe fourni correspond au hash stocké
     * @param passwordFourni Mot de passe fourni en clair
     * @param passwordStocke Hash du mot de passe stocké
     * @return true si les mots de passe correspondent, false sinon
     */
    private boolean verifierMotDePasse(String passwordFourni, String passwordStocke) {
        // Dans une application réelle, vous utiliseriez un algorithme de hachage sécurisé
        // comme BCrypt ou Argon2, mais pour simplifier, nous utilisons une méthode basique
        return SecurityUtils.verifyPassword(passwordFourni, passwordStocke);
    }

    /**
     * Déconnecte l'utilisateur courant
     */
    public void deconnecter() {
        // Cette méthode pourrait effectuer des opérations supplémentaires lors de la déconnexion
        // comme enregistrer l'heure de déconnexion, etc.
    }
}