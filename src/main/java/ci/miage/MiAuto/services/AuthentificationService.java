package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.UtilisateurDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthentificationService {

    private UtilisateurDAOImpl utilisateurDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    public AuthentificationService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Authentifie un utilisateur
     * @param login Nom d'utilisateur
     * @param password Mot de passe
     * @return L'utilisateur authentifié ou null si l'authentification échoue
     */
    public Utilisateur authenticate(String login, String password) {
        try {
            Utilisateur utilisateur = utilisateurDAO.findByLogin(login);

            if (utilisateur != null) {
                // Pour simplifier, on compare directement les mots de passe
                // Dans une vraie application, utiliser SecurityUtils.verifyPassword
                if (password.equals(utilisateur.getMotDePasse())) {

                    // Enregistrer l'activité de connexion
                    ActiviteLog log = new ActiviteLog();
                    log.setTypeActivite("CONNEXION");
                    log.setTypeReference("UTILISATEUR");
                    log.setIdReference(utilisateur.getIdUtilisateur());
                    log.setDescription("Connexion de l'utilisateur " + utilisateur.getLogin());
                    activiteLogDAO.save(log);

                    return utilisateur;
                }
            }

            return null;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour la date de dernière connexion d'un utilisateur
     * @param idUtilisateur ID de l'utilisateur
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateLastConnection(int idUtilisateur) {
        try {
            return utilisateurDAO.updateLastConnection(idUtilisateur, LocalDateTime.now());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la dernière connexion: " + e.getMessage());
            return false;
        }
    }
}