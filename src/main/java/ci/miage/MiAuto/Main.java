package main.java.ci.miage.MiAuto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.config.DatabaseConnection;

/**
 * Classe principale de l'application de gestion de parc automobile
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Charger la vue principale
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../../../resources/fxml/vehicule/liste_vehicules.fxml"));
            Parent root = loader.load();

            // Configurer la scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../../../../resources/css/main.css").toExternalForm());

            // Configurer la fenêtre principale
            primaryStage.setTitle("MiAuto - Gestion de Parc Automobile");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Méthode appelée lors de la fermeture de l'application
     */
    @Override
    public void stop() {
        // Fermer proprement la connexion à la base de données
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Application fermée, connexion à la base de données fermée.");
    }

    /**
     * Point d'entrée principal de l'application
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}