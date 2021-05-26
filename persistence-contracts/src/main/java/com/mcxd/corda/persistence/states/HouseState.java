package com.mcxd.corda.persistence.states;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@BelongsToContract(com.mcxd.corda.persistence.contracts.HouseContract.class)
public class HouseState implements QueryableState {
    private final String location;
    private final double price;
    private final double size;
    private final Party holder;

    public HouseState(String location, double price, double size, Party holder) {
        this.location = location;
        this.price = price;
        this.size = size;
        this.holder = holder;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Collections.singletonList(this.holder);
    }

    @NotNull
    @Override
    /*
      Generate a persistent object for this state according to the given schema.
     */
    public PersistentState generateMappedObject(@NotNull MappedSchema schema) {
        if (schema instanceof HouseSchemaV1){
            HouseSchemaV1.PersistentHouseState persistence = new HouseSchemaV1.PersistentHouseState();
            persistence.setHolder(this.holder);
            persistence.setLocation(this.location);
            persistence.setPrice(this.price);
            persistence.setSize(this.size);

            return persistence;
        }else{
            throw new IllegalArgumentException("Cannot recognize schema: " + schema);
        }
    }

    @NotNull
    @Override
    /*
     * Provide information about what schemas are supported for persistence of this state.
     */
    public Iterable<MappedSchema> supportedSchemas() {
        return Collections.singletonList(new HouseSchemaV1());
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public double getSize() {
        return size;
    }

    public Party getHolder() {
        return holder;
    }
}
