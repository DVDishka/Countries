package dvdcraft.countries.common.Classes;

import org.bukkit.block.Block;

import java.io.Serializable;

public class Territory implements Serializable {

    private int fromX;
    private int toX;
    private int fromZ;
    private int toZ;

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
