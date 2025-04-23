package main.java.ci.miage.MiAuto.models;

import java.util.List;

/**
 * Modèle représentant un rôle dans le système
 */
public class Role {
    private int idRole;
    private String nomRole;
    private String description;

    // Relations
    private List<Privilege> privileges;

    // Constructeur par défaut
    public Role() {
    }

    // Constructeur avec les champs obligatoires
    public Role(String nomRole) {
        this.nomRole = nomRole;
    }

    // Constructeur complet
    public Role(int idRole, String nomRole, String description) {
        this.idRole = idRole;
        this.nomRole = nomRole;
        this.description = description;
    }

    // Getters et Setters
    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getNomRole() {
        return nomRole;
    }

    public void setNomRole(String nomRole) {
        this.nomRole = nomRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * Vérifie si le rôle possède un privilège spécifique
     * @param nomPrivilege Le nom du privilège à vérifier
     * @return true si le rôle possède le privilège, false sinon
     */
    public boolean hasPrivilege(String nomPrivilege) {
        if (privileges == null || privileges.isEmpty()) {
            return false;
        }

        return privileges.stream()
                .anyMatch(privilege -> privilege.getNomPrivilege().equals(nomPrivilege));
    }

    @Override
    public String toString() {
        return nomRole;
    }
}