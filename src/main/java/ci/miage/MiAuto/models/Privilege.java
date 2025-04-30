package main.java.ci.miage.MiAuto.models;

/**
 * Modèle représentant un privilège utilisateur
 */
public class Privilege {
    private int idPrivilege;
    private String nomPrivilege;
    private String description;

    // Constructeur par défaut
    public Privilege() {
    }

    // Constructeur avec champs obligatoires
    public Privilege(int idPrivilege, String nomPrivilege) {
        this.idPrivilege = idPrivilege;
        this.nomPrivilege = nomPrivilege;
    }

    // Constructeur complet
    public Privilege(int idPrivilege, String nomPrivilege, String description) {
        this.idPrivilege = idPrivilege;
        this.nomPrivilege = nomPrivilege;
        this.description = description;
    }

    // Getters et Setters
    public int getIdPrivilege() {
        return idPrivilege;
    }

    public void setIdPrivilege(int idPrivilege) {
        this.idPrivilege = idPrivilege;
    }

    public String getNomPrivilege() {
        return nomPrivilege;
    }

    public void setNomPrivilege(String nomPrivilege) {
        this.nomPrivilege = nomPrivilege;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Privilege privilege = (Privilege) o;

        return idPrivilege == privilege.idPrivilege;
    }

    @Override
    public int hashCode() {
        return idPrivilege;
    }

    @Override
    public String toString() {
        return nomPrivilege;
    }
}