package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant un utilisateur du système
 */
public class Utilisateur {
    private int idUtilisateur;
    private Integer idPersonnel; // Peut être null pour les comptes admin sans personnel
    private int idRole;
    private String login;
    private String motDePasse;
    private String email;
    private boolean statut;
    private LocalDateTime derniereConnexion;

    // Relations
    private Personnel personnel;
    private Role role;

    // Constructeur par défaut
    public Utilisateur() {
    }

    // Constructeur avec les champs obligatoires
    public Utilisateur(int idRole, String login, String motDePasse) {
        this.idRole = idRole;
        this.login = login;
        this.motDePasse = motDePasse;
        this.statut = true;
    }

    // Constructeur complet
    public Utilisateur(int idUtilisateur, Integer idPersonnel, int idRole, String login,
                       String motDePasse, String email, boolean statut,
                       LocalDateTime derniereConnexion) {
        this.idUtilisateur = idUtilisateur;
        this.idPersonnel = idPersonnel;
        this.idRole = idRole;
        this.login = login;
        this.motDePasse = motDePasse;
        this.email = email;
        this.statut = statut;
        this.derniereConnexion = derniereConnexion;
    }

    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
        if (personnel != null) {
            this.idPersonnel = personnel.getIdPersonnel();
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            this.idRole = role.getIdRole();
        }
    }

    @Override
    public String toString() {
        return login + " (" + (role != null ? role.getNomRole() : "Rôle inconnu") + ")";
    }

}