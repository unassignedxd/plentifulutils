package unassigned.plentifulutilities.tile;

import net.minecraft.nbt.NBTTagCompound;
import unassigned.plentifulutilities.utils.ItemUtil;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class TileEntityVoidAccumulator extends TileEntityInventoryBase {

    public TileEntityVoidAccumulator() { super("void_accumulator", 1); }

    @Override
    public void update() {
        super.update();

        if(this.inv.getStackInSlot(0) != ItemUtil.getEmpty())
        {
            System.out.println("client? " + world.isRemote + " - item: " + this.inv.getStackInSlot(0).getItem().getUnlocalizedName());
        }
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

}
