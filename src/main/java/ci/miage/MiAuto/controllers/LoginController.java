package main.java.ci.miage.MiAuto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.AuthentificationService;
import main.java.ci.miage.MiAuto.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    private AuthentificationService authentificationService;

    public LoginController() {
        this.authentificationService = new AuthentificationService();
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        // Validation basique
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        // Tentative d'authentification
        Utilisateur utilisateur = authentificationService.authenticate(username, password);

        if (utilisateur != null) {
            // Stockage de l'utilisateur connecté dans la session
            SessionManager.getInstance().setUtilisateurConnecte(utilisateur);

            // Mise à jour de la dernière connexion
            authentificationService.updateLastConnection(utilisateur.getIdUtilisateur());

            // Ouvrir l'écran principal
            openMainScreen();
        } else {
            showError("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }

    private void openMainScreen() {
        try {
            // Charger l'écran principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/main.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur principal pour initialiser les données utilisateur
            DashboardController controller = loader.getController();
            controller.initUserData();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../../../../../resources/css/main.css").toExternalForm());

            // Récupérer la fenêtre de connexion et la reconfigurer
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setTitle("MiAuto - Gestion du Parc Automobile");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de l'application.");
        }
    }
}