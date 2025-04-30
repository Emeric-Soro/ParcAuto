package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.models.VisiteTechnique;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.services.VisiteTechniqueService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le formulaire d'ajout/modification de visite technique
 */
public class FormVisiteTechniqueController implements Initializable {

    @FXML
    private ComboBox<Vehicule> comboVehicule;

    @FXML
    private DatePicker dateVisite;

    @FXML
    private DatePicker dateExpiration;

    @FXML
    private ComboBox<String> comboResultat;

    @FXML
    private TextField txtCout;

    @FXML
    private TextField txtCentre;

    @FXML
    private TextArea txtObservations;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private VisiteTechniqueService visiteTechniqueService;
    private VehiculeService vehiculeService;
    private VisiteTechnique visiteTechnique;
    private VisiteTechniqueController parentController;
    private boolean modeEdition = false;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vehiculeService = new VehiculeService();

        // Initialiser les validations pour les champs numériques
        txtCout.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCout.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Configuration de l'affichage des véhicules dans le combobox
        comboVehicule.setCellFactory(param -> new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMarque() + " " + item.getModele() + " (" + item.getImmatriculation() + ")");
                }
            }
        });

        comboVehicule.setButtonCell(new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMarque() + " " + item.getModele() + " (" + item.getImmatriculation() + ")");
                }
            }
        });

        // Par défaut, date de visite aujourd'hui
        dateVisite.setValue(LocalDate.now());

        // Par défaut, date d'expiration dans 6 mois
        dateExpiration.setValue(LocalDate.now().plusMonths(6));

        // Initialiser le combobox des résultats
        comboResultat.setItems(FXCollections.observableArrayList(
                "Favorable", "Défavorable", "Favorable avec remarque"));

        // Charger les véhicules
        chargerVehicules();
    }

    /**
     * Charge la liste des véhicules dans le ComboBox
     */
    private void chargerVehicules() {
        try {
            List<Vehicule> vehicules = vehiculeService.getAllVehicules();
            comboVehicule.setItems(FXCollections.observableArrayList(vehicules));
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erreur", "Impossible de charger la liste des véhicules.");
        }
    }

    /**
     * Définit le service de visite technique à utiliser
     * @param visiteTechniqueService Service de visite technique
     */
    public void setVisiteTechniqueService(VisiteTechniqueService visiteTechniqueService) {
        this.visiteTechniqueService = visiteTechniqueService;
    }

    /**
     * Définit la visite technique à modifier (ou null pour un ajout)
     * @param visiteTechnique Visite technique à modifier
     */
    public void setVisiteTechnique(VisiteTechnique visiteTechnique) {
        this.visiteTechnique = visiteTechnique;
        modeEdition = (visiteTechnique != null);

        if (modeEdition) {
            remplirFormulaire();
        }
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     * @param parentController Contrôleur parent
     */
    public void setParentController(VisiteTechniqueController parentController) {
        this.parentController = parentController;
    }

    /**
     * Remplit le formulaire avec les données de la visite technique à modifier
     */
    private void remplirFormulaire() {
        if (visiteTechnique == null) return;

        // Sélectionner le véhicule
        for (Vehicule vehicule : comboVehicule.getItems()) {
            if (vehicule.getIdVehicule() == visiteTechnique.getIdVehicule()) {
                comboVehicule.getSelectionModel().select(vehicule);
                break;
            }
        }

        // Définir les dates
        if (visiteTechnique.getDateVisite() != null) {
            dateVisite.setValue(visiteTechnique.getDateVisite().toLocalDate());
        }

        if (visiteTechnique.getDateExpiration() != null) {
            dateExpiration.setValue(visiteTechnique.getDateExpiration().toLocalDate());
        }

        // Définir le résultat
        comboResultat.setValue(visiteTechnique.getResultat());

        txtCout.setText(String.valueOf(visiteTechnique.getCout()));
        txtCentre.setText(visiteTechnique.getCentreVisite());
        txtObservations.setText(visiteTechnique.getObservations());
    }

    /**
     * Valide les champs du formulaire
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (comboVehicule.getSelectionModel().getSelectedItem() == null) {
            erreurs.append("- Le véhicule est obligatoire\n");
        }

        if (dateVisite.getValue() == null) {
            erreurs.append("- La date de visite est obligatoire\n");
        }

        if (dateExpiration.getValue() == null) {
            erreurs.append("- La date d'expiration est obligatoire\n");
        }

        if (comboResultat.getValue() == null) {
            erreurs.append("- Le résultat est obligatoire\n");
        }

        // Vérifier la cohérence des dates
        if (dateVisite.getValue() != null && dateExpiration.getValue() != null) {
            if (dateVisite.getValue().isAfter(dateExpiration.getValue())) {
                erreurs.append("- La date de visite ne peut pas être postérieure à la date d'expiration\n");
            }
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
     * Gère le clic sur le bouton d'enregistrement
     */
    @FXML
    void handleEnregistrerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        if (modeEdition) {
            enregistrerModification();
        } else {
            enregistrerNouvelleVisite();
        }
    }

    /**
     * Enregistre une nouvelle visite technique
     */
    private void enregistrerNouvelleVisite() {
        VisiteTechnique nouvelleVisite = creerVisiteTechniqueDepuisFormulaire();

        boolean success = visiteTechniqueService.addVisiteTechnique(nouvelleVisite);
        if (success) {
            AlertUtils.showInformationAlert("Succès", "La visite technique a été ajoutée avec succès.");

            if (parentController != null) {
                parentController.refreshVisiteTechniqueList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter la visite technique.");
        }
    }

    /**
     * Enregistre les modifications d'une visite technique existante
     */
    private void enregistrerModification() {
        if (visiteTechnique != null) {
            mettreAJourVisiteTechniqueDepuisFormulaire();

            boolean success = visiteTechniqueService.updateVisiteTechnique(visiteTechnique);
            if (success) {
                AlertUtils.showInformationAlert("Succès", "La visite technique a été mise à jour avec succès.");

                if (parentController != null) {
                    parentController.refreshVisiteTechniqueList();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour la visite technique.");
            }
        }
    }

    /**
     * Crée un nouvel objet VisiteTechnique à partir des valeurs du formulaire
     * @return Nouvel objet VisiteTechnique
     */
    private VisiteTechnique creerVisiteTechniqueDepuisFormulaire() {
        VisiteTechnique newVisite = new VisiteTechnique();

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            newVisite.setIdVehicule(vehiculeSelectionne.getIdVehicule());
        }

        // Conversion des dates
        if (dateVisite.getValue() != null) {
            newVisite.setDateVisite(dateVisite.getValue().atStartOfDay());
        }

        if (dateExpiration.getValue() != null) {
            newVisite.setDateExpiration(dateExpiration.getValue().atTime(23, 59, 59));
        }

        newVisite.setResultat(comboResultat.getValue());

        try {
            newVisite.setCout(Integer.parseInt(txtCout.getText().trim()));
        } catch (NumberFormatException e) {
            newVisite.setCout(0);
        }

        newVisite.setCentreVisite(txtCentre.getText().trim());
        newVisite.setObservations(txtObservations.getText().trim());

        return newVisite;
    }

    /**
     * Met à jour l'objet VisiteTechnique existant avec les valeurs du formulaire
     */
    private void mettreAJourVisiteTechniqueDepuisFormulaire() {
        if (visiteTechnique == null) return;

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            visiteTechnique.setIdVehicule(vehiculeSelectionne.getIdVehicule());
        }

        // Conversion des dates
        if (dateVisite.getValue() != null) {
            visiteTechnique.setDateVisite(dateVisite.getValue().atStartOfDay());
        } else {
            visiteTechnique.setDateVisite(null);
        }

        if (dateExpiration.getValue() != null) {
            visiteTechnique.setDateExpiration(dateExpiration.getValue().atTime(LocalTime.MAX));
        } else {
            visiteTechnique.setDateExpiration(null);
        }

        visiteTechnique.setResultat(comboResultat.getValue());

        try {
            visiteTechnique.setCout(Integer.parseInt(txtCout.getText().trim()));
        } catch (NumberFormatException e) {
            visiteTechnique.setCout(0);
        }

        visiteTechnique.setCentreVisite(txtCentre.getText().trim());
        visiteTechnique.setObservations(txtObservations.getText().trim());
    }

    /**
     * Gère le clic sur le bouton d'annulation
     */
    @FXML
    void handleAnnulerButton(ActionEvent event) {
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