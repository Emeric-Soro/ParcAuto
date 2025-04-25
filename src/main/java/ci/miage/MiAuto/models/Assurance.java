package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

public class Assurance {
    private int numCarteAssurance;
    private LocalDateTime dateDebutAssurance;
    private LocalDateTime dateFinAssurance;
    private String agence;
    private int coutAssurance;

    public Assurance() {
    }

    public Assurance(int numCarteAssurance, LocalDateTime dateDebutAssurance, LocalDateTime dateFinAssurance, String agence, int coutAssurance) {
        this.numCarteAssurance = numCarteAssurance;
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateFinAssurance = dateFinAssurance;
        this.agence = agence;
        this.coutAssurance = coutAssurance;
    }

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

    @Override
    public String toString() {
        return "Assurance{" +
                "numCarteAssurance=" + numCarteAssurance +
                ", dateDebutAssurance=" + dateDebutAssurance +
                ", dateFinAssurance=" + dateFinAssurance +
                ", agence='" + agence + '\'' +
                ", coutAssurance=" + coutAssurance +
                '}';
    }


}
