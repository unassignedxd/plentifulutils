package unassigned.plentifulutilities.tile.interfacing;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public interface IGiveEnergy {

    boolean canShareToTile(TileEntity tile);

    int getEnergyToShare();

    boolean doesGiveEnergy();

    EnumFacing[] getGiveSides();
}
