package main.java.ci.miage.MiAuto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Assurance;
import main.java.ci.miage.MiAuto.services.AssuranceService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le formulaire d'assurance (ajout/modification)
 */
public class AssuranceFormController implements Initializable {

    @FXML
    private TextField txtAgence;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField txtCout;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private AssuranceService assuranceService;
    private Assurance assuranceEnEdition;
    private AssuranceListController parentController;
    private boolean modeEdition = false;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les validateurs pour les champs numériques
        txtCout.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCout.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Définit le service d'assurance à utiliser
     *
     * @param assuranceService Service d'assurance
     */
    public void setAssuranceService(AssuranceService assuranceService) {
        this.assuranceService = assuranceService;
    }

    /**
     * Définit l'assurance à modifier (ou null pour un ajout)
     *
     * @param assurance Assurance à modifier
     */
    public void setAssurance(Assurance assurance) {
        this.assuranceEnEdition = assurance;
        modeEdition = (assurance != null);

        if (modeEdition) {
            remplirFormulaire();
        }
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     *
     * @param parentController Contrôleur parent
     */
    public void setParentController(AssuranceListController parentController) {
        this.parentController = parentController;
    }

    /**
     * Remplit le formulaire avec les données de l'assurance à modifier
     */
    private void remplirFormulaire() {
        if (assuranceEnEdition == null) return;

        txtAgence.setText(assuranceEnEdition.getAgence());

        if (assuranceEnEdition.getDateDebutAssurance() != null) {
            dateDebut.setValue(assuranceEnEdition.getDateDebutAssurance().toLocalDate());
        }

        if (assuranceEnEdition.getDateFinAssurance() != null) {
            dateFin.setValue(assuranceEnEdition.getDateFinAssurance().toLocalDate());
        }

        if (assuranceEnEdition.getCoutAssurance() > 0) {
            txtCout.setText(String.valueOf(assuranceEnEdition.getCoutAssurance()));
        }
    }

    /**
     * Gère le clic sur le bouton d'enregistrement
     */
    @FXML
    private void handleEnregistrerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        if (modeEdition) {
            enregistrerModification();
        } else {
            enregistrerNouvelleAssurance();
        }
    }

    /**
     * Valide les champs du formulaire
     *
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (txtAgence.getText().trim().isEmpty()) {
            erreurs.append("- L'agence d'assurance est obligatoire\n");
        }

        if (dateDebut.getValue() == null) {
            erreurs.append("- La date de début est obligatoire\n");
        }

        if (dateFin.getValue() == null) {
            erreurs.append("- La date d'expiration est obligatoire\n");
        }

        // Vérifier la cohérence des dates
        if (dateDebut.getValue() != null && dateFin.getValue() != null) {
            if (dateFin.getValue().isBefore(dateDebut.getValue())) {
                erreurs.append("- La date d'expiration ne peut pas être antérieure à la date de début\n");
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
     * Enregistre une nouvelle assurance
     */
    private void enregistrerNouvelleAssurance() {
        Assurance nouvelleAssurance = creerAssuranceDepuisFormulaire();

        Assurance savedAssurance = assuranceService.addAssurance(nouvelleAssurance);
        if (savedAssurance != null) {
            AlertUtils.showInformationAlert("Succès", "L'assurance a été ajoutée avec succès.");
            fermerFenetre();
            if (parentController != null) {
                parentController.chargerAssurances();
            }
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter l'assurance.");
        }
    }

    /**
     * Enregistre les modifications d'une assurance existante
     */
    private void enregistrerModification() {
        if (assuranceEnEdition != null) {
            mettreAJourAssuranceDepuisFormulaire();

            boolean success = assuranceService.updateAssurance(assuranceEnEdition);
            if (success) {
                AlertUtils.showInformationAlert("Succès", "L'assurance a été mise à jour avec succès.");
                fermerFenetre();
                if (parentController != null) {
                    parentController.chargerAssurances();
                }
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour l'assurance.");
            }
        }
    }

    /**
     * Crée un nouvel objet Assurance à partir des valeurs du formulaire
     *
     * @return Nouvel objet Assurance
     */
    private Assurance creerAssuranceDepuisFormulaire() {
        Assurance assurance = new Assurance();

        assurance.setAgence(txtAgence.getText().trim());

        if (dateDebut.getValue() != null) {
            assurance.setDateDebutAssurance(dateDebut.getValue().atStartOfDay());
        }

        if (dateFin.getValue() != null) {
            assurance.setDateFinAssurance(dateFin.getValue().atStartOfDay());
        }

        try {
            if (!txtCout.getText().trim().isEmpty()) {
                assurance.setCoutAssurance(Integer.parseInt(txtCout.getText().trim()));
            }
        } catch (NumberFormatException e) {
            assurance.setCoutAssurance(0);
        }

        return assurance;
    }

    /**
     * Met à jour l'objet Assurance existant avec les valeurs du formulaire
     */
    private void mettreAJourAssuranceDepuisFormulaire() {
        if (assuranceEnEdition == null) return;

        assuranceEnEdition.setAgence(txtAgence.getText().trim());

        if (dateDebut.getValue() != null) {
            assuranceEnEdition.setDateDebutAssurance(dateDebut.getValue().atStartOfDay());
        } else {
            assuranceEnEdition.setDateDebutAssurance(null);
        }

        if (dateFin.getValue() != null) {
            assuranceEnEdition.setDateFinAssurance(dateFin.getValue().atStartOfDay());
        } else {
            assuranceEnEdition.setDateFinAssurance(null);
        }

        try {
            if (!txtCout.getText().trim().isEmpty()) {
                assuranceEnEdition.setCoutAssurance(Integer.parseInt(txtCout.getText().trim()));
            } else {
                assuranceEnEdition.setCoutAssurance(0);
            }
        } catch (NumberFormatException e) {
            assuranceEnEdition.setCoutAssurance(0);
        }
    }

    /**
     * Gère le clic sur le bouton d'annulation
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