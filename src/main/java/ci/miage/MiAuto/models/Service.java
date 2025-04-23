package main.java.ci.miage.MiAuto.models;

public class Service {
    private int idService;
    private String libelleService;
    private String localisationService;


    public Service() {

    }

    // Constructeur avec les champs obligatoires
    public Service(int idService, String libelleService) {
        this.idService = idService;
        this.libelleService = libelleService;
    }

    // Constructeur complet
    public Service(int idService, String libelleService, String localisationService) {
        this.idService = idService;
        this.libelleService = libelleService;
        this.localisationService = localisationService;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getLibelleService() {
        return libelleService;
    }

    public void setLibelleService(String libelleService) {
        this.libelleService = libelleService;
    }

    public String getLocalisationService() {
        return localisationService;
    }

    public void setLocalisationService(String localisationService) {
        this.localisationService = localisationService;
    }
}
