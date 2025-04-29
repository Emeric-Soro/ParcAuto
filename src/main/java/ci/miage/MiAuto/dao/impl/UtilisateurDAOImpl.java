package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IUtilisateurDAO;
import main.java.ci.miage.MiAuto.models.Role;
import main.java.ci.miage.MiAuto.models.Utilisateur;
import main.java.ci.miage.MiAuto.utils.SecurityUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les opérations liées aux utilisateurs
 */
public class UtilisateurDAOImpl extends BaseDAOImpl<Utilisateur> implements IUtilisateurDAO {

    /**
     * Constructeur
     */
    public UtilisateurDAOImpl() {
        super();
    }

    @Override
    public Utilisateur findById(int id) throws SQLException {
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION AS ROLE_DESCRIPTION " +
                "FROM utilisateur u " +
                "JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
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
    public Utilisateur findById(String id) throws SQLException {
        return findById(Integer.parseInt(id));
    }

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION AS ROLE_DESCRIPTION " +
                "FROM utilisateur u " +
                "JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
                "ORDER BY u.LOGIN";

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
    public Utilisateur save(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateur (ID_PERSONNEL, ID_ROLE, LOGIN, MOT_DE_PASSE, EMAIL, STATUT) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Paramètres
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
        String query = "UPDATE utilisateur SET ID_PERSONNEL = ?, ID_ROLE = ?, LOGIN = ?, " +
                "EMAIL = ?, STATUT = ?, DERNIERE_CONNEXION = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            Integer idPersonnel = utilisateur.getIdPersonnel();
            if (idPersonnel != null && idPersonnel != 0) {
                stmt.setInt(1, idPersonnel);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, utilisateur.getIdRole());
            stmt.setString(3, utilisateur.getLogin());
            stmt.setString(4, utilisateur.getEmail());
            stmt.setBoolean(5, utilisateur.isStatut());

            if (utilisateur.getDerniereConnexion() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(utilisateur.getDerniereConnexion()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            stmt.setInt(7, utilisateur.getIdUtilisateur());

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
    public void delete(String id) throws SQLException {
        delete(Integer.parseInt(id));
    }

    @Override
    public Utilisateur findByEmail(String email) throws SQLException {
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION AS ROLE_DESCRIPTION " +
                "FROM utilisateur u " +
                "JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
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
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION AS ROLE_DESCRIPTION " +
                "FROM utilisateur u " +
                "JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
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
    public List<Utilisateur> findByRole(int idRole) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT u.*, r.NOM_ROLE, r.DESCRIPTION AS ROLE_DESCRIPTION " +
                "FROM utilisateur u " +
                "JOIN role r ON u.ID_ROLE = r.ID_ROLE " +
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
    public List<Utilisateur> findByRole(String idRole) throws SQLException {
        return findByRole(Integer.parseInt(idRole));
    }

    @Override
    public boolean existsByLogin(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE LOGIN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE EMAIL = ?";

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
    public boolean desactiver(int idUtilisateur) throws SQLException {
        return setActivationStatus(String.valueOf(idUtilisateur), false);
    }

    @Override
    public boolean activer(int idUtilisateur) throws SQLException {
        return setActivationStatus(String.valueOf(idUtilisateur), true);
    }

    @Override
    public boolean updatePassword(int idUtilisateur, String newPassword) throws SQLException {
        return updateMotDePasse(String.valueOf(idUtilisateur), newPassword);
    }

    @Override
    public boolean authenticate(String email, String motDePasse) throws SQLException {
        Utilisateur utilisateur = findByEmail(email);
        if (utilisateur == null) {
            // Essayer avec le login si l'email n'existe pas
            utilisateur = findByLogin(email);
        }

        if (utilisateur != null && utilisateur.isStatut()) {
            // Vérifier le mot de passe
            return SecurityUtils.verifyPassword(motDePasse, utilisateur.getMotDePasse());
        }

        return false;
    }

    @Override
    public boolean setActivationStatus(String idUtilisateur, boolean actif) throws SQLException {
        String query = "UPDATE utilisateur SET STATUT = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, actif);
            stmt.setInt(2, Integer.parseInt(idUtilisateur));

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateMotDePasse(String idUtilisateur, String nouveauMotDePasse) throws SQLException {
        String query = "UPDATE utilisateur SET MOT_DE_PASSE = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nouveauMotDePasse);
            stmt.setInt(2, Integer.parseInt(idUtilisateur));

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Convertit un ResultSet en objet Utilisateur
     *
     * @param rs ResultSet à convertir
     * @return Objet Utilisateur
     * @throws SQLException En cas d'erreur SQL
     */
    @Override
    protected Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setIdUtilisateur(rs.getInt("ID_UTILISATEUR"));

        // Récupérer l'ID du personnel s'il existe
        int idPersonnel = rs.getInt("ID_PERSONNEL");
        if (!rs.wasNull()) {
            utilisateur.setIdPersonnel(idPersonnel);
        }

        utilisateur.setIdRole(rs.getInt("ID_ROLE"));
        utilisateur.setLogin(rs.getString("LOGIN"));
        utilisateur.setMotDePasse(rs.getString("MOT_DE_PASSE"));
        utilisateur.setEmail(rs.getString("EMAIL"));
        utilisateur.setStatut(rs.getBoolean("STATUT"));

        Timestamp lastLogin = rs.getTimestamp("DERNIERE_CONNEXION");
        if (lastLogin != null) {
            utilisateur.setDerniereConnexion(lastLogin.toLocalDateTime());
        }

        // Informations sur le rôle
        if (rs.getMetaData().getColumnCount() > 7) {
            Role role = new Role();
            role.setIdRole(rs.getInt("ID_ROLE"));
            role.setNomRole(rs.getString("NOM_ROLE"));
            role.setDescription(rs.getString("ROLE_DESCRIPTION"));
            utilisateur.setRole(role);
        }

        return utilisateur;
    }
}