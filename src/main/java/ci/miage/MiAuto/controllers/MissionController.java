package main.java.ci.miage.MiAuto.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.services.MissionService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.DateUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MissionController implements Initializable {

    @FXML
    private ComboBox<String> comboFiltre;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private Button btnFiltrer;

    @FXML
    private Button btnReinitialiser;

    @FXML
    private TableView<Mission> tableMissions;

    @FXML
    private TableColumn<Mission, Integer> colId;

    @FXML
    private TableColumn<Mission, String> colLibelle;

    @FXML
    private TableColumn<Mission, String> colVehicule;

    @FXML
    private TableColumn<Mission, String> colDateDebut;

    @FXML
    private TableColumn<Mission, String> colDateFin;

    @FXML
    private TableColumn<Mission, String> colStatut;

    @FXML
    private TableColumn<Mission, String> colCout;

    @FXML
    private TableColumn<Mission, String> colParticipants;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnTerminer;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnGererParticipants;

    private MissionService missionService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        missionService = new MissionService();

        // Initialiser le ComboBox de filtre
        comboFiltre.setItems(FXCollections.observableArrayList(
                "Toutes", "En cours", "À venir", "Terminées"));
        comboFiltre.getSelectionModel().selectFirst();

        // Configurer les colonnes
        configureColumns();

        // Charger les données
        loadMissions();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableMissions.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean hasSelection = (newValue != null);
                    btnModifier.setDisable(!hasSelection);
                    btnSupprimer.setDisable(!hasSelection);
                    btnGererParticipants.setDisable(!hasSelection);

                    // Le bouton Terminer est actif seulement pour les missions en cours
                    if (hasSelection) {
                        btnTerminer.setDisable(!newValue.isEnCours() ||
                                newValue.getDateFinMission() != null);
                    } else {
                        btnTerminer.setDisable(true);
                    }
                });
    }

    /**
     * Configure les colonnes de la TableView
     */
    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMission"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libMission"));

        // Afficher les informations du véhicule
        colVehicule.setCellValueFactory(cellData -> {
            if (cellData.getValue().getVehicule() != null) {
                return new SimpleStringProperty(cellData.getValue().getVehicule().getMarque() + " " +
                        cellData.getValue().getVehicule().getModele() + " (" +
                        cellData.getValue().getVehicule().getImmatriculation() + ")");
            } else {
                return new SimpleStringProperty("Non défini");
            }
        });

        // Formater les dates
        colDateDebut.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateDebutMission();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        colDateFin.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateFinMission();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        // Déterminer le statut de la mission
        colStatut.setCellValueFactory(cellData -> {
            Mission mission = cellData.getValue();
            LocalDateTime now = LocalDateTime.now();

            if (mission.getDateFinMission() != null && mission.getDateFinMission().isBefore(now)) {
                return new SimpleStringProperty("Terminée");
            } else if (mission.getDateDebutMission() != null && mission.getDateDebutMission().isAfter(now)) {
                return new SimpleStringProperty("À venir");
            } else {
                return new SimpleStringProperty("En cours");
            }
        });

        // Afficher le coût total (mission + carburant)
        colCout.setCellValueFactory(cellData -> {
            Mission mission = cellData.getValue();
            int coutTotal = mission.getCoutMission() + mission.getCoutCarburant();
            return new SimpleStringProperty(String.format("%,d FCFA", coutTotal));
        });

        // Afficher le nombre de participants
        colParticipants.setCellValueFactory(cellData -> {
            List<Personnel> participants = cellData.getValue().getParticipants();
            if (participants != null) {
                return new SimpleStringProperty(String.valueOf(participants.size()));
            } else {
                return new SimpleStringProperty("0");
            }
        });
    }

    /**
     * Charge les missions selon le filtre sélectionné
     */
    private void loadMissions() {
        String filtre = comboFiltre.getValue();
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();

        List<Mission> missions;

        if (debut != null && fin != null) {
            // Filtrer par période
            missions = missionService.getMissionsByPeriode(
                    debut.atStartOfDay(),
                    fin.atTime(23, 59, 59)
            );
        } else if ("En cours".equals(filtre)) {
            missions = missionService.getMissionsEnCours();
        } else if ("À venir".equals(filtre)) {
            missions = missionService.getMissionsAVenir();
        } else if ("Terminées".equals(filtre)) {
            missions = missionService.getMissionsTerminees();
        } else {
            // Par défaut, charger toutes les missions
            missions = missionService.getAllMissions();
        }

        tableMissions.setItems(FXCollections.observableArrayList(missions));
    }

    @FXML
    void handleFiltrerButton(ActionEvent event) {
        loadMissions();
    }

    @FXML
    void handleReinitialiserButton(ActionEvent event) {
        comboFiltre.getSelectionModel().selectFirst();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        loadMissions();
    }

    @FXML
    void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireMission(null);
    }

    @FXML
    void handleModifierButton(ActionEvent event) {
        Mission selectedMission = tableMissions.getSelectionModel().getSelectedItem();
        if (selectedMission != null) {
            // Vérifier si la mission est terminée
            if (selectedMission.getDateFinMission() != null &&
                    selectedMission.getDateFinMission().isBefore(LocalDateTime.now())) {
                AlertUtils.showWarningAlert("Modification impossible",
                        "Cette mission est déjà terminée et ne peut plus être modifiée.");
                return;
            }

            ouvrirFormulaireMission(selectedMission);
        }
    }

    @FXML
    void handleTerminerButton(ActionEvent event) {
        Mission selectedMission = tableMissions.getSelectionModel().getSelectedItem();
        if (selectedMission != null && selectedMission.isEnCours()) {
            ouvrirFormulaireTerminerMission(selectedMission);
        }
    }

    @FXML
    void handleSupprimerButton(ActionEvent event) {
        Mission selectedMission = tableMissions.getSelectionModel().getSelectedItem();
        if (selectedMission != null) {
            // Vérifier si la mission est terminée
            if (selectedMission.getDateFinMission() == null ||
                    selectedMission.getDateFinMission().isAfter(LocalDateTime.now())) {
                AlertUtils.showWarningAlert("Suppression impossible",
                        "Seules les missions terminées peuvent être supprimées.");
                return;
            }

            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer cette mission ?",
                    "Mission: " + selectedMission.getLibMission());

            if (confirmation) {
                boolean success = missionService.deleteMission(selectedMission.getIdMission());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "La mission a été supprimée avec succès.");
                    loadMissions();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer la mission.");
                }
            }
        }
    }

    @FXML
    void handleGererParticipantsButton(ActionEvent event) {
        Mission selectedMission = tableMissions.getSelectionModel().getSelectedItem();
        if (selectedMission != null) {
            ouvrirFormulaireParticipants(selectedMission);
        }
    }

    /**
     * Ouvre le formulaire d'ajout/modification de mission
     * @param mission Mission à modifier (null pour un ajout)
     */
    private void ouvrirFormulaireMission(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/mission/form_mission.fxml"));
            Parent root = loader.load();

            FormMissionController controller = loader.getController();
            controller.setMissionService(missionService);
            controller.setMission(mission);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(mission == null ? "Ajouter une mission" : "Modifier une mission");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de mission.");
        }
    }

    /**
     * Ouvre le formulaire pour terminer une mission
     * @param mission Mission à terminer
     */
    private void ouvrirFormulaireTerminerMission(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/mission/terminer_mission.fxml"));
            Parent root = loader.load();

            TerminerMissionController controller = loader.getController();
            controller.setMissionService(missionService);
            controller.setMission(mission);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Terminer la mission");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnTerminer.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de fin de mission.");
        }
    }

    /**
     * Ouvre le formulaire de gestion des participants
     * @param mission Mission concernée
     */
    private void ouvrirFormulaireParticipants(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/mission/participants_mission.fxml"));
            Parent root = loader.load();

            ParticipantsMissionController controller = loader.getController();
            controller.setMissionService(missionService);
            controller.setMission(mission);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Gestion des participants");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnGererParticipants.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de gestion des participants.");
        }
    }

    /**
     * Rafraîchit la liste des missions
     * Cette méthode est appelée après une modification
     */
    public void refreshMissionList() {
        loadMissions();
    }
}