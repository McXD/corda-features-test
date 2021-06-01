import { Component, OnInit } from '@angular/core';
import { Form, FormBuilder } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm = this.formBuilder.group({
    username: "",
    password : ""
})

  constructor(
    private http : HttpClient,
    private formBuilder : FormBuilder,
    private router : Router) {}

  ngOnInit(): void {
  }

  onSubmit(){

    var postBody = {
      username : this.registerForm.value.username,
      password : this.registerForm.value.password
    }

    this.http.post<RegisterResponse>("http://localhost:8080/api/account/register", postBody).subscribe(
      (response) => {
        if (response.success){
          this.router.navigate(["login"])
        }else{
          this.router.navigate(["register"])
        }
      }
    )
  }
}

class RegisterResponse{
  success : Boolean = false
}