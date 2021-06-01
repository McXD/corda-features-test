import { Injectable } from '@angular/core';
import { MatTable } from '@angular/material/table' 
import { IOU } from './IOU'

@Injectable({
  providedIn: 'root'
})
export class TableMonitorService {
  table? : MatTable<IOU>
  dataSource : IOU[] = []

  constructor() { }

  registerTable(table : MatTable<IOU> | undefined){
    this.table = table
  }

  registerDataSource(dataSource: IOU[]){
    this.dataSource = dataSource
  }

  fetchTable() : MatTable<IOU> | undefined{
    return this.table
  }

  fetchDataSource() : IOU[]{
    return this.dataSource
  }

}
