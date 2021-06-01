import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activity',
  templateUrl: './activity.component.html',
  styleUrls: ['./activity.component.css']
})
export class ActivityComponent implements OnInit {

  constructor(private router : Router) { }

  ngOnInit(): void {
  }

  onClick(){
    this.router.navigate(["activity","issue-loan"]);
  }
}
