package dvdcraft.countries.common.Classes;

import org.bukkit.block.Block;

import java.io.Serializable;

public class Territory implements Serializable {

    private int fromX;
    private int toX;
    private int fromZ;
    private int toZ;

    public Territory(int fromX, int toX, int fromZ, int toZ) {
        this.fromX = fromX;
        this.toX = toX;
        this.fromZ = fromZ;
        this.toZ = toZ;
    }

    public int getFromX() {
        return Math.min(this.fromX, this.toX);
    }

    public int getFromZ() {
        return Math.min(this.fromZ, this.toZ);
    }

    public int getToX() {
        return Math.max(this.fromX, this.toX);
    }

    public int getToZ() {
        return Math.max(this.fromZ, this.toZ);
    }
}
