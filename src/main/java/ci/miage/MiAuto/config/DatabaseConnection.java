package main.java.ci.miage.MiAuto.config;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe de gestion de la connexion à la base de données
 */
public class DatabaseConnection {
    private static final String CONFIG_FILE = "/data/config.properties";
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String user;
    private String password;

    /**
     * Constructeur privé pour le singleton
     */
    private DatabaseConnection() {
        loadConfig();
    }

    /**
     * Charge la configuration depuis le fichier de propriétés
     */
    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Si le fichier n'existe pas, utiliser des valeurs par défaut
                url = "jdbc:mysql://localhost:3306/bdGestionParcAuto?useSSL=false&serverTimezone=UTC";
                user = "root";
                password = "";
                System.out.println("Fichier de configuration non trouvé, utilisation des valeurs par défaut");
                return;
            }

            props.load(input);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier de configuration : " + e.getMessage());
            // Utiliser des valeurs par défaut en cas d'erreur
            url = "jdbc:mysql://localhost:3306/bdGestionParcAuto?useSSL=false&serverTimezone=UTC";
            user = "root";
            password = "";
        }
    }

    /**
     * Récupère l'instance unique de la classe (Singleton)
     *
     * @return L'instance de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Établit une connexion à la base de données
     *
     * @return La connexion établie
     * @throws SQLException En cas d'erreur lors de la connexion
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL non trouvé", e);
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}