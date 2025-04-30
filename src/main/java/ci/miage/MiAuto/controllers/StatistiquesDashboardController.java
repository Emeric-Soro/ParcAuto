package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.EtatVoiture;
import main.java.ci.miage.MiAuto.services.StatistiquesService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.services.MissionService;
import main.java.ci.miage.MiAuto.services.EntretienService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le tableau de bord des statistiques
 */
public class StatistiquesDashboardController implements Initializable {

    @FXML
    private PieChart chartEtatVehicules;

    @FXML
    private BarChart<String, Number> chartActivitesRecentes;

    @FXML
    private Label lblTotalVehicules;

    @FXML
    private Label lblVehiculesDisponibles;

    @FXML
    private Label lblVehiculesEnMission;

    @FXML
    private Label lblEntretiensEnCours;

    @FXML
    private Label lblVisitesAPlanifier;

    @FXML
    private Label lblAssurancesARenouveler;

    @FXML
    private VBox activitesContainer;

    private StatistiquesService statistiquesService;
    private VehiculeService vehiculeService;
    private MissionService missionService;
    private EntretienService entretienService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les services
        statistiquesService = new StatistiquesService();
        vehiculeService = new VehiculeService();
        missionService = new MissionService();
        entretienService = new EntretienService();

        // Charger les données
        chargerStatistiques();
        chargerGraphiqueEtatVehicules();
        chargerGraphiqueActivites();
        chargerActivitesRecentes();
    }

    /**
     * Charge les statistiques générales
     */
    private void chargerStatistiques() {
        // Statistiques générales
        int totalVehicules = statistiquesService.getTotalVehicules();
        int vehiculesDisponibles = statistiquesService.getVehiculesDisponibles();
        int vehiculesEnMission = statistiquesService.getVehiculesEnMission();
        int entretiensEnCours = statistiquesService.getEntretiensEnCours();
        int visitesAPlanifier = statistiquesService.getVehiculesVisiteAPlanifier(30); // 30 jours
        int assurancesARenouveler = 0; // À implémenter

        // Mise à jour des labels
        lblTotalVehicules.setText(String.valueOf(totalVehicules));
        lblVehiculesDisponibles.setText(String.valueOf(vehiculesDisponibles));
        lblVehiculesEnMission.setText(String.valueOf(vehiculesEnMission));
        lblEntretiensEnCours.setText(String.valueOf(entretiensEnCours));
        lblVisitesAPlanifier.setText(String.valueOf(visitesAPlanifier));
        lblAssurancesARenouveler.setText(String.valueOf(assurancesARenouveler));
    }

    /**
     * Charge le graphique d'état des véhicules
     */
    private void chargerGraphiqueEtatVehicules() {
        try {
            // Récupérer les états
            List<EtatVoiture> etats = vehiculeService.getAllEtats();
            Map<Integer, Integer> countByEtat = statistiquesService.getVehiculesByEtat();

            // Créer les données du graphique
            chartEtatVehicules.setData(FXCollections.observableArrayList());

            for (EtatVoiture etat : etats) {
                int count = countByEtat.getOrDefault(etat.getIdEtatVoiture(), 0);
                if (count > 0) {
                    PieChart.Data slice = new PieChart.Data(etat.getLibEtatVoiture() + " (" + count + ")", count);
                    chartEtatVehicules.getData().add(slice);
                }
            }

            // Appliquer des styles différents aux tranches du graphique
            int colorIndex = 0;
            for (PieChart.Data data : chartEtatVehicules.getData()) {
                String color = "";
                switch (colorIndex % 5) {
                    case 0: color = "status-disponible"; break;
                    case 1: color = "status-en-mission"; break;
                    case 2: color = "status-hors-service"; break;
                    case 3: color = "status-en-entretien"; break;
                    case 4: color = "status-attribue"; break;
                }
                data.getNode().getStyleClass().add(color);
                colorIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Créer une donnée factice en cas d'erreur
            chartEtatVehicules.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Erreur de chargement", 1)
            ));
        }
    }

    /**
     * Charge le graphique des activités récentes (7 derniers jours)
     */
    private void chargerGraphiqueActivites() {
        try {
            // Préparation des données
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            chartActivitesRecentes.setTitle("Activités des 7 derniers jours");
            xAxis.setLabel("Type d'activité");
            yAxis.setLabel("Nombre");

            // Récupérer les activités des 7 derniers jours
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = now.minusDays(7);

            // Séries de données
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Nombre d'activités");

            // Types d'activités à analyser
            String[] typesActivites = {"CREATION", "MODIFICATION", "ATTRIBUTION", "ENTRETIEN", "MISSION"};

            for (String type : typesActivites) {
                int count = 0;
                // Ici nous utiliserions un comptage par type sur 7 jours
                // Exemple simplifié: compter dans toutes les activités récentes (max 50)
                List<ActiviteLog> activites = statistiquesService.getActivitesRecentes(50);
                for (ActiviteLog activite : activites) {
                    if (activite.getTypeActivite().contains(type) &&
                            activite.getDateActivite().isAfter(startDate)) {
                        count++;
                    }
                }

                series.getData().add(new XYChart.Data<>(type, count));
            }

            chartActivitesRecentes.getData().clear();
            chartActivitesRecentes.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'erreur, on laisse le graphique vide
        }
    }

    /**
     * Charge les activités récentes dans le tableau de bord
     */
    private void chargerActivitesRecentes() {
        List<ActiviteLog> activitesRecentes = statistiquesService.getActivitesRecentes(10);

        // Vider le conteneur
        activitesContainer.getChildren().clear();

        // Formatter pour les dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Ajouter chaque activité
        for (ActiviteLog activite : activitesRecentes) {
            javafx.scene.layout.HBox activityItem = createActivityItem(activite, formatter);
            activitesContainer.getChildren().add(activityItem);
        }
    }

    /**
     * Crée un élément d'activité pour l'affichage
     * @param activite Activité à afficher
     * @param formatter Formatter pour les dates
     * @return HBox contenant l'activité formatée
     */
    private javafx.scene.layout.HBox createActivityItem(ActiviteLog activite, DateTimeFormatter formatter) {
        javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox();
        hbox.getStyleClass().add("activity-item");
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Description de l'activité
        Label lblDescription = new Label(activite.getDescription());
        hbox.getChildren().add(lblDescription);

        // Espace flexible
        javafx.scene.layout.Pane spacer = new javafx.scene.layout.Pane();
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

    /**
     * Rafraîchit toutes les données du tableau de bord
     */
    @FXML
    public void refreshDashboard() {
        chargerStatistiques();
        chargerGraphiqueEtatVehicules();
        chargerGraphiqueActivites();
        chargerActivitesRecentes();
    }
}