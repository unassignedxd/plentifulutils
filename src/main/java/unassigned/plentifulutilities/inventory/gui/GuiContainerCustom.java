package unassigned.plentifulutilities.inventory.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public abstract class GuiContainerCustom extends GuiContainer {

    public GuiContainerCustom(Container inventorySlotsIn){
        super(inventorySlotsIn);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
