import { Component, OnInit } from '@angular/core';
import { IOU } from "../IOU"
import { IouService } from '../iou.service';
import { IdentityService} from '../identity.service'
import { ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-borrower-activity',
  templateUrl: './borrower-activity.component.html',
  styleUrls: ['./borrower-activity.component.css']
})
export class BorrowerActivityComponent implements OnInit {
  @ViewChild(MatTable) table: MatTable<any> | undefined;

  username : String = ""

  dataSource : IOU[] = [
  ]

  displayedColumns: string[] = ["lender", "amount", "linearId", "action"];

  constructor(private identityService : IdentityService,
              private iouService : IouService) { }

  ngOnInit(): void { 
    this.username = this.identityService.getUser()
    this.iouService.getIOUs(this.username).subscribe(
      (response) => {
        for (let iou of response){
          if (iou.borrower === this.username) this.dataSource.push(iou);
        }
        
        this.table?.renderRows();
      }
    )
  }

  payOnClick(iou : IOU){

    var payment : number = 0;
    var input = prompt("How much?");
    if (input){
      payment = parseInt(input)
    }

    this.iouService.payIOU(this.identityService.getUser(), iou.uid, payment).subscribe(
      (response) => {

        if (response.success){
          iou.amount-= payment;
          this.table?.renderRows()
        }
      }
    )

    alert("Payed " + iou.uid + " " + payment)
  }

  retireOnClick(iou : IOU){
    this.iouService.deleteIOU(this.identityService.getUser(), iou.uid).subscribe(
      (response) => {

        if (response.success){
          this.dataSource = this.dataSource.filter(obj => obj !== iou);
          this.table?.renderRows()
        }
      }
    )

    alert("Retire " + iou.uid)
  }

  onClick(iou : IOU){
    if (iou.amount == 0){
      this.retireOnClick(iou)
    }else{
      this.payOnClick(iou)
    }
  }
}
