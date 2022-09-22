package ru.dvdishka.countries.Classes;

import java.io.Serializable;
import java.util.HashSet;

public class Rank implements Serializable {

    private String name;
    private HashSet<Permission> permissions = new HashSet<>();

    public Rank(String name, HashSet<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public Rank(String name) {
        this.name = name;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(HashSet<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return this.name;
    }

    public HashSet<Permission> getPermissions() {
        return this.permissions;
    }
}
