import { Component, OnInit } from '@angular/core';
import { Form, FormBuilder } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { IdentityService } from '../identity.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = this.formBuilder.group({
    username: "",
    password : ""
})

  constructor(
    private http : HttpClient,
    private formBuilder : FormBuilder,
    private router : Router,
    private identity : IdentityService) {}

  ngOnInit(): void {
  }

  onSubmit(){
    // Post the form
    var formData = new FormData();
    formData.append("username", this.loginForm.value.username)
    formData.append("password", this.loginForm.value.password)

  
    this.http.post<LoginResponse>("http://localhost:8080/login", formData).subscribe(
      (response) => {
        if (response.success){
          this.router.navigate(["activity"])
          this.identity.setUser(this.loginForm.value.username)
        }else{
          this.router.navigate(["login"])
        }
      }
    )
  }
}

class LoginResponse{
  success : Boolean = false
}
