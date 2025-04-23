package main.java.ci.miage.MiAuto.models;


/**
 * Modèle pour les fonctions
 * */
public class Fonction {
    private int idFonction;
    private String libelleFonction;


    public Fonction(){

    }

    public Fonction(int idFonction, String libelleFonction){
        this.idFonction = idFonction;
        this.libelleFonction = libelleFonction;
    }

    public int getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibelleFonction() {
        return libelleFonction;
    }

    public void setLibelleFonction(String libelleFonction) {
        this.libelleFonction = libelleFonction;
    }
}
