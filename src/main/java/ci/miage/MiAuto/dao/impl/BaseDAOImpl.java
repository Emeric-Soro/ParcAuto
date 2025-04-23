package main.java.ci.miage.MiAuto.dao.impl;


import main.java.ci.miage.MiAuto.config.DatabaseConnection;
import main.java.ci.miage.MiAuto.dao.interfaces.IBaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation de base pour les DAO
 *
 * @param <T> type d'entité manipulée par le DAO
 */
public abstract class BaseDAOImpl<T> implements IBaseDAO<T> {

    protected DatabaseConnection dbConnection;

    /**
     * Constructeur par defaut
     */
    public BaseDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Recuperer une connexion a la bd
     *
     * @return Connexion active
     * @throws SQLException en cas d'erreur de connexion
     */
    protected Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    /**
     * Ferme les ressources
     * note : la connexion n'est pas fermée car elle est gérée par DataBaseConnection
     * @param rs resulSet a fermer
     * @param ps PreparedStatement à fermer
     */
    protected void closeResources(ResultSet rs, PreparedStatement ps){
        if(rs != null){
            try{
                rs.close();
            }catch (SQLException e){
                System.out.println("Erreur lors de la fermeture du ResultSet : "+e.getMessage());
            }
        }

        if(ps != null){
            try{
                ps.close();
            }catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Exécute une requête avec paramètres et renvoie un objet
     * @param query Requête SQL à exécuter
     * @param parameters Paramètres de la requête
     * @return Objet résultant
     * @throws SQLException En cas d'erreur SQL
     */
    protected T executeSingleResultQuery(String query, Object... parameters) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);

            // Définir les paramètres
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                result = mapResultSetToEntity(rs);
            }
        } finally {
            closeResources(rs, ps);
        }

        return result;
    }

    /**
     * Exécute une requête avec paramètres et renvoie une liste d'objets
     * @param query Requête SQL à exécuter
     * @param parameters Paramètres de la requête
     * @return Liste d'objets résultants
     * @throws SQLException En cas d'erreur SQL
     */
    protected List<T> executeListQuery(String query, Object... parameters) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> results = new ArrayList<>();

        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);

            // Définir les paramètres
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } finally {
            closeResources(rs, ps);
        }

        return results;
    }

    /**
     * Exécute une mise à jour avec paramètres (INSERT, UPDATE, DELETE)
     * @param query Requête SQL à exécuter
     * @param parameters Paramètres de la requête
     * @return Nombre de lignes affectées
     * @throws SQLException En cas d'erreur SQL
     */
    protected int executeUpdate(String query, Object... parameters) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);

            // Définir les paramètres
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            result = ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }

        return result;
    }

    /**
     * Exécute un INSERT et récupère l'ID généré
     * @param query Requête SQL à exécuter
     * @param parameters Paramètres de la requête
     * @return ID généré ou -1 si aucun ID n'a été généré
     * @throws SQLException En cas d'erreur SQL
     */
    protected int executeInsertWithId(String query, Object... parameters) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int generatedId = -1;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Définir les paramètres
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } finally {
            closeResources(rs, ps);
        }

        return generatedId;
    }

    @Override
    public T save(T entity) throws SQLException {
        // Cette méthode doit être implémentée par les classes enfants
        throw new UnsupportedOperationException("La méthode save doit être implémentée par les classes enfants");
    }

    @Override
    public boolean update(T entity) throws SQLException {
        // Cette méthode doit être implémentée par les classes enfants
        throw new UnsupportedOperationException("La méthode update doit être implémentée par les classes enfants");
    }

    @Override
    public boolean delete(int id) throws SQLException {
        // Cette méthode doit être implémentée par les classes enfants
        throw new UnsupportedOperationException("La méthode delete doit être implémentée par les classes enfants");
    }

    @Override
    public T findById(int id) throws SQLException {
        // Cette méthode doit être implémentée par les classes enfants
        throw new UnsupportedOperationException("La méthode findById doit être implémentée par les classes enfants");
    }

    @Override
    public List<T> findAll() throws SQLException {
        // Cette méthode doit être implémentée par les classes enfants
        throw new UnsupportedOperationException("La méthode findAll doit être implémentée par les classes enfants");
    }

    /**
     * Convertit un ResultSet en entité
     * @param rs ResultSet à convertir
     * @return Entité correspondante
     * @throws SQLException En cas d'erreur SQL
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
