package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant un entretien de véhicule
 */
public class Entretien {
    private int idEntretien;
    private int idVehicule;
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private int coutEntr;
    private String lieuEntr;

    // Relation avec le véhicule (à charger depuis la base)
    private Vehicule vehicule;

    /**
     * Constructeur par défaut
     */
    public Entretien() {
    }

    /**
     * Constructeur avec les champs obligatoires
     * @param idVehicule ID du véhicule en entretien
     * @param dateEntreeEntr Date d'entrée en entretien
     * @param motifEntr Motif de l'entretien
     */
    public Entretien(int idVehicule, LocalDateTime dateEntreeEntr, String motifEntr) {
        this.idVehicule = idVehicule;
        this.dateEntreeEntr = dateEntreeEntr;
        this.motifEntr = motifEntr;
    }

    /**
     * Constructeur complet
     */
    public Entretien(int idEntretien, int idVehicule, LocalDateTime dateEntreeEntr,
                     LocalDateTime dateSortieEntr, String motifEntr, String observation,
                     int coutEntr, String lieuEntr) {
        this.idEntretien = idEntretien;
        this.idVehicule = idVehicule;
        this.dateEntreeEntr = dateEntreeEntr;
        this.dateSortieEntr = dateSortieEntr;
        this.motifEntr = motifEntr;
        this.observation = observation;
        this.coutEntr = coutEntr;
        this.lieuEntr = lieuEntr;
    }

    // Getters et Setters

    public int getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(int idEntretien) {
        this.idEntretien = idEntretien;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public LocalDateTime getDateEntreeEntr() {
        return dateEntreeEntr;
    }

    public void setDateEntreeEntr(LocalDateTime dateEntreeEntr) {
        this.dateEntreeEntr = dateEntreeEntr;
    }

    public LocalDateTime getDateSortieEntr() {
        return dateSortieEntr;
    }

    public void setDateSortieEntr(LocalDateTime dateSortieEntr) {
        this.dateSortieEntr = dateSortieEntr;
    }

    public String getMotifEntr() {
        return motifEntr;
    }

    public void setMotifEntr(String motifEntr) {
        this.motifEntr = motifEntr;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getCoutEntr() {
        return coutEntr;
    }

    public void setCoutEntr(int coutEntr) {
        this.coutEntr = coutEntr;
    }

    public String getLieuEntr() {
        return lieuEntr;
    }

    public void setLieuEntr(String lieuEntr) {
        this.lieuEntr = lieuEntr;
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
     * Vérifie si l'entretien est en cours
     * @return true si l'entretien est en cours, false s'il est terminé
     */
    public boolean isEnCours() {
        return dateSortieEntr == null;
    }

    /**
     * Calcule la durée de l'entretien en jours
     * @return Durée en jours ou 0 si l'entretien n'est pas terminé
     */
    public long getDureeJours() {
        if (dateEntreeEntr != null && dateSortieEntr != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(dateEntreeEntr, dateSortieEntr);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Entretien #" + idEntretien + " - " + motifEntr;
    }
}