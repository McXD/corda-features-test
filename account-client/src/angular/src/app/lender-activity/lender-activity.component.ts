import { Component, OnInit } from '@angular/core';
import { IdentityService } from '../identity.service';
import { MatTable } from "@angular/material/table"
import {IOU} from '../IOU'
import { IouService } from '../iou.service';
import { ViewChild } from '@angular/core';
import { TableMonitorService } from '../table-monitor.service';

@Component({
  selector: 'app-lender-activity',
  templateUrl: './lender-activity.component.html',
  styleUrls: ['./lender-activity.component.css']
})
export class LenderActivityComponent implements OnInit {
  @ViewChild(MatTable) table: MatTable<IOU> | undefined;

  username : String = ""
  dataSource : IOU[] = [
    
  ]
  displayedColumns: string[] = ["borrower", "amount", "linearId"];

  constructor(private iouService : IouService,
              private identityService : IdentityService,
              private tableMonitorService : TableMonitorService) { }

  ngOnInit(): void {
    this.username = this.identityService.getUser()
    this.iouService.getIOUs(this.username).subscribe(
      (response) => {
        for (let iou of response){
          if (iou.lender === this.username) this.dataSource.push(iou);
        }
        
        this.tableMonitorService.registerDataSource(this.dataSource)
        this.tableMonitorService.registerTable(this.table);
        this.table?.renderRows();
      }
    )
  }

}
