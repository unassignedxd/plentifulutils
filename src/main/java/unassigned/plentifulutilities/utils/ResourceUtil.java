package unassigned.plentifulutilities.utils;

import io.netty.util.internal.StringUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class ResourceUtil {

    public static final ResourceLocation GUI_INVENTORY_LOCATION = getGuiLocation("gui_inventory");

    public static ResourceLocation getGuiLocation(String file){
        return new ResourceLocation(ModUtil.MODID, "textures/gui/"+file+".png");
    }

    @SideOnly(Side.CLIENT)
    public static void displayNameString(FontRenderer font, int xSize, int yPositionOfMachineText, String text){
        font.drawString(text, xSize/2-font.getStringWidth(text)/2, yPositionOfMachineText, 0);
    }

}
