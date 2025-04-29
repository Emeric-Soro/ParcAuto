package main.java.ci.miage.MiAuto.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.Main;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.ActiviteLogService;
import main.java.ci.miage.MiAuto.services.AuthentificationService;
import main.java.ci.miage.MiAuto.services.StatistiquesService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private Label lblTitle;

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
    private ActiviteLogService activiteLogService;

    /**
     * Initialise le contrôleur
     * @param url URL de localisation
     * @param rb Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("DashboardController.initialize() called");

        // Initialiser les services
        vehiculeService = new VehiculeService();
        statistiquesService = new StatistiquesService();
        authentificationService = new AuthentificationService();
        activiteLogService = new ActiviteLogService();

        // Vérifier si contentArea est initialisé
        if (contentArea == null) {
            System.err.println("ERREUR: contentArea est null dans initialize()");
        } else {
            System.out.println("contentArea initialisé correctement dans initialize()");

            // Important: Initialiser le MainController ici
            MainController.getInstance().setContentArea(contentArea);
        }

        // Afficher la date actuelle
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")));

        // Charger les informations de l'utilisateur connecté
        chargerInfosUtilisateur();

        // Activer le bouton Dashboard et définir le titre
        setActiveButton(btnDashboard);
        lblTitle.setText("Tableau de bord");
    }

    /**
     * Getter pour la zone de contenu (utilisé par MainController)
     * @return La zone de contenu
     */
    public StackPane getContentArea() {
        System.out.println("DashboardController.getContentArea() called");
        if (contentArea == null) {
            System.err.println("ERREUR: contentArea est null dans getContentArea()");
        }
        return contentArea;
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
        } else {
            // Valeurs par défaut si l'utilisateur n'est pas connecté
            lblUserName.setText("Utilisateur");
            lblUserRole.setText("Invité");
        }
    }

    /**
     * Gère le clic sur le bouton Tableau de bord
     * @param event Événement de clic
     */
    @FXML
    private void handleDashboardButton(ActionEvent event) {
        setActiveButton(btnDashboard);
        lblTitle.setText("Tableau de bord");

        // Utiliser MainController pour charger le tableau de bord
        MainController.getInstance().loadDashboard();
    }

    /**
     * Gère le clic sur le bouton Véhicules
     * @param event Événement de clic
     */
    @FXML
    private void handleVehiculesButton(ActionEvent event) {
        setActiveButton(btnVehicules);
        lblTitle.setText("Gestion des Véhicules");

        // Utiliser MainController pour naviguer vers la vue des véhicules
        MainController.getInstance().navigateTo("vehicule/liste_vehicules");
    }

    /**
     * Gère le clic sur le bouton Missions
     * @param event Événement de clic
     */
    @FXML
    private void handleMissionsButton(ActionEvent event) {
        setActiveButton(btnMissions);
        lblTitle.setText("Gestion des Missions");

        // Utiliser MainController pour naviguer vers la vue des missions
        MainController.getInstance().navigateTo("mission/liste_missions");
    }

    /**
     * Gère le clic sur le bouton Personnel
     * @param event Événement de clic
     */
    @FXML
    private void handlePersonnelButton(ActionEvent event) {
        setActiveButton(btnPersonnel);
        lblTitle.setText("Gestion du Personnel");

        // Utiliser MainController pour naviguer vers la vue du personnel
        MainController.getInstance().navigateTo("personnel/personnels");
    }

    /**
     * Gère le clic sur le bouton Entretiens
     * @param event Événement de clic
     */
    @FXML
    private void handleEntretiensButton(ActionEvent event) {
        setActiveButton(btnEntretiens);
        lblTitle.setText("Gestion des Entretiens");

        // Utiliser MainController pour naviguer vers la vue des entretiens
        MainController.getInstance().navigateTo("entretien/liste_entretiens");
    }

    /**
     * Gère le clic sur le bouton Visites
     * @param event Événement de clic
     */
    @FXML
    private void handleVisitesButton(ActionEvent event) {
        setActiveButton(btnVisites);
        lblTitle.setText("Gestion des Visites Techniques");

        // Utiliser MainController pour naviguer vers la vue des visites techniques
        MainController.getInstance().navigateTo("visite/liste_visites");
    }

    /**
     * Gère le clic sur le bouton Assurances
     * @param event Événement de clic
     */
    @FXML
    private void handleAssurancesButton(ActionEvent event) {
        setActiveButton(btnAssurances);
        lblTitle.setText("Gestion des Assurances");

        // Utiliser MainController pour naviguer vers la vue des assurances
        MainController.getInstance().navigateTo("assurance/liste_assurances");
    }

    /**
     * Gère le clic sur le bouton Utilisateurs
     * @param event Événement de clic
     */
    @FXML
    private void handleUtilisateursButton(ActionEvent event) {
        setActiveButton(btnUtilisateurs);
        lblTitle.setText("Gestion des Utilisateurs");

        // Utiliser MainController pour naviguer vers la vue des utilisateurs
        MainController.getInstance().navigateTo("utilisateur/utilisateurs");
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

            // Ouvrir une nouvelle fenêtre de connexion
            FXMLLoader loader = new FXMLLoader();
            URL loginUrl = getClass().getResource("/fxml/login.fxml");

            // Si le chemin direct ne fonctionne pas, essayer d'autres chemins courants
            if (loginUrl == null) {
                loginUrl = getClass().getResource("/resources/fxml/login.fxml");
                if (loginUrl == null) {
                    loginUrl = getClass().getResource("/main/resources/fxml/login.fxml");
                    if (loginUrl == null) {
                        loginUrl = new URL("file:src/main/resources/fxml/login.fxml");
                    }
                }
            }

            loader.setLocation(loginUrl);
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("MiAuto - Connexion");
            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (Exception e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ouvrir la page de connexion: " + e.getMessage());
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