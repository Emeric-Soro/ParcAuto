package main.java.ci.miage.MiAuto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.controllers.DashboardController;
import main.java.ci.miage.MiAuto.controllers.MainController;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Point d'entrée principal de l'application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Déterminer le mode de chargement selon la structure du projet
            String basePath = determinerCheminBase();

            // Charger le login FXML
            URL loginUrl = getClass().getResource(basePath + "fxml/login.fxml");
            if (loginUrl == null) {
                // Si toujours pas trouvé, utiliser un chemin absolu
                loginUrl = new URL("file:src/main/resources/fxml/login.fxml");
            }

            // Afficher le chemin pour le debug
            System.out.println("Chemin de login.fxml: " + loginUrl);

            // Charger la page de login
            FXMLLoader loader = new FXMLLoader(loginUrl);
            Parent root = loader.load();

            // Configurer la scène
            Scene scene = new Scene(root);

            // Rechercher le CSS
            URL cssUrl = getClass().getResource(basePath + "css/main.css");
            if (cssUrl == null) {
                cssUrl = new URL("file:src/main/resources/css/main.css");
            }

            // Afficher le chemin pour le debug
            System.out.println("Chemin de main.css: " + cssUrl);

            // Appliquer le CSS s'il est trouvé
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            // Configurer et afficher la fenêtre
            primaryStage.setTitle("MiAuto - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible de démarrer l'application", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Détermine le chemin de base pour les ressources selon la structure du projet
     * @return Le chemin de base avec le séparateur à la fin
     */
    private String determinerCheminBase() {
        // Tester différents chemins pour trouver celui qui fonctionne
        String[] cheminsTest = {
                "/",
                "/resources/",
                "/main/resources/"
        };

        for (String chemin : cheminsTest) {
            if (getClass().getResource(chemin + "fxml/login.fxml") != null) {
                System.out.println("Chemin de base trouvé: " + chemin);
                return chemin;
            }
        }

        // Valeur par défaut si aucun chemin ne fonctionne
        System.out.println("Aucun chemin de base trouvé, utilisation de la valeur par défaut: /");
        return "/";
    }

    /**
     * Ouvre la fenêtre principale du tableau de bord
     * @param primaryStage Stage existant à réutiliser ou null pour en créer un nouveau
     */
    public static void openMainWindow(Stage primaryStage) {
        try {
            // Déterminer le chemin de base
            String basePath = "";
            URL testUrl = Main.class.getResource("/fxml/main.fxml");
            if (testUrl != null) {
                basePath = "/";
            } else {
                testUrl = Main.class.getResource("/resources/fxml/main.fxml");
                if (testUrl != null) {
                    basePath = "/resources/";
                } else {
                    testUrl = Main.class.getResource("/main/resources/fxml/main.fxml");
                    if (testUrl != null) {
                        basePath = "/main/resources/";
                    }
                }
            }

            // Si aucun chemin ne fonctionne, essayer le chemin absolu
            URL mainUrl;
            if (basePath.isEmpty()) {
                mainUrl = new URL("file:src/main/resources/fxml/main.fxml");
            } else {
                mainUrl = Main.class.getResource(basePath + "fxml/main.fxml");
            }

            // Afficher le chemin pour le debug
            System.out.println("Chemin de main.fxml: " + mainUrl);

            // Charger le fichier FXML principal
            FXMLLoader loader = new FXMLLoader(mainUrl);
            Parent root = loader.load();

            // Créer une nouvelle fenêtre si nécessaire
            if (primaryStage == null) {
                primaryStage = new Stage();
            }

            // Configurer la scène
            Scene scene = new Scene(root);

            // Rechercher le CSS
            URL cssUrl;
            if (basePath.isEmpty()) {
                cssUrl = new URL("file:src/main/resources/css/main.css");
            } else {
                cssUrl = Main.class.getResource(basePath + "css/main.css");
            }

            // Appliquer le CSS s'il est trouvé
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setTitle("MiAuto - Gestion du parc automobile");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            // IMPORTANT: Récupérer la référence au contentArea dans le DashboardController
            DashboardController dashboardController = loader.getController();

            // Utilisation de Platform.runLater pour garantir que l'initialisation de contentArea est terminée
            javafx.application.Platform.runLater(() -> {
                StackPane contentArea = dashboardController.getContentArea();

                if (contentArea != null) {
                    System.out.println("ContentArea récupéré avec succès dans openMainWindow");

                    // Configurer le MainController avec la zone de contenu
                    MainController.getInstance().setContentArea(contentArea);

                    // Charger le contenu du tableau de bord
                    MainController.getInstance().loadDashboard();
                } else {
                    System.err.println("ERREUR: ContentArea est null dans openMainWindow");
                }
            });

        } catch (Exception e) {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ouvrir la fenêtre principale", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode principale
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}