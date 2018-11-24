package unassigned.plentifulutilities.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import unassigned.plentifulutilities.items.base.ItemBase;
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
@GameRegistry.ObjectHolder(ModUtil.MODID)
public final class ModItems {

    public static Item testItem;

    public static Item item_staticVoidShard;

    public static Item item_voidMesh;
    public static Item item_reinforcedVoidMesh;

    public static Item item_voidSensor;

    public static void init() {
        testItem = new ItemBase("item_test");

        item_staticVoidShard = new ItemStaticVoidShard("item_static_void");
        item_voidMesh = new ItemVoidMesh("item_void_mesh_basic", 16, false);
        item_voidMesh = new ItemVoidMesh("item_void_mesh_reinforced", 16, false); //possibly more variations;

        item_voidSensor = new ItemVoidSensor("item_void_sensor");
    }
}
