import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http'

import {IOU} from './IOU'
import { ThrowStmt } from '@angular/compiler';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IouService {
  /*
    - Get ious for user as lender
    - Get ious for user as borrower
    - Issue iou
    - Retire iou
  */

  private ious : IOU[] = []
  
  constructor(private http : HttpClient) { }

  getIOUs(username : String) : Observable<IOU[]> {
    return this.http.get<IOU[]>("http://localhost:8080/api/iou/" + username +"/all");
  }

  getIOUAsLender(username : String) : IOU[] {

    this.http.get<IOU[]>("http://localhost:8080/api/iou/" + username +"/all").subscribe((response : IOU[]) => this.ious = response)

    var result : IOU[] = []

    for (let iou of this.ious){
      if (iou.lender == username){
        result.push(iou)
      }
    }

    return result;
  }

  getIOUAsBorrower(username : String) : IOU[] {

    this.http.get<IOU[]>("http://localhost:8080/api/iou/" + username +"/all").subscribe((response : IOU[]) => this.ious = response)

    var result : IOU[] = []

    for (let iou of this.ious){
      if (iou.borrower == username){
        result.push(iou)
      }
    }

    return result;
  }

  issueIOU(username: String, borrower: String, amount: Number) : Observable<IssueResponse>{

    return this.http.post<IssueResponse>("http://localhost:8080/api/iou" + username + "/issue", {
      borrower : borrower,
      amount : amount
    })
  }

  payIOU(username : String, uid : String, payment : Number) : Observable<Status>{
    return this.http.post<Status>("http://localhost:8080/api/iou/" + username + "/pay", {uid : uid, payment : payment})
  }

  deleteIOU(username: String, uid: String) : Observable<Status>{
    return this.http.post<Status>("http://localhost:8080/api/iou/" + username + "/delete", 
    {uid : uid})
  }
}

export class IssueResponse{
  public uid:String = ""
}

export class Status{
  public success : Boolean = false;
}