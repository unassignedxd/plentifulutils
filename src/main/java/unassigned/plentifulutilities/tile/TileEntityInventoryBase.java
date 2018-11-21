package unassigned.plentifulutilities.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
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
public abstract class TileEntityInventoryBase extends TileEntityBase {

    public final ItemStackHandler inv;

    public TileEntityInventoryBase(String name, int slots){
        super(name);
        inv = new TileStackHandler(slots);
    }

    public static void saveSlotsToNBT(IItemHandler slots, NBTTagCompound tag) {
        if(slots != null && slots.getSlots() > 0)
        {
            NBTTagList tList = new NBTTagList();
            for(int i = 0; i < slots.getSlots(); i++)
            {
                ItemStack stackInSlot = slots.getStackInSlot(i);

                NBTTagCompound compound = new NBTTagCompound();
                if(ItemUtil.isValid(stackInSlot)) stackInSlot.writeToNBT(compound);

                tList.appendTag(compound);
            }
            tag.setTag("Items", tList);
        }
    }

    public static void loadSlotsFromNBT(IItemHandlerModifiable slots, NBTTagCompound tag){
        if(slots != null && slots.getSlots() > 0)
        {
            NBTTagList tList = tag.getTagList("Items", 10);
            for(int i = 0; i < slots.getSlots(); i++)
            {
                NBTTagCompound compound = tList.getCompoundTagAt(i);
                slots.setStackInSlot(i, compound != null && compound.hasKey("id") ? new ItemStack(compound) : ItemUtil.getEmpty());
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(shouldSyncIventory()) saveSlotsToNBT(this.inv, compound);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(shouldSyncIventory()) loadSlotsFromNBT(this.inv, compound);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if(shouldSyncIventory()) this.sendUpdate();
    }

    @Override
    public IItemHandler getItemHandler(EnumFacing facing) {
        return this.inv;
    }

    public int getMaxStackSize(int x) { return 64; }

    public boolean shouldSyncIventory() { return false; }

    protected class TileStackHandler extends ItemStackHandler {
        protected TileStackHandler(int slots){ super(slots); }

        @Override
        public int getSlotLimit(int slot) {
            return TileEntityInventoryBase.this.getMaxStackSize(slot);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TileEntityInventoryBase.this.markDirty();
        }
    }
}
