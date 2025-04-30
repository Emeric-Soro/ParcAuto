package main.java.ci.miage.MiAuto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.services.StatistiquesService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

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

    private StatistiquesService statistiquesService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statistiquesService = new StatistiquesService();

        // Charger les statistiques
        chargerStatistiques();

        // Charger les activités récentes
        chargerActivitesRecentes();
    }

    /**
     * Charge les statistiques dans le tableau de bord
     */
    private void chargerStatistiques() {
        int totalVehicules = statistiquesService.getTotalVehicules();
        int vehiculesEnMission = statistiquesService.getVehiculesEnMission();
        int vehiculesDisponibles = statistiquesService.getVehiculesDisponibles();
        int visitesAPlanifier = statistiquesService.getVehiculesVisiteAPlanifier(30); // 30 jours avant expiration
        int entretiensEnCours = statistiquesService.getEntretiensEnCours();

        // Mettre à jour les labels
        lblTotalVehicules.setText(String.valueOf(totalVehicules));
        lblVehiculesEnMission.setText(String.valueOf(vehiculesEnMission));
        lblVehiculesDisponibles.setText(String.valueOf(vehiculesDisponibles));
        lblVisitesAPlanifier.setText(String.valueOf(visitesAPlanifier));
        lblEntretiensEnCours.setText(String.valueOf(entretiensEnCours));
    }

    /**
     * Charge les activités récentes dans le tableau de bord
     */
    private void chargerActivitesRecentes() {
        List<ActiviteLog> activitesRecentes = statistiquesService.getActivitesRecentes(5);

        // Vider le conteneur
        activitesContainer.getChildren().clear();

        // Si aucune activité, afficher un message
        if (activitesRecentes.isEmpty()) {
            Label lblNoActivity = new Label("Aucune activité récente à afficher");
            lblNoActivity.getStyleClass().add("activity-item");
            activitesContainer.getChildren().add(lblNoActivity);
            return;
        }

        // Formatter pour les dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Ajouter chaque activité
        for (ActiviteLog activite : activitesRecentes) {
            HBox activityItem = createActivityItem(activite, formatter);
            activitesContainer.getChildren().add(activityItem);
        }
    }

    /**
     * Crée un élément d'activité pour l'affichage
     * @param activite Activité à afficher
     * @param formatter Formatter pour les dates
     * @return HBox contenant l'activité formatée
     */
    private HBox createActivityItem(ActiviteLog activite, DateTimeFormatter formatter) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("activity-item");
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Description de l'activité
        Label lblDescription = new Label(activite.getDescription());
        hbox.getChildren().add(lblDescription);

        // Espace flexible
        Pane spacer = new Pane();
        spacer.setPrefHeight(200);
        spacer.setPrefWidth(200);
        hbox.getChildren().add(spacer);

        // Date de l'activité
        Label lblTime = new Label(formatTimeAgo(activite.getDateActivite()));
        lblTime.getStyleClass().add("activity-time");
        hbox.getChildren().add(lblTime);

        hbox.setPadding(new javafx.geometry.Insets(10, 15, 10, 15));

        return hbox;
    }

    /**
     * Formate une date en "il y a X temps"
     * @param dateTime Date à formater
     * @return Chaîne formatée
     */
    private String formatTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);

        if (minutes < 60) {
            return "Il y a " + minutes + " minute" + (minutes > 1 ? "s" : "");
        }

        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) {
            return "Il y a " + hours + " heure" + (hours > 1 ? "s" : "");
        }

        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) {
            return "Il y a " + days + " jour" + (days > 1 ? "s" : "");
        }

        // Sinon, afficher la date exacte
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}