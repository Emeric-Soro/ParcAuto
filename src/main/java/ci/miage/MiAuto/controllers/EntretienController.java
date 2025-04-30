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
import main.java.ci.miage.MiAuto.models.Entretien;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.EntretienService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion des entretiens des véhicules
 */
public class EntretienController implements Initializable {

    @FXML
    private ComboBox<String> comboFiltre;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnRafraichir;

    @FXML
    private TableView<Entretien> tableEntretiens;

    @FXML
    private TableColumn<Entretien, Integer> colId;

    @FXML
    private TableColumn<Entretien, String> colVehicule;

    @FXML
    private TableColumn<Entretien, String> colMotif;

    @FXML
    private TableColumn<Entretien, String> colDateEntree;

    @FXML
    private TableColumn<Entretien, String> colDateSortie;

    @FXML
    private TableColumn<Entretien, Integer> colCout;

    @FXML
    private TableColumn<Entretien, String> colLieu;

    @FXML
    private TableColumn<Entretien, String> colStatut;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnTerminer;

    @FXML
    private Button btnSupprimer;

    private EntretienService entretienService;
    private VehiculeService vehiculeService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entretienService = new EntretienService();
        vehiculeService = new VehiculeService();

        // Initialiser le ComboBox de filtre
        comboFiltre.setItems(FXCollections.observableArrayList(
                "Tous", "En cours", "Terminés"));
        comboFiltre.getSelectionModel().selectFirst();

        // Configurer les colonnes
        configureColumns();

        // Charger les données
        loadEntretiens();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableEntretiens.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean hasSelection = (newValue != null);
                    btnModifier.setDisable(!hasSelection);
                    btnSupprimer.setDisable(!hasSelection);

                    // Le bouton Terminer est actif seulement pour les entretiens en cours
                    if (hasSelection) {
                        btnTerminer.setDisable(newValue.getDateSortieEntr() != null);
                    } else {
                        btnTerminer.setDisable(true);
                    }
                });
    }

    /**
     * Configure les colonnes de la TableView
     */
    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEntretien"));

        // Afficher les informations du véhicule
        colVehicule.setCellValueFactory(cellData -> {
            int idVehicule = cellData.getValue().getIdVehicule();
            try {
                Vehicule vehicule = vehiculeService.getVehiculeById(idVehicule);
                if (vehicule != null) {
                    return new SimpleStringProperty(vehicule.getMarque() + " " +
                            vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty("Non trouvé (ID: " + idVehicule + ")");
        });

        colMotif.setCellValueFactory(new PropertyValueFactory<>("motifEntr"));

        // Formater les dates
        colDateEntree.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateEntreeEntr();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        colDateSortie.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateSortieEntr();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        colCout.setCellValueFactory(new PropertyValueFactory<>("coutEntr"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieuEntr"));

        // Déterminer le statut de l'entretien
        colStatut.setCellValueFactory(cellData -> {
            Entretien entretien = cellData.getValue();
            if (entretien.getDateSortieEntr() == null) {
                return new SimpleStringProperty("En cours");
            } else {
                return new SimpleStringProperty("Terminé");
            }
        });
    }

    /**
     * Charge les entretiens selon le filtre sélectionné
     */
    private void loadEntretiens() {
        String filtre = comboFiltre.getValue();
        String recherche = txtRecherche.getText().trim();

        List<Entretien> entretiens;

        if (!recherche.isEmpty()) {
            // Recherche par motif ou lieu
            entretiens = entretienService.searchEntretiens(recherche);
        } else if ("En cours".equals(filtre)) {
            entretiens = entretienService.getEntretiensEnCours();
        } else if ("Terminés".equals(filtre)) {
            entretiens = entretienService.getEntretiensTermines();
        } else {
            // Par défaut, charger tous les entretiens
            entretiens = entretienService.getAllEntretiens();
        }

        tableEntretiens.setItems(FXCollections.observableArrayList(entretiens));
    }

    @FXML
    void handleRechercherButton(ActionEvent event) {
        loadEntretiens();
    }

    @FXML
    void handleRafraichirButton(ActionEvent event) {
        txtRecherche.clear();
        comboFiltre.getSelectionModel().selectFirst();
        loadEntretiens();
    }

    @FXML
    void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireEntretien(null);
    }

    @FXML
    void handleModifierButton(ActionEvent event) {
        Entretien selectedEntretien = tableEntretiens.getSelectionModel().getSelectedItem();
        if (selectedEntretien != null) {
            ouvrirFormulaireEntretien(selectedEntretien);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un entretien à modifier.");
        }
    }

    @FXML
    void handleTerminerButton(ActionEvent event) {
        Entretien selectedEntretien = tableEntretiens.getSelectionModel().getSelectedItem();
        if (selectedEntretien != null && selectedEntretien.getDateSortieEntr() == null) {
            boolean terminer = AlertUtils.showConfirmationAlert("Terminer l'entretien",
                    "Voulez-vous terminer cet entretien ?",
                    "L'entretien sera marqué comme terminé avec la date d'aujourd'hui.");

            if (terminer) {
                boolean success = entretienService.terminerEntretien(selectedEntretien.getIdEntretien());
                if (success) {
                    AlertUtils.showInformationAlert("Succès", "L'entretien a été terminé avec succès.");
                    loadEntretiens();
                } else {
                    AlertUtils.showErrorAlert("Erreur", "Impossible de terminer l'entretien.");
                }
            }
        }
    }

    @FXML
    void handleSupprimerButton(ActionEvent event) {
        Entretien selectedEntretien = tableEntretiens.getSelectionModel().getSelectedItem();
        if (selectedEntretien != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer cet entretien ?",
                    "Cette action est irréversible.");

            if (confirmation) {
                boolean success = entretienService.deleteEntretien(selectedEntretien.getIdEntretien());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "L'entretien a été supprimé avec succès.");
                    loadEntretiens();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer l'entretien.");
                }
            }
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un entretien à supprimer.");
        }
    }

    /**
     * Ouvre le formulaire d'ajout/modification d'entretien
     * @param entretien Entretien à modifier (null pour un ajout)
     */
    private void ouvrirFormulaireEntretien(Entretien entretien) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/entretien/form_entretien.fxml"));
            Parent root = loader.load();

            FormEntretienController controller = loader.getController();
            controller.setEntretien(entretien);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(entretien == null ? "Ajouter un entretien" : "Modifier un entretien");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire d'entretien.");
        }
    }

    /**
     * Rafraîchit la liste des entretiens
     * Cette méthode est appelée depuis le FormEntretienController après une modification
     */
    public void refreshEntretienList() {
        loadEntretiens();
    }
}