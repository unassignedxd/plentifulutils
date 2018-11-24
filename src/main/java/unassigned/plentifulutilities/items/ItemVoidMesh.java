package unassigned.plentifulutilities.items;

import net.minecraft.item.ItemStack;
import unassigned.plentifulutilities.items.base.ItemBase;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class ItemVoidMesh extends ItemBase {

    private boolean isSpecial;

    public ItemVoidMesh(String name, int dura, boolean isSpecial) {
        super(name);
        this.setMaxDamage(dura);

        this.isSpecial = isSpecial;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.isSpecial;
    }
}
