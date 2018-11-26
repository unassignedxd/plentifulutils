package unassigned.plentifulutilities.voidenergy.base;

import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorageCustom;

/**
    Most of this class is not my code! This class is mostly derived from MinecraftForge's EnergyStorage.class
 */
public class VoidStorage implements IVoidStorageCustom {

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    protected int ticks;
    protected int dangerThreshold;

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
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
        this.capacity = capacity;

        this.dangerThreshold = capacity - (capacity / 4);
        this.ticks = 0;
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
    public int getVoidStored() {
        return this.energy;
    }

    @Override
    public int getMaxVoidStored()
    {
        return this.capacity;
    }

    @Override
    public int getDangerThreshold() { return this.dangerThreshold; }

}
