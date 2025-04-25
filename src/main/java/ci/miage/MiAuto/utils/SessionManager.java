package main.java.ci.miage.MiAuto.utils;

import main.java.ci.miage.MiAuto.models.Utilisateur;

/**
 * Gestionnaire de session pour maintenir l'état de connexion et les informations utilisateur
 * Utilise le pattern Singleton
 */
public class SessionManager {

    private static SessionManager instance;
    private Utilisateur utilisateurConnecte;

    /**
     * Constructeur privé pour le Singleton
     */
    private SessionManager() {
        // Empêche l'instanciation directe
    }

    /**
     * Récupère l'instance unique du SessionManager
     * @return Instance unique
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Récupère l'utilisateur actuellement connecté
     * @return Utilisateur connecté ou null si aucun utilisateur n'est connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * Définit l'utilisateur connecté
     * @param utilisateur Utilisateur à définir comme connecté
     */
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    /**
     * Vérifie si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isConnecte() {
        return utilisateurConnecte != null;
    }

    /**
     * Déconnecte l'utilisateur actuel
     */
    public void deconnecter() {
        utilisateurConnecte = null;
    }

    /**
     * Vérifie si l'utilisateur connecté possède un privilège spécifique
     * @param nomPrivilege Nom du privilège à vérifier
     * @return true si l'utilisateur a le privilège, false sinon
     */
    public boolean hasPrivilege(String nomPrivilege) {
        if (utilisateurConnecte == null || utilisateurConnecte.getRole() == null) {
            return false;
        }

        // Cette méthode dépendra de votre implémentation exacte du modèle de rôles et privilèges
        // Voici une implémentation simple qui suppose que le rôle a une méthode hasPrivilege
        return utilisateurConnecte.getRole().hasPrivilege(nomPrivilege);
    }
}