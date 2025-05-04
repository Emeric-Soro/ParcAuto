package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Fonction;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Service;
import main.java.ci.miage.MiAuto.services.FonctionService;
import main.java.ci.miage.MiAuto.services.PersonnelService;
import main.java.ci.miage.MiAuto.services.ServiceAppService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormPersonnelController implements Initializable {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private ComboBox<String> comboGenre;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAdresse;

    @FXML
    private ComboBox<Fonction> comboFonction;

    @FXML
    private ComboBox<Service> comboService;

    @FXML
    private DatePicker dateEmbauche;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private PersonnelService personnelService;
    private ServiceAppService serviceAppService;
    private Personnel personnel;
    private PersonnelController parentController;
    private boolean modeEdition = false;
    private FonctionService fonctionService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fonctionService = new FonctionService();
        serviceAppService = new ServiceAppService();
        chargerFonctions();
        chargerServices();
        // Initialiser les listes déroulantes
        comboGenre.setItems(FXCollections.observableArrayList("M", "F"));

        // Charger les fonctions et services
        chargerFonctionsEtServices();

        // Initialiser les validations
        initValidations();
    }

    private void chargerFonctionsEtServices() {
        // TODO: Charger les services depuis la base de données
        comboService.setItems(FXCollections.observableArrayList(serviceAppService.getAllServices()));

        // Personnaliser l'affichage des services
        comboService.setCellFactory(param -> new ListCell<Service>() {
            @Override
            protected void updateItem(Service item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelleService());
                }
            }
        });
        comboService.setButtonCell(new ListCell<Service>() {
            @Override
            protected void updateItem(Service item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelleService());
                }
            }
        });

        comboFonction.setCellFactory(param -> new ListCell<Fonction>(){
            @Override
            protected void updateItem(Fonction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelleFonction());
                }
            }
        });
        comboFonction.setButtonCell(new ListCell<Fonction>() {
            @Override
            protected void updateItem(Fonction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelleFonction());
                }
            }
        });
    }

    private void initValidations() {
        // Validation email
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidEmail(newValue)) {
                txtEmail.setStyle("-fx-border-color: red;");
            } else {
                txtEmail.setStyle("");
            }
        });

        // Validation contact (uniquement des chiffres)
        txtContact.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtContact.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public void setPersonnelService(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
        modeEdition = (personnel != null);

        if (modeEdition) {
            remplirFormulaire();
        } else {
            // Valeurs par défaut pour un nouveau personnel
            comboGenre.getSelectionModel().selectFirst();
            dateEmbauche.setValue(LocalDate.now());
        }
    }

    public void setParentController(PersonnelController parentController) {
        this.parentController = parentController;
    }

    private void remplirFormulaire() {
        if (personnel == null) return;

        txtNom.setText(personnel.getNomPersonnel());
        txtPrenom.setText(personnel.getPrenomPersonnel());
        comboGenre.setValue(personnel.getGenrePersonnel());
        txtContact.setText(personnel.getContactPersonnel());
        txtEmail.setText(personnel.getEmailPersonnel());
        txtAdresse.setText(personnel.getAdressePersonnel());

        // Sélectionner la fonction et le service
        // TODO: Implémenter une fois les fonctions disponibles

        if (personnel.getDateEmbauche() != null) {
            dateEmbauche.setValue(personnel.getDateEmbauche().toLocalDate());
        }
    }

    @FXML
    private void handleEnregistrerButton(ActionEvent event) {
        if (!validerFormulaire()) {
            return;
        }

        if (modeEdition) {
            enregistrerModification();
        } else {
            enregistrerNouveauPersonnel();
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        // Vérifier les champs obligatoires
        if (txtNom.getText().trim().isEmpty()) {
            erreurs.append("- Le nom est obligatoire\n");
        }

        if (txtPrenom.getText().trim().isEmpty()) {
            erreurs.append("- Le prénom est obligatoire\n");
        }

        if (comboGenre.getValue() == null) {
            erreurs.append("- Le genre est obligatoire\n");
        }

        if (txtEmail.getText().trim().isEmpty()) {
            erreurs.append("- L'email est obligatoire\n");
        } else if (!isValidEmail(txtEmail.getText().trim())) {
            erreurs.append("- L'email n'est pas valide\n");
        }

        if (comboService.getValue() == null) {
            erreurs.append("- Le service est obligatoire\n");
        }

        // Vérifier l'unicité de l'email
        String email = txtEmail.getText().trim();
        int idPersonnel = modeEdition ? personnel.getIdPersonnel() : 0;

        if (!email.isEmpty() && personnelService.emailExiste(email, idPersonnel)) {
            erreurs.append("- Cette adresse email existe déjà\n");
        }

        // S'il y a des erreurs, les afficher
        if (erreurs.length() > 0) {
            AlertUtils.showErrorAlert("Validation du formulaire",
                    "Veuillez corriger les erreurs suivantes :", erreurs.toString());
            return false;
        }

        return true;
    }

    private void enregistrerNouveauPersonnel() {
        Personnel nouveauPersonnel = creerPersonnelDepuisFormulaire();

        Personnel savedPersonnel = personnelService.addPersonnel(nouveauPersonnel);
        if (savedPersonnel != null) {
            AlertUtils.showInformationAlert("Succès", "Le personnel a été ajouté avec succès.");

            if (parentController != null) {
                parentController.refreshPersonnelList();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter le personnel.");
        }
    }

    private void enregistrerModification() {
        if (personnel != null) {
            mettreAJourPersonnelDepuisFormulaire();

            boolean success = personnelService.updatePersonnel(personnel);
            if (success) {
                AlertUtils.showInformationAlert("Succès", "Le personnel a été mis à jour avec succès.");

                if (parentController != null) {
                    parentController.refreshPersonnelList();
                }

                // Fermer la fenêtre
                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour le personnel.");
            }
        }
    }

    private Personnel creerPersonnelDepuisFormulaire() {
        Personnel newPersonnel = new Personnel();

        newPersonnel.setNomPersonnel(txtNom.getText().trim());
        newPersonnel.setPrenomPersonnel(txtPrenom.getText().trim());
        newPersonnel.setGenrePersonnel(comboGenre.getValue());
        newPersonnel.setContactPersonnel(txtContact.getText().trim());
        newPersonnel.setEmailPersonnel(txtEmail.getText().trim());
        newPersonnel.setAdressePersonnel(txtAdresse.getText().trim());

        // Fonction
        Fonction selectedFonction = comboFonction.getValue();
        if (selectedFonction != null) {
            newPersonnel.setIdFonction(selectedFonction.getIdFonction());
        }

        // Service
        Service selectedService = comboService.getValue();
        if (selectedService != null) {
            newPersonnel.setIdService(selectedService.getIdService());
        }

        // Date d'embauche
        if (dateEmbauche.getValue() != null) {
            newPersonnel.setDateEmbauche(dateEmbauche.getValue().atStartOfDay());
        }

        // Véhicule non attribué par défaut
        newPersonnel.setIdVehicule(0);

        return newPersonnel;
    }

    private void mettreAJourPersonnelDepuisFormulaire() {
        if (personnel == null) return;

        personnel.setNomPersonnel(txtNom.getText().trim());
        personnel.setPrenomPersonnel(txtPrenom.getText().trim());
        personnel.setGenrePersonnel(comboGenre.getValue());
        personnel.setContactPersonnel(txtContact.getText().trim());
        personnel.setEmailPersonnel(txtEmail.getText().trim());
        personnel.setAdressePersonnel(txtAdresse.getText().trim());

        // Service
        Service selectedService = comboService.getValue();
        if (selectedService != null) {
            personnel.setIdService(selectedService.getIdService());
        }

        // Date d'embauche
        if (dateEmbauche.getValue() != null) {
            personnel.setDateEmbauche(dateEmbauche.getValue().atStartOfDay());
        } else {
            personnel.setDateEmbauche(null);
        }
    }

    @FXML
    private void handleAnnulerButton(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void chargerFonctions() {
        List<Fonction> fonctions = fonctionService.getAllFonctions();
        comboFonction.setItems(FXCollections.observableArrayList(fonctions));
    }

    private void chargerServices() {
        List<Service> services = serviceAppService.getAllServices();
        comboService.setItems(FXCollections.observableArrayList(services));
    }
}