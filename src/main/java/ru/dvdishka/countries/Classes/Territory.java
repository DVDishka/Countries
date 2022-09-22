package ru.dvdishka.countries.Classes;

import java.io.Serializable;

public class Territory implements Serializable {

    private final int fromX;
    private final int toX;
    private final int fromZ;
    private final int toZ;
    private boolean hidden;

    public Territory(int fromX, int fromZ, int toX, int toZ, boolean hidden) {
        this.fromX = Math.min(fromX, toX);
        this.toX = Math.max(fromX, toX);
        this.fromZ = Math.min(fromZ, toZ);
        this.toZ = Math.max(fromZ, toZ);
        this.hidden = hidden;
    }

    public int getFromX() {
        return this.fromX;
    }

    public int getFromZ() {
        return this.fromZ;
    }

    public int getToX() {
        return this.toX;
    }

    public int getToZ() {
        return this.toZ;
    }

    public boolean isHidden() {
        return hidden;
    }

     public void setHidden(boolean hidden) {
        this.hidden = hidden;
     }
}
