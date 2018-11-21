package unassigned.plentifulutilities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;
import unassigned.plentifulutilities.tile.TileEntityBase;
import unassigned.plentifulutilities.tile.TileEntityVoidAccumulator;
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
public class ContainerVoidAccumulator extends Container {

    private final TileEntityVoidAccumulator accumulator;

    public ContainerVoidAccumulator(InventoryPlayer inventoryPlayer, TileEntityBase tile){
        this.accumulator = (TileEntityVoidAccumulator)tile;

        this.addSlotToContainer(new SlotItemHandler(this.accumulator.inv, 0, 87, 43));

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 97+i*18));
            }
        }
        for(int i = 0; i < 9; i++){
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 155));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 1;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                        return ItemUtil.getEmpty();
                    }
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                    return ItemUtil.getEmpty();
                }
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)){
                return ItemUtil.getEmpty();
            }

            if(!ItemUtil.isValid(newStack)){
                theSlot.putStack(ItemUtil.getEmpty());
            }
            else{
                theSlot.onSlotChanged();
            }

            if(newStack.getCount() == currentStack.getCount()){
                return ItemUtil.getEmpty();
            }
            theSlot.onTake(player, newStack);

            return currentStack;
        }
        return ItemUtil.getEmpty();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return true;
    }
}
