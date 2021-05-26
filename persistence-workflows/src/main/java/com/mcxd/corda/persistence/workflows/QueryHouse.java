package com.mcxd.corda.persistence.workflows;

import com.mcxd.corda.persistence.states.HouseSchema;
import com.mcxd.corda.persistence.states.HouseSchemaV1;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * List all the HouseStates kept in this node
 */
@StartableByRPC
public class QueryHouse extends FlowLogic<String> {

    @Override
    public String call() throws FlowException {
        // Utilize Corda build-in service
        List<HouseSchemaV1.PersistentHouseState> results = getServiceHub().withEntityManager(entityManager ->
                {
                    CriteriaQuery<HouseSchemaV1.PersistentHouseState> query = entityManager.getCriteriaBuilder().createQuery(HouseSchemaV1.PersistentHouseState.class);
                    Root<HouseSchemaV1.PersistentHouseState> type = query.from(HouseSchemaV1.PersistentHouseState.class);
                    query.select(type);
                    return entityManager.createQuery(query).getResultList();
                }
            );

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (HouseSchemaV1.PersistentHouseState state : results){
            sb.append(state.toString());
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }
}
