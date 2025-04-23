package main.java.ci.miage.MiAuto.models;

public class RolePrivilege {
    private int idRole;
    private int idPrivilege;

    public RolePrivilege() {
    }

    public RolePrivilege(int idRole, int idPrivilege) {
        this.idRole = idRole;
        this.idPrivilege = idPrivilege;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public int getIdPrivilege() {
        return idPrivilege;
    }

    public void setIdPrivilege(int idPrivilege) {
        this.idPrivilege = idPrivilege;
    }

    @Override
    public String toString() {
        return "RolePrivilege{" +
                "idRole=" + idRole +
                ", idPrivilege=" + idPrivilege +
                '}';
    }
}
