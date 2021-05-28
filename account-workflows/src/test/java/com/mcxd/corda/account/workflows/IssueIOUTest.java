package com.mcxd.corda.account.workflows;

import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static net.corda.testing.node.TestCordapp.findCordapp;

public class IssueIOUTest {

    private final MockNetwork mockNet = new MockNetwork(
            new MockNetworkParameters(Arrays.asList(
                    findCordapp("com.mcxd.corda.account.contracts"), //our own cordapp
                    findCordapp("com.mcxd.corda.account.states"), //our own cordapp
                    findCordapp("com.mcxd.corda.account.workflows") //our own cordapp
            ))
    );

    private StartedMockNode nodeA;
    private StartedMockNode nodeB;

    @Before
    public void setUp() {
        nodeA = mockNet.createNode(new CordaX500Name("PartyA", "Paris", "FR"));
        nodeB = mockNet.createNode(new CordaX500Name("PartyB", "London", "GB"));
    }

    @Test
    public void SampleTest(){
        CordaFuture<String> future = nodeA.startFlow(new IssueIOU.Initiator(
                "Andy",
                "Bob",
                50
        ));
    }

    @After
    public void cleanUp() {
        mockNet.stopNodes();
    }
}