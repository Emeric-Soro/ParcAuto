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
import main.java.ci.miage.MiAuto.models.Fonction;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Service;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.FonctionService;
import main.java.ci.miage.MiAuto.services.PersonnelService;
import main.java.ci.miage.MiAuto.services.ServiceAppService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PersonnelController implements Initializable {

    @FXML
    private TextField txtRecherche;

    @FXML
    private TableColumn<Personnel, String> colVehicule;

    @FXML
    private TableView<Personnel> tablePersonnels;

    @FXML
    private TableColumn<Personnel, Integer> colId;

    @FXML
    private TableColumn<Personnel, String> colNom;

    @FXML
    private TableColumn<Personnel, String> colPrenom;

    @FXML
    private TableColumn<Personnel, String> colEmail;

    @FXML
    private TableColumn<Personnel, String> colContact;

    @FXML
    private TableColumn<Personnel, String> colFonction;

    @FXML
    private TableColumn<Personnel, String> colService;


    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnRafraichir;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnAttribuerVehicule;

    private PersonnelService personnelService;
    private VehiculeService vehiculeService;
    private FonctionService fonctionService;
    private ServiceAppService serviceAppService;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personnelService = new PersonnelService();
        vehiculeService = new VehiculeService();
        fonctionService = new FonctionService();
        serviceAppService = new ServiceAppService();

        // Configurer les colonnes de la table
        configureTable();

        // Charger les données initiales
        chargerPersonnels();

        // Ajouter un écouteur de sélection
        tablePersonnels.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disableButtons = (newValue == null);
                    btnModifier.setDisable(disableButtons);
                    btnSupprimer.setDisable(disableButtons);
                    btnAttribuerVehicule.setDisable(disableButtons);
                });
    }

    private void configureTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPersonnel"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPersonnel"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomPersonnel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailPersonnel"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactPersonnel"));

        // Afficher le libellé de la fonction
        colFonction.setCellValueFactory(cellData -> {
            Personnel p = cellData.getValue();
            Fonction fonction = fonctionService.getFonctionById(p.getIdFonction());
            String libelle = fonction != null ? fonction.getLibelleFonction() : "Non définie";
            return new SimpleStringProperty(libelle);
        });

        // Afficher le libellé du service
        colService.setCellValueFactory(cellData -> {
            Personnel p = cellData.getValue();
            Service service = serviceAppService.getServiceById(p.getIdService());
            String libelle = service != null ? service.getLibelleService() : "Non défini";
            return new SimpleStringProperty(libelle);
        });

        // Afficher l'ID du véhicule ou "Aucun"
        colVehicule.setCellValueFactory(cellData -> {
            Personnel p = cellData.getValue();
            if (p.getIdVehicule() > 0) {
                Vehicule vehicule = vehiculeService.getVehiculeById(p.getIdVehicule());
                if (vehicule != null) {
                    return new SimpleStringProperty(vehicule.getMarque() + " " + vehicule.getModele() + " - " + vehicule.getImmatriculation());
                }
            }
            return new SimpleStringProperty("Non attribué");
        });
    }

    private void chargerPersonnels() {
        List<Personnel> personnels = personnelService.getAllPersonnels();
        tablePersonnels.setItems(FXCollections.observableArrayList(personnels));
    }

    @FXML
    void handleRechercherButton(ActionEvent event) {
        String terme = txtRecherche.getText().trim();

        if (terme.isEmpty()) {
            chargerPersonnels();
        } else {
            List<Personnel> resultats = personnelService.searchPersonnel(terme);
            tablePersonnels.setItems(FXCollections.observableArrayList(resultats));
        }
    }

    @FXML
    void handleRafraichirButton(ActionEvent event) {
        chargerPersonnels();
        txtRecherche.clear();
    }

    @FXML
    void handleAjouterButton(ActionEvent event) {
        ouvrirFormulairePersonnel(null);
    }

    @FXML
    void handleModifierButton(ActionEvent event) {
        Personnel selectedPersonnel = tablePersonnels.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            ouvrirFormulairePersonnel(selectedPersonnel);
        }
    }

    @FXML
    void handleSupprimerButton(ActionEvent event) {
        Personnel selectedPersonnel = tablePersonnels.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer ce personnel ?",
                    "Personnel: " + selectedPersonnel.getNomPersonnel() + " " + selectedPersonnel.getPrenomPersonnel());

            if (confirmation) {
                boolean success = personnelService.deletePersonnel(selectedPersonnel.getIdPersonnel());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "Le personnel a été supprimé avec succès.");
                    chargerPersonnels();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer le personnel.");
                }
            }
        }
    }

    @FXML
    void handleAttribuerVehiculeButton(ActionEvent event) {
        Personnel selectedPersonnel = tablePersonnels.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/personnel/form_attribution_vehicule.fxml"));
                Parent root = loader.load();

                FormAttributionVehiculeController controller = loader.getController();
                controller.setPersonnel(selectedPersonnel);

                Stage stage = new Stage();
                stage.setTitle("Attribution de véhicule");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(btnAttribuerVehicule.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isValide()) {
                    // Recharger la liste pour refléter les changements
                    chargerPersonnels();
                }
            } catch (IOException e) {
                AlertUtils.showErrorAlert("Erreur", "Impossible de charger la boîte de dialogue d'attribution.");
                e.printStackTrace();
            }
        }
    }

    private void ouvrirFormulairePersonnel(Personnel personnel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/personnel/form_personnel.fxml"));
            Parent root = loader.load();

            FormPersonnelController controller = loader.getController();
            controller.setPersonnelService(personnelService);
            controller.setPersonnel(personnel);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle(personnel == null ? "Ajouter un personnel" : "Modifier un personnel");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Recharger la liste après fermeture du formulaire
            chargerPersonnels();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire de personnel.");
            e.printStackTrace();
        }
    }

    // Méthode pour rafraîchir la liste (appelée depuis d'autres contrôleurs)
    public void refreshPersonnelList() {
        chargerPersonnels();
    }
}