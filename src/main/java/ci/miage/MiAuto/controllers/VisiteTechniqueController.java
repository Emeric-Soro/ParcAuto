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
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.models.VisiteTechnique;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.services.VisiteTechniqueService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion des visites techniques
 */
public class VisiteTechniqueController implements Initializable {

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
    private TableView<VisiteTechnique> tableVisites;

    @FXML
    private TableColumn<VisiteTechnique, Integer> colId;

    @FXML
    private TableColumn<VisiteTechnique, String> colVehicule;

    @FXML
    private TableColumn<VisiteTechnique, String> colDateVisite;

    @FXML
    private TableColumn<VisiteTechnique, String> colDateExpiration;

    @FXML
    private TableColumn<VisiteTechnique, String> colResultat;

    @FXML
    private TableColumn<VisiteTechnique, Integer> colCout;

    @FXML
    private TableColumn<VisiteTechnique, String> colCentre;

    @FXML
    private TableColumn<VisiteTechnique, String> colStatut;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnAlerte;

    private VisiteTechniqueService visiteTechniqueService;
    private VehiculeService vehiculeService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visiteTechniqueService = new VisiteTechniqueService();
        vehiculeService = new VehiculeService();

        // Initialiser le ComboBox de filtre
        comboFiltre.setItems(FXCollections.observableArrayList(
                "Toutes", "Valides", "Expirées", "À renouveler (30 jours)"));
        comboFiltre.getSelectionModel().selectFirst();

        // Configurer les colonnes
        configureColumns();

        // Charger les données
        loadVisitesTechniques();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableVisites.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean hasSelection = (newValue != null);
                    btnModifier.setDisable(!hasSelection);
                    btnSupprimer.setDisable(!hasSelection);
                });
    }

    /**
     * Configure les colonnes de la TableView
     */
    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idVisite"));

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

        // Formater les dates
        colDateVisite.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateVisite();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        colDateExpiration.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateExpiration();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "Non définie");
        });

        colResultat.setCellValueFactory(new PropertyValueFactory<>("resultat"));
        colCout.setCellValueFactory(new PropertyValueFactory<>("cout"));
        colCentre.setCellValueFactory(new PropertyValueFactory<>("centreVisite"));

        // Déterminer le statut de la visite technique
        colStatut.setCellValueFactory(cellData -> {
            VisiteTechnique visite = cellData.getValue();
            LocalDateTime now = LocalDateTime.now();

            if (visite.getDateExpiration() == null) {
                return new SimpleStringProperty("Non défini");
            } else if (visite.getDateExpiration().isBefore(now)) {
                return new SimpleStringProperty("Expirée");
            } else if (visite.getDateExpiration().minusDays(30).isBefore(now)) {
                return new SimpleStringProperty("À renouveler");
            } else {
                return new SimpleStringProperty("Valide");
            }
        });

        // Appliquer des styles selon le statut
        colStatut.setCellFactory(column -> new TableCell<VisiteTechnique, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Expirée":
                            setStyle("-fx-text-fill: #e74c3c;"); // Rouge
                            break;
                        case "À renouveler":
                            setStyle("-fx-text-fill: #f39c12;"); // Orange
                            break;
                        case "Valide":
                            setStyle("-fx-text-fill: #2ecc71;"); // Vert
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
    }

    /**
     * Charge les visites techniques selon le filtre sélectionné
     */
    private void loadVisitesTechniques() {
        String filtre = comboFiltre.getValue();
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();

        List<VisiteTechnique> visites;

        if (debut != null && fin != null) {
            // Filtrer par période
            visites = visiteTechniqueService.getVisitesByPeriode(
                    debut.atStartOfDay(),
                    fin.atTime(23, 59, 59)
            );
        } else if ("Valides".equals(filtre)) {
            visites = visiteTechniqueService.getVisitesValides();
        } else if ("Expirées".equals(filtre)) {
            visites = visiteTechniqueService.getVisitesExpirees();
        } else if ("À renouveler (30 jours)".equals(filtre)) {
            visites = visiteTechniqueService.getVisitesARenouveler(30);
        } else {
            // Par défaut, charger toutes les visites
            visites = visiteTechniqueService.getAllVisitesTechniques();
        }

        tableVisites.setItems(FXCollections.observableArrayList(visites));
    }

    @FXML
    void handleFiltrerButton(ActionEvent event) {
        loadVisitesTechniques();
    }

    @FXML
    void handleReinitialiserButton(ActionEvent event) {
        dateDebut.setValue(null);
        dateFin.setValue(null);
        comboFiltre.getSelectionModel().selectFirst();
        loadVisitesTechniques();
    }

    @FXML
    void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireVisiteTechnique(null);
    }

    @FXML
    void handleModifierButton(ActionEvent event) {
        VisiteTechnique selectedVisite = tableVisites.getSelectionModel().getSelectedItem();
        if (selectedVisite != null) {
            ouvrirFormulaireVisiteTechnique(selectedVisite);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner une visite technique à modifier.");
        }
    }

    @FXML
    void handleSupprimerButton(ActionEvent event) {
        VisiteTechnique selectedVisite = tableVisites.getSelectionModel().getSelectedItem();
        if (selectedVisite != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer cette visite technique ?",
                    "Cette action est irréversible.");

            if (confirmation) {
                boolean success = visiteTechniqueService.deleteVisiteTechnique(selectedVisite.getIdVisite());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "La visite technique a été supprimée avec succès.");
                    loadVisitesTechniques();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer la visite technique.");
                }
            }
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner une visite technique à supprimer.");
        }
    }

    @FXML
    void handleAlerteButton(ActionEvent event) {
        List<VisiteTechnique> visites = visiteTechniqueService.getVisitesARenouveler(30);

        if (visites.isEmpty()) {
            AlertUtils.showInformationAlert("Alerte expiration",
                    "Aucune visite technique n'expire dans les 30 prochains jours.");
            return;
        }

        StringBuilder message = new StringBuilder("Les visites techniques suivantes expirent bientôt :\n\n");
        for (VisiteTechnique visite : visites) {
            try {
                Vehicule vehicule = vehiculeService.getVehiculeById(visite.getIdVehicule());
                if (vehicule != null) {
                    message.append("- ")
                            .append(vehicule.getMarque())
                            .append(" ")
                            .append(vehicule.getModele())
                            .append(" (")
                            .append(vehicule.getImmatriculation())
                            .append("), expiration: ")
                            .append(visite.getDateExpiration().format(dateFormatter))
                            .append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AlertUtils.showWarningAlert("Alerte expiration",
                "Visites techniques à renouveler");
    }

    /**
     * Ouvre le formulaire d'ajout/modification de visite technique
     * @param visiteTechnique Visite technique à modifier (null pour un ajout)
     */
    private void ouvrirFormulaireVisiteTechnique(VisiteTechnique visiteTechnique) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/visite/form_visite.fxml"));
            Parent root = loader.load();

            FormVisiteTechniqueController controller = loader.getController();
            controller.setVisiteTechnique(visiteTechnique);
            controller.setVisiteTechniqueService(visiteTechniqueService);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(visiteTechnique == null ? "Ajouter une visite technique" : "Modifier une visite technique");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de visite technique.");
        }
    }

    /**
     * Rafraîchit la liste des visites techniques
     * Cette méthode est appelée depuis le FormVisiteTechniqueController après une modification
     */
    public void refreshVisiteTechniqueList() {
        loadVisitesTechniques();
    }
}