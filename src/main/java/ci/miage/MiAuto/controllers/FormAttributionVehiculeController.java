package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FormAttributionVehiculeController implements Initializable {

    @FXML
    private Label lblPersonnel;

    @FXML
    private ComboBox<VehiculeWrapper> comboVehicule;

    @FXML
    private Button btnAttribuer;

    @FXML
    private Button btnAnnuler;

    private Personnel personnel;
    private VehiculeService vehiculeService;
    private boolean valide = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vehiculeService = new VehiculeService();
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
        if (personnel != null) {
            lblPersonnel.setText("Personnel: " + personnel.getNomPersonnel() + " " + personnel.getPrenomPersonnel());
            chargerVehicules();
        }
    }

    private void chargerVehicules() {
        ObservableList<VehiculeWrapper> vehicules = FXCollections.observableArrayList();

        // Ajouter l'option "Aucun" pour retirer l'attribution
        vehicules.add(new VehiculeWrapper(null, "Aucun"));

        // Charger les véhicules disponibles
        List<Vehicule> vehiculesDisponibles = vehiculeService.getVehiculesDisponibles();
        for (Vehicule v : vehiculesDisponibles) {
            vehicules.add(new VehiculeWrapper(v, formatVehicule(v)));
        }

        comboVehicule.setItems(vehicules);

        // Sélectionner le véhicule actuel si disponible
        if (personnel.getIdVehicule() > 0) {
            Vehicule vehiculeActuel = vehiculeService.getVehiculeById(personnel.getIdVehicule());
            if (vehiculeActuel != null) {
                vehicules.add(new VehiculeWrapper(vehiculeActuel, formatVehicule(vehiculeActuel)));
                comboVehicule.setValue(new VehiculeWrapper(vehiculeActuel, formatVehicule(vehiculeActuel)));
            }
        } else {
            comboVehicule.setValue(vehicules.get(0)); // Sélectionner "Aucun"
        }
    }

    private String formatVehicule(Vehicule v) {
        return v.getMarque() + " " + v.getModele() + " - " + v.getImmatriculation();
    }

    @FXML
    void handleAttribuerButton(ActionEvent event) {
        VehiculeWrapper selection = comboVehicule.getValue();
        if (selection != null) {
            int nouveauIdVehicule = selection.getVehicule() != null ? selection.getVehicule().getIdVehicule() : 0;

            boolean success = vehiculeService.attribuerVehicule(nouveauIdVehicule, personnel.getIdPersonnel());
            if (success) {
                valide = true;
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible d'attribuer le véhicule.");
            }
        }
    }

    @FXML
    void handleAnnulerButton(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    public boolean isValide() {
        return valide;
    }

    // Classe interne pour wrapper les véhicules dans le ComboBox
    private static class VehiculeWrapper {
        private final Vehicule vehicule;
        private final String display;

        public VehiculeWrapper(Vehicule vehicule, String display) {
            this.vehicule = vehicule;
            this.display = display;
        }

        public Vehicule getVehicule() {
            return vehicule;
        }

        @Override
        public String toString() {
            return display;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            VehiculeWrapper that = (VehiculeWrapper) obj;

            // Si les deux véhicules sont null, ils sont égaux (option "Aucun")
            if (this.vehicule == null && that.vehicule == null) return true;

            // Si l'un est null mais pas l'autre, ils sont différents
            if (this.vehicule == null || that.vehicule == null) return false;

            // Sinon, comparer les IDs
            return this.vehicule.getIdVehicule() == that.vehicule.getIdVehicule();
        }
    }
}