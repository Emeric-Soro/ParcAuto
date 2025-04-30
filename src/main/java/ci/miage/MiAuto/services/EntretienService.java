package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.EntretienDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Entretien;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des entretiens de véhicules
 */
public class EntretienService {

    private EntretienDAOImpl entretienDAO;
    private VehiculeDAOImpl vehiculeDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    /**
     * Constructeur par défaut
     */
    public EntretienService() {
        this.entretienDAO = new EntretienDAOImpl();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère tous les entretiens
     * @return Liste de tous les entretiens
     */
    public List<Entretien> getAllEntretiens() {
        try {
            return entretienDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des entretiens: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère un entretien par son ID
     * @param idEntretien ID de l'entretien
     * @return L'entretien trouvé ou null
     */
    public Entretien getEntretienById(int idEntretien) {
        try {
            return entretienDAO.findById(idEntretien);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'entretien: " + e.getMessage());
            return null;
        }
    }

    /**
     * Ajoute un nouvel entretien
     * @param entretien Entretien à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEntretien(Entretien entretien) {
        try {
            // Vérifier que le véhicule existe
            Vehicule vehicule = vehiculeDAO.findById(entretien.getIdVehicule());
            if (vehicule == null) {
                return false;
            }

            // Sauvegarder l'entretien
            Entretien saved = entretienDAO.save(entretien);

            if (saved != null) {
                // Mettre à jour l'état du véhicule à "En entretien"
                vehiculeDAO.updateEtat(entretien.getIdVehicule(), 4); // 4 = En entretien

                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("ENTRETIEN");
                log.setTypeReference("VEHICULE");
                log.setIdReference(entretien.getIdVehicule());
                log.setDescription("Le véhicule " + vehicule.getMarque() + " " + vehicule.getModele() +
                        " (" + vehicule.getImmatriculation() + ") est entré en maintenance");
                activiteLogDAO.save(log);

                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'entretien: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un entretien existant
     * @param entretien Entretien à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateEntretien(Entretien entretien) {
        try {
            boolean updated = entretienDAO.update(entretien);

            if (updated) {
                // Vérifier si l'entretien est terminé
                if (entretien.getDateSortieEntr() != null) {
                    // Mettre à jour l'état du véhicule à "Disponible"
                    vehiculeDAO.updateEtat(entretien.getIdVehicule(), 1); // 1 = Disponible

                    // Récupérer le véhicule pour le message
                    Vehicule vehicule = vehiculeDAO.findById(entretien.getIdVehicule());

                    // Enregistrer l'activité
                    ActiviteLog log = new ActiviteLog();
                    log.setTypeActivite("FIN_ENTRETIEN");
                    log.setTypeReference("VEHICULE");
                    log.setIdReference(entretien.getIdVehicule());
                    log.setDescription("Le véhicule " + vehicule.getMarque() + " " + vehicule.getModele() +
                            " (" + vehicule.getImmatriculation() + ") est sorti de maintenance");
                    activiteLogDAO.save(log);
                }
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'entretien: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un entretien
     * @param idEntretien ID de l'entretien à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteEntretien(int idEntretien) {
        try {
            // Récupérer l'entretien avant suppression pour référence
            Entretien entretien = entretienDAO.findById(idEntretien);
            if (entretien == null) {
                return false;
            }

            // Si l'entretien n'est pas terminé, vérifier si le véhicule doit être remis à disponible
            if (entretien.getDateSortieEntr() == null) {
                Vehicule vehicule = vehiculeDAO.findById(entretien.getIdVehicule());
                if (vehicule != null && vehicule.getIdEtatVoiture() == 4) { // 4 = En entretien
                    vehiculeDAO.updateEtat(entretien.getIdVehicule(), 1); // 1 = Disponible
                }
            }

            // Supprimer l'entretien
            boolean deleted = entretienDAO.delete(idEntretien);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION_ENTRETIEN");
                log.setTypeReference("ENTRETIEN");
                log.setIdReference(idEntretien);
                log.setDescription("Suppression d'un entretien (ID: " + idEntretien + ")");
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'entretien: " + e.getMessage());
            return false;
        }
    }

    /**
     * Termine un entretien en cours
     * @param idEntretien ID de l'entretien à terminer
     * @return true si la terminaison a réussi, false sinon
     */
    public boolean terminerEntretien(int idEntretien) {
        try {
            // Récupérer l'entretien
            Entretien entretien = entretienDAO.findById(idEntretien);
            if (entretien == null) {
                return false;
            }

            // Vérifier que l'entretien n'est pas déjà terminé
            if (entretien.getDateSortieEntr() != null) {
                return false;
            }

            // Mettre à jour la date de sortie
            entretien.setDateSortieEntr(LocalDateTime.now());

            // Mettre à jour l'entretien
            boolean updated = entretienDAO.update(entretien);

            if (updated) {
                // Mettre à jour l'état du véhicule à "Disponible"
                vehiculeDAO.updateEtat(entretien.getIdVehicule(), 1); // 1 = Disponible

                // Récupérer le véhicule pour le message
                Vehicule vehicule = vehiculeDAO.findById(entretien.getIdVehicule());

                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("FIN_ENTRETIEN");
                log.setTypeReference("VEHICULE");
                log.setIdReference(entretien.getIdVehicule());
                log.setDescription("Le véhicule " + vehicule.getMarque() + " " + vehicule.getModele() +
                        " (" + vehicule.getImmatriculation() + ") est sorti de maintenance");
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la terminaison de l'entretien: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les entretiens d'un véhicule spécifique
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens du véhicule
     */
    public List<Entretien> getEntretiensByVehicule(int idVehicule) {
        try {
            return entretienDAO.findByVehicule(idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des entretiens par véhicule: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les entretiens en cours (sans date de sortie)
     * @return Liste des entretiens en cours
     */
    public List<Entretien> getEntretiensEnCours() {
        try {
            List<Entretien> entretiens = entretienDAO.findAll();
            List<Entretien> entretiensEnCours = new ArrayList<>();

            for (Entretien entretien : entretiens) {
                if (entretien.getDateSortieEntr() == null) {
                    entretiensEnCours.add(entretien);
                }
            }

            return entretiensEnCours;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des entretiens en cours: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les entretiens terminés (avec date de sortie)
     * @return Liste des entretiens terminés
     */
    public List<Entretien> getEntretiensTermines() {
        try {
            List<Entretien> entretiens = entretienDAO.findAll();
            List<Entretien> entretiensTermines = new ArrayList<>();

            for (Entretien entretien : entretiens) {
                if (entretien.getDateSortieEntr() != null) {
                    entretiensTermines.add(entretien);
                }
            }

            return entretiensTermines;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des entretiens terminés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des entretiens par motif ou lieu
     * @param terme Terme de recherche
     * @return Liste des entretiens correspondants
     */
    public List<Entretien> searchEntretiens(String terme) {
        try {
            if (terme == null || terme.trim().isEmpty()) {
                return getAllEntretiens();
            }

            List<Entretien> entretiens = entretienDAO.findAll();
            List<Entretien> resultats = new ArrayList<>();
            String termeLower = terme.toLowerCase();

            for (Entretien entretien : entretiens) {
                if ((entretien.getMotifEntr() != null && entretien.getMotifEntr().toLowerCase().contains(termeLower)) ||
                        (entretien.getLieuEntr() != null && entretien.getLieuEntr().toLowerCase().contains(termeLower))) {
                    resultats.add(entretien);
                }
            }

            return resultats;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'entretiens: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}