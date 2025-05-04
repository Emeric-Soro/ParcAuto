package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.RoleDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.UtilisateurDAOImpl;
import main.java.ci.miage.MiAuto.models.Role;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.utils.SecurityUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {

    private UtilisateurDAOImpl utilisateurDAO;
    private RoleDAOImpl roleDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
        this.roleDAO = new RoleDAOImpl();
    }

    public List<Utilisateur> getAllUtilisateurs() {
        try {
            return utilisateurDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Utilisateur getUtilisateurById(int id) {
        try {
            return utilisateurDAO.findById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
            return null;
        }
    }

    public List<Role> getAllRoles() {
        try {
            List<Role> roles = roleDAO.findAll();
            if (roles == null) {
                System.err.println("Warning: roleDAO.findAll() returned null");
                return new ArrayList<>();
            }
            return roles;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rôles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Utilisateur addUtilisateur(String login, String motDePasse, String email, Integer idPersonnel, int idRole) {
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setLogin(login);
            utilisateur.setMotDePasse(motDePasse);
            utilisateur.setEmail(email);

            if (idPersonnel != null && idPersonnel != 0) {
                utilisateur.setIdPersonnel(idPersonnel);
            }

            utilisateur.setIdRole(idRole);
            utilisateur.setStatut(true); // Actif par défaut

            return utilisateurDAO.save(utilisateur);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUtilisateur(Utilisateur utilisateur) {
        try {
            return utilisateurDAO.update(utilisateur);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUtilisateur(int id) {
        try {
            utilisateurDAO.delete(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean activerUtilisateur(int id) {
        try {
            return utilisateurDAO.activer(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean desactiverUtilisateur(int id) {
        try {
            return utilisateurDAO.desactiver(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désactivation de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(int id, String newPassword) {
        try {
            String hashedPassword = SecurityUtils.hashPassword(newPassword);
            return utilisateurDAO.updatePassword(id, hashedPassword);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du mot de passe: " + e.getMessage());
            return false;
        }
    }

    public Utilisateur findByLogin(String login) {
        try {
            return utilisateurDAO.findByLogin(login);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur: " + e.getMessage());
            return null;
        }
    }
}