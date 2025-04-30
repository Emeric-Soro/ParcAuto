package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant une mission
 */
public class Mission {
    private int idMission;
    private int idVehicule;
    private String libMission;
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission;
    private int coutMission;
    private int coutCarburant;
    private String observationMission;
    private String circuitMission;

    // Relation avec le véhicule (à charger depuis la base)
    private Vehicule vehicule;

    // Relation avec les participants (à charger depuis la base)
    private java.util.List<Personnel> participants;

    // Constructeur par défaut
    public Mission() {
        this.participants = new java.util.ArrayList<>();
    }

    // Constructeur avec champs obligatoires
    public Mission(int idVehicule, String libMission, LocalDateTime dateDebutMission) {
        this.idVehicule = idVehicule;
        this.libMission = libMission;
        this.dateDebutMission = dateDebutMission;
        this.participants = new java.util.ArrayList<>();
    }

    // Constructeur complet
    public Mission(int idMission, int idVehicule, String libMission, LocalDateTime dateDebutMission,
                   LocalDateTime dateFinMission, int coutMission, int coutCarburant,
                   String observationMission, String circuitMission) {
        this.idMission = idMission;
        this.idVehicule = idVehicule;
        this.libMission = libMission;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.coutMission = coutMission;
        this.coutCarburant = coutCarburant;
        this.observationMission = observationMission;
        this.circuitMission = circuitMission;
        this.participants = new java.util.ArrayList<>();
    }

    // Getters et Setters
    public int getIdMission() {
        return idMission;
    }

    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getLibMission() {
        return libMission;
    }

    public void setLibMission(String libMission) {
        this.libMission = libMission;
    }

    public LocalDateTime getDateDebutMission() {
        return dateDebutMission;
    }

    public void setDateDebutMission(LocalDateTime dateDebutMission) {
        this.dateDebutMission = dateDebutMission;
    }

    public LocalDateTime getDateFinMission() {
        return dateFinMission;
    }

    public void setDateFinMission(LocalDateTime dateFinMission) {
        this.dateFinMission = dateFinMission;
    }

    public int getCoutMission() {
        return coutMission;
    }

    public void setCoutMission(int coutMission) {
        this.coutMission = coutMission;
    }

    public int getCoutCarburant() {
        return coutCarburant;
    }

    public void setCoutCarburant(int coutCarburant) {
        this.coutCarburant = coutCarburant;
    }

    public String getObservationMission() {
        return observationMission;
    }

    public void setObservationMission(String observationMission) {
        this.observationMission = observationMission;
    }

    public String getCircuitMission() {
        return circuitMission;
    }

    public void setCircuitMission(String circuitMission) {
        this.circuitMission = circuitMission;
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

    public java.util.List<Personnel> getParticipants() {
        return participants;
    }

    public void setParticipants(java.util.List<Personnel> participants) {
        this.participants = participants;
    }

    /**
     * Ajoute un participant à la mission
     * @param personnel Personnel à ajouter
     */
    public void addParticipant(Personnel personnel) {
        if (!this.participants.contains(personnel)) {
            this.participants.add(personnel);
        }
    }

    /**
     * Supprime un participant de la mission
     * @param personnel Personnel à supprimer
     */
    public void removeParticipant(Personnel personnel) {
        this.participants.remove(personnel);
    }

    /**
     * Calcule la durée de la mission en jours
     * @return Durée en jours (0 si la date de fin n'est pas définie)
     */
    public long getDureeMission() {
        if (dateDebutMission != null && dateFinMission != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(dateDebutMission, dateFinMission);
        }
        return 0;
    }

    /**
     * Vérifie si la mission est en cours
     * @return true si la mission est en cours, false sinon
     */
    public boolean isEnCours() {
        LocalDateTime now = LocalDateTime.now();
        return dateDebutMission != null &&
                (dateDebutMission.isBefore(now) || dateDebutMission.isEqual(now)) &&
                (dateFinMission == null || dateFinMission.isAfter(now));
    }

    @Override
    public String toString() {
        return libMission;
    }
}