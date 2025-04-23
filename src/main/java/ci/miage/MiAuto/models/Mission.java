package main.java.ci.miage.MiAuto.models;


import java.time.LocalDateTime;
import java.util.List;

/**
 * Modèle représentant une mission effectuée par un véhicule
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

    // Relations
    private Vehicule vehicule;
    private List<Personnel> participants;

    // Constructeur par défaut
    public Mission() {
    }

    // Constructeur avec les champs obligatoires
    public Mission(int idVehicule, String libMission, LocalDateTime dateDebutMission) {
        this.idVehicule = idVehicule;
        this.libMission = libMission;
        this.dateDebutMission = dateDebutMission;
    }

    // Constructeur complet
    public Mission(int idMission, int idVehicule, String libMission,
                   LocalDateTime dateDebutMission, LocalDateTime dateFinMission,
                   int coutMission, int coutCarburant, String observationMission,
                   String circuitMission) {
        this.idMission = idMission;
        this.idVehicule = idVehicule;
        this.libMission = libMission;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.coutMission = coutMission;
        this.coutCarburant = coutCarburant;
        this.observationMission = observationMission;
        this.circuitMission = circuitMission;
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

    public List<Personnel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Personnel> participants) {
        this.participants = participants;
    }

    /**
     * Calcule la durée de la mission en jours
     *
     * @return nombre de jours de la mission, ou 0 si la date de fin n'est pas définie
     */
    public long getDureeEnJours() {
        if (dateFinMission == null) {
            return 0;
        }

        // Calcul de la différence en jours
        return java.time.Duration.between(dateDebutMission, dateFinMission).toDays();
    }

    /**
     * Vérifie si la mission est terminée
     *
     * @return true si la mission est terminée, false sinon
     */
    public boolean isTerminee() {
        return dateFinMission != null && dateFinMission.isBefore(LocalDateTime.now());
    }

    /**
     * Calcule le coût total de la mission (mission + carburant)
     *
     * @return le coût total en FCFA
     */
    public int getCoutTotal() {
        return coutMission + coutCarburant;
    }

    @Override
    public String toString() {
        return libMission + " (" + (vehicule != null ? vehicule.getImmatriculation() : "Véhicule inconnu") + ")";
    }
}