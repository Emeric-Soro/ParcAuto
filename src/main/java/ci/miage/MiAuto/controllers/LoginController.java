package main.java.ci.miage.MiAuto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la page de connexion
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    private AuthentificationService authentificationService;

    /**
     * Initialise le contrôleur
     *
     * @param url URL de localisation
     * @param rb  Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser le service d'authentification
        authentificationService = new AuthentificationService();

        // Cacher le message d'erreur au démarrage
        lblError.setText("");

        // Ajouter un écouteur pour permettre la connexion avec la touche Entrée
        txtPassword.setOnAction(this::handleLoginButton);
    }

    /**
     * Gère le clic sur le bouton de connexion
     *
     * @param event Événement de clic
     */
    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        // Vérifier que les champs ne sont pas vides
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Veuillez remplir tous les champs");
            return;
        }

        try {
            // Tenter l'authentification
            Utilisateur utilisateur = authentificationService.authentifier(username, password);

            if (utilisateur != null) {
                // Authentification réussie
                // Stocker l'utilisateur dans la session
                SessionManager.getInstance().setUtilisateurConnecte(utilisateur);

                // Ouvrir la page principale
                ouvrirDashboard();

                // Fermer la fenêtre de connexion
                ((Stage) btnLogin.getScene().getWindow()).close();
            } else {
                // Authentification échouée
                lblError.setText("Nom d'utilisateur ou mot de passe incorrect");
            }
        } catch (Exception e) {
            lblError.setText("Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ouvre le tableau de bord principal
     */
    private void ouvrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../resources/fxml/main.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("MiAuto - Gestion du Parc Automobile");
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblError.setText("Erreur lors de l'ouverture du tableau de bord: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

