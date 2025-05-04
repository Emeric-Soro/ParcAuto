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
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.UtilisateurService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UtilisateurController implements Initializable {

    @FXML
    private TextField txtRecherche;

    @FXML
    private TableView<Utilisateur> tableUtilisateurs;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colLogin;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, String> colRole;

    @FXML
    private TableColumn<Utilisateur, String> colPersonnel;

    @FXML
    private TableColumn<Utilisateur, String> colStatut;

    @FXML
    private TableColumn<Utilisateur, String> colDerniereConnexion;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnActiverDesactiver;

    @FXML
    private Button btnRafraichir;

    private UtilisateurService utilisateurService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utilisateurService = new UtilisateurService();

        // Configurer les colonnes de la table
        configureTable();

        // Charger les données initiales
        chargerUtilisateurs();

        // Ajouter un écouteur de sélection pour activer/désactiver les boutons
        tableUtilisateurs.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disableButtons = (newValue == null);
                    btnModifier.setDisable(disableButtons);
                    btnSupprimer.setDisable(disableButtons);
                    btnActiverDesactiver.setDisable(disableButtons);
                    if (!disableButtons) {
                        btnActiverDesactiver.setText(newValue.isStatut() ? "Désactiver" : "Activer");
                    }
                });
    }

    /**
     * Configure les colonnes de la table
     */
    private void configureTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Pour afficher le nom du rôle
        colRole.setCellValueFactory(cellData -> {
            if (cellData.getValue().getRole() != null) {
                return new SimpleStringProperty(cellData.getValue().getRole().getNomRole());
            } else {
                return new SimpleStringProperty("");
            }
        });

        // Pour afficher le nom complet du personnel
        colPersonnel.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPersonnel() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getPersonnel().getPrenomPersonnel() + " " +
                                cellData.getValue().getPersonnel().getNomPersonnel()
                );
            } else {
                return new SimpleStringProperty("");
            }
        });

        // Pour afficher "Actif" ou "Inactif"
        colStatut.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isStatut() ? "Actif" : "Inactif"));

        // Pour formater la date de dernière connexion
        colDerniereConnexion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDerniereConnexion() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(cellData.getValue().getDerniereConnexion().format(formatter));
            } else {
                return new SimpleStringProperty("");
            }
        });
    }

    /**
     * Charge la liste des utilisateurs dans la table
     */
    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        tableUtilisateurs.setItems(FXCollections.observableArrayList(utilisateurs));
    }

    /**
     * Gère le clic sur le bouton de recherche
     */
    @FXML
    private void handleRechercherButton(ActionEvent event) {
        String recherche = txtRecherche.getText().trim();
        if (recherche.isEmpty()) {
            chargerUtilisateurs();
            return;
        }

        List<Utilisateur> allUtilisateurs = utilisateurService.getAllUtilisateurs();
        List<Utilisateur> resultats = new ArrayList<>();

        for (Utilisateur utilisateur : allUtilisateurs) {
            if (utilisateur.getLogin().toLowerCase().contains(recherche.toLowerCase()) ||
                    (utilisateur.getEmail() != null && utilisateur.getEmail().toLowerCase().contains(recherche.toLowerCase()))) {
                resultats.add(utilisateur);
            }
        }

        tableUtilisateurs.setItems(FXCollections.observableArrayList(resultats));
    }

    /**
     * Gère le clic sur le bouton d'ajout
     */
    @FXML
    private void handleAjouterButton(ActionEvent event) {
        ouvrirFormulaireUtilisateur(null);
    }

    /**
     * Gère le clic sur le bouton de modification
     */
    @FXML
    private void handleModifierButton(ActionEvent event) {
        Utilisateur selectedUtilisateur = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            ouvrirFormulaireUtilisateur(selectedUtilisateur);
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    /**
     * Gère le clic sur le bouton de suppression
     */
    @FXML
    private void handleSupprimerButton(ActionEvent event) {
        Utilisateur selectedUtilisateur = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert(
                    "Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer cet utilisateur ?",
                    "Utilisateur: " + selectedUtilisateur.getLogin());

            if (confirmation) {
                boolean success = utilisateurService.deleteUtilisateur(selectedUtilisateur.getIdUtilisateur());
                if (success) {
                    AlertUtils.showInformationAlert("Suppression réussie",
                            "L'utilisateur a été supprimé avec succès.");
                    chargerUtilisateurs();
                } else {
                    AlertUtils.showErrorAlert("Erreur de suppression",
                            "Impossible de supprimer l'utilisateur.");
                }
            }
        } else {
            AlertUtils.showWarningAlert("Sélection requise", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    /**
     * Gère le clic sur le bouton d'activation/désactivation
     */
    @FXML
    private void handleActiverDesactiverButton(ActionEvent event) {
        Utilisateur selectedUtilisateur = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            String action = selectedUtilisateur.isStatut() ? "désactiver" : "activer";
            boolean confirmation = AlertUtils.showConfirmationAlert(
                    "Confirmation",
                    "Êtes-vous sûr de vouloir " + action + " cet utilisateur ?",
                    "Utilisateur: " + selectedUtilisateur.getLogin());

            if (confirmation) {
                boolean success;
                if (selectedUtilisateur.isStatut()) {
                    success = utilisateurService.desactiverUtilisateur(selectedUtilisateur.getIdUtilisateur());
                } else {
                    success = utilisateurService.activerUtilisateur(selectedUtilisateur.getIdUtilisateur());
                }

                if (success) {
                    AlertUtils.showInformationAlert("Succès",
                            "L'utilisateur a été " + (selectedUtilisateur.isStatut() ? "désactivé" : "activé") + " avec succès.");
                    chargerUtilisateurs();
                } else {
                    AlertUtils.showErrorAlert("Erreur",
                            "Impossible de " + action + " l'utilisateur.");
                }
            }
        }
    }

    /**
     * Gère le clic sur le bouton de rafraîchissement
     */
    @FXML
    private void handleRafraichirButton(ActionEvent event) {
        chargerUtilisateurs();
        txtRecherche.clear();
    }

    /**
     * Ouvre le formulaire d'ajout/modification d'utilisateur
     */
    private void ouvrirFormulaireUtilisateur(Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/utilisateur/form_utilisateur.fxml"));
            Parent root = loader.load();

            FormUtilisateurController controller = loader.getController();
            controller.setParentController(this);
            controller.setUtilisateur(utilisateur);

            Stage stage = new Stage();
            stage.setTitle(utilisateur == null ? "Ajouter un utilisateur" : "Modifier un utilisateur");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAjouter.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger le formulaire d'utilisateur.");
            e.printStackTrace();
        }
    }

    /**
     * Rafraîchit la liste des utilisateurs
     */
    public void refreshUtilisateurList() {
        chargerUtilisateurs();
    }
}