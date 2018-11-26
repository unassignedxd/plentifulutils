package unassigned.plentifulutilities.voidenergy.base.energy;

import net.minecraft.util.math.ChunkPos;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
@Deprecated
public interface IVoidHolderModifiable extends IVoidHolder {

    void setVoidEnergy(final ChunkPos chunkPos, final IVoidStorage voidStorage);

    void removeVoidEnergy(final ChunkPos chunkPos);
}
