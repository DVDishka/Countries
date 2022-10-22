package ru.dvdishka.countries.Classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashSet;

public class Rank implements Serializable {

    private String name;
    private HashSet<Permission> permissions = new HashSet<>();
    private String icon = "BARRIER";
    private int priority = 1;

    public Rank(String name, HashSet<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
        this.icon = "BARRIER";
    }

    public Rank(String name, HashSet<Permission> permissions, String icon, int priority) {
        this.name = name;
        this.permissions = permissions;
        if (icon == null) {
            icon = "BARRIER";
        }
        this.icon = icon;
        this.priority = priority;
    }

    public Rank(String name) {
        this.name = name;
        this.icon = "BARRIER";
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

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public HashSet<Permission> getPermissions() {
        return this.permissions;
    }

    public String getIcon() {
        return this.icon;
    }

    public int getPriority() {
        return this.priority;
    }
}
