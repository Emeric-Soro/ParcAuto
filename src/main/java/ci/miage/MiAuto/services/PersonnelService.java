package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.PersonnelDAOImpl;
import main.java.ci.miage.MiAuto.models.Personnel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion du personnel
 */
public class PersonnelService {

    private PersonnelDAOImpl personnelDAO;

    public PersonnelService() {
        this.personnelDAO = new PersonnelDAOImpl();
    }

    /**
     * Récupère tous les membres du personnel
     * @return Liste de tout le personnel
     */
    public List<Personnel> getAllPersonnels() {
        try {
            return personnelDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du personnel: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère un membre du personnel par son ID
     * @param idPersonnel ID du personnel
     * @return Personnel trouvé ou null
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
     * Recherche du personnel par nom ou prénom
     * @param terme Terme de recherche
     * @return Liste du personnel correspondant
     */
    public List<Personnel> searchPersonnel(String terme) {
        try {
            return personnelDAO.findByNomOrPrenom(terme);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du personnel: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}