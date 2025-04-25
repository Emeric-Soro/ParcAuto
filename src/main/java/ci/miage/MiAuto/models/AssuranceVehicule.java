package main.java.ci.miage.MiAuto.models;

/**
 * Classe représentant l'association entre une assurance et un véhicule
 */
public class AssuranceVehicule {
    private int idVehicule;
    private int numCarteAssurance;

    // Optionnel: références aux objets complets
    private Vehicule vehicule;
    private Assurance assurance;

    public AssuranceVehicule() {
    }

    public AssuranceVehicule(int idVehicule, int numCarteAssurance) {
        this.idVehicule = idVehicule;
        this.numCarteAssurance = numCarteAssurance;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(int numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
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

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
        if (assurance != null) {
            this.numCarteAssurance = assurance.getNumCarteAssurance();
        }
    }
}