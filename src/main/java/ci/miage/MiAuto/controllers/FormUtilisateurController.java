package main.java.ci.miage.MiAuto.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Role;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.services.PersonnelService;
import main.java.ci.miage.MiAuto.services.UtilisateurService;
import main.java.ci.miage.MiAuto.utils.AlertUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FormUtilisateurController implements Initializable {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<Personnel> comboPersonnel;

    @FXML
    private ComboBox<Role> comboRole;

    @FXML
    private ComboBox<String> comboStatut;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private UtilisateurService utilisateurService;
    private PersonnelService personnelService;
    private Utilisateur utilisateur;
    private UtilisateurController parentController;
    private boolean modeEdition = false;

    public FormUtilisateurController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des services
        utilisateurService = new UtilisateurService();
        personnelService = new PersonnelService();

        // Initialiser les ComboBox
        initCombos();
    }

    /**
     * Initialise les listes déroulantes
     */
    private void initCombos() {
        // Remplir la liste des rôles
        List<Role> roles = utilisateurService.getAllRoles();
        if (roles == null) {
            roles = new java.util.ArrayList<>();
        }
        comboRole.setItems(FXCollections.observableArrayList(roles));

        // Configurer l'affichage des rôles
        comboRole.setCellFactory(param -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomRole());
                }
            }
        });

        comboRole.setButtonCell(new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomRole());
                }
            }
        });

        // Remplir la liste des personnels non associés à un utilisateur
        List<Personnel> personnels = personnelService.getPersonnelsDisponibles();
        if (personnels == null) {
            personnels = new java.util.ArrayList<>();
        }
        comboPersonnel.setItems(FXCollections.observableArrayList(personnels));

        // Ajouter une option "Aucun" au début
        Personnel aucun = new Personnel();
        aucun.setIdPersonnel(0);
        aucun.setNomPersonnel("Aucun");
        aucun.setPrenomPersonnel("personnel");
        comboPersonnel.getItems().add(0, aucun);
        comboPersonnel.getSelectionModel().selectFirst();

        // Configurer l'affichage des personnels
        comboPersonnel.setCellFactory(param -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item.getIdPersonnel() == 0) {
                    setText("Aucun personnel");
                } else {
                    setText(item.getNomPersonnel() + " " + item.getPrenomPersonnel());
                }
            }
        });

        comboPersonnel.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item.getIdPersonnel() == 0) {
                    setText("Aucun personnel");
                } else {
                    setText(item.getNomPersonnel() + " " + item.getPrenomPersonnel());
                }
            }
        });

        // Initialiser les statuts
        comboStatut.setItems(FXCollections.observableArrayList("Actif", "Inactif"));
        comboStatut.getSelectionModel().selectFirst();
    }

    /**
     * Définit le contrôleur parent pour le rafraîchissement
     */
    public void setParentController(UtilisateurController parentController) {
        this.parentController = parentController;
    }

    /**
     * Définit l'utilisateur à modifier (ou null pour un ajout)
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        modeEdition = (utilisateur != null);

        if (modeEdition) {
            remplirFormulaire();
        } else {
            comboStatut.getSelectionModel().selectFirst();
        }
    }

    /**
     * Remplit le formulaire avec les données de l'utilisateur à modifier
     */
    private void remplirFormulaire() {
        if (utilisateur == null) return;

        txtLogin.setText(utilisateur.getLogin());
        txtEmail.setText(utilisateur.getEmail());

        // Sélectionner le personnel correspondant
        for (Personnel personnel : comboPersonnel.getItems()) {
            if (personnel.getIdPersonnel() == utilisateur.getIdPersonnel()) {
                comboPersonnel.getSelectionModel().select(personnel);
                break;
            }
        }

        // Sélectionner le rôle correspondant
        for (Role role : comboRole.getItems()) {
            if (role.getIdRole() == utilisateur.getIdRole()) {
                comboRole.getSelectionModel().select(role);
                break;
            }
        }

        // Sélectionner le statut
        if (utilisateur.isStatut()) {
            comboStatut.getSelectionModel().select("Actif");
        } else {
            comboStatut.getSelectionModel().select("Inactif");
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
            enregistrerNouvelUtilisateur();
        }
    }

    /**
     * Valide les champs du formulaire
     */
    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (txtLogin.getText().trim().isEmpty()) {
            erreurs.append("- Le login est obligatoire\n");
        }

        if (!modeEdition && txtPassword.getText().trim().isEmpty()) {
            erreurs.append("- Le mot de passe est obligatoire\n");
        }

        if (comboRole.getSelectionModel().getSelectedItem() == null) {
            erreurs.append("- Le rôle est obligatoire\n");
        }

        if (erreurs.length() > 0) {
            AlertUtils.showErrorAlert("Validation du formulaire",
                    "Veuillez corriger les erreurs suivantes :", erreurs.toString());
            return false;
        }

        return true;
    }

    /**
     * Enregistre un nouvel utilisateur
     */
    private void enregistrerNouvelUtilisateur() {
        String login = txtLogin.getText().trim();
        String password = txtPassword.getText().trim();
        String email = txtEmail.getText().trim();

        Personnel selectedPersonnel = comboPersonnel.getSelectionModel().getSelectedItem();
        Integer idPersonnel = (selectedPersonnel != null && selectedPersonnel.getIdPersonnel() != 0)
                ? selectedPersonnel.getIdPersonnel()
                : null;

        Role selectedRole = comboRole.getSelectionModel().getSelectedItem();
        int idRole = selectedRole.getIdRole();

        Utilisateur nouveauUtilisateur = utilisateurService.addUtilisateur(login, password, email, idPersonnel, idRole);

        if (nouveauUtilisateur != null) {
            AlertUtils.showInformationAlert("Succès", "L'utilisateur a été ajouté avec succès.");

            if (parentController != null) {
                parentController.refreshUtilisateurList();
            }

            fermerFenetre();
        } else {
            AlertUtils.showErrorAlert("Erreur", "Impossible d'ajouter l'utilisateur.");
        }
    }

    /**
     * Enregistre les modifications d'un utilisateur existant
     */
    private void enregistrerModification() {
        if (utilisateur != null) {
            utilisateur.setLogin(txtLogin.getText().trim());
            utilisateur.setEmail(txtEmail.getText().trim());

            Personnel selectedPersonnel = comboPersonnel.getSelectionModel().getSelectedItem();
            if (selectedPersonnel != null && selectedPersonnel.getIdPersonnel() != 0) {
                utilisateur.setIdPersonnel(selectedPersonnel.getIdPersonnel());
            }

            Role selectedRole = comboRole.getSelectionModel().getSelectedItem();
            utilisateur.setIdRole(selectedRole.getIdRole());

            utilisateur.setStatut("Actif".equals(comboStatut.getValue()));

            boolean success = utilisateurService.updateUtilisateur(utilisateur);

            if (success) {
                // Si un mot de passe a été saisi, le mettre à jour
                if (!txtPassword.getText().trim().isEmpty()) {
                    utilisateurService.updatePassword(utilisateur.getIdUtilisateur(), txtPassword.getText().trim());
                }

                AlertUtils.showInformationAlert("Succès", "L'utilisateur a été mis à jour avec succès.");

                if (parentController != null) {
                    parentController.refreshUtilisateurList();
                }

                fermerFenetre();
            } else {
                AlertUtils.showErrorAlert("Erreur", "Impossible de mettre à jour l'utilisateur.");
            }
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