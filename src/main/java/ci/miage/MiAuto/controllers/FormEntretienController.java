package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Entretien;
import main.java.ci.miage.MiAuto.models.Vehicule;
import main.java.ci.miage.MiAuto.services.EntretienService;
import main.java.ci.miage.MiAuto.services.VehiculeService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le formulaire d'ajout/modification d'entretien
 */
public class FormEntretienController implements Initializable {

    @FXML
    private ComboBox<Vehicule> comboVehicule;

    @FXML
    private DatePicker dateEntree;

    @FXML
    private DatePicker dateSortie;

    @FXML
    private TextField txtMotif;

    @FXML
    private TextArea txtObservation;

    @FXML
    private TextField txtCout;

    @FXML
    private TextField txtLieu;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private EntretienService entretienService;
    private VehiculeService vehiculeService;
    private Entretien entretien;
    private EntretienController parentController;
    private boolean modeEdition = false;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.entretienService = new EntretienService();
        this.vehiculeService = new VehiculeService();

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

        // Par défaut, date d'entrée aujourd'hui
        dateEntree.setValue(LocalDate.now());

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
     * Définit l'entretien à modifier (ou null pour un ajout)
     *
     * @param entretien Entretien à modifier
     */
    public void setEntretien(Entretien entretien) {
        this.entretien = entretien;
        modeEdition = (entretien != null);

        if (modeEdition) {
            remplirFormulaire();
        }
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     *
     * @param parentController Contrôleur parent
     */
    public void setParentController(EntretienController parentController) {
        this.parentController = parentController;
    }

    /**
     * Remplit le formulaire avec les données de l'entretien à modifier
     */
    private void remplirFormulaire() {
        if (entretien == null) return;

        // Sélectionner le véhicule
        for (Vehicule vehicule : comboVehicule.getItems()) {
            if (vehicule.getIdVehicule() == entretien.getIdVehicule()) {
                comboVehicule.getSelectionModel().select(vehicule);
                break;
            }
        }

        // Définir les dates
        if (entretien.getDateEntreeEntr() != null) {
            dateEntree.setValue(entretien.getDateEntreeEntr().toLocalDate());
        }

        if (entretien.getDateSortieEntr() != null) {
            dateSortie.setValue(entretien.getDateSortieEntr().toLocalDate());
        }

        txtMotif.setText(entretien.getMotifEntr());
        txtObservation.setText(entretien.getObservation());
        txtCout.setText(String.valueOf(entretien.getCoutEntr()));
        txtLieu.setText(entretien.getLieuEntr());
    }

    /**
     * Valide les champs du formulaire
     *
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (comboVehicule.getSelectionModel().getSelectedItem() == null) {
            erreurs.append("- Le véhicule est obligatoire\n");
        }

        if (dateEntree.getValue() == null) {
            erreurs.append("- La date d'entrée est obligatoire\n");
        }

        if (txtMotif.getText().trim().isEmpty()) {
            erreurs.append("- Le motif de l'entretien est obligatoire\n");
        }

        // Vérifier la cohérence des dates
        if (dateEntree.getValue() != null && dateSortie.getValue() != null) {
            if (dateEntree.getValue().isAfter(dateSortie.getValue())) {
                erreurs.append("- La date d'entrée ne peut pas être postérieure à la date de sortie\n");
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
            enregistrerNouvelEntretien();
        }
    }

    /**
     * Enregistre un nouvel entretien
     */
    private void enregistrerNouvelEntretien() {
        Entretien nouvelEntretien = creerEntretienDepuisFormulaire();

        boolean success = entretienService.addEntretien(nouvelEntretien);
        if (success) {
            AlertUtils.showInformationAlert("Succès", "L'entretien a été ajouté avec succès.");

            if (parentController != null) {
                parentController.refreshEntretienList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter l'entretien.");
        }
    }

    /**
     * Enregistre les modifications d'un entretien existant
     */
    private void enregistrerModification() {
        if (entretien != null) {
            mettreAJourEntretienDepuisFormulaire();

            boolean success = entretienService.updateEntretien(entretien);
            if (success) {
                AlertUtils.showInformationAlert("Succès", "L'entretien a été mis à jour avec succès.");

                if (parentController != null) {
                    parentController.refreshEntretienList();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour l'entretien.");
            }
        }
    }

    /**
     * Crée un nouvel objet Entretien à partir des valeurs du formulaire
     *
     * @return Nouvel objet Entretien
     */
    private Entretien creerEntretienDepuisFormulaire() {
        Entretien newEntretien = new Entretien();

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            newEntretien.setIdVehicule(vehiculeSelectionne.getIdVehicule());
        }

        // Conversion des dates
        if (dateEntree.getValue() != null) {
            newEntretien.setDateEntreeEntr(dateEntree.getValue().atStartOfDay());
        }

        if (dateSortie.getValue() != null) {
            newEntretien.setDateSortieEntr(dateSortie.getValue().atStartOfDay());
        }

        newEntretien.setMotifEntr(txtMotif.getText().trim());
        newEntretien.setObservation(txtObservation.getText().trim());

        try {
            newEntretien.setCoutEntr(Integer.parseInt(txtCout.getText().trim()));
        } catch (NumberFormatException e) {
            newEntretien.setCoutEntr(0);
        }

        newEntretien.setLieuEntr(txtLieu.getText().trim());

        return newEntretien;
    }

    /**
     * Met à jour l'objet Entretien existant avec les valeurs du formulaire
     */
    private void mettreAJourEntretienDepuisFormulaire() {
        if (entretien == null) return;

        Vehicule vehiculeSelectionne = comboVehicule.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            entretien.setIdVehicule(vehiculeSelectionne.getIdVehicule());
        }

        // Conversion des dates
        if (dateEntree.getValue() != null) {
            entretien.setDateEntreeEntr(dateEntree.getValue().atStartOfDay());
        } else {
            entretien.setDateEntreeEntr(null);
        }

        if (dateSortie.getValue() != null) {
            entretien.setDateSortieEntr(dateSortie.getValue().atTime(LocalTime.MAX));
        } else {
            entretien.setDateSortieEntr(null);
        }

        entretien.setMotifEntr(txtMotif.getText().trim());
        entretien.setObservation(txtObservation.getText().trim());

        try {
            entretien.setCoutEntr(Integer.parseInt(txtCout.getText().trim()));
        } catch (NumberFormatException e) {
            entretien.setCoutEntr(0);
        }

        entretien.setLieuEntr(txtLieu.getText().trim());
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