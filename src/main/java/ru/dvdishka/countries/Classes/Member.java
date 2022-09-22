package ru.dvdishka.countries.Classes;

import java.io.Serializable;

public class Member implements Serializable {

    private String name;
    private Rank rank;

    public Member(String name, Rank rank) {
        this.name = name;
        this.rank = rank;
    }

    public Member(String name) {
        this.name = name;
    }

    public boolean hasPermission(Permission permission) {

        Country memberCountry = Country.getCountry(this.name);

        if (memberCountry.getCountryLeader().equals(this.name)) {
            return true;
        }

        if (this.rank == null) {
            return false;
        }

        return this.rank.hasPermission(permission);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getName() {
        return this.name;
    }

    public Rank getRank() {
        return this.rank;
    }
}
