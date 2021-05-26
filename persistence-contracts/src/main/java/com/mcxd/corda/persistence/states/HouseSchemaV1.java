package com.mcxd.corda.persistence.states;

import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collections;

/**
 * A concrete schema (version 1) of HouseSchema.
 * A schema provides information about available persistent states.
 */
public class HouseSchemaV1 extends MappedSchema {
    public HouseSchemaV1() {
        super(HouseSchema.class, 1, Collections.singletonList(PersistentHouseState.class));
    }

    @Entity
    @Table(name = "contract_house_state")
    public static class PersistentHouseState extends PersistentState{
        private String location;
        private double size;
        private double price;
        private Party holder;

        public PersistentHouseState(){

        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Party getHolder() {
            return holder;
        }

        public void setHolder(Party holder) {
            this.holder = holder;
        }

        @Override
        public String toString() {
            return "{ location: " + this.location +
                    " size: " + this.size +
                    " price: " + this.price +
                    " holder: " + this.holder.getName() +
                    " }";
        }
    }
}
