import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms'
import { MatTableModule} from '@angular/material/table';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AccountComponent } from './account/account.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { IndexComponent } from './index/index.component';
import { ActivityComponent } from './activity/activity.component';
import { LenderActivityComponent } from './lender-activity/lender-activity.component';
import { BorrowerActivityComponent } from './borrower-activity/borrower-activity.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IssueLoanComponent } from './issue-loan/issue-loan.component';

@NgModule({
  declarations: [
    AppComponent,
    AccountComponent,
    LoginComponent,
    RegisterComponent,
    IndexComponent,
    ActivityComponent,
    LenderActivityComponent,
    BorrowerActivityComponent,
    IssueLoanComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    BrowserAnimationsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
