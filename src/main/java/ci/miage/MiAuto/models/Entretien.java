package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

public class Entretien {
    private int idEntretien;
    private int idVehicule;
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private Integer coutEntr;
    private String lieuEntr;

    public Entretien() {
    }

    public Entretien(int idEntretien, LocalDateTime dateSortieEntr, LocalDateTime dateEntreeEntr, int idVehicule, String motifEntr, Integer coutEntr, String observation, String lieuEntr) {
        this.idEntretien = idEntretien;
        this.dateSortieEntr = dateSortieEntr;
        this.dateEntreeEntr = dateEntreeEntr;
        this.idVehicule = idVehicule;
        this.motifEntr = motifEntr;
        this.coutEntr = coutEntr;
        this.observation = observation;
        this.lieuEntr = lieuEntr;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getLieuEntr() {
        return lieuEntr;
    }

    public void setLieuEntr(String lieuEntr) {
        this.lieuEntr = lieuEntr;
    }

    public Integer getCoutEntr() {
        return coutEntr;
    }

    public void setCoutEntr(Integer coutEntr) {
        this.coutEntr = coutEntr;
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

    public LocalDateTime getDateEntreeEntr() {
        return dateEntreeEntr;
    }

    public void setDateEntreeEntr(LocalDateTime dateEntreeEntr) {
        this.dateEntreeEntr = dateEntreeEntr;
    }

    public int getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(int idEntretien) {
        this.idEntretien = idEntretien;
    }

    @Override
    public String toString() {
        return "Entretien{" +
                "idEntretien=" + idEntretien +
                ", idVehicule=" + idVehicule +
                ", dateEntreeEntr=" + dateEntreeEntr +
                ", dateSortieEntr=" + dateSortieEntr +
                ", motifEntr='" + motifEntr + '\'' +
                ", observation='" + observation + '\'' +
                ", coutEntr=" + coutEntr +
                ", lieuEntr='" + lieuEntr + '\'' +
                '}';
    }
}
