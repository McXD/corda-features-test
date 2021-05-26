package com.mcxd.corda.persistence.workflows;


import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.persistence.workflows.schema.DistrictSchemaV1;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

@StartableByRPC
public class QueryDistrictById extends FlowLogic<String> {
    private final long id;

    public QueryDistrictById(long id) {
        this.id = id;
    }

    @Override
    @Suspendable
    public String call() throws FlowException {
        return getServiceHub().withEntityManager(
                entityManager -> {
                    return entityManager.find(DistrictSchemaV1.District.class, this.id);
                }
        ).toString();
    }
}
