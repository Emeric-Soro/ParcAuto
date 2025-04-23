package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

public class VisiteTechnique {
    private int idVisite;
    private int idVehicule;
    private LocalDateTime dateVisite;
    private LocalDateTime dateExpiration;
    private String resultat;
    private Integer cout;
    private String centreVisite;
    private String observations;

    public VisiteTechnique() {
    }

    public VisiteTechnique(int idVisite, LocalDateTime dateVisite, int idVehicule, LocalDateTime dateExpiration, String resultat, Integer cout, String centreVisite, String observations) {
        this.idVisite = idVisite;
        this.dateVisite = dateVisite;
        this.idVehicule = idVehicule;
        this.dateExpiration = dateExpiration;
        this.resultat = resultat;
        this.cout = cout;
        this.centreVisite = centreVisite;
        this.observations = observations;
    }

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

    public Integer getCout() {
        return cout;
    }

    public void setCout(Integer cout) {
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

    @Override
    public String toString() {
        return "VisiteTechnique{" +
                "idVisite=" + idVisite +
                ", idVehicule=" + idVehicule +
                ", dateVisite=" + dateVisite +
                ", dateExpiration=" + dateExpiration +
                ", resultat='" + resultat + '\'' +
                ", cout=" + cout +
                ", centreVisite='" + centreVisite + '\'' +
                ", observations='" + observations + '\'' +
                '}';
    }
}
