package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant un journal d'activité
 */
public class ActiviteLog {
    private int idActivite;
    private String typeActivite;
    private String typeReference;
    private int idReference;
    private String description;
    private LocalDateTime dateActivite;

    // Constructeur par défaut
    public ActiviteLog() {
    }

    // Constructeur complet
    public ActiviteLog(int idActivite, String typeActivite, String typeReference, int idReference,
                       String description, LocalDateTime dateActivite) {
        this.idActivite = idActivite;
        this.typeActivite = typeActivite;
        this.typeReference = typeReference;
        this.idReference = idReference;
        this.description = description;
        this.dateActivite = dateActivite;
    }

    // Getters et Setters
    public int getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(int idActivite) {
        this.idActivite = idActivite;
    }

    public String getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(String typeReference) {
        this.typeReference = typeReference;
    }

    public int getIdReference() {
        return idReference;
    }

    public void setIdReference(int idReference) {
        this.idReference = idReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(LocalDateTime dateActivite) {
        this.dateActivite = dateActivite;
    }

    @Override
    public String toString() {
        return "ActiviteLog{" +
                "idActivite=" + idActivite +
                ", typeActivite='" + typeActivite + '\'' +
                ", typeReference='" + typeReference + '\'' +
                ", idReference=" + idReference +
                ", description='" + description + '\'' +
                ", dateActivite=" + dateActivite +
                '}';
    }
}