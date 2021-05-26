package com.mcxd.corda.persistence.workflows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.persistence.workflows.schema.DistrictSchema;
import com.mcxd.corda.persistence.workflows.schema.DistrictSchemaV1;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

@StartableByRPC
public class CreateDistrict extends FlowLogic<String> {
    private final long id;
    private final String location;
    private final String description;

    public CreateDistrict(long id, String location, String description){
        this.id = id;
        this.location = location;
        this.description = description;
    }

    @Override
    @Suspendable
    public String call() throws FlowException {
        DistrictSchemaV1.District district = new DistrictSchemaV1.District(this.id, this.location, this.description);

        getServiceHub().withEntityManager(entityManager ->
        {
            entityManager.persist(district);
        });

        return district + " persisted";
    }
}
