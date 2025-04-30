package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant une visite technique
 */
public class VisiteTechnique {
    private int idVisite;
    private int idVehicule;
    private LocalDateTime dateVisite;
    private LocalDateTime dateExpiration;
    private String resultat;
    private int cout;
    private String centreVisite;
    private String observations;

    // Relation avec le véhicule (à charger depuis la base)
    private Vehicule vehicule;

    /**
     * Constructeur par défaut
     */
    public VisiteTechnique() {
    }

    /**
     * Constructeur avec les champs obligatoires
     * @param idVehicule ID du véhicule concerné
     * @param dateVisite Date de la visite
     * @param dateExpiration Date d'expiration
     * @param resultat Résultat de la visite
     */
    public VisiteTechnique(int idVehicule, LocalDateTime dateVisite, LocalDateTime dateExpiration, String resultat) {
        this.idVehicule = idVehicule;
        this.dateVisite = dateVisite;
        this.dateExpiration = dateExpiration;
        this.resultat = resultat;
    }

    /**
     * Constructeur complet
     */
    public VisiteTechnique(int idVisite, int idVehicule, LocalDateTime dateVisite,
                           LocalDateTime dateExpiration, String resultat, int cout,
                           String centreVisite, String observations) {
        this.idVisite = idVisite;
        this.idVehicule = idVehicule;
        this.dateVisite = dateVisite;
        this.dateExpiration = dateExpiration;
        this.resultat = resultat;
        this.cout = cout;
        this.centreVisite = centreVisite;
        this.observations = observations;
    }

    // Getters et Setters

    public int getIdVisite() {
        return idVisite;
    }

    public void setIdVisite(int idVisite) {
        this.idVisite = idVisite;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public LocalDateTime getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(LocalDateTime dateVisite) {
        this.dateVisite = dateVisite;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public String getCentreVisite() {
        return centreVisite;
    }

    public void setCentreVisite(String centreVisite) {
        this.centreVisite = centreVisite;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        if (vehicule != null) {
            this.idVehicule = vehicule.getIdVehicule();
        }
    }

    /**
     * Vérifie si la visite technique est valide (non expirée)
     * @return true si la visite est valide, false sinon
     */
    public boolean isValide() {
        return dateExpiration != null && dateExpiration.isAfter(LocalDateTime.now());
    }

    /**
     * Vérifie si la visite technique expire prochainement
     * @param joursAvantExpiration Nombre de jours avant expiration
     * @return true si la visite expire dans le délai indiqué, false sinon
     */
    public boolean estProcheDExpiration(int joursAvantExpiration) {
        if (dateExpiration == null) {
            return false;
        }

        LocalDateTime dateLimite = LocalDateTime.now().plusDays(joursAvantExpiration);
        return dateExpiration.isBefore(dateLimite) && dateExpiration.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Visite technique #" + idVisite + " - " + resultat;
    }
}