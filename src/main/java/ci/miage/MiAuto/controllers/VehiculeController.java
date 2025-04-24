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
import main.java.ci.miage.MiAuto.models.EtatVoiture;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.DateUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion des véhicules
 */
public class VehiculeController implements Initializable {

    @FXML
    private ComboBox<String> comboEtats;

    @FXML
    private TableView<Vehicule> tableVehicules;

    @FXML
    private TableColumn<Vehicule, Integer> colId;

    @FXML
    private TableColumn<Vehicule, String> colImmatriculation;

    @FXML
    private TableColumn<Vehicule, String> colMarque;

    @FXML
    private TableColumn<Vehicule, String> colModele;

    @FXML
    private TableColumn<Vehicule, String> colEnergie;

    @FXML
    private TableColumn<Vehicule, Integer> colKilometrage;

    @FXML
    private TableColumn<Vehicule, String> colEtat;

    @FXML
    private TableColumn<Vehicule, Boolean> colStatut;

    @FXML
    private TextField txtRecherche;

    @FXML
    private ComboBox<String> comboTypeCritere;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnRafraichir;

    private VehiculeService vehiculeService;

    /**
     * Initialise le contrôleur
     * @param url URL de localisation
     * @param rb Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        vehiculeService = new VehiculeService();

        chargerEtats();
        // Ajouter un écouteur au comboTypeCritere pour montrer/cacher le ComboBox des états
        comboTypeCritere.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("État".equals(newValue)) {
                comboEtats.setVisible(true);
                txtRecherche.setVisible(false);
            } else {
                comboEtats.setVisible(false);
                txtRecherche.setVisible(true);
            }
        });
        // Initialiser la liste déroulante des critères de recherche
        comboTypeCritere.setItems(FXCollections.observableArrayList(
                "Immatriculation", "Marque", "Modèle", "État"));
        comboTypeCritere.getSelectionModel().selectFirst();

        // Configurer les colonnes de la table
        configureTable();

        // Charger les données initiales
        chargerVehicules();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableVehicules.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disableButtons = (newValue == null);
                    btnModifier.setDisable(disableButtons);
                    btnSupprimer.setDisable(disableButtons);
                });
    }
    // Méthode pour charger les états dans le ComboBox
    private void chargerEtats() {
        try {
            // Récupérer tous les états (vous devez ajouter cette méthode dans VehiculeService)
            List<EtatVoiture> etats = vehiculeService.getAllEtats();
            List<String> libelles = new ArrayList<>();

            // Ajouter "Tous" comme premier élément
            libelles.add("Tous");

            // Ajouter tous les libellés d'état
            for (EtatVoiture etat : etats) {
                libelles.add(etat.getLibEtatVoiture());
            }

            comboEtats.setItems(FXCollections.observableArrayList(libelles));
            comboEtats.getSelectionModel().selectFirst();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des états: " + e.getMessage());
        }
    }

    /**
     * Configure les colonnes de la table
     */
    private void configureTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idVehicule"));
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colEnergie.setCellValueFactory(new PropertyValueFactory<>("energie"));
        colKilometrage.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));

        // Pour afficher le libellé de l'état au lieu de l'ID
        colEtat.setCellValueFactory(cellData -> {
            EtatVoiture etat = cellData.getValue().getEtatVoiture();
            if (etat != null) {
                return javafx.beans.binding.Bindings.createStringBinding(() -> etat.getLibEtatVoiture());
            } else {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Non défini");
            }
        });

        // Pour afficher "Oui" ou "Non" au lieu de true/false
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutAttribution"));
        colStatut.setCellFactory(col -> new TableCell<Vehicule, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Attribué" : "Disponible");
                }
            }
        });
    }

    /**
     * Charge la liste des véhicules dans la table
     */
    private void chargerVehicules() {
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        tableVehicules.setItems(FXCollections.observableArrayList(vehicules));
    }

    /**
     * Gère le clic sur le bouton de recherche
     * @param event Événement de clic
     */
    @FXML
    private void handleRechercherButton(ActionEvent event) {
        String critere = comboTypeCritere.getValue();
        List<Vehicule> resultats = null;

        if ("État".equals(critere)) {
            String etatSelectionne = comboEtats.getValue();

            if (etatSelectionne == null || "Tous".equals(etatSelectionne)) {
                chargerVehicules();
                return;
            }

            resultats = vehiculeService.getVehiculesByEtat(etatSelectionne);
        } else {
            String valeur = txtRecherche.getText().trim();

            if (valeur.isEmpty()) {
                chargerVehicules();
                return;
            }

            switch (critere) {
                case "Immatriculation":
                    resultats = vehiculeService.getVehiculesByImmatriculation(valeur);
                    break;
                case "Marque":
                    resultats = vehiculeService.getVehiculesByMarque(valeur);
                    break;
                case "Modèle":
                    resultats = vehiculeService.rechercheAvancee(null, valeur, 0, false);
                    break;
            }
        }

        if (resultats != null) {
            tableVehicules.setItems(FXCollections.observableArrayList(resultats));
        }
    }

    /**
     * Gère le clic sur le bouton d'ajout
     * @param event Événement de clic
     */
    @FXML
    private void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireVehicule(null);
    }

    /**
     * Gère le clic sur le bouton de modification
     * @param event Événement de clic
     */
    @FXML
    private void handleModifierButton(ActionEvent event) {
        Vehicule selectedVehicule = tableVehicules.getSelectionModel().getSelectedItem();
        if (selectedVehicule != null) {
            ouvrirFormulaireVehicule(selectedVehicule);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un véhicule à modifier.");
        }
    }

    /**
     * Gère le clic sur le bouton de suppression
     * @param event Événement de clic
     */
    @FXML
    private void handleSupprimerButton(ActionEvent event) {
        Vehicule selectedVehicule = tableVehicules.getSelectionModel().getSelectedItem();
        if (selectedVehicule != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer ce véhicule ?",
                    "Véhicule: " + selectedVehicule.getMarque() + " " + selectedVehicule.getModele() +
                            " (" + selectedVehicule.getImmatriculation() + ")");

            if (confirmation) {
                boolean success = vehiculeService.deleteVehicule(selectedVehicule.getIdVehicule());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "Le véhicule a été supprimé avec succès.");
                    chargerVehicules();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer le véhicule. Il est peut-être référencé par d'autres entités.");
                }
            }
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un véhicule à supprimer.");
        }
    }

    /**
     * Gère le clic sur le bouton de rafraîchissement
     * @param event Événement de clic
     */
    @FXML
    private void handleRafraichirButton(ActionEvent event) {
        chargerVehicules();
        txtRecherche.clear();
    }

    /**
     * Ouvre le formulaire d'ajout/modification de véhicule
     * @param vehicule Véhicule à modifier (null pour un ajout)
     */
    private void ouvrirFormulaireVehicule(Vehicule vehicule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/vehicule/form_vehicule.fxml"));
            Parent root = loader.load();

            FormVehiculeController controller = loader.getController();
            controller.setVehiculeService(vehiculeService);
            controller.setVehicule(vehicule);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(vehicule == null ? "Ajouter un véhicule" : "Modifier un véhicule");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de véhicule.");
            e.printStackTrace();
        }
    }
    /**
     * Rafraîchit la liste des véhicules
     * Cette méthode est publique pour être appelée depuis d'autres contrôleurs
     */
    public void refreshVehiculeList() {
        chargerVehicules();
    }
}