package unassigned.plentifulutilities.items.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.utils.RegistryUtil;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class ItemBase extends Item {

    private final String name;

    public ItemBase(String name)
    {
        this.name = name;

        this.registerItem();
    }

    private void registerItem()
    {
        RegistryUtil.registerItem(this, this.getBaseName());

        registerRendering(); //make sure this goes after the registry, or it will crash!
    }

    protected void registerRendering(){
        PlentifulUtilities.proxy.addRenderRegister(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    protected String getBaseName() { return this.name; }



}
