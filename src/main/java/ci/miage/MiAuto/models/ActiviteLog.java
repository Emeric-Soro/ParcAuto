package main.java.ci.miage.MiAuto.models;

import java.time.LocalDateTime;

/**
 * Classe représentant les logs d'activité dans le système
 */
public class ActiviteLog {
    private int idActivite;
    private String typeActivite;  // ex: CREATION, MODIFICATION, SUPPRESSION, ATTRIBUTION, etc.
    private String typeReference; // ex: VEHICULE, MISSION, ENTRETIEN, etc.
    private int idReference;      // ID de l'entité concernée
    private String description;
    private LocalDateTime dateActivite;

    /**
     * Constructeur par défaut
     */
    public ActiviteLog() {
        this.dateActivite = LocalDateTime.now();
    }

    /**
     * Constructeur avec paramètres
     * @param typeActivite Type d'activité
     * @param typeReference Type de référence
     * @param idReference ID de référence
     * @param description Description de l'activité
     */
    public ActiviteLog(String typeActivite, String typeReference, int idReference, String description) {
        this.typeActivite = typeActivite;
        this.typeReference = typeReference;
        this.idReference = idReference;
        this.description = description;
        this.dateActivite = LocalDateTime.now();
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