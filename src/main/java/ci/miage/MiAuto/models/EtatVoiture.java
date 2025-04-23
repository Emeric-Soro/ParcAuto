package main.java.ci.miage.MiAuto.models;

/**
 * Modèle représentant l'état d'un véhicule (disponible, en mission, hors service, etc.)
 */
public class EtatVoiture {
    private int idEtatVoiture;
    private String libEtatVoiture;

    // Constantes pour les états prédéfinis
    public static final int DISPONIBLE = 1;
    public static final int EN_MISSION = 2;
    public static final int HORS_SERVICE = 3;
    public static final int EN_ENTRETIEN = 4;
    public static final int ATTRIBUER = 5;

    // Constructeur par défaut
    public EtatVoiture() {
    }

    // Constructeur avec les champs obligatoires
    public EtatVoiture(String libEtatVoiture) {
        this.libEtatVoiture = libEtatVoiture;
    }

    // Constructeur complet
    public EtatVoiture(int idEtatVoiture, String libEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
        this.libEtatVoiture = libEtatVoiture;
    }

    // Getters et Setters
    public int getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(int idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getLibEtatVoiture() {
        return libEtatVoiture;
    }

    public void setLibEtatVoiture(String libEtatVoiture) {
        this.libEtatVoiture = libEtatVoiture;
    }

    @Override
    public String toString() {
        return libEtatVoiture;
    }

    /**
     * Vérifie si l'état actuel permet d'attribuer une mission
     *
     * @return true si le véhicule est disponible pour une mission
     */
    public boolean peutPartirEnMission() {
        return idEtatVoiture == DISPONIBLE;
    }

    /**
     * Vérifie si l'état actuel permet d'effectuer un entretien
     *
     * @return true si le véhicule peut être mis en entretien
     */
    public boolean peutEtreEnEntretien() {
        return idEtatVoiture == DISPONIBLE || idEtatVoiture == HORS_SERVICE;
    }

    /**
     * Vérifie si le véhicule peut être attribué à un employé
     *
     * @return true si le véhicule peut être attribué
     */
    public boolean peutEtreAttribue() {
        return idEtatVoiture == DISPONIBLE;
    }
}