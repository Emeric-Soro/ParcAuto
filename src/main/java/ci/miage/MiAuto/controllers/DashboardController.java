package main.java.ci.miage.MiAuto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.AuthentificationService;
import main.java.ci.miage.MiAuto.services.StatistiquesService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le tableau de bord principal
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserRole;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTotalVehicules;

    @FXML
    private Label lblVehiculesEnMission;

    @FXML
    private Label lblVehiculesDisponibles;

    @FXML
    private Label lblVisitesAPlanifier;

    @FXML
    private Label lblEntretiensEnCours;

    @FXML
    private VBox activitesContainer;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnVehicules;

    @FXML
    private Button btnMissions;

    @FXML
    private Button btnPersonnel;

    @FXML
    private Button btnEntretiens;

    @FXML
    private Button btnVisites;

    @FXML
    private Button btnAssurances;

    @FXML
    private Button btnUtilisateurs;

    @FXML
    private Button btnLogout;

    private VehiculeService vehiculeService;
    private StatistiquesService statistiquesService;
    private AuthentificationService authentificationService;

    /**
     * Initialise le contrôleur
     * @param url URL de localisation
     * @param rb Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser les services
        vehiculeService = new VehiculeService();
        statistiquesService = new StatistiquesService();
        authentificationService = new AuthentificationService();

        // Afficher la date actuelle
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")));

        // Charger les informations de l'utilisateur connecté
        chargerInfosUtilisateur();

        // Charger les statistiques
        chargerStatistiques();

        // Activer le bouton Dashboard
        setActiveButton(btnDashboard);
    }

    /**
     * Charge les informations de l'utilisateur connecté
     */
    private void chargerInfosUtilisateur() {
        Utilisateur utilisateur = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateur != null) {
            lblUserName.setText(utilisateur.getLogin());
            if (utilisateur.getRole() != null) {
                lblUserRole.setText(utilisateur.getRole().getNomRole());
            } else {
                lblUserRole.setText("Utilisateur");
            }
        }
    }

    /**
     * Charge les statistiques du tableau de bord
     */
    private void chargerStatistiques() {
        try {
            // Statistiques des véhicules
            int totalVehicules = statistiquesService.getNombreTotalVehicules();
            int vehiculesEnMission = statistiquesService.getNombreVehiculesEnMission();
            int vehiculesDisponibles = statistiquesService.getNombreVehiculesDisponibles();

            lblTotalVehicules.setText(String.valueOf(totalVehicules));
            lblVehiculesEnMission.setText(String.valueOf(vehiculesEnMission));
            lblVehiculesDisponibles.setText(String.valueOf(vehiculesDisponibles));

            // Statistiques de maintenance
            int visitesAPlanifier = statistiquesService.getNombreVisitesAPlanifier();
            int entretiensEnCours = statistiquesService.getNombreEntretiensEnCours();

            lblVisitesAPlanifier.setText(String.valueOf(visitesAPlanifier));
            lblEntretiensEnCours.setText(String.valueOf(entretiensEnCours));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur le bouton Tableau de bord
     * @param event Événement de clic
     */
    @FXML
    private void handleDashboardButton(ActionEvent event) {
        setActiveButton(btnDashboard);
        // Le tableau de bord est déjà affiché, pas besoin de charger une autre vue
    }

    /**
     * Gère le clic sur le bouton Véhicules
     * @param event Événement de clic
     */
    @FXML
    private void handleVehiculesButton(ActionEvent event) {
        setActiveButton(btnVehicules);
        loadPage("vehicule/liste_vehicules");
    }

    /**
     * Gère le clic sur le bouton Missions
     * @param event Événement de clic
     */
    @FXML
    private void handleMissionsButton(ActionEvent event) {
        setActiveButton(btnMissions);
        loadPage("mission/liste_missions");
    }

    /**
     * Gère le clic sur le bouton Personnel
     * @param event Événement de clic
     */
    @FXML
    private void handlePersonnelButton(ActionEvent event) {
        setActiveButton(btnPersonnel);
        loadPage("personnel/liste_personnels");
    }

    /**
     * Gère le clic sur le bouton Entretiens
     * @param event Événement de clic
     */
    @FXML
    private void handleEntretiensButton(ActionEvent event) {
        setActiveButton(btnEntretiens);
        loadPage("entretien/liste_entretiens");
    }

    /**
     * Gère le clic sur le bouton Visites
     * @param event Événement de clic
     */
    @FXML
    private void handleVisitesButton(ActionEvent event) {
        setActiveButton(btnVisites);
        loadPage("visite/liste_visites");
    }

    /**
     * Gère le clic sur le bouton Assurances
     * @param event Événement de clic
     */
    @FXML
    private void handleAssurancesButton(ActionEvent event) {
        setActiveButton(btnAssurances);
        loadPage("assurance/liste_assurances");
    }

    /**
     * Gère le clic sur le bouton Utilisateurs
     * @param event Événement de clic
     */
    @FXML
    private void handleUtilisateursButton(ActionEvent event) {
        setActiveButton(btnUtilisateurs);
        loadPage("utilisateur/liste_utilisateurs");
    }

    /**
     * Gère le clic sur le bouton de déconnexion
     * @param event Événement de clic
     */
    @FXML
    private void handleLogoutButton(ActionEvent event) {
        // Déconnecter l'utilisateur
        authentificationService.deconnecter();
        SessionManager.getInstance().deconnecter();

        try {
            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.close();

            // Ouvrir la fenêtre de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("MiAuto - Connexion");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ouvrir la page de connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge une page dans la zone de contenu
     * @param page Nom de la page à charger (sans l'extension .fxml)
     */
    private void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + page + ".fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger la page " + page + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Définit le bouton actif dans le menu
     * @param btn Bouton à activer
     */
    private void setActiveButton(Button btn) {
        // Réinitialiser tous les boutons
        btnDashboard.getStyleClass().remove("active");
        btnVehicules.getStyleClass().remove("active");
        btnMissions.getStyleClass().remove("active");
        btnPersonnel.getStyleClass().remove("active");
        btnEntretiens.getStyleClass().remove("active");
        btnVisites.getStyleClass().remove("active");
        btnAssurances.getStyleClass().remove("active");
        btnUtilisateurs.getStyleClass().remove("active");

        // Activer le bouton sélectionné
        btn.getStyleClass().add("active");
    }
}