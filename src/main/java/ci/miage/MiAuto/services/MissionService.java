package main.java.ci.miage.MiAuto.services;

import main.java.ci.miage.MiAuto.dao.impl.ActiviteLogDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.MissionDAOImpl;
import main.java.ci.miage.MiAuto.dao.impl.VehiculeDAOImpl;
import main.java.ci.miage.MiAuto.models.ActiviteLog;
import main.java.ci.miage.MiAuto.models.Mission;
import main.java.ci.miage.MiAuto.models.Personnel;
import main.java.ci.miage.MiAuto.models.Vehicule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionService {

    private MissionDAOImpl missionDAO;
    private VehiculeDAOImpl vehiculeDAO;
    private ActiviteLogDAOImpl activiteLogDAO;

    public MissionService() {
        this.missionDAO = new MissionDAOImpl();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.activiteLogDAO = new ActiviteLogDAOImpl();
    }

    /**
     * Récupère une mission par son ID
     * @param idMission ID de la mission
     * @return Mission trouvée ou null
     */
    public Mission getMissionById(int idMission) {
        try {
            return missionDAO.findById(idMission);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la mission: " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère toutes les missions
     * @return Liste de toutes les missions
     */
    public List<Mission> getAllMissions() {
        try {
            return missionDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ajoute une nouvelle mission
     * @param mission Mission à ajouter
     * @return Mission ajoutée avec son ID généré, ou null en cas d'erreur
     */
    public Mission addMission(Mission mission) {
        try {
            // Vérifier que le véhicule existe
            Vehicule vehicule = vehiculeDAO.findById(mission.getIdVehicule());
            if (vehicule == null) {
                return null;
            }

            // Vérifier que le véhicule est disponible
            if (vehicule.getIdEtatVoiture() != 1) { // 1 = Disponible
                return null;
            }

            // Créer la mission
            Mission nouvelleMission = missionDAO.save(mission);

            if (nouvelleMission != null) {
                // Mettre à jour l'état du véhicule à "En mission"
                vehiculeDAO.updateEtat(mission.getIdVehicule(), 2); // 2 = En mission

                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("CREATION");
                log.setTypeReference("MISSION");
                log.setIdReference(nouvelleMission.getIdMission());
                log.setDescription("Une nouvelle mission a été créée: " + nouvelleMission.getLibMission());
                activiteLogDAO.save(log);
            }

            return nouvelleMission;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la mission: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour une mission existante
     * @param mission Mission à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateMission(Mission mission) {
        try {
            // Récupérer l'ancienne mission
            Mission ancienneMission = missionDAO.findById(mission.getIdMission());
            if (ancienneMission == null) {
                return false;
            }

            // Si le véhicule a changé, vérifier que le nouveau véhicule est disponible
            if (ancienneMission.getIdVehicule() != mission.getIdVehicule()) {
                Vehicule nouveauVehicule = vehiculeDAO.findById(mission.getIdVehicule());
                if (nouveauVehicule == null || nouveauVehicule.getIdEtatVoiture() != 1) { // 1 = Disponible
                    return false;
                }

                // Remettre l'ancien véhicule à l'état "Disponible"
                vehiculeDAO.updateEtat(ancienneMission.getIdVehicule(), 1); // 1 = Disponible

                // Mettre le nouveau véhicule à l'état "En mission"
                vehiculeDAO.updateEtat(mission.getIdVehicule(), 2); // 2 = En mission
            }

            // Mettre à jour la mission
            boolean updated = missionDAO.update(mission);

            if (updated) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("MODIFICATION");
                log.setTypeReference("MISSION");
                log.setIdReference(mission.getIdMission());
                log.setDescription("Mission modifiée: " + mission.getLibMission());
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la mission: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime une mission
     * @param idMission ID de la mission à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteMission(int idMission) {
        try {
            // Récupérer la mission
            Mission mission = missionDAO.findById(idMission);
            if (mission == null) {
                return false;
            }

            // Vérifier si la mission est terminée
            if (mission.getDateFinMission() == null || mission.getDateFinMission().isAfter(LocalDateTime.now())) {
                // Si la mission n'est pas terminée, on ne peut pas la supprimer
                return false;
            }

            // Supprimer la mission
            boolean deleted = missionDAO.delete(idMission);

            if (deleted) {
                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("SUPPRESSION");
                log.setTypeReference("MISSION");
                log.setIdReference(idMission);
                log.setDescription("Mission supprimée: " + mission.getLibMission());
                activiteLogDAO.save(log);
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la mission: " + e.getMessage());
            return false;
        }
    }

    /**
     * Termine une mission
     * @param idMission ID de la mission à terminer
     * @param dateFinMission Date de fin de la mission
     * @param coutMission Coût total de la mission
     * @param coutCarburant Coût du carburant
     * @param observations Observations sur la mission
     * @param kilometrage Nouveau kilométrage du véhicule
     * @return true si la terminaison a réussi, false sinon
     */
    public boolean terminerMission(int idMission, LocalDateTime dateFinMission, int coutMission,
                                   int coutCarburant, String observations, int kilometrage) {
        try {
            // Récupérer la mission
            Mission mission = missionDAO.findById(idMission);
            if (mission == null) {
                return false;
            }

            // Vérifier que la mission n'est pas déjà terminée
            if (mission.getDateFinMission() != null && mission.getDateFinMission().isBefore(LocalDateTime.now())) {
                return false;
            }

            // Mettre à jour la mission
            mission.setDateFinMission(dateFinMission);
            mission.setCoutMission(coutMission);
            mission.setCoutCarburant(coutCarburant);
            mission.setObservationMission(observations);

            boolean updated = missionDAO.update(mission);

            if (updated) {
                // Mettre à jour le véhicule
                vehiculeDAO.updateEtat(mission.getIdVehicule(), 1); // 1 = Disponible
                vehiculeDAO.updateKilometrage(mission.getIdVehicule(), kilometrage);

                // Enregistrer l'activité
                ActiviteLog log = new ActiviteLog();
                log.setTypeActivite("TERMINER_MISSION");
                log.setTypeReference("MISSION");
                log.setIdReference(idMission);
                log.setDescription("Mission terminée: " + mission.getLibMission());
                activiteLogDAO.save(log);
            }

            return updated;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la terminaison de la mission: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ajoute un participant à une mission
     * @param idMission ID de la mission
     * @param idPersonnel ID du personnel
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addParticipant(int idMission, int idPersonnel) {
        try {
            return missionDAO.addParticipant(idMission, idPersonnel);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du participant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un participant d'une mission
     * @param idMission ID de la mission
     * @param idPersonnel ID du personnel
     * @return true si la suppression a réussi, false sinon
     */
    public boolean removeParticipant(int idMission, int idPersonnel) {
        try {
            return missionDAO.removeParticipant(idMission, idPersonnel);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du participant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les participants d'une mission
     * @param idMission ID de la mission
     * @return Liste des participants
     */
    public List<Personnel> getMissionParticipants(int idMission) {
        try {
            return missionDAO.findParticipants(idMission);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participants: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les missions d'un véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des missions du véhicule
     */
    public List<Mission> getMissionsByVehicule(int idVehicule) {
        try {
            return missionDAO.findByVehicule(idVehicule);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions du véhicule: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les missions en cours
     * @return Liste des missions en cours
     */
    public List<Mission> getMissionsEnCours() {
        try {
            return missionDAO.findEnCours();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions en cours: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les missions à venir
     * @return Liste des missions à venir
     */
    public List<Mission> getMissionsAVenir() {
        try {
            return missionDAO.findAVenir();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions à venir: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les missions terminées
     * @return Liste des missions terminées
     */
    public List<Mission> getMissionsTerminees() {
        try {
            return missionDAO.findTerminees();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions terminées: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Récupère les missions dans une période donnée
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des missions dans la période
     */
    public List<Mission> getMissionsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return missionDAO.findByPeriode(debut, fin);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des missions par période: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}