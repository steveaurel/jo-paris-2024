import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {User} from "../../models/user.model";
import {take, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Role} from "../../models/role.enum";

@Component({
  selector: 'app-register-dialog',
  templateUrl: './register-dialog.component.html',
  styleUrl: './register-dialog.component.css'
})
export class RegisterDialogComponent implements OnInit {
  errorMessage: string = "";
  registerForm!: FormGroup;

  constructor(
    private authenticationService: AuthenticationService,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegisterDialogComponent>,
    private router: Router, @Inject(MAT_DIALOG_DATA)
    public data: any) { }

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id) {
      this.navigateToEventDetails(this.data.id);
    }
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required]
    })
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

  register(): void {
    if(this.registerForm.valid){
      const formValue = this.registerForm.value;

      const user: User ={
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        role: Role.USER,
        email: formValue.email,
        password: formValue.password
      };

      this.authenticationService.register(user).pipe(
        take(1),
        catchError(err => {
          if (err?.status === 409) {
            this.errorMessage = 'Email already exists.';
          } else {
            this.errorMessage = 'Unexpected error occurred.';
            console.log(err);
          }
          return throwError(() => err);
        })
      ).subscribe({
        next: () => {},
        error: () => {}
      });
      this.dialogRef.close(this.registerForm.value);
    } else {
      console.error('Form is not valid');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
