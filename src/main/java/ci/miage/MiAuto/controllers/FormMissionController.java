package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.MissionService;
import main.java.ci.miage.MiAuto.services.PersonnelService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur pour le formulaire d'ajout/modification de mission
 */
public class FormMissionController implements Initializable {

    @FXML
    private TextField txtLibelle;

    @FXML
    private ComboBox<Vehicule> comboVehicule;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextArea txtCircuit;

    @FXML
    private TextArea txtObservations;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    // Nouveaux éléments UI pour les participants
    @FXML
    private TextField txtRecherchePersonnel;

    @FXML
    private ListView<Personnel> listPersonnelsDisponibles;

    @FXML
    private ListView<Personnel> listParticipants;

    @FXML
    private Button btnAjouterParticipant;

    @FXML
    private Button btnRetirerParticipant;

    private MissionService missionService;
    private VehiculeService vehiculeService;
    private PersonnelService personnelService;
    private Mission mission;
    private MissionController parentController;
    private boolean modeEdition = false;

    // Liste des participants sélectionnés
    private List<Personnel> participantsSelectionnes = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.vehiculeService = new VehiculeService();
        this.personnelService = new PersonnelService();

        // Charger les véhicules disponibles
        chargerVehicules();

        // Charger les personnels disponibles
        chargerPersonnelsDisponibles();

        // Configuration du ComboBox pour l'affichage des véhicules
        comboVehicule.setCellFactory(param -> new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMarque() + " " + item.getModele() + " (" + item.getImmatriculation() + ")");
                }
            }
        });

        comboVehicule.setButtonCell(new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMarque() + " " + item.getModele() + " (" + item.getImmatriculation() + ")");
                }
            }
        });

        // Configuration des cellules pour l'affichage des personnels
        listPersonnelsDisponibles.setCellFactory(param -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenomPersonnel() + " " + item.getNomPersonnel());
                }
            }
        });

        listParticipants.setCellFactory(param -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenomPersonnel() + " " + item.getNomPersonnel());
                }
            }
        });

        // Ajouter des écouteurs de sélection pour activer/désactiver les boutons
        listPersonnelsDisponibles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> btnAjouterParticipant.setDisable(newValue == null));

        listParticipants.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> btnRetirerParticipant.setDisable(newValue == null));

        // Ajouter un écouteur pour la recherche de personnel
        txtRecherchePersonnel.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherPersonnel(newValue);
        });

        // Désactiver les boutons par défaut
        btnAjouterParticipant.setDisable(true);
        btnRetirerParticipant.setDisable(true);
    }

    /**
     * Définit le service de mission à utiliser
     * @param missionService Service de mission
     */
    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    /**
     * Définit la mission à modifier (ou null pour un ajout)
     * @param mission Mission à modifier
     */
    public void setMission(Mission mission) {
        this.mission = mission;
        modeEdition = (mission != null);

        if (modeEdition) {
            remplirFormulaire();
        } else {
            // Valeurs par défaut pour une nouvelle mission
            dateDebut.setValue(LocalDate.now());

            // Initialiser une nouvelle mission
            this.mission = new Mission();
            this.mission.setParticipants(new ArrayList<>());
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
     * Charge les véhicules disponibles dans le ComboBox
     */
    private void chargerVehicules() {
        List<Vehicule> vehicules;

        if (modeEdition && mission != null) {
            // En mode édition, inclure tous les véhicules disponibles + le véhicule actuel de la mission
            vehicules = vehiculeService.getAllVehicules();
        } else {
            // En mode ajout, uniquement les véhicules disponibles
            vehicules = vehiculeService.getVehiculesDisponibles();
        }

        comboVehicule.setItems(FXCollections.observableArrayList(vehicules));
    }

    /**
     * Charge tous les personnels disponibles dans la ListView
     */
    private void chargerPersonnelsDisponibles() {
        List<Personnel> personnels = personnelService.getAllPersonnels();

        // En mode édition, exclure les personnels déjà participants
        if (modeEdition && mission != null && mission.getParticipants() != null) {
            participantsSelectionnes = new ArrayList<>(mission.getParticipants());
            personnels = personnels.stream()
                    .filter(p -> !participantsSelectionnes.contains(p))
                    .collect(Collectors.toList());
        }

        listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(personnels));
        listParticipants.setItems(FXCollections.observableArrayList(participantsSelectionnes));
    }

    /**
     * Recherche des personnels en fonction du texte saisi
     * @param texte Texte de recherche
     */
    private void rechercherPersonnel(String texte) {
        if (texte == null || texte.isEmpty()) {
            // Si le texte est vide, afficher tous les personnels disponibles
            chargerPersonnelsDisponibles();
            return;
        }

        String recherche = texte.toLowerCase();
        List<Personnel> tousPersonnels = personnelService.getAllPersonnels();

        // Filtrer les personnels selon la recherche et exclure ceux déjà sélectionnés
        List<Personnel> personnelsFiltres = tousPersonnels.stream()
                .filter(p -> !participantsSelectionnes.contains(p))
                .filter(p -> p.getNomPersonnel().toLowerCase().contains(recherche) ||
                        p.getPrenomPersonnel().toLowerCase().contains(recherche))
                .collect(Collectors.toList());

        listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(personnelsFiltres));
    }

    /**
     * Remplit le formulaire avec les données de la mission à modifier
     */
    private void remplirFormulaire() {
        if (mission == null) return;

        txtLibelle.setText(mission.getLibMission());

        // Sélectionner le véhicule de la mission
        for (Vehicule vehicule : comboVehicule.getItems()) {
            if (vehicule.getIdVehicule() == mission.getIdVehicule()) {
                comboVehicule.getSelectionModel().select(vehicule);
                break;
            }
        }

        // Définir les dates
        if (mission.getDateDebutMission() != null) {
            dateDebut.setValue(mission.getDateDebutMission().toLocalDate());
        }

        if (mission.getDateFinMission() != null) {
            dateFin.setValue(mission.getDateFinMission().toLocalDate());
        }

        txtCircuit.setText(mission.getCircuitMission());
        txtObservations.setText(mission.getObservationMission());

        // Charger les participants
        if (mission.getParticipants() == null) {
            mission.setParticipants(missionService.getMissionParticipants(mission.getIdMission()));
        }

        participantsSelectionnes = new ArrayList<>(mission.getParticipants());
        listParticipants.setItems(FXCollections.observableArrayList(participantsSelectionnes));
    }

    /**
     * Valide les champs du formulaire
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (txtLibelle.getText().trim().isEmpty()) {
            erreurs.append("- Le libellé de la mission est obligatoire\n");
        }

        if (comboVehicule.getSelectionModel().getSelectedItem() == null) {
            erreurs.append("- Un véhicule doit être sélectionné\n");
        }

        if (dateDebut.getValue() == null) {
            erreurs.append("- La date de début est obligatoire\n");
        }

        // Vérifier la cohérence des dates
        if (dateDebut.getValue() != null && dateFin.getValue() != null) {
            if (dateDebut.getValue().isAfter(dateFin.getValue())) {
                erreurs.append("- La date de début ne peut pas être postérieure à la date de fin\n");
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
     * Gère le clic sur le bouton d'enregistrement
     */
    @FXML
    void handleEnregistrerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        if (modeEdition) {
            enregistrerModification();
        } else {
            enregistrerNouvelleMission();
        }
    }

    /**
     * Enregistre une nouvelle mission
     */
    private void enregistrerNouvelleMission() {
        Mission nouvelleMission = creerMissionDepuisFormulaire();

        Mission savedMission = missionService.addMission(nouvelleMission);
        if (savedMission != null) {
            // Ajouter les participants à la mission
            boolean allParticipantsAdded = true;
            for (Personnel participant : participantsSelectionnes) {
                boolean success = missionService.addParticipant(savedMission.getIdMission(), participant.getIdPersonnel());
                if (!success) {
                    allParticipantsAdded = false;
                }
            }

            String message = "La mission a été ajoutée avec succès.";
            if (!allParticipantsAdded) {
                message += "\nAttention: Certains participants n'ont pas pu être ajoutés.";
            }

            AlertUtils.showInformationAlert("Succès", message);

            if (parentController != null) {
                parentController.refreshMissionList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter la mission.");
        }
    }

    /**
     * Enregistre les modifications d'une mission existante
     */
    private void enregistrerModification() {
        if (mission != null) {
            mettreAJourMissionDepuisFormulaire();

            boolean success = missionService.updateMission(mission);
            if (success) {
                // D'abord, récupérer les participants actuels
                List<Personnel> participantsActuels = missionService.getMissionParticipants(mission.getIdMission());

                // Supprimer les participants qui ne sont plus sélectionnés
                for (Personnel participant : participantsActuels) {
                    if (!participantsSelectionnes.contains(participant)) {
                        missionService.removeParticipant(mission.getIdMission(), participant.getIdPersonnel());
                    }
                }

                // Ajouter les nouveaux participants
                boolean allParticipantsAdded = true;
                for (Personnel participant : participantsSelectionnes) {
                    if (!participantsActuels.contains(participant)) {
                        boolean addSuccess = missionService.addParticipant(mission.getIdMission(), participant.getIdPersonnel());
                        if (!addSuccess) {
                            allParticipantsAdded = false;
                        }
                    }
                }

                String message = "La mission a été mise à jour avec succès.";
                if (!allParticipantsAdded) {
                    message += "\nAttention: Certains participants n'ont pas pu être ajoutés.";
                }

                AlertUtils.showInformationAlert("Succès", message);

                if (parentController != null) {
                    parentController.refreshMissionList();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour la mission.");
            }
        }
    }

    /**
     * Crée un nouvel objet Mission à partir des valeurs du formulaire
     * @return Nouvel objet Mission
     */
    private Mission creerMissionDepuisFormulaire() {
        Mission newMission = new Mission();

        newMission.setLibMission(txtLibelle.getText().trim());

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            newMission.setIdVehicule(vehiculeSelectionne.getIdVehicule());
            newMission.setVehicule(vehiculeSelectionne);
        }

        // Conversion des dates
        if (dateDebut.getValue() != null) {
            newMission.setDateDebutMission(dateDebut.getValue().atStartOfDay());
        }

        if (dateFin.getValue() != null) {
            newMission.setDateFinMission(dateFin.getValue().atTime(LocalTime.MAX));
        }

        newMission.setCircuitMission(txtCircuit.getText().trim());
        newMission.setObservationMission(txtObservations.getText().trim());

        // Initialiser les coûts à 0 (ils seront mis à jour lors de la fin de mission)
        newMission.setCoutMission(0);
        newMission.setCoutCarburant(0);

        // Ajouter les participants
        newMission.setParticipants(participantsSelectionnes);

        return newMission;
    }

    /**
     * Met à jour l'objet Mission existant avec les valeurs du formulaire
     */
    private void mettreAJourMissionDepuisFormulaire() {
        if (mission == null) return;

        mission.setLibMission(txtLibelle.getText().trim());

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            mission.setIdVehicule(vehiculeSelectionne.getIdVehicule());
            mission.setVehicule(vehiculeSelectionne);
        }

        // Conversion des dates
        if (dateDebut.getValue() != null) {
            mission.setDateDebutMission(dateDebut.getValue().atStartOfDay());
        } else {
            mission.setDateDebutMission(null);
        }

        if (dateFin.getValue() != null) {
            mission.setDateFinMission(dateFin.getValue().atTime(LocalTime.MAX));
        } else {
            mission.setDateFinMission(null);
        }

        mission.setCircuitMission(txtCircuit.getText().trim());
        mission.setObservationMission(txtObservations.getText().trim());

        // Mettre à jour la liste des participants
        mission.setParticipants(participantsSelectionnes);
    }

    /**
     * Gère le clic sur le bouton d'ajout d'un participant
     */
    @FXML
    void handleAjouterParticipantButton(ActionEvent event) {
        Personnel selectedPersonnel = listPersonnelsDisponibles.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            // Ajouter à la liste des participants sélectionnés
            participantsSelectionnes.add(selectedPersonnel);

            // Mettre à jour les listes
            listParticipants.setItems(FXCollections.observableArrayList(participantsSelectionnes));

            // Retirer de la liste des disponibles
            List<Personnel> disponibles = new ArrayList<>(listPersonnelsDisponibles.getItems());
            disponibles.remove(selectedPersonnel);
            listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(disponibles));

            // Désélectionner
            listPersonnelsDisponibles.getSelectionModel().clearSelection();
            btnAjouterParticipant.setDisable(true);
        }
    }

    /**
     * Gère le clic sur le bouton de retrait d'un participant
     */
    @FXML
    void handleRetirerParticipantButton(ActionEvent event) {
        Personnel selectedPersonnel = listParticipants.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            // Retirer de la liste des participants sélectionnés
            participantsSelectionnes.remove(selectedPersonnel);

            // Mettre à jour les listes
            listParticipants.setItems(FXCollections.observableArrayList(participantsSelectionnes));

            // Ajouter à la liste des disponibles
            List<Personnel> disponibles = new ArrayList<>(listPersonnelsDisponibles.getItems());
            disponibles.add(selectedPersonnel);
            listPersonnelsDisponibles.setItems(FXCollections.observableArrayList(disponibles));

            // Désélectionner
            listParticipants.getSelectionModel().clearSelection();
            btnRetirerParticipant.setDisable(true);
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