package unassigned.plentifulutilities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unassigned.plentifulutilities.inventory.gui.GuiHandler;
import unassigned.plentifulutilities.proxies.IProxy;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.utils.registry.ModTileEntity;
import unassigned.plentifulutilities.utils.registry.RegistryHandler;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
@Mod(name = ModUtil.MOD_NAME, modid = ModUtil.MODID, version = ModUtil.VERSION)
public class PlentifulUtilities {

    @Instance
    public static PlentifulUtilities instance;

    @SidedProxy(clientSide = "unassigned.plentifulutilities.proxies.ClientProxy", serverSide = "unassigned.plentifulutilities.proxies.ServerProxy")
    public static IProxy proxy;

    public static final Logger LOG = LogManager.getLogger();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        MinecraftForge.EVENT_BUS.register(new RegistryHandler());

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event){

        GuiHandler.init();
        ModTileEntity.init();

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}
