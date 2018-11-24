package unassigned.plentifulutilities.tile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class TileEntityVoidCollector extends TileEntityInventoryBase {


    public int processTime;
    public int timeToProcess = 220;

    public TileEntityVoidCollector(){
        super("void_collector", 1);
    }

    @Override
    public void update() {
        super.update();


    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.processTime = compound.getInteger("ProcessTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ProcessTime", this.processTime);
        return super.writeToNBT(compound);
    }
}
