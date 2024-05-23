import {Component, Inject, OnInit} from '@angular/core';
import {AuthRequest} from "../../models/authrequest.model";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {RegisterDialogComponent} from "../register-dialog/register-dialog.component";

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrl: './login-dialog.component.css'
})
export class LoginDialogComponent implements OnInit {

  errorMessage: string = "";
  loginForm!: FormGroup;

  constructor(
    private authenticationService: AuthenticationService,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<LoginDialogComponent>,
    private router: Router,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id) {
      this.navigateToEventDetails(this.data.id);
    }
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  navigateToEventDetails(eventId: number | undefined): void {
    this.router.navigate(['/event-details', eventId]).then(success => {
      if (success) {
        console.log('Navigation was successful!');
      } else {
        console.log('Navigation failed!');
      }
    }).catch(error => {
      console.error('Error navigating:', error);
    });
  }

  login() {
    if(this.loginForm.valid){
      const formValue = this.loginForm.value;
      const authRequest: AuthRequest ={
        email: formValue.email,
        password: formValue.password
      };

      this.authenticationService.login(authRequest).subscribe({
        next: () => {
        },
        error: (error) => {
          this.errorMessage = 'Email or password is incorrect.';
          console.log(error);
        }
      });
      this.dialogRef.close(this.loginForm.value);
    } else {
      console.error('Form is not valid');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  register() {
    this.dialogRef.close();
    if (!this.authenticationService.currentUserValue?.id) {
      const dialogRef = this.dialog.open(RegisterDialogComponent, {
        width: '500px',
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
        }
      });
    }
  }
}
