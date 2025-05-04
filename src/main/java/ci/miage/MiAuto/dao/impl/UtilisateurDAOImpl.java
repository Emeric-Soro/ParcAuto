package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IUtilisateurDAO;
import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl extends BaseDAOImpl<Utilisateur> implements IUtilisateurDAO {

    public UtilisateurDAOImpl() {
        super();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateur (ID_PERSONNEL, ID_ROLE, LOGIN, MOT_DE_PASSE, EMAIL, STATUT) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (utilisateur.getIdPersonnel() > 0) {
                stmt.setInt(1, utilisateur.getIdPersonnel());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, utilisateur.getIdRole());
            stmt.setString(3, utilisateur.getLogin());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getEmail());
            stmt.setBoolean(6, utilisateur.isStatut());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateur.setIdUtilisateur(generatedKeys.getInt(1));
                        return utilisateur;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(Utilisateur utilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET ID_PERSONNEL = ?, ID_ROLE = ?, LOGIN = ?, EMAIL = ?, STATUT = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (utilisateur.getIdPersonnel() > 0) {
                stmt.setInt(1, utilisateur.getIdPersonnel());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, utilisateur.getIdRole());
            stmt.setString(3, utilisateur.getLogin());
            stmt.setString(4, utilisateur.getEmail());
            stmt.setBoolean(5, utilisateur.isStatut());
            stmt.setInt(6, utilisateur.getIdUtilisateur());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM utilisateur WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Utilisateur findById(int id) throws SQLException {
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION as ROLE_DESCRIPTION, " +
                "p.NOM_PERSONNEL, p.PRENOM_PERSONNEL " +
                "FROM utilisateur u " +
                "LEFT JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "LEFT JOIN personnel p ON u.ID_PERSONNEL = p.ID_PERSONNEL " +
                "WHERE u.ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION as ROLE_DESCRIPTION, " +
                "p.NOM_PERSONNEL, p.PRENOM_PERSONNEL " +
                "FROM utilisateur u " +
                "LEFT JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "LEFT JOIN personnel p ON u.ID_PERSONNEL = p.ID_PERSONNEL " +
                "ORDER BY u.ID_UTILISATEUR";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                utilisateurs.add(mapResultSetToEntity(rs));
            }
        }

        return utilisateurs;
    }

    @Override
    public Utilisateur findByEmail(String email) throws SQLException {
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION as ROLE_DESCRIPTION, " +
                "p.NOM_PERSONNEL, p.PRENOM_PERSONNEL " +
                "FROM utilisateur u " +
                "LEFT JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "LEFT JOIN personnel p ON u.ID_PERSONNEL = p.ID_PERSONNEL " +
                "WHERE u.EMAIL = ?";

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
    public Utilisateur findByLogin(String login) throws SQLException {
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION as ROLE_DESCRIPTION, " +
                "p.NOM_PERSONNEL, p.PRENOM_PERSONNEL " +
                "FROM utilisateur u " +
                "LEFT JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "LEFT JOIN personnel p ON u.ID_PERSONNEL = p.ID_PERSONNEL " +
                "WHERE u.LOGIN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public boolean updateLastConnection(int idUtilisateur, LocalDateTime dateConnexion) throws SQLException {
        String query = "UPDATE utilisateur SET DERNIERE_CONNEXION = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(dateConnexion));
            stmt.setInt(2, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Utilisateur> findByRole(int idRole) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION as ROLE_DESCRIPTION, " +
                "p.NOM_PERSONNEL, p.PRENOM_PERSONNEL " +
                "FROM utilisateur u " +
                "LEFT JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "LEFT JOIN personnel p ON u.ID_PERSONNEL = p.ID_PERSONNEL " +
                "WHERE u.ID_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRole);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapResultSetToEntity(rs));
                }
            }
        }

        return utilisateurs;
    }

    @Override
    public boolean existsByLogin(String login) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM utilisateur WHERE LOGIN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean desactiver(int idUtilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET STATUT = 0 WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean activer(int idUtilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET STATUT = 1 WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePassword(int idUtilisateur, String newPassword) throws SQLException {
        String query = "UPDATE utilisateur SET MOT_DE_PASSE = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM utilisateur WHERE EMAIL = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean authenticate(String email, String motDePasse) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM utilisateur WHERE EMAIL = ? AND MOT_DE_PASSE = ? AND STATUT = 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean setActivationStatus(String idUtilisateur, boolean actif) throws SQLException {
        String query = "UPDATE utilisateur SET STATUT = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, actif);
            stmt.setString(2, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateMotDePasse(String idUtilisateur, String nouveauMotDePasse) throws SQLException {
        String query = "UPDATE utilisateur SET MOT_DE_PASSE = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nouveauMotDePasse);
            stmt.setString(2, idUtilisateur);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Utilisateur> findByRole(String idRole) throws SQLException {
        return findByRole(Integer.parseInt(idRole));
    }

    @Override
    public void delete(String id) throws SQLException {
        delete(Integer.parseInt(id));
    }

    @Override
    public Utilisateur findById(String id) throws SQLException {
        return findById(Integer.parseInt(id));
    }

    @Override
    public boolean loginExists(String login, int idUtilisateur) throws SQLException {
        return false;
    }

    @Override
    public boolean updateStatus(int idUtilisateur, boolean statut) throws SQLException {
        return false;
    }

    @Override
    public List<Utilisateur> findActive() throws SQLException {
        return List.of();
    }

    @Override
    public List<Utilisateur> findInactive() throws SQLException {
        return List.of();
    }

    @Override
    protected Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setIdUtilisateur(rs.getInt("ID_UTILISATEUR"));
        utilisateur.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
        utilisateur.setIdRole(rs.getInt("ID_ROLE"));
        utilisateur.setLogin(rs.getString("LOGIN"));
        utilisateur.setMotDePasse(rs.getString("MOT_DE_PASSE"));
        utilisateur.setEmail(rs.getString("EMAIL"));
        utilisateur.setStatut(rs.getBoolean("STATUT"));

        Timestamp tsDerniereConnexion = rs.getTimestamp("DERNIERE_CONNEXION");
        if (tsDerniereConnexion != null) {
            utilisateur.setDerniereConnexion(tsDerniereConnexion.toLocalDateTime());
        }

        // Charger les informations du rôle
        try {
            String nomRole = rs.getString("NOM_ROLE");
            if (nomRole != null) {
                main.java.ci.miage.MiAuto.models.Role role = new main.java.ci.miage.MiAuto.models.Role();
                role.setIdRole(rs.getInt("ID_ROLE"));
                role.setNomRole(nomRole);
                role.setDescription(rs.getString("ROLE_DESCRIPTION"));
                utilisateur.setRole(role);
            }
        } catch (SQLException e) {
            // Ignorer si les colonnes ne sont pas présentes
        }

        // Charger les informations du personnel
        try {
            String nomPersonnel = rs.getString("NOM_PERSONNEL");
            if (nomPersonnel != null) {
                main.java.ci.miage.MiAuto.models.Personnel personnel = new main.java.ci.miage.MiAuto.models.Personnel();
                personnel.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
                personnel.setNomPersonnel(nomPersonnel);
                personnel.setPrenomPersonnel(rs.getString("PRENOM_PERSONNEL"));
                utilisateur.setPersonnel(personnel);
            }
        } catch (SQLException e) {
            // Ignorer si les colonnes ne sont pas présentes
        }

        return utilisateur;
    }
}