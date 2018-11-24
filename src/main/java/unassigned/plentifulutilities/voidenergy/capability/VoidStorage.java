package unassigned.plentifulutilities.voidenergy.capability;

import unassigned.plentifulutilities.voidenergy.base.IVoidStorageCustom;

/**
    Most of this class is not my code! This class is mostly derived from MinecraftForge's EnergyStorage.class
 */
public class VoidStorage implements IVoidStorageCustom {

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public VoidStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public VoidStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public VoidStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public VoidStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }

    @Override
    public int receiveVoid(int maxReceive, boolean simulate)
    {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractVoid(int maxExtract, boolean simulate)
    {
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getVoidStored()
    {
        return energy;
    }

    @Override
    public int getMaxVoidStored()
    {
        return capacity;
    }
}
