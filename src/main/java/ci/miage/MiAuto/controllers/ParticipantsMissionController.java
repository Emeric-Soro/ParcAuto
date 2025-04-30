package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.services.MissionService;
import main.java.ci.miage.MiAuto.services.PersonnelService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion des participants d'une mission
 */
public class ParticipantsMissionController implements Initializable {

    @FXML
    private Label lblTitreMission;

    @FXML
    private ListView<Personnel> listParticipants;

    @FXML
    private ListView<Personnel> listPersonnelsDisponibles;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnRetirer;

    @FXML
    private Button btnFermer;

    private Mission mission;
    private MissionService missionService;
    private PersonnelService personnelService;
    private MissionController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.personnelService = new PersonnelService();

        // Ajouter des écouteurs de sélection pour activer/désactiver les boutons
        listParticipants.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> btnRetirer.setDisable(newValue == null));

        listPersonnelsDisponibles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> btnAjouter.setDisable(newValue == null));
    }

    /**
     * Définit la mission à gérer
     * @param mission Mission
     */
    public void setMission(Mission mission) {
        this.mission = mission;
        if (mission != null) {
            lblTitreMission.setText("Participants à la mission : " + mission.getLibMission());
            chargerParticipants();
            chargerPersonnelsDisponibles();
        }
    }

    /**
     * Définit le service de mission à utiliser
     * @param missionService Service de mission
     */
    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     * @param parentController Contrôleur parent
     */
    public void setParentController(MissionController parentController) {
        this.parentController = parentController;
    }

    /**
     * Charge la liste des participants à la mission
     */
    private void chargerParticipants() {
        if (mission != null) {
            List<Personnel> participants = missionService.getMissionParticipants(mission.getIdMission());
            mission.setParticipants(participants);
            listParticipants.setItems(FXCollections.observableArrayList(participants));
        }
    }

    /**
     * Charge la liste des personnels disponibles (non participants à la mission)
     */
    private void chargerPersonnelsDisponibles() {
        List<Personnel> tousPersonnels = personnelService.getAllPersonnels();

        // Filtrer les personnels qui ne sont pas déjà participants
        List<Personnel> personnelsDisponibles = tousPersonnels.stream()
                .filter(p -> !mission.getParticipants().contains(p))
                .collect(Collectors.toList());

        listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(personnelsDisponibles));
    }

    /**
     * Gère le clic sur le bouton de recherche
     */
    @FXML
    void handleRechercherButton(ActionEvent event) {
        String recherche = txtRecherche.getText().trim().toLowerCase();

        if (recherche.isEmpty()) {
            chargerPersonnelsDisponibles();
            return;
        }

        // Filtrer les personnels selon la recherche
        List<Personnel> tousPersonnels = personnelService.getAllPersonnels();
        List<Personnel> personnelsFiltres = tousPersonnels.stream()
                .filter(p -> !mission.getParticipants().contains(p))
                .filter(p -> p.getNomPersonnel().toLowerCase().contains(recherche) ||
                        p.getPrenomPersonnel().toLowerCase().contains(recherche))
                .collect(Collectors.toList());

        listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(personnelsFiltres));
    }

    /**
     * Gère le clic sur le bouton d'ajout d'un participant
     */
    @FXML
    void handleAjouterButton(ActionEvent event) {
        Personnel selectedPersonnel = listPersonnelsDisponibles.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            boolean success = missionService.addParticipant(mission.getIdMission(), selectedPersonnel.getIdPersonnel());
            if (success) {
                // Mettre à jour les listes
                chargerParticipants();
                chargerPersonnelsDisponibles();
                txtRecherche.clear();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter le participant à la mission.");
            }
        }
    }

    /**
     * Gère le clic sur le bouton de retrait d'un participant
     */
    @FXML
    void handleRetirerButton(ActionEvent event) {
        Personnel selectedPersonnel = listParticipants.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            boolean success = missionService.removeParticipant(mission.getIdMission(), selectedPersonnel.getIdPersonnel());
            if (success) {
                // Mettre à jour les listes
                chargerParticipants();
                chargerPersonnelsDisponibles();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de retirer le participant de la mission.");
            }
        }
    }

    /**
     * Gère le clic sur le bouton de fermeture
     */
    @FXML
    void handleFermerButton(ActionEvent event) {
        // Rafraîchir la liste des missions si le contrôleur parent existe
        if (parentController != null) {
            parentController.refreshMissionList();
        }

        // Fermer la fenêtre
        Stage stage = (Stage) btnFermer.getScene().getWindow();
        stage.close();
    }
}