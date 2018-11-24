package unassigned.plentifulutilities.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import unassigned.plentifulutilities.utils.registry.CRegistryHandler;

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
public class ClientProxy implements IProxy {

    private final Minecraft MINECRAFT = Minecraft.getMinecraft();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CRegistryHandler());
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void addRenderRegister(ItemStack stack, ResourceLocation location, String variant) {
        CRegistryHandler.REGISTER_MODEL_LIST.put(stack, new ModelResourceLocation(location, variant));
    }

    @Override
    public IThreadListener getThreadListener(MessageContext context) {
        if (context.side.isClient()) {
            return MINECRAFT;
        } else {
            return context.getServerHandler().player.mcServer;
        }
    }

    @Nullable
    @Override
    public EntityPlayer getClientPlayer() {
        return MINECRAFT.player;
    }

    @Nullable
    @Override
    public World getClientWorld() {
        return MINECRAFT.world;
    }

    @Override
    public EntityPlayer getPlayer(MessageContext context) {
        if (context.side.isClient()) {
            return MINECRAFT.player;
        } else {
            return context.getServerHandler().player;
        }
    }
}
