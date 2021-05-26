package com.mcxd.corda.persistence.workflows.schema;

import net.corda.core.schemas.MappedSchema;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collections;

public class DistrictSchemaV1 extends MappedSchema {
    public DistrictSchemaV1(){
        super(DistrictSchema.class, 1, Collections.singleton(District.class));
    }

    @Entity
    @Table(name="off_ledger_district")
    public static class District {
        @Id
        private long id;

        private String location;
        private String description;

        public District(){}
        public District(long id, String location, String description){
            this.id = id;
            this.description = description;
            this.location = location;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString(){
            return "District(" + + this.id + ", " + this.location + ", " + this.description + ")";
        }
    }
}
