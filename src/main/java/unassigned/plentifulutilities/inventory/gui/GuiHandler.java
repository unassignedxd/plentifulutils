package unassigned.plentifulutilities.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.inventory.ContainerVoidAccumulator;
import unassigned.plentifulutilities.tile.TileEntityBase;

import javax.annotation.Nullable;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class GuiHandler implements IGuiHandler {

    public static void init() {
        PlentifulUtilities.LOG.info("Initializing GuiHandler...");
        NetworkRegistry.INSTANCE.registerGuiHandler(PlentifulUtilities.instance, new GuiHandler());
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityBase tile = null;
        if(GuiTypes.values()[ID].checkTileEntity) { tile = (TileEntityBase)world.getTileEntity(new BlockPos(x,y,z)); }

        switch(GuiTypes.values()[ID])
        {
            case VOID_ACCUMULATOR:
                return new ContainerVoidAccumulator(player.inventory, tile);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityBase tile = null;
        if(GuiTypes.values()[ID].checkTileEntity) { tile = (TileEntityBase)world.getTileEntity(new BlockPos(x,y,z)); }
        switch(GuiTypes.values()[ID])
        {
            case VOID_ACCUMULATOR:
                return new GuiVoidAccumulator(player.inventory, tile);
            default:
                return null;
        }
    }

    public enum GuiTypes {
        VOID_ACCUMULATOR;

        public final boolean checkTileEntity;

        GuiTypes(){ this(true); }

        GuiTypes(boolean checkTileEntity) { this.checkTileEntity = checkTileEntity; }
    }
}
