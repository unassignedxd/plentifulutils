package unassigned.plentifulutilities.utils.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import unassigned.plentifulutilities.blocks.client.IHasModel;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class CRegistryHandler {

    public static final Map<ItemStack, ModelResourceLocation> REGISTER_MODEL_LIST = new HashMap<ItemStack, ModelResourceLocation>();

    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event){
        for(Block block : RegistryHandler.BLOCKS_LIST)
        {
            if(block instanceof IHasModel)
            {
                ((IHasModel)block).registerRendering();
            }
        }

        for(Map.Entry<ItemStack, ModelResourceLocation> entry : REGISTER_MODEL_LIST.entrySet()){
            ModelLoader.setCustomModelResourceLocation(entry.getKey().getItem(), entry.getKey().getItemDamage(), entry.getValue());
        }
    }
}
