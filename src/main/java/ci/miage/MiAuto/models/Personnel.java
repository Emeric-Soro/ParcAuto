package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant un membre du personnel
 */
public class Personnel {
    private int idPersonnel;
    private int idVehicule;
    private int idFonction;
    private int idService;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String contactPersonnel;
    private LocalDateTime dateAttribution;
    private String emailPersonnel;
    private String adressePersonnel;
    private LocalDateTime dateEmbauche;

    // Relations
    private Vehicule vehicule;
    private Fonction fonction;
    private Service service;

    // Constructeur par défaut
    public Personnel() {
    }

    // Constructeur avec les champs obligatoires
    public Personnel(int idVehicule, int idFonction, int idService,
                     String nomPersonnel, String prenomPersonnel) {
        this.idVehicule = idVehicule;
        this.idFonction = idFonction;
        this.idService = idService;
        this.nomPersonnel = nomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
    }

    // Constructeur complet
    public Personnel(int idPersonnel, int idVehicule, int idFonction, int idService,
                     String nomPersonnel, String prenomPersonnel, String contactPersonnel,
                     LocalDateTime dateAttribution, String emailPersonnel,
                     String adressePersonnel, LocalDateTime dateEmbauche) {
        this.idPersonnel = idPersonnel;
        this.idVehicule = idVehicule;
        this.idFonction = idFonction;
        this.idService = idService;
        this.nomPersonnel = nomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.contactPersonnel = contactPersonnel;
        this.dateAttribution = dateAttribution;
        this.emailPersonnel = emailPersonnel;
        this.adressePersonnel = adressePersonnel;
        this.dateEmbauche = dateEmbauche;
    }

    // Getters et Setters
    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomPersonnel() {
        return nomPersonnel;
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    public String getPrenomPersonnel() {
        return prenomPersonnel;
    }

    public void setPrenomPersonnel(String prenomPersonnel) {
        this.prenomPersonnel = prenomPersonnel;
    }

    public String getContactPersonnel() {
        return contactPersonnel;
    }

    public void setContactPersonnel(String contactPersonnel) {
        this.contactPersonnel = contactPersonnel;
    }

    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public String getEmailPersonnel() {
        return emailPersonnel;
    }

    public void setEmailPersonnel(String emailPersonnel) {
        this.emailPersonnel = emailPersonnel;
    }

    public String getAdressePersonnel() {
        return adressePersonnel;
    }

    public void setAdressePersonnel(String adressePersonnel) {
        this.adressePersonnel = adressePersonnel;
    }

    public LocalDateTime getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDateTime dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
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

    public Fonction getFonction() {
        return fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
        if (fonction != null) {
            this.idFonction = fonction.getIdFonction();
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
        if (service != null) {
            this.idService = service.getIdService();
        }
    }

    @Override
    public String toString() {
        return nomPersonnel + " " + prenomPersonnel;
    }

    public String getNomComplet() {
        return prenomPersonnel + " " + nomPersonnel;
    }
}