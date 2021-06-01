import { componentFactoryName } from '@angular/compiler';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActivityComponent } from './activity/activity.component';
import { IndexComponent } from './index/index.component';
import { IssueLoanComponent } from './issue-loan/issue-loan.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  {path: "", redirectTo: "index", pathMatch: "full"},
  {path: "index", component: IndexComponent},
  {path : "activity", component : ActivityComponent, children : [
    {path : "issue-loan", component : IssueLoanComponent}
  ]},
  {path: "login", component: LoginComponent},
  {path: "register", component: RegisterComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
