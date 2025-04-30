package main.java.ci.miage.MiAuto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.MissionService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le formulaire de fin de mission
 */
public class TerminerMissionController implements Initializable {

    @FXML
    private Label lblMission;

    @FXML
    private Label lblVehicule;

    @FXML
    private Label lblDateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField txtCoutMission;

    @FXML
    private TextField txtCoutCarburant;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private TextArea txtObservations;

    @FXML
    private Button btnTerminer;

    @FXML
    private Button btnAnnuler;

    private MissionService missionService;
    private Mission mission;
    private MissionController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser la date de fin à aujourd'hui
        dateFin.setValue(LocalDate.now());

        // Ajouter des validations pour les champs numériques
        txtCoutMission.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCoutMission.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtCoutCarburant.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCoutCarburant.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtKilometrage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtKilometrage.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Définit le service de mission à utiliser
     * @param missionService Service de mission
     */
    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    /**
     * Définit la mission à terminer
     * @param mission Mission à terminer
     */
    public void setMission(Mission mission) {
        this.mission = mission;
        if (mission != null) {
            remplirFormulaire();
        }
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     * @param parentController Contrôleur parent
     */
    public void setParentController(MissionController parentController) {
        this.parentController = parentController;
    }

    /**
     * Remplit le formulaire avec les données de la mission
     */
    private void remplirFormulaire() {
        if (mission == null) return;

        lblMission.setText(mission.getLibMission());

        Vehicule vehicule = mission.getVehicule();
        if (vehicule != null) {
            lblVehicule.setText(vehicule.getMarque() + " " + vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")");

            // Préremplir le kilométrage avec le kilométrage actuel + une estimation
            long dureeMission = mission.getDureeMission();
            int kilometrageEstime = vehicule.getKilometrage() + (int)(dureeMission * 50); // Estimation de 50 km par jour
            txtKilometrage.setText(String.valueOf(kilometrageEstime));
        } else {
            lblVehicule.setText("Non défini");
        }

        if (mission.getDateDebutMission() != null) {
            lblDateDebut.setText(mission.getDateDebutMission().toLocalDate().toString());
        } else {
            lblDateDebut.setText("Non définie");
        }

        // Utiliser les observations existantes si présentes
        if (mission.getObservationMission() != null && !mission.getObservationMission().isEmpty()) {
            txtObservations.setText(mission.getObservationMission());
        }
    }

    /**
     * Valide les champs du formulaire
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (dateFin.getValue() == null) {
            erreurs.append("- La date de fin est obligatoire\n");
        }

        if (txtKilometrage.getText().trim().isEmpty()) {
            erreurs.append("- Le kilométrage est obligatoire\n");
        } else {
            try {
                int kilometrage = Integer.parseInt(txtKilometrage.getText().trim());

                // Vérifier que le kilométrage est supérieur au kilométrage actuel
                if (mission.getVehicule() != null && kilometrage <= mission.getVehicule().getKilometrage()) {
                    erreurs.append("- Le nouveau kilométrage doit être supérieur au kilométrage actuel du véhicule\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le kilométrage doit être un nombre entier\n");
            }
        }

        // Vérifier la cohérence des dates
        if (dateFin.getValue() != null && mission.getDateDebutMission() != null) {
            if (dateFin.getValue().isBefore(mission.getDateDebutMission().toLocalDate())) {
                erreurs.append("- La date de fin ne peut pas être antérieure à la date de début\n");
            }
        }

        // S'il y a des erreurs, les afficher
        if (erreurs.length() > 0) {
            AlertUtils.showErrorAlert("Validation du formulaire",
                    "Veuillez corriger les erreurs suivantes :", erreurs.toString());
            return false;
        }

        return true;
    }

    /**
     * Gère le clic sur le bouton de terminaison
     */
    @FXML
    void handleTerminerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        // Récupérer les valeurs du formulaire
        LocalDateTime dateFinMission = dateFin.getValue().atTime(LocalTime.MAX);

        int coutMission = 0;
        if (!txtCoutMission.getText().trim().isEmpty()) {
            coutMission = Integer.parseInt(txtCoutMission.getText().trim());
        }

        int coutCarburant = 0;
        if (!txtCoutCarburant.getText().trim().isEmpty()) {
            coutCarburant = Integer.parseInt(txtCoutCarburant.getText().trim());
        }

        int kilometrage = Integer.parseInt(txtKilometrage.getText().trim());

        String observations = txtObservations.getText().trim();

        // Terminer la mission
        boolean success = missionService.terminerMission(
                mission.getIdMission(),
                dateFinMission,
                coutMission,
                coutCarburant,
                observations,
                kilometrage
        );

        if (success) {
            AlertUtils.showInformationAlert("Succès", "La mission a été terminée avec succès.");

            if (parentController != null) {
                parentController.refreshMissionList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible de terminer la mission.");
        }
    }

    /**
     * Gère le clic sur le bouton d'annulation
     */
    @FXML
    void handleAnnulerButton(ActionEvent event) {
        fermerFenetre();
    }

    /**
     * Ferme la fenêtre courante
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}