package main.java.ci.miage.MiAuto.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.services.ActiviteLogService;
import main.java.ci.miage.MiAuto.services.StatistiquesService;
import main.java.ci.miage.MiAuto.services.VehiculeService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le contenu du tableau de bord
 */
public class DashboardContentController implements Initializable {

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

    private VehiculeService vehiculeService;
    private StatistiquesService statistiquesService;
    private ActiviteLogService activiteLogService;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vehiculeService = new VehiculeService();
        statistiquesService = new StatistiquesService();
        activiteLogService = new ActiviteLogService();

        // Charger les statistiques
        chargerStatistiques();

        // Charger les activités récentes
        chargerActivitesRecentes();
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

            // En cas d'erreur, afficher des valeurs par défaut
            lblTotalVehicules.setText("--");
            lblVehiculesEnMission.setText("--");
            lblVehiculesDisponibles.setText("--");
            lblVisitesAPlanifier.setText("--");
            lblEntretiensEnCours.setText("--");
        }
    }

    /**
     * Charge les activités récentes dans le conteneur d'activités
     */
    private void chargerActivitesRecentes() {
        // Chargement asynchrone pour ne pas bloquer l'interface utilisateur
        new Thread(() -> {
            try {
                // Récupérer les 10 dernières activités
                List<ActiviteLog> activitesRecentes = activiteLogService.getActivitesRecentes(10);

                // Mettre à jour l'interface utilisateur sur le thread JavaFX
                Platform.runLater(() -> {
                    // Vider le conteneur
                    activitesContainer.getChildren().clear();

                    if (activitesRecentes.isEmpty()) {
                        // Message si aucune activité n'est trouvée
                        Label lblNoActivities = new Label("Aucune activité récente à afficher");
                        lblNoActivities.getStyleClass().add("activity-item");
                        lblNoActivities.setPadding(new Insets(15));
                        activitesContainer.getChildren().add(lblNoActivities);
                    } else {
                        // Afficher chaque activité
                        for (ActiviteLog activite : activitesRecentes) {
                            activitesContainer.getChildren().add(creerItemActivite(activite));
                        }
                    }
                });
            } catch (Exception e) {
                // Gérer les erreurs
                System.err.println("Erreur lors du chargement des activités récentes: " + e.getMessage());
                e.printStackTrace();

                // Afficher un message d'erreur sur l'interface utilisateur
                Platform.runLater(() -> {
                    activitesContainer.getChildren().clear();
                    Label lblError = new Label("Erreur lors du chargement des activités récentes");
                    lblError.getStyleClass().add("activity-item");
                    lblError.setPadding(new Insets(15));
                    activitesContainer.getChildren().add(lblError);
                });
            }
        }).start();
    }

    /**
     * Crée un élément d'interface pour afficher une activité
     * @param activite L'activité à afficher
     * @return L'élément HBox représentant l'activité
     */
    private HBox creerItemActivite(ActiviteLog activite) {
        // Créer le conteneur principal
        HBox activityItem = new HBox();
        activityItem.getStyleClass().add("activity-item");
        activityItem.setPadding(new Insets(10, 15, 10, 15));
        activityItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        activityItem.setSpacing(5);

        // Texte principal de l'activité
        Label lblDescription = new Label(activite.getDescription());
        activityItem.getChildren().add(lblDescription);

        // Ajouter un espace flexible
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        activityItem.getChildren().add(spacer);

        // Ajouter le temps écoulé
        String tempsRelative = activiteLogService.formatDateRelative(activite.getDateActivite());
        Label lblTime = new Label(tempsRelative);
        lblTime.getStyleClass().add("activity-time");
        activityItem.getChildren().add(lblTime);

        return activityItem;
    }
}