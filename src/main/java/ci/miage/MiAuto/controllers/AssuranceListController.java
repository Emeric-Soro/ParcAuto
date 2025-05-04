package main.java.ci.miage.MiAuto.controllers;

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
import javafx.util.StringConverter;
import main.java.ci.miage.MiAuto.models.Assurance;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.AssuranceService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion de la liste des assurances
 */
public class AssuranceListController implements Initializable {

    @FXML
    private ComboBox<String> comboTypeCritere;

    @FXML
    private TextField txtRecherche;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Assurance> tableAssurances;

    @FXML
    private TableColumn<Assurance, Integer> colNumCarte;

    @FXML
    private TableColumn<Assurance, String> colAgence;

    @FXML
    private TableColumn<Assurance, LocalDateTime> colDateDebut;

    @FXML
    private TableColumn<Assurance, LocalDateTime> colDateFin;

    @FXML
    private TableColumn<Assurance, Integer> colCout;

    @FXML
    private TableColumn<Assurance, String> colStatut;

    @FXML
    private TableColumn<Assurance, String> colVehicule;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnAssignerVehicule;

    private AssuranceService assuranceService;
    private VehiculeService vehiculeService;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assuranceService = new AssuranceService();
        vehiculeService = new VehiculeService();

        // Initialiser la liste déroulante des critères de recherche
        comboTypeCritere.setItems(FXCollections.observableArrayList(
                "Agence", "Statut", "Date d'expiration"));

        comboTypeCritere.getSelectionModel().selectFirst();

        // Ajouter un écouteur de changement pour afficher le DatePicker si nécessaire
        comboTypeCritere.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Date d'expiration".equals(newVal)) {
                datePicker.setVisible(true);
                txtRecherche.setVisible(false);
            } else {
                datePicker.setVisible(false);
                txtRecherche.setVisible(true);
            }
        });

        // Initialiser les colonnes de la table
        configureAssuranceTable();
        chargerAssurances();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableAssurances.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disableButtons = (newValue == null);
                    btnModifier.setDisable(disableButtons);
                    btnSupprimer.setDisable(disableButtons);
                    btnAssignerVehicule.setDisable(disableButtons);
                });
    }

    /**
     * Configure les colonnes du tableau des assurances
     */
    private void configureAssuranceTable() {
        colNumCarte.setCellValueFactory(new PropertyValueFactory<>("numCarteAssurance"));
        colAgence.setCellValueFactory(new PropertyValueFactory<>("agence"));

        // Formater les dates
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebutAssurance"));
        colDateDebut.setCellFactory(column -> new TableCell<Assurance, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFinAssurance"));
        colDateFin.setCellFactory(column -> new TableCell<Assurance, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colCout.setCellValueFactory(new PropertyValueFactory<>("coutAssurance"));
        colCout.setCellFactory(column -> new TableCell<Assurance, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d FCFA", item));
                }
            }
        });

        // Afficher le statut (valide, expiré, proche expiration)
        colStatut.setCellFactory(column -> new TableCell<Assurance, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Assurance assurance = getTableView().getItems().get(getIndex());
                    if (assurance.isValide()) {
                        if (assurance.estProcheDExpiration(30)) {
                            setText("Expire bientôt");
                            setStyle("-fx-text-fill: #f39c12;"); // Orange
                        } else {
                            setText("Valide");
                            setStyle("-fx-text-fill: #2ecc71;"); // Vert
                        }
                    } else {
                        setText("Expiré");
                        setStyle("-fx-text-fill: #e74c3c;"); // Rouge
                    }
                }
            }
        });

        // Afficher le véhicule associé
        colVehicule.setCellFactory(column -> new TableCell<Assurance, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Assurance assurance = getTableView().getItems().get(getIndex());
                    List<Vehicule> vehicules = assuranceService.getVehiculesForAssurance(assurance.getNumCarteAssurance());
                    if (vehicules.isEmpty()) {
                        setText("Non assigné");
                    } else {
                        setText(vehicules.get(0).toString());
                    }
                }
            }
        });
    }

    /**
     * Charge la liste des assurances
     */
    public void chargerAssurances() {
        List<Assurance> assurances = assuranceService.getAllAssurances();
        tableAssurances.setItems(FXCollections.observableArrayList(assurances));
    }

    /**
     * Gère le clic sur le bouton de recherche
     */
    @FXML
    private void handleRechercherButton(ActionEvent event) {
        String critere = comboTypeCritere.getValue();
        List<Assurance> resultats = null;

        if ("Agence".equals(critere)) {
            String valeur = txtRecherche.getText().trim();
            if (!valeur.isEmpty()) {
                resultats = assuranceService.findByAgence(valeur);
            }
        } else if ("Statut".equals(critere)) {
            String valeur = txtRecherche.getText().trim().toLowerCase();
            if ("valide".equals(valeur)) {
                resultats = assuranceService.findValides();
            } else if ("expiré".equals(valeur) || "expire".equals(valeur) || "expiree".equals(valeur)) {
                resultats = assuranceService.findExpirees();
            } else if ("bientôt".equals(valeur) || "bientot".equals(valeur) || "proche".equals(valeur)) {
                resultats = assuranceService.findProchesExpiration(30);
            }
        } else if ("Date d'expiration".equals(critere)) {
            LocalDate date = datePicker.getValue();
            if (date != null) {
                LocalDateTime dateTime = date.atStartOfDay();
                resultats = assuranceService.findByDateExpiration(dateTime);
            }
        }

        if (resultats != null) {
            tableAssurances.setItems(FXCollections.observableArrayList(resultats));
        } else {
            chargerAssurances();
        }
    }

    /**
     * Gère le clic sur le bouton de rafraîchissement
     */
    @FXML
    private void handleRafraichirButton(ActionEvent event) {
        chargerAssurances();
        txtRecherche.clear();
        datePicker.setValue(null);
    }

    /**
     * Gère le clic sur le bouton d'ajout
     */
    @FXML
    private void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireAssurance(null);
    }

    /**
     * Gère le clic sur le bouton de modification
     */
    @FXML
    private void handleModifierButton(ActionEvent event) {
        Assurance selectedAssurance = tableAssurances.getSelectionModel().getSelectedItem();
        if (selectedAssurance != null) {
            ouvrirFormulaireAssurance(selectedAssurance);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner une assurance à modifier.");
        }
    }

    /**
     * Gère le clic sur le bouton de suppression
     */
    @FXML
    private void handleSupprimerButton(ActionEvent event) {
        Assurance selectedAssurance = tableAssurances.getSelectionModel().getSelectedItem();
        if (selectedAssurance != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer cette assurance ?",
                    "N° Carte: " + selectedAssurance.getNumCarteAssurance() + ", Agence: " + selectedAssurance.getAgence());

            if (confirmation) {
                boolean success = assuranceService.deleteAssurance(selectedAssurance.getNumCarteAssurance());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "L'assurance a été supprimée avec succès.");
                    chargerAssurances();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer l'assurance. Elle est peut-être référencée par des véhicules.");
                }
            }
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner une assurance à supprimer.");
        }
    }

    /**
     * Gère le clic sur le bouton d'assignation à un véhicule
     */
    @FXML
    private void handleAssignerVehiculeButton(ActionEvent event) {
        Assurance selectedAssurance = tableAssurances.getSelectionModel().getSelectedItem();
        if (selectedAssurance != null) {
            ouvrirDialogueAssignationVehicule(selectedAssurance);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner une assurance à assigner.");
        }
    }

    /**
     * Ouvre le formulaire d'assurance pour ajout ou modification
     */
    private void ouvrirFormulaireAssurance(Assurance assurance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/assurance/form_assurance.fxml"));
            Parent root = loader.load();

            AssuranceFormController controller = loader.getController();
            controller.setAssuranceService(assuranceService);
            controller.setAssurance(assurance);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(assurance == null ? "Ajouter une assurance" : "Modifier une assurance");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire d'assurance.");
        }
    }

    /**
     * Ouvre le dialogue d'assignation d'un véhicule à une assurance
     */
    private void ouvrirDialogueAssignationVehicule(Assurance assurance) {
        try {
            // Créer la boîte de dialogue
            Dialog<Vehicule> dialog = new Dialog<>();
            dialog.setTitle("Assigner un véhicule");
            dialog.setHeaderText("Sélectionnez un véhicule à assurer");

            // Ajouter les boutons
            ButtonType buttonTypeOk = new ButtonType("Assigner", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

            // Créer la liste des véhicules disponibles
            ComboBox<Vehicule> comboVehicules = new ComboBox<>();
            List<Vehicule> vehiculesDisponibles = vehiculeService.getAllVehicules(); // Vous pouvez filtrer selon vos besoins
            comboVehicules.setItems(FXCollections.observableArrayList(vehiculesDisponibles));
            comboVehicules.setPrefWidth(400);

            // Configurer l'affichage des véhicules dans la ComboBox
            comboVehicules.setConverter(new StringConverter<Vehicule>() {
                @Override
                public String toString(Vehicule vehicule) {
                    if (vehicule == null) return null;
                    return vehicule.getMarque() + " " + vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")";
                }

                @Override
                public Vehicule fromString(String string) {
                    return null; // Non utilisé
                }
            });

            // Ajouter la ComboBox à la boîte de dialogue
            dialog.getDialogPane().setContent(comboVehicules);

            // Configurer le résultat de la boîte de dialogue
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == buttonTypeOk) {
                    return comboVehicules.getValue();
                }
                return null;
            });

            // Afficher la boîte de dialogue et traiter le résultat
            dialog.showAndWait().ifPresent(vehicule -> {
                if (vehicule != null) {
                    boolean success = assuranceService.assignerVehicule(assurance.getNumCarteAssurance(), vehicule.getIdVehicule());
                    if (success) {
                        AlertUtils.showInformationAlert("Assignation réussie",
                                "L'assurance a été assignée au véhicule avec succès.");
                        chargerAssurances();
                    } else {
                        AlertUtils.showErrorAlert("Erreur d'assignation",
                                "Impossible d'assigner l'assurance au véhicule.");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ouvrir le dialogue d'assignation de véhicule.");
        }
    }
}