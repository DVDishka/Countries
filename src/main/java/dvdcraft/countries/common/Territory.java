package dvdcraft.countries.common;

import org.bukkit.block.Block;

public class Territory {

    private Block fromBlock;
    private Block toBlock;

    public Territory(Block firstBlock, Block lastBlock) {
        this.fromBlock = firstBlock;
        this.toBlock = lastBlock;
    }

    public int getFromX() {
        return this.fromBlock.getX();
    }

    public int getFromY() {
        return this.fromBlock.getY();
    }

    public int getFromZ() {
        return this.fromBlock.getZ();
    }

    public int getToX() {
        return this.toBlock.getX();
    }

    public int getToY() {
        return this.toBlock.getY();
    }

    public int getToZ() {
        return this.toBlock.getZ();
    }
}
