package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant une assurance
 */
public class Assurance {
    private int numCarteAssurance;
    private LocalDateTime dateDebutAssurance;
    private LocalDateTime dateFinAssurance;
    private String agence;
    private int coutAssurance;

    /**
     * Constructeur par défaut
     */
    public Assurance() {
    }

    /**
     * Constructeur avec les champs obligatoires
     * @param dateDebutAssurance Date de début de l'assurance
     * @param dateFinAssurance Date de fin de l'assurance
     * @param agence Agence d'assurance
     */
    public Assurance(LocalDateTime dateDebutAssurance, LocalDateTime dateFinAssurance, String agence) {
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateFinAssurance = dateFinAssurance;
        this.agence = agence;
    }

    /**
     * Constructeur complet
     */
    public Assurance(int numCarteAssurance, LocalDateTime dateDebutAssurance,
                     LocalDateTime dateFinAssurance, String agence, int coutAssurance) {
        this.numCarteAssurance = numCarteAssurance;
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateFinAssurance = dateFinAssurance;
        this.agence = agence;
        this.coutAssurance = coutAssurance;
    }

    // Getters et Setters
    public int getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(int numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public LocalDateTime getDateDebutAssurance() {
        return dateDebutAssurance;
    }

    public void setDateDebutAssurance(LocalDateTime dateDebutAssurance) {
        this.dateDebutAssurance = dateDebutAssurance;
    }

    public LocalDateTime getDateFinAssurance() {
        return dateFinAssurance;
    }

    public void setDateFinAssurance(LocalDateTime dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public int getCoutAssurance() {
        return coutAssurance;
    }

    public void setCoutAssurance(int coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    /**
     * Vérifie si l'assurance est valide (non expirée)
     * @return true si l'assurance est valide, false sinon
     */
    public boolean isValide() {
        return dateFinAssurance != null && dateFinAssurance.isAfter(LocalDateTime.now());
    }

    /**
     * Vérifie si l'assurance expire prochainement
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return true si l'assurance expire dans le délai indiqué, false sinon
     */
    public boolean estProcheDExpiration(int joursAvantExpiration) {
        if (dateFinAssurance == null) {
            return false;
        }

        LocalDateTime dateLimite = LocalDateTime.now().plusDays(joursAvantExpiration);
        return dateFinAssurance.isBefore(dateLimite) && dateFinAssurance.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Assurance #" + numCarteAssurance + " - " + agence;
    }
}