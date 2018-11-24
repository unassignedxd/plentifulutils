package unassigned.plentifulutilities.inventory.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import unassigned.plentifulutilities.inventory.ContainerVoidAccumulator;
import unassigned.plentifulutilities.tile.TileEntityBase;
import unassigned.plentifulutilities.tile.TileEntityVoidAccumulator;
import unassigned.plentifulutilities.utils.ResourceUtil;

import javax.annotation.Resource;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */

@SideOnly(Side.CLIENT)
public class GuiVoidAccumulator extends GuiContainerCustom {

    private static final ResourceLocation RES_LOC = ResourceUtil.getGuiLocation("gui_void_accumulator");
    private final TileEntityVoidAccumulator accumulator;


    public GuiVoidAccumulator(InventoryPlayer inventoryPlayer, TileEntityBase tile){
        super(new ContainerVoidAccumulator(inventoryPlayer, tile));
        this.accumulator = (TileEntityVoidAccumulator)tile;
        this.xSize = 176;
        this.ySize = 93+86;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ResourceUtil.displayNameString(this.fontRenderer, this.xSize, -10, this.accumulator.name);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(ResourceUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop+93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);
    }
}
