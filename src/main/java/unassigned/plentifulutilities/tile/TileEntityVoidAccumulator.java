package unassigned.plentifulutilities.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import unassigned.plentifulutilities.tile.interfacing.IGiveVoid;
import unassigned.plentifulutilities.voidenergy.base.VoidStorage;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class TileEntityVoidAccumulator extends TileEntityInventoryBase implements IGiveVoid {

    public static final int SLOT_VOID = 0;

    public VoidStorage voidStorage = new VoidStorage(1000, 100, 100, 0); //creates a void storage with 1k storage / 1k max receive/extract

    public TileEntityVoidAccumulator() { super("void_accumulator", 1); }

    @Override
    public void update() {
        super.update();

        if(!this.world.isRemote)
        {
            System.out.println("Current SV: " + this.voidStorage.getVoidStored());
        }
    }

    public boolean hasVoidCollector(){
        for(TileEntity tileEntity : this.tilesNear)
        {
            if(tileEntity instanceof TileEntityVoidCollector) { return true; }
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public boolean shouldSyncInventory() { return true; }

    @Override
    public VoidStorage getVoidStorage() {
        return this.voidStorage;
    }
}
