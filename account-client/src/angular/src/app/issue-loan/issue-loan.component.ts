import { Component, OnInit } from '@angular/core';
import {FormBuilder} from '@angular/forms'
import {HttpClient} from '@angular/common/http'
import {Router} from '@angular/router'
import { IdentityService } from '../identity.service';
import { IouService } from '../iou.service';
import { IssueResponse} from '../iou.service'
import { areAllEquivalent } from '@angular/compiler/src/output/output_ast';
import { TableMonitorService } from '../table-monitor.service';
import { IOU } from '../IOU';

@Component({
  selector: 'app-issue-loan',
  templateUrl: './issue-loan.component.html',
  styleUrls: ['./issue-loan.component.css']
})
export class IssueLoanComponent implements OnInit {

  issueForm = this.formBuilder.group({
    borrower: "",
    amount : ""
})

  constructor(
    private http : HttpClient,
    private formBuilder : FormBuilder,
    private router : Router,
    private identityService : IdentityService,
    private tableMonitorService : TableMonitorService
    ){}

  ngOnInit(): void {
  }

  onSubmit(){

    var postBody = {
      borrower : this.issueForm.value.borrower,
      amount : this.issueForm.value.amount
    }

    this.http.post<IssueResponse>("http://localhost:8080/api/iou/" + this.identityService.getUser() + "/issue", postBody).subscribe(
      (response) => {
        alert(
          "IOU issued. Id: " + response.uid
        )
        
        this.tableMonitorService.fetchDataSource().push(new IOU(this.identityService.getUser(), postBody.borrower, postBody.amount, response.uid))
        this.tableMonitorService.fetchTable()?.renderRows()
        this.router.navigate(["activity"])
      }
    )
  }

}
