package com.mcxd.corda.account.client.controllers;

import com.mcxd.corda.account.client.model.IOUDeleteDetail;
import com.mcxd.corda.account.client.model.IOUPaymentDetail;
import com.mcxd.corda.account.client.model.Status;
import com.mcxd.corda.account.client.utils.NodeRPCConnection;
import com.mcxd.corda.account.workflows.GetIOUsForAccount;
import com.mcxd.corda.account.workflows.IssueIOU;
import com.mcxd.corda.account.workflows.PayIOU;
import com.mcxd.corda.account.workflows.RetireIOU;
import com.mcxd.corda.account.workflows.model.IOU;
import com.mcxd.corda.account.client.model.IOUDetail;
import net.corda.core.messaging.CordaRPCOps;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/iou/")
public class IOUController {

    private final CordaRPCOps cordaProxy;

    public IOUController(NodeRPCConnection connection ) {
        this.cordaProxy = connection.getProxy();
    }

    @GetMapping("{username}/all")
    @PreAuthorize("authentication.principal.username==#username")
    public List<IOU> getIOUsForUser(@PathVariable("username") String username){
        try {
            return cordaProxy.startFlowDynamic(GetIOUsForAccount.class, username).getReturnValue().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @PostMapping("{username}/issue")
    @PreAuthorize("authentication.principal.username==#username")
    public Map<java.lang.String, java.lang.String> issueIOU(@PathVariable("username") String username,
                                                  @RequestBody IOUDetail iou){
        Map<java.lang.String, java.lang.String> result = new HashMap<>();
        java.lang.String uid;

        try {
            uid = cordaProxy.startFlowDynamic(IssueIOU.Initiator.class, username, iou.getBorrower(),
                            iou.getAmount()).getReturnValue().get();

            result.put("uid", uid);

            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("/{username}/pay")
    @PreAuthorize("authentication.principal.username==#username")
    public Status payIOU(@PathVariable("username") String username, @RequestBody IOUPaymentDetail iouPaymentDetail){
        Status status = new Status(false);

        try{
            cordaProxy.startFlowDynamic(PayIOU.Initiator.class, iouPaymentDetail.getUid(),
                    iouPaymentDetail.getPayment());

            status.setSuccess(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        return status;
    }

    @PostMapping("/{username}/delete")
    @PreAuthorize("authentication.principal.username==#username")
    public Status deleteIOU(@PathVariable("username") String username, @RequestBody IOUDeleteDetail iouDeleteDetail){
        Status status = new Status(false);

        try{
            cordaProxy.startFlowDynamic(RetireIOU.Initiator.class, iouDeleteDetail.getUid()).getReturnValue().get();

            status.setSuccess(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        return status;
    }

}
