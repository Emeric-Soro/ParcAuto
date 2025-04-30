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
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Role;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.StatistiquesService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

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
    private Label lblUserName;

    @FXML
    private Label lblUserRole;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDate;

    @FXML
    private StackPane contentArea;

    private StatistiquesService statistiquesService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statistiquesService = new StatistiquesService();

        // Définir la date du jour
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        lblDate.setText(LocalDate.now().format(formatter));

        // Charger le tableau de bord par défaut
        loadDashboard();
    }

    /**
     * Initialise les données de l'utilisateur connecté
     */
    public void initUserData() {
        Utilisateur user = SessionManager.getInstance().getUtilisateurConnecte();
        if (user != null) {
            lblUserName.setText(user.getLogin());

            Role role = user.getRole();
            if (role != null) {
                lblUserRole.setText(role.getNomRole());
            } else {
                lblUserRole.setText("Non défini");
            }

            // Configurer les permissions selon le rôle
            configurePermissions(user);
        }
    }

    /**
     * Configure les permissions selon le rôle de l'utilisateur
     */
    private void configurePermissions(Utilisateur user) {
        // Exemple de configuration des permissions
        if (!SessionManager.getInstance().hasPrivilege("GESTION_UTILISATEURS")) {
            btnUtilisateurs.setDisable(true);
            btnUtilisateurs.setVisible(false);
        }

        // Autres permissions à configurer selon les besoins
    }

    @FXML
    void handleDashboardButton(ActionEvent event) {
        setActiveButton(btnDashboard);
        lblTitle.setText("Tableau de bord");
        loadDashboard();
    }

    @FXML
    void handleVehiculesButton(ActionEvent event) {
        setActiveButton(btnVehicules);
        lblTitle.setText("Gestion des Véhicules");
        loadPage("vehicule/liste_vehicules.fxml");
    }

    @FXML
    void handleMissionsButton(ActionEvent event) {
        setActiveButton(btnMissions);
        lblTitle.setText("Gestion des Missions");
        loadPage("mission/liste_missions.fxml");
    }

    @FXML
    void handlePersonnelButton(ActionEvent event) {
        setActiveButton(btnPersonnel);
        lblTitle.setText("Gestion du Personnel");
        loadPage("personnel/personnels.fxml");
    }

    @FXML
    void handleEntretiensButton(ActionEvent event) {
        setActiveButton(btnEntretiens);
        lblTitle.setText("Gestion des Entretiens");
        loadPage("entretien/liste_entretiens.fxml");
    }

    @FXML
    void handleVisitesButton(ActionEvent event) {
        setActiveButton(btnVisites);
        lblTitle.setText("Gestion des Visites Techniques");
        loadPage("visite/liste_visites.fxml");
    }

    @FXML
    void handleAssurancesButton(ActionEvent event) {
        setActiveButton(btnAssurances);
        lblTitle.setText("Gestion des Assurances");
        loadPage("assurance/liste_assurances.fxml");
    }

    @FXML
    void handleUtilisateursButton(ActionEvent event) {
        setActiveButton(btnUtilisateurs);
        lblTitle.setText("Gestion des Utilisateurs");
        loadPage("utilisateur/utilisateur.fxml");
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        // Confirmation de déconnexion
        boolean confirm = AlertUtils.showConfirmationAlert(
                "Déconnexion",
                "Êtes-vous sûr de vouloir vous déconnecter ?",
                "Toutes les modifications non enregistrées seront perdues.");

        if (confirm) {
            // Déconnexion
            SessionManager.getInstance().deconnecter();

            // Retour à l'écran de connexion
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../../../../../resources/fxml/login.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("../../../../../resources/css/main.css").toExternalForm());

                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.setTitle("MiAuto - Connexion");
                stage.setScene(scene);
                stage.setMaximized(false);
                stage.setWidth(700);
                stage.setHeight(500);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Erreur", "Erreur lors du chargement de l'écran de connexion.");
            }
        }
    }

    /**
     * Charge le tableau de bord
     */
    private void loadDashboard() {
        loadPage("dashboard.fxml");
    }

    /**
     * Charge une page dans la zone de contenu
     * @param fxml Chemin du fichier FXML à charger
     */
    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/" + fxml));
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Erreur lors du chargement de la page: " + fxml);
        }
    }

    /**
     * Définit le bouton actif dans le menu
     * @param activeButton Bouton à activer
     */
    private void setActiveButton(Button activeButton) {
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
        activeButton.getStyleClass().add("active");
    }

    public StackPane getContentArea() {
        return null;
    }
}