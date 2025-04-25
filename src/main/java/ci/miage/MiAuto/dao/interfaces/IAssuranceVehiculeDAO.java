package main.java.ci.miage.MiAuto.dao.interfaces;

import main.java.ci.miage.MiAuto.models.AssuranceVehicule;
import java.sql.SQLException;
import java.util.List;

public interface IAssuranceVehiculeDAO {

    // Associe une assurance à un véhicule
    boolean associer(int idVehicule, int numCarteAssurance) throws SQLException;

    // Supprime l'association entre une assurance et un véhicule
    boolean dissocier(int idVehicule, int numCarteAssurance) throws SQLException;

    // Trouve toutes les assurances d'un véhicule
    List<AssuranceVehicule> findByVehicule(int idVehicule) throws SQLException;

    // Trouve tous les véhicules associés à une assurance
    List<AssuranceVehicule> findByAssurance(int numCarteAssurance) throws SQLException;
}