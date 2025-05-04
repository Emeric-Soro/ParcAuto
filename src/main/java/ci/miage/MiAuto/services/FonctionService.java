package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.FonctionDAOImpl;
import main.java.ci.miage.MiAuto.models.Fonction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FonctionService {

    private FonctionDAOImpl fonctionDAO;

    public FonctionService() {
        this.fonctionDAO = new FonctionDAOImpl();
    }

    public List<Fonction> getAllFonctions() {
        try {
            return fonctionDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fonctions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Fonction getFonctionById(int id) {
        try {
            return fonctionDAO.findById(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la fonction: " + e.getMessage());
            return null;
        }
    }

    public Fonction getFonctionByLibelle(String libelle) {
        try {
            return fonctionDAO.findByLibelle(libelle);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la fonction: " + e.getMessage());
            return null;
        }
    }
}