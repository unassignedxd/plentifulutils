package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.util.math.ChunkPos;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolderModifiable;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.HashMap;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class VoidEnergyHolder implements IVoidHolderModifiable {

    private static final Map<ChunkPos, IVoidStorage> voidEnergies = new HashMap<>();

    @Nullable
    @Override
    public IVoidStorage getVoidEnergy(final ChunkPos chunkPos){
        return voidEnergies.get(chunkPos);
    }

    @Override
    public void setVoidEnergy(final ChunkPos chunkPos, final IVoidStorage voidStorage){
        voidEnergies.put(chunkPos, voidStorage);
    }

    @Override
    public void removeVoidEnergy(final ChunkPos chunkPos){
        voidEnergies.remove(chunkPos);
    }

    public static Map<ChunkPos, IVoidStorage> getVoidEnergies() { return voidEnergies; }
}
