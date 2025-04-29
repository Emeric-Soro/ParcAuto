package main.java.ci.miage.MiAuto.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import main.java.ci.miage.MiAuto.Main;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Contrôleur principal pour gérer la navigation entre les différentes vues
 * Implémente le pattern Singleton pour être accessible de partout
 */
public class MainController {

    private static MainController instance;
    private StackPane contentArea;

    /**
     * Constructeur privé (pattern Singleton)
     */
    private MainController() {
    }

    /**
     * Récupère l'instance unique du contrôleur
     * @return L'instance du contrôleur
     */
    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Définit la zone de contenu pour y charger les vues
     * @param contentArea Zone de contenu
     */
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }

    /**
     * Navigue vers une vue spécifique
     * @param fxmlPath Chemin du fichier FXML (sans extension ".fxml")
     */
    public void navigateTo(String fxmlPath) {
        try {
            if (contentArea == null) {
                throw new IllegalStateException("La zone de contenu n'a pas été initialisée");
            }

            // Essayer plusieurs chemins possibles
            URL fxmlUrl = Main.class.getResource("/main/resources/fxml/" + fxmlPath + ".fxml");
            if (fxmlUrl == null) {
                fxmlUrl = Main.class.getResource("/main/resources/fxml/" + fxmlPath + ".fxml");
                if (fxmlUrl == null) {
                    fxmlUrl = Main.class.getResource("/main/resources/fxml/" + fxmlPath + ".fxml");
                    if (fxmlUrl == null) {
                        fxmlUrl = new URL("file:src/main/resources/fxml/" + fxmlPath + ".fxml");
                    }
                }
            }

            // Afficher le chemin pour le debug
            System.out.println("Chargement de: " + fxmlUrl);

            // Charger la vue FXML
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Vider la zone de contenu et ajouter la nouvelle vue
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

        } catch (IOException e) {
            AlertUtils.showErrorAlert("Erreur de navigation",
                    "Impossible de charger la page " + fxmlPath,
                    e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge le tableau de bord principal
     */
    public void loadDashboard() {
        navigateTo("dashboard_content");
    }
}