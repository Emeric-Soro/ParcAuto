package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IUtilisateurDAO;
import main.java.ci.miage.MiAuto.models.Role;
import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl extends BaseDAOImpl<Utilisateur> implements IUtilisateurDAO {

    private RoleDAOImpl roleDAO;

    public UtilisateurDAOImpl() {
        super();
        this.roleDAO = new RoleDAOImpl();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateur (ID_PERSONNEL, ID_ROLE, LOGIN, MOT_DE_PASSE, EMAIL, STATUT) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setObject(1, utilisateur.getIdPersonnel() > 0 ? utilisateur.getIdPersonnel() : null, Types.INTEGER);
            stmt.setInt(2, utilisateur.getIdRole());
            stmt.setString(3, utilisateur.getLogin());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getEmail());
            stmt.setBoolean(6, utilisateur.isStatut());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    utilisateur.setIdUtilisateur(rs.getInt(1));
                    return utilisateur;
                }
            }
        } finally {
            closeResources(rs, stmt);
        }

        return null;
    }

    @Override
    public boolean update(Utilisateur utilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET ID_PERSONNEL = ?, ID_ROLE = ?, LOGIN = ?, " +
                "MOT_DE_PASSE = ?, EMAIL = ?, STATUT = ? WHERE ID_UTILISATEUR = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, utilisateur.getIdPersonnel() > 0 ? utilisateur.getIdPersonnel() : null, Types.INTEGER);
            stmt.setInt(2, utilisateur.getIdRole());
            stmt.setString(3, utilisateur.getLogin());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getEmail());
            stmt.setBoolean(6, utilisateur.isStatut());
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
    public Utilisateur findById(int id) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE ID_UTILISATEUR = ?";

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
        String query = "SELECT * FROM utilisateur";

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
        return null;
    }

    @Override
    public Utilisateur findByLogin(String login) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE LOGIN = ?";

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
        String query = "SELECT * FROM utilisateur WHERE ID_ROLE = ?";

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
        return false;
    }

    @Override
    public boolean desactiver(int idUtilisateur) throws SQLException {
        return false;
    }

    @Override
    public boolean activer(int idUtilisateur) throws SQLException {
        return false;
    }

    @Override
    public boolean updatePassword(int idUtilisateur, String newPassword) throws SQLException {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        return false;
    }

    @Override
    public boolean authenticate(String email, String motDePasse) throws SQLException {
        return false;
    }

    @Override
    public boolean setActivationStatus(String idUtilisateur, boolean actif) throws SQLException {
        return false;
    }

    @Override
    public boolean updateMotDePasse(String idUtilisateur, String nouveauMotDePasse) throws SQLException {
        return false;
    }

    @Override
    public List<Utilisateur> findByRole(String idRole) throws SQLException {
        return List.of();
    }

    @Override
    public void delete(String id) throws SQLException {

    }

    @Override
    public Utilisateur findById(String id) throws SQLException {
        return null;
    }

    @Override
    protected Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setIdUtilisateur(rs.getInt("ID_UTILISATEUR"));

        // Gestion de l'ID personnel qui peut être NULL
        Object idPersonnel = rs.getObject("ID_PERSONNEL");
        if (idPersonnel != null) {
            utilisateur.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
        }

        utilisateur.setIdRole(rs.getInt("ID_ROLE"));
        utilisateur.setLogin(rs.getString("LOGIN"));
        utilisateur.setMotDePasse(rs.getString("MOT_DE_PASSE"));
        utilisateur.setEmail(rs.getString("EMAIL"));
        utilisateur.setStatut(rs.getBoolean("STATUT"));

        Timestamp derniereConnexion = rs.getTimestamp("DERNIERE_CONNEXION");
        if (derniereConnexion != null) {
            utilisateur.setDerniereConnexion(derniereConnexion.toLocalDateTime());
        }

        // Charger le rôle associé
        try {
            Role role = roleDAO.findById(utilisateur.getIdRole());
            utilisateur.setRole(role);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement du rôle: " + e.getMessage());
        }

        return utilisateur;
    }
}