package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.EtatVoiture;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;
import main.java.ci.miage.MiAuto.utils.DateUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le formulaire d'ajout/modification de véhicule
 */
public class FormVehiculeController implements Initializable {

    @FXML
    private TextField txtImmatriculation;

    @FXML
    private TextField txtNumeroChassi;

    @FXML
    private TextField txtMarque;

    @FXML
    private TextField txtModele;

    @FXML
    private ComboBox<EtatVoiture> comboEtat;

    @FXML
    private ComboBox<String> comboEnergie;

    @FXML
    private TextField txtNbPlaces;

    @FXML
    private TextField txtPuissance;

    @FXML
    private TextField txtCouleur;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private DatePicker dateAcquisition;

    @FXML
    private DatePicker dateAmortissement;

    @FXML
    private DatePicker dateMiseEnService;

    @FXML
    private DatePicker dateDerniereVisite;

    @FXML
    private DatePicker dateProchainVisite;

    @FXML
    private CheckBox checkStatutAttribution;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private VehiculeService vehiculeService;
    private Vehicule vehicule;
    private VehiculeController parentController;
    private boolean modeEdition = false;

    /**
     * Initialise le contrôleur
     * @param url URL de localisation
     * @param rb Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser les listes déroulantes
        comboEnergie.setItems(FXCollections.observableArrayList(
                "Essence", "Diesel", "Électrique", "Hybride", "GPL"));

        // TODO: Charger les états depuis la base de données
        // Pour l'instant, on utilise des états statiques
        comboEtat.setItems(FXCollections.observableArrayList(
                new EtatVoiture(1, "Disponible"),
                new EtatVoiture(2, "En mission"),
                new EtatVoiture(3, "En maintenance"),
                new EtatVoiture(4, "Hors service")
        ));
        comboEtat.setCellFactory(param -> new ListCell<EtatVoiture>() {
            @Override
            protected void updateItem(EtatVoiture item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibEtatVoiture());
                }
            }
        });
        comboEtat.setButtonCell(new ListCell<EtatVoiture>() {
            @Override
            protected void updateItem(EtatVoiture item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibEtatVoiture());
                }
            }
        });

        // Initialiser les validations
        initValidations();
    }

    /**
     * Initialise les validations des champs
     */
    private void initValidations() {
        // Validations pour les champs numériques
        txtNbPlaces.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtNbPlaces.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtPuissance.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPuissance.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtPrix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPrix.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtKilometrage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtKilometrage.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Définit le service de véhicule à utiliser
     * @param vehiculeService Service de véhicule
     */
    public void setVehiculeService(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    /**
     * Définit le véhicule à modifier (ou null pour un ajout)
     * @param vehicule Véhicule à modifier
     */
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        modeEdition = (vehicule != null);

        if (modeEdition) {
            remplirFormulaire();
        } else {
            // Valeurs par défaut pour un nouveau véhicule
            comboEtat.getSelectionModel().select(0); // Premier état (Disponible)
            checkStatutAttribution.setSelected(false);
            dateAcquisition.setValue(LocalDate.now());
        }
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     * @param parentController Contrôleur parent
     */
    public void setParentController(VehiculeController parentController) {
        this.parentController = parentController;
    }

    /**
     * Remplit le formulaire avec les données du véhicule à modifier
     */
    private void remplirFormulaire() {
        if (vehicule == null) return;

        txtImmatriculation.setText(vehicule.getImmatriculation());
        txtNumeroChassi.setText(vehicule.getNumeroChassi());
        txtMarque.setText(vehicule.getMarque());
        txtModele.setText(vehicule.getModele());

        // Sélectionner l'état correspondant
        for (EtatVoiture etat : comboEtat.getItems()) {
            if (etat.getIdEtatVoiture() == vehicule.getIdEtatVoiture()) {
                comboEtat.getSelectionModel().select(etat);
                break;
            }
        }

        comboEnergie.setValue(vehicule.getEnergie());
        txtNbPlaces.setText(String.valueOf(vehicule.getNbPlaces()));
        txtPuissance.setText(String.valueOf(vehicule.getPuissance()));
        txtCouleur.setText(vehicule.getCouleur());
        txtPrix.setText(String.valueOf(vehicule.getPrixVehicule()));
        txtKilometrage.setText(String.valueOf(vehicule.getKilometrage()));

        // Convertir les dates de LocalDateTime à LocalDate
        if (vehicule.getDateAcquisition() != null) {
            dateAcquisition.setValue(vehicule.getDateAcquisition().toLocalDate());
        }

        if (vehicule.getDateAmmortissement() != null) {
            dateAmortissement.setValue(vehicule.getDateAmmortissement().toLocalDate());
        }

        if (vehicule.getDateMiseEnService() != null) {
            dateMiseEnService.setValue(vehicule.getDateMiseEnService().toLocalDate());
        }

        if (vehicule.getDateDerniereVisite() != null) {
            dateDerniereVisite.setValue(vehicule.getDateDerniereVisite().toLocalDate());
        }

        if (vehicule.getDateProchainVisite() != null) {
            dateProchainVisite.setValue(vehicule.getDateProchainVisite().toLocalDate());
        }

        checkStatutAttribution.setSelected(vehicule.isStatutAttribution());
    }

    /**
     * Gère le clic sur le bouton d'enregistrement
     * @param event Événement de clic
     */
    @FXML
    private void handleEnregistrerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        if (modeEdition) {
            enregistrerModification();
        } else {
            enregistrerNouveauVehicule();
        }
    }

    /**
     * Valide les champs du formulaire
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (txtImmatriculation.getText().trim().isEmpty()) {
            erreurs.append("- L'immatriculation est obligatoire\n");
        }

        if (txtNumeroChassi.getText().trim().isEmpty()) {
            erreurs.append("- Le numéro de châssis est obligatoire\n");
        }

        if (txtMarque.getText().trim().isEmpty()) {
            erreurs.append("- La marque est obligatoire\n");
        }

        if (txtModele.getText().trim().isEmpty()) {
            erreurs.append("- Le modèle est obligatoire\n");
        }

        if (comboEtat.getSelectionModel().getSelectedItem() == null) {
            erreurs.append("- L'état du véhicule est obligatoire\n");
        }

        // Vérifier les doublons potentiels
        String immatriculation = txtImmatriculation.getText().trim();
        String numeroChassi = txtNumeroChassi.getText().trim();
        int idVehicule = modeEdition ? vehicule.getIdVehicule() : 0;

        if (!immatriculation.isEmpty() && vehiculeService.immatriculationExiste(immatriculation, idVehicule)) {
            erreurs.append("- Cette immatriculation existe déjà pour un autre véhicule\n");
        }

        if (!numeroChassi.isEmpty() && vehiculeService.numeroChassiExiste(numeroChassi, idVehicule)) {
            erreurs.append("- Ce numéro de châssis existe déjà pour un autre véhicule\n");
        }

        // S'il y a des erreurs, les afficher
        if (erreurs.length() > 0) {
            AlertUtils.showErrorAlert("Validation du formulaire",
                    "Veuillez corriger les erreurs suivantes :", erreurs.toString());
            return false;
        }

        return true;
    }

    /**
     * Enregistre un nouveau véhicule
     */
    private void enregistrerNouveauVehicule() {
        Vehicule nouveauVehicule = creerVehiculeDepuisFormulaire();

        Vehicule savedVehicule = vehiculeService.addVehicule(nouveauVehicule);
        if (savedVehicule != null) {
            AlertUtils.showInformationAlert("Succès", "Le véhicule a été ajouté avec succès.");

            if (parentController != null) {
                // Méthode supposée existante dans le contrôleur parent pour rafraîchir la liste
                // On pourrait aussi implémenter un design pattern Observer/Observable
                parentController.refreshVehiculeList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter le véhicule.");
        }
    }

    /**
     * Enregistre les modifications d'un véhicule existant
     */
    private void enregistrerModification() {
        if (vehicule != null) {
            mettreAJourVehiculeDepuisFormulaire();

            boolean success = vehiculeService.updateVehicule(vehicule);
            if (success) {
                AlertUtils.showInformationAlert("Succès", "Le véhicule a été mis à jour avec succès.");

                if (parentController != null) {
                    // Méthode supposée existante dans le contrôleur parent pour rafraîchir la liste
                    parentController.refreshVehiculeList();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour le véhicule.");
            }
        }
    }

    /**
     * Crée un nouvel objet Vehicule à partir des valeurs du formulaire
     * @return Nouvel objet Vehicule
     */
    private Vehicule creerVehiculeDepuisFormulaire() {
        Vehicule newVehicule = new Vehicule();

        EtatVoiture etatSelectionne = comboEtat.getSelectionModel().getSelectedItem();
        newVehicule.setIdEtatVoiture(etatSelectionne.getIdEtatVoiture());
        newVehicule.setEtatVoiture(etatSelectionne);

        newVehicule.setNumeroChassi(txtNumeroChassi.getText().trim());
        newVehicule.setImmatriculation(txtImmatriculation.getText().trim());
        newVehicule.setMarque(txtMarque.getText().trim());
        newVehicule.setModele(txtModele.getText().trim());

        // Conversion des valeurs numériques avec gestion des erreurs
        try {
            newVehicule.setNbPlaces(Integer.parseInt(txtNbPlaces.getText().trim()));
        } catch (NumberFormatException e) {
            newVehicule.setNbPlaces(0);
        }

        newVehicule.setEnergie(comboEnergie.getValue());

        try {
            newVehicule.setPuissance(Integer.parseInt(txtPuissance.getText().trim()));
        } catch (NumberFormatException e) {
            newVehicule.setPuissance(0);
        }

        newVehicule.setCouleur(txtCouleur.getText().trim());

        try {
            newVehicule.setPrixVehicule(Integer.parseInt(txtPrix.getText().trim()));
        } catch (NumberFormatException e) {
            newVehicule.setPrixVehicule(0);
        }

        try {
            newVehicule.setKilometrage(Integer.parseInt(txtKilometrage.getText().trim()));
        } catch (NumberFormatException e) {
            newVehicule.setKilometrage(0);
        }

        // Conversion des dates
        if (dateAcquisition.getValue() != null) {
            newVehicule.setDateAcquisition(dateAcquisition.getValue().atStartOfDay());
        }

        if (dateAmortissement.getValue() != null) {
            newVehicule.setDateAmmortissement(dateAmortissement.getValue().atStartOfDay());
        }

        if (dateMiseEnService.getValue() != null) {
            newVehicule.setDateMiseEnService(dateMiseEnService.getValue().atStartOfDay());
        }

        if (dateDerniereVisite.getValue() != null) {
            newVehicule.setDateDerniereVisite(dateDerniereVisite.getValue().atStartOfDay());
        }

        if (dateProchainVisite.getValue() != null) {
            newVehicule.setDateProchainVisite(dateProchainVisite.getValue().atStartOfDay());
        }

        newVehicule.setDateEtat(LocalDateTime.now());
        newVehicule.setStatutAttribution(checkStatutAttribution.isSelected());

        return newVehicule;
    }

    /**
     * Met à jour l'objet Vehicule existant avec les valeurs du formulaire
     */
    private void mettreAJourVehiculeDepuisFormulaire() {
        if (vehicule == null) return;

        EtatVoiture etatSelectionne = comboEtat.getSelectionModel().getSelectedItem();
        vehicule.setIdEtatVoiture(etatSelectionne.getIdEtatVoiture());
        vehicule.setEtatVoiture(etatSelectionne);

        vehicule.setNumeroChassi(txtNumeroChassi.getText().trim());
        vehicule.setImmatriculation(txtImmatriculation.getText().trim());
        vehicule.setMarque(txtMarque.getText().trim());
        vehicule.setModele(txtModele.getText().trim());

        // Conversion des valeurs numériques avec gestion des erreurs
        try {
            vehicule.setNbPlaces(Integer.parseInt(txtNbPlaces.getText().trim()));
        } catch (NumberFormatException e) {
            vehicule.setNbPlaces(0);
        }

        vehicule.setEnergie(comboEnergie.getValue());

        try {
            vehicule.setPuissance(Integer.parseInt(txtPuissance.getText().trim()));
        } catch (NumberFormatException e) {
            vehicule.setPuissance(0);
        }

        vehicule.setCouleur(txtCouleur.getText().trim());

        try {
            vehicule.setPrixVehicule(Integer.parseInt(txtPrix.getText().trim()));
        } catch (NumberFormatException e) {
            vehicule.setPrixVehicule(0);
        }

        try {
            vehicule.setKilometrage(Integer.parseInt(txtKilometrage.getText().trim()));
        } catch (NumberFormatException e) {
            vehicule.setKilometrage(0);
        }

        // Conversion des dates
        if (dateAcquisition.getValue() != null) {
            vehicule.setDateAcquisition(dateAcquisition.getValue().atStartOfDay());
        } else {
            vehicule.setDateAcquisition(null);
        }

        if (dateAmortissement.getValue() != null) {
            vehicule.setDateAmmortissement(dateAmortissement.getValue().atStartOfDay());
        } else {
            vehicule.setDateAmmortissement(null);
        }

        if (dateMiseEnService.getValue() != null) {
            vehicule.setDateMiseEnService(dateMiseEnService.getValue().atStartOfDay());
        } else {
            vehicule.setDateMiseEnService(null);
        }

        if (dateDerniereVisite.getValue() != null) {
            vehicule.setDateDerniereVisite(dateDerniereVisite.getValue().atStartOfDay());
        } else {
            vehicule.setDateDerniereVisite(null);
        }

        if (dateProchainVisite.getValue() != null) {
            vehicule.setDateProchainVisite(dateProchainVisite.getValue().atStartOfDay());
        } else {
            vehicule.setDateProchainVisite(null);
        }

        vehicule.setDateEtat(LocalDateTime.now());
        vehicule.setStatutAttribution(checkStatutAttribution.isSelected());
    }

    /**
     * Gère le clic sur le bouton d'annulation
     * @param event Événement de clic
     */
    @FXML
    private void handleAnnulerButton(ActionEvent event) {
        fermerFenetre();
    }

    /**
     * Ferme la fenêtre courante
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}