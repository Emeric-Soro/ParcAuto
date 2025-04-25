package main.java.ci.miage.MiAuto.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe utilitaire pour les fonctionnalités liées à la sécurité
 */
public class SecurityUtils {

    private static final int SALT_LENGTH = 16;

    /**
     * Génère un sel aléatoire pour le hachage des mots de passe
     * @return Sel généré
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hache un mot de passe avec SHA-256 et un sel
     * @param password Mot de passe à hacher
     * @param salt Sel à utiliser
     * @return Mot de passe haché
     */
    public static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Combine le sel et le mot de passe haché sous forme de chaîne Base64
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    /**
     * Hache un mot de passe avec un nouveau sel
     * @param password Mot de passe à hacher
     * @return Mot de passe haché
     */
    public static String hashPassword(String password) {
        return hashPassword(password, generateSalt());
    }

    /**
     * Vérifie si un mot de passe correspond à un hash stocké
     * @param password Mot de passe à vérifier
     * @param storedHash Hash stocké
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Décoder le hash stocké
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extraire le sel et le hash
            byte[] salt = new byte[SALT_LENGTH];
            byte[] hash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, hash, 0, hash.length);

            // Hacher le mot de passe fourni avec le même sel
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] newHash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Comparer les hashs
            return MessageDigest.isEqual(hash, newHash);
        } catch (Exception e) {
            // Pour une implémentation simple, toute erreur signifie que les mots de passe ne correspondent pas
            return false;
        }
    }

    /**
     * Génère un token aléatoire (utile pour les tokens de réinitialisation de mot de passe, etc.)
     * @param length Longueur du token
     * @return Token généré
     */
    public static String generateToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}