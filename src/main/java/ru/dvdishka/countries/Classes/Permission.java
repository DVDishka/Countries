package ru.dvdishka.countries.Classes;

import java.io.Serializable;

public enum Permission implements Serializable {

    FRIENDLY_FIRE("FRIENDLY_FIRE"),
    INVITE_MEMBER("INVITE_MEMBER"),
    REMOVE_MEMBER("REMOVE_MEMBER"),
    CHANGE_COUNTRY_COLOR("CHANGE_COUNTRY_COLOR"),
    DELETE_COUNTRY("DELETE_COUNTRY"),
    CREATE_RANK("CREATE_RANK"),
    SET_PLAYER_RANK("SET_PLAYER_RANK"),
    EDIT_RANK("EDIT_RANK"),
    REMOVE_RANK("REMOVE_RANK");

    String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
