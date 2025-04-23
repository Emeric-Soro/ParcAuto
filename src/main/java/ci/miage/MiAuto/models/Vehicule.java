package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;


/**
 * Modèle représentant un véhicule du parc automobile
 */
public class Vehicule {
    private int idVehicule;
    private int idEtatVoiture;
    private String numeroChassi;
    private String immatriculation;
    private String marque;
    private String modele;
    private int nbPlaces;
    private String energie;
    private LocalDateTime dateAcquisition;
    private LocalDateTime dateAmmortissement;
    private LocalDateTime dateMiseEnService;
    private int puissance;
    private String couleur;
    private int prixVehicule;
    private LocalDateTime dateEtat;
    private int kilometrage;
    private LocalDateTime dateDerniereVisite;
    private LocalDateTime dateProchainVisite;
    private boolean statutAttribution;

    // État actuel du véhicule (relation)
    private EtatVoiture etatVoiture;

    // Constructeur par défaut
    public Vehicule() {
    }

    // Constructeur avec les champs obligatoires
    public Vehicule(int idEtatVoiture, String numeroChassi, String immatriculation,
                    String marque, String modele) {
        this.idEtatVoiture = idEtatVoiture;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
    }

    // Constructeur complet
    public Vehicule(int idVehicule, int idEtatVoiture, String numeroChassi, String immatriculation,
                    String marque, String modele, int nbPlaces, String energie,
                    LocalDateTime dateAcquisition, LocalDateTime dateAmmortissement,
                    LocalDateTime dateMiseEnService, int puissance, String couleur,
                    int prixVehicule, LocalDateTime dateEtat, int kilometrage,
                    LocalDateTime dateDerniereVisite, LocalDateTime dateProchainVisite,
                    boolean statutAttribution) {
        this.idVehicule = idVehicule;
        this.idEtatVoiture = idEtatVoiture;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.energie = energie;
        this.dateAcquisition = dateAcquisition;
        this.dateAmmortissement = dateAmmortissement;
        this.dateMiseEnService = dateMiseEnService;
        this.puissance = puissance;
        this.couleur = couleur;
        this.prixVehicule = prixVehicule;
        this.dateEtat = dateEtat;
        this.kilometrage = kilometrage;
        this.dateDerniereVisite = dateDerniereVisite;
        this.dateProchainVisite = dateProchainVisite;
        this.statutAttribution = statutAttribution;
    }

    // Getters et Setters
    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(int idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public LocalDateTime getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public LocalDateTime getDateAmmortissement() {
        return dateAmmortissement;
    }

    public void setDateAmmortissement(LocalDateTime dateAmmortissement) {
        this.dateAmmortissement = dateAmmortissement;
    }

    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getPrixVehicule() {
        return prixVehicule;
    }

    public void setPrixVehicule(int prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    public LocalDateTime getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDateTime dateEtat) {
        this.dateEtat = dateEtat;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    public LocalDateTime getDateDerniereVisite() {
        return dateDerniereVisite;
    }

    public void setDateDerniereVisite(LocalDateTime dateDerniereVisite) {
        this.dateDerniereVisite = dateDerniereVisite;
    }

    public LocalDateTime getDateProchainVisite() {
        return dateProchainVisite;
    }

    public void setDateProchainVisite(LocalDateTime dateProchainVisite) {
        this.dateProchainVisite = dateProchainVisite;
    }

    public boolean isStatutAttribution() {
        return statutAttribution;
    }

    public void setStatutAttribution(boolean statutAttribution) {
        this.statutAttribution = statutAttribution;
    }

    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
        if (etatVoiture != null) {
            this.idEtatVoiture = etatVoiture.getIdEtatVoiture();
        }
    }

    @Override
    public String toString() {
        return marque + " " + modele + " (" + immatriculation + ")";
    }
}
