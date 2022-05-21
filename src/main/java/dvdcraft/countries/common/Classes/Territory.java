package dvdcraft.countries.common.Classes;

import java.io.Serializable;

public class Territory implements Serializable {

    private final int fromX;
    private final int toX;
    private final int fromZ;
    private final int toZ;

    public Territory(int fromX, int fromZ, int toX, int toZ) {
        this.fromX = Math.min(fromX, toX);
        this.toX = Math.max(fromX, toX);
        this.fromZ = Math.min(fromZ, toZ);
        this.toZ = Math.max(fromZ, toZ);
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
}
