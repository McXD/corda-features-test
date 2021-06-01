import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IdentityService {
  private user : String = "";
  constructor() { }

  public getUser() : String{
    return this.user
  }

  public setUser(user : String) : void {
    this.user = user;
  }
}
