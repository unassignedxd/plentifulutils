package unassigned.plentifulutilities.tile;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.tile.TileEntityBase;
import unassigned.plentifulutilities.tile.TileEntityVoidAccumulator;
import unassigned.plentifulutilities.tile.TileEntityVoidCable;
import unassigned.plentifulutilities.tile.TileEntityVoidHole;
import unassigned.plentifulutilities.utils.ModUtil;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class ModTileEntity {

    public static void init() {
        registerTE(TileEntityVoidAccumulator.class);
        registerTE(TileEntityVoidCable.class);
        registerTE(TileEntityVoidHole.class);
    }

    private static void registerTE(Class<? extends TileEntityBase> teClass){ //accepts any class that extends TEB
        try {
            ResourceLocation teName = new ResourceLocation(ModUtil.MODID, teClass.newInstance().name); // - I have to create a new instance for this to access
            GameRegistry.registerTileEntity(teClass, teName);
        } catch(Exception e){
            PlentifulUtilities.LOG.fatal("Attempted to register a tile entity, but failed!", e);
        }
    }
}
