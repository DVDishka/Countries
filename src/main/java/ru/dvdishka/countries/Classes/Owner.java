package ru.dvdishka.countries.Classes;

public class Owner {

    private String name;
    private Territory territory;

    public Owner(String name, Territory territory) {
        this.name = name;
        this.territory = territory;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Territory getTerritory() {
        return this.territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }
}
