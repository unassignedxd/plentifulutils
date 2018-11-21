package unassigned.plentifulutilities.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class SlotHandlerCustom extends SlotItemHandler {

    private final ItemStackHandler inv;

    public SlotHandlerCustom(ItemStackHandler inv, int index, int xPos, int yPos){
        super(inv, index, xPos, yPos);
        this.inv = inv;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return false;

        ItemStack currentStack = this.inv.getStackInSlot(this.getSlotIndex());
        this.inv.setStackInSlot(this.getSlotIndex(), ItemStack.EMPTY);
        ItemStack remainder = this.inv.insertItem(this.getSlotIndex(), stack, true);
        this.inv.setStackInSlot(this.getSlotIndex(), currentStack);
        return remainder.isEmpty() || remainder.getCount() < stack.getCount();
    }

    @Override
    @Nonnull
    public ItemStack getStack() {
        return this.inv.getStackInSlot(this.getSlotIndex());
    }

    @Override
    public void putStack(ItemStack stack) {
        this.inv.setStackInSlot(this.getSlotIndex(), stack);
        this.onSlotChanged();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        maxAdd.setCount(stack.getMaxStackSize());
        ItemStack currentStack = this.inv.getStackInSlot(this.getSlotIndex());
        this.inv.setStackInSlot(this.getSlotIndex(), ItemStack.EMPTY);
        ItemStack remainder = this.inv.insertItem(this.getSlotIndex(), maxAdd, true);
        this.inv.setStackInSlot(this.getSlotIndex(), currentStack);
        return stack.getMaxStackSize() - remainder.getCount();
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !this.inv.extractItem(this.getSlotIndex(), 1, true).isEmpty();
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return this.inv.extractItem(this.getSlotIndex(), amount, false);
    }
}
