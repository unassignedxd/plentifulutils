package unassigned.plentifulutilities.tile;

import net.minecraft.tileentity.TileEntity;
import unassigned.plentifulutilities.tile.interfacing.IGiveVoid;
import unassigned.plentifulutilities.voidenergy.base.VoidStorage;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/25/2018 for plentifulutils
 */
public class TileEntityVoidCable extends TileEntityBase implements IGiveVoid {

    private static final int VOID_BUFFER = 100;
    private static final int INTERACTION_SPEED = 100;

    public VoidStorage voidStorage = new VoidStorage(VOID_BUFFER, INTERACTION_SPEED, INTERACTION_SPEED, 1000); //creates a void storage with 1k storage / 1k max receive/extract

    public TileEntityVoidCable() { super("void_cable"); }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public VoidStorage getVoidStorage() {
        return this.voidStorage;
    }
}
