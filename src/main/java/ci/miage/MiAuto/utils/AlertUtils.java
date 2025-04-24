package main.java.ci.miage.MiAuto.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Classe utilitaire pour l'affichage de boîtes de dialogue
 */
public class AlertUtils {

    /**
     * Affiche une boîte de dialogue d'information
     * @param titre Titre de la boîte de dialogue
     * @param message Message à afficher
     */
    public static void showInformationAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'erreur
     * @param titre Titre de la boîte de dialogue
     * @param message Message d'erreur à afficher
     */
    public static void showErrorAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec un en-tête et un contenu détaillé
     * @param titre Titre de la boîte de dialogue
     * @param header En-tête de la boîte de dialogue
     * @param content Contenu détaillé
     */
    public static void showErrorAlert(String titre, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'avertissement
     * @param titre Titre de la boîte de dialogue
     * @param message Message d'avertissement
     */
    public static void showWarningAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue de confirmation et retourne la réponse de l'utilisateur
     * @param titre Titre de la boîte de dialogue
     * @param header En-tête de la boîte de dialogue
     * @param message Message de confirmation
     * @return true si l'utilisateur a confirmé, false sinon
     */
    public static boolean showConfirmationAlert(String titre, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}