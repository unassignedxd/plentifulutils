package unassigned.plentifulutilities.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.blocks.client.IHasModel;
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
public class BlockBase extends Block implements IHasModel {
    private final String name;

    public BlockBase(Material mat, String name) {
        super(mat);
        this.name = name;

        this.registerBlock();
    }

    private void registerBlock(){
        RegistryUtil.registerBlock(this, this.getItemBlock(), this.getBaseName());
    }

    public void registerRendering() {
        PlentifulUtilities.proxy.addRenderRegister(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    protected String getBaseName() { return this.name; }
    protected ItemBlockBase getItemBlock() { return new ItemBlockBase(this); }


}
