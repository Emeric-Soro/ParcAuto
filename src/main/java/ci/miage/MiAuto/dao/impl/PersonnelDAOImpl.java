package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IPersonnelDAO;
import main.java.ci.miage.MiAuto.models.Personnel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDAOImpl extends BaseDAOImpl<Personnel> implements IPersonnelDAO {

    public PersonnelDAOImpl() {
        super();
    }

    @Override
    public Personnel findById(int id) throws SQLException {
        String query = "SELECT * FROM personnel WHERE ID_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    @Override
    public List<Personnel> findAll() throws SQLException {
        List<Personnel> list = new ArrayList<>();
        String query = "SELECT * FROM personnel";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        }
        return list;
    }

    @Override
    public Personnel save(Personnel p) throws SQLException {
        String query = "INSERT INTO personnel (NOM_PERSONNEL, PRENOM_PERSONNEL, GENRE, CONTACT_PERSONNEL, DATE_ATTRIBUTION, EMAIL_PERSONNEL, ADRESSE_PERSONNEL, DATE_EMBAUCHE, ID_FONCTION, ID_SERVICE, ID_VEHICULE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getNomPersonnel());
            stmt.setString(2, p.getPrenomPersonnel());
            stmt.setString(3, p.getGenrePersonnel());
            stmt.setString(4, p.getContactPersonnel());

            if (p.getDateAttribution() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(p.getDateAttribution()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.setString(6, p.getEmailPersonnel());
            stmt.setString(7, p.getAdressePersonnel());

            if (p.getDateEmbauche() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(p.getDateEmbauche()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }

            stmt.setInt(9, p.getIdFonction());
            stmt.setInt(10, p.getIdService());

            if (p.getIdVehicule() > 0) {
                stmt.setInt(11, p.getIdVehicule());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        p.setIdPersonnel(keys.getInt(1));
                        return p;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Personnel p) throws SQLException {
        String query = "UPDATE personnel SET NOM_PERSONNEL = ?, PRENOM_PERSONNEL = ?, GENRE = ?, CONTACT_PERSONNEL = ?, DATE_ATTRIBUTION = ?, EMAIL_PERSONNEL = ?, ADRESSE_PERSONNEL = ?, DATE_EMBAUCHE = ?, ID_FONCTION = ?, ID_SERVICE = ?, ID_VEHICULE = ? WHERE ID_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, p.getNomPersonnel());
            stmt.setString(2, p.getPrenomPersonnel());
            stmt.setString(3, p.getGenrePersonnel());
            stmt.setString(4, p.getContactPersonnel());

            if (p.getDateAttribution() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(p.getDateAttribution()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.setString(6, p.getEmailPersonnel());
            stmt.setString(7, p.getAdressePersonnel());

            if (p.getDateEmbauche() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(p.getDateEmbauche()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }

            stmt.setInt(9, p.getIdFonction());
            stmt.setInt(10, p.getIdService());

            if (p.getIdVehicule() > 0) {
                stmt.setInt(11, p.getIdVehicule());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }

            stmt.setInt(12, p.getIdPersonnel());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM personnel WHERE ID_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Personnel mapResultSetToEntity(ResultSet rs) throws SQLException {
        Personnel p = new Personnel();

        p.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
        p.setNomPersonnel(rs.getString("NOM_PERSONNEL"));
        p.setPrenomPersonnel(rs.getString("PRENOM_PERSONNEL"));
        p.setGenrePersonnel(rs.getString("GENRE"));
        p.setContactPersonnel(rs.getString("CONTACT_PERSONNEL"));

        Timestamp dateAttribution = rs.getTimestamp("DATE_ATTRIBUTION");
        if (dateAttribution != null) {
            p.setDateAttribution(dateAttribution.toLocalDateTime());
        }

        p.setEmailPersonnel(rs.getString("EMAIL_PERSONNEL"));
        p.setAdressePersonnel(rs.getString("ADRESSE_PERSONNEL"));

        Timestamp dateEmbauche = rs.getTimestamp("DATE_EMBAUCHE");
        if (dateEmbauche != null) {
            p.setDateEmbauche(dateEmbauche.toLocalDateTime());
        }

        p.setIdFonction(rs.getInt("ID_FONCTION"));
        p.setIdService(rs.getInt("ID_SERVICE"));

        int idVehicule = rs.getInt("ID_VEHICULE");
        if (!rs.wasNull()) {
            p.setIdVehicule(idVehicule);
        }

        return p;
    }

    @Override
    public Personnel findByEmail(String email) throws SQLException {
        String query = "SELECT * FROM personnel WHERE EMAIL_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM personnel WHERE EMAIL_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    @Override
    public List<Personnel> findByFonction(String idFonction) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String query = "SELECT * FROM personnel WHERE ID_FONCTION = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idFonction);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToEntity(rs));
                }
            }
        }

        return personnels;
    }

    @Override
    public List<Personnel> findByService(String idService) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String query = "SELECT * FROM personnel WHERE ID_SERVICE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idService);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToEntity(rs));
                }
            }
        }

        return personnels;
    }

    @Override
    public boolean updateService(String idPersonnel, String idNouveauService) throws SQLException {
        String query = "UPDATE personnel SET ID_SERVICE = ? WHERE ID_PERSONNEL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idNouveauService);
            stmt.setString(2, idPersonnel);

            return stmt.executeUpdate() > 0;
        }
    }
}