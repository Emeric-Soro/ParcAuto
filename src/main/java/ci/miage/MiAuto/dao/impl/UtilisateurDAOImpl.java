package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IUtilisateurDAO;
import main.java.ci.miage.MiAuto.models.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements IUtilisateurDAO {

    private final Connection connection;

    public UtilisateurDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateur (idUtilisateur, idPersonnel, idRole, login, motDePasse, email, statut, derniereConnexion, nom) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, utilisateur.getIdUtilisateur());
            if (utilisateur.getIdPersonnel() != null) {
                stmt.setInt(2, utilisateur.getIdPersonnel());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, utilisateur.getIdRole());
            stmt.setString(4, utilisateur.getLogin());
            stmt.setString(5, utilisateur.getMotDePasse());
            stmt.setString(6, utilisateur.getEmail());
            stmt.setBoolean(7, utilisateur.isStatut());
            stmt.setTimestamp(8, Timestamp.valueOf(utilisateur.getDerniereConnexion()));
            stmt.executeUpdate();
        }
        return utilisateur;
    }

    @Override
    public Utilisateur findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extract(rs);
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean authenticate(String email, String motDePasse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean setActivationStatus(String id, boolean actif) throws SQLException {
        String sql = "UPDATE utilisateur SET statut = ? WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, actif);
            stmt.setInt(2, Integer.parseInt(id));
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateMotDePasse(String id, String nouveauMotDePasse) throws SQLException {
        String sql = "UPDATE utilisateur SET motDePasse = ? WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nouveauMotDePasse);
            stmt.setInt(2, Integer.parseInt(id));
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Utilisateur> findByRole(String idRole) throws SQLException {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur WHERE idRole = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idRole));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extract(rs));
        }
        return list;
    }

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extract(rs));
        }
        return list;
    }

    @Override
    public boolean update(Utilisateur u) throws SQLException {
        String sql = "UPDATE utilisateur SET idPersonnel = ?, idRole = ?, login = ?, motDePasse = ?, email = ?, statut = ?, derniereConnexion = ?, nom = ? WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (u.getIdPersonnel() != null) {
                stmt.setInt(1, u.getIdPersonnel());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, u.getIdRole());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getEmail());
            stmt.setBoolean(6, u.isStatut());
            stmt.setTimestamp(7, Timestamp.valueOf(u.getDerniereConnexion()));
            stmt.setInt(9, u.getIdUtilisateur());
            stmt.executeUpdate();
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return false;
    }

    @Override
    public Utilisateur findById(int id) throws SQLException {
        return null;
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        }
    }

    @Override
    public Utilisateur findById(String id) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extract(rs);
        }
        return null;
    }

    private Utilisateur extract(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getInt("idUtilisateur"));
        int idPers = rs.getInt("idPersonnel");
        u.setIdPersonnel(rs.wasNull() ? null : idPers);
        u.setIdRole(rs.getInt("idRole"));
        u.setLogin(rs.getString("login"));
        u.setMotDePasse(rs.getString("motDePasse"));
        u.setEmail(rs.getString("email"));
        u.setStatut(rs.getBoolean("statut"));
        Timestamp ts = rs.getTimestamp("derniereConnexion");
        u.setDerniereConnexion(ts != null ? ts.toLocalDateTime() : null);
        return u;
    }
}
