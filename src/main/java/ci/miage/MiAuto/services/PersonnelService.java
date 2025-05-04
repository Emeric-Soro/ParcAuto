package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.PersonnelDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.UtilisateurDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnelService {

    private PersonnelDAOImpl personnelDAO;
    private ActiviteLogDAOImpl activiteLogDAO;
    private UtilisateurDAOImpl utilisateurDAO;

    public PersonnelService() {
        this.personnelDAO = new PersonnelDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
        this.utilisateurDAO = new UtilisateurDAOImpl();
    }

    /**
     * Récupère tous les personnels
     * @return Liste de tous les personnels
     */
    public List<Personnel> getAllPersonnels() {
        try {
            return personnelDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnels: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère un personnel par son ID
     * @param idPersonnel ID du personnel
     * @return Le personnel trouvé ou null
     */
    public Personnel getPersonnelById(int idPersonnel) {
        try {
            return personnelDAO.findById(idPersonnel);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du personnel: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recherche des personnels par nom ou prénom
     * @param terme Terme de recherche
     * @return Liste des personnels correspondants
     */
    public List<Personnel> searchPersonnel(String terme) {
        try {
            List<Personnel> tousPersonnels = personnelDAO.findAll();
            List<Personnel> resultats = new ArrayList<>();

            String termeLower = terme.toLowerCase();
            for (Personnel p : tousPersonnels) {
                if (p.getNomPersonnel().toLowerCase().contains(termeLower) ||
                        p.getPrenomPersonnel().toLowerCase().contains(termeLower)) {
                    resultats.add(p);
                }
            }

            return resultats;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de personnel: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ajoute un nouveau personnel
     * @param personnel Personnel à ajouter
     * @return Le personnel ajouté avec son ID généré, ou null
     */
    public Personnel addPersonnel(Personnel personnel) {
        try {
            Personnel nouveauPersonnel = personnelDAO.save(personnel);

            if (nouveauPersonnel != null) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("CREATION");
                log.setTypeReference("PERSONNEL");
                log.setIdReference(nouveauPersonnel.getIdPersonnel());
                log.setDescription("Nouveau personnel ajouté: " + nouveauPersonnel.getNomPersonnel() + " " +
                        nouveauPersonnel.getPrenomPersonnel());
                activiteLogDAO.save(log);
            }

            return nouveauPersonnel;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du personnel: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour un personnel existant
     * @param personnel Personnel à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updatePersonnel(Personnel personnel) {
        try {
            boolean updated = personnelDAO.update(personnel);

            if (updated) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("MODIFICATION");
                log.setTypeReference("PERSONNEL");
                log.setIdReference(personnel.getIdPersonnel());
                log.setDescription("Personnel modifié: " + personnel.getNomPersonnel() + " " +
                        personnel.getPrenomPersonnel());
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du personnel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un personnel
     * @param idPersonnel ID du personnel à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deletePersonnel(int idPersonnel) {
        try {
            Personnel personnel = personnelDAO.findById(idPersonnel);
            if (personnel == null) {
                return false;
            }

            boolean deleted = personnelDAO.delete(idPersonnel);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION");
                log.setTypeReference("PERSONNEL");
                log.setIdReference(idPersonnel);
                log.setDescription("Personnel supprimé: " + personnel.getNomPersonnel() + " " +
                        personnel.getPrenomPersonnel());
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du personnel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si un email existe déjà pour un autre personnel
     * @param email Email à vérifier
     * @param idPersonnel ID du personnel à exclure (pour les mises à jour)
     * @return true si l'email existe déjà, false sinon
     */
    public boolean emailExiste(String email, int idPersonnel) {
        try {
            return personnelDAO.emailExiste(email, idPersonnel);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère la liste des personnels non associés à un utilisateur
     */
    public List<Personnel> getPersonnelsDisponibles() {
        try {
            List<Personnel> allPersonnels = personnelDAO.findAll();
            List<Utilisateur> allUsers = utilisateurDAO.findAll();

            // Filtrer les personnels qui ont déjà un compte utilisateur
            List<Personnel> disponibles = new ArrayList<>();
            for (Personnel personnel : allPersonnels) {
                boolean hasUser = false;
                for (Utilisateur user : allUsers) {
                    if (user.getIdPersonnel() == personnel.getIdPersonnel()) {
                        hasUser = true;
                        break;
                    }
                }
                if (!hasUser) {
                    disponibles.add(personnel);
                }
            }
            return disponibles;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnels disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}