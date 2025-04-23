package main.java.ci.miage.MiAuto.dao.interfaces;


import java.sql.SQLException;
import java.util.List;

/**
 * Interface générique pour les opérations CRUD de base
 *
 * @param <T> Type d'entité
 */
public interface IBaseDAO<T> {

    /**
     * Enregistre une nouvelle entité
     *
     * @param entity Entité à enregistrer
     * @return L'entité avec l'ID généré
     * @throws SQLException En cas d'erreur SQL
     */
    T save(T entity) throws SQLException;

    /**
     * Met à jour une entité existante
     *
     * @param entity Entité à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean update(T entity) throws SQLException;

    /**
     * Supprime une entité par son ID
     *
     * @param id ID de l'entité à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    boolean delete(int id) throws SQLException;

    /**
     * Récupère une entité par son ID
     *
     * @param id ID de l'entité à récupérer
     * @return L'entité correspondante ou null si non trouvée
     * @throws SQLException En cas d'erreur SQL
     */
    T findById(int id) throws SQLException;

    /**
     * Récupère toutes les entités
     *
     * @return Liste de toutes les entités
     * @throws SQLException En cas d'erreur SQL
     */
    List<T> findAll() throws SQLException;
}