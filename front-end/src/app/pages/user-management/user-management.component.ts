import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {UserService} from "../../services/user.service";
import {Role} from "../../models/role.enum";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit{
  filteredUsers: User[] = [];
  users: User[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'role', 'actions'];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
  }

  filterUsers(event: Event) {
    const inputElement = event.target as HTMLInputElement; // Safe type assertion in TypeScript
    if (inputElement && inputElement.value) {
      const value = inputElement.value;
      this.filteredUsers = this.users.filter(user =>
        user.firstName?.toLowerCase().includes(value.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(value.toLowerCase()) ||
        user.email?.toLowerCase().includes(value.toLowerCase()) ||
        user.role?.toLowerCase().includes(value.toLowerCase())
      );
    } else {
      this.filteredUsers = this.users; // Reset if no value
    }
  }

  changeUserRole(user: User) {
    const newRole = user.role === Role.ADMIN ? Role.USER : Role.ADMIN;
    user.role = newRole;
    this.userService.updateUser(user?.id, user).subscribe({
      next: (updatedUser) => {
        user.role = newRole;
      },
      error: (err) => {
        console.error('Failed to update user role', err);
      }
    });
  }

  protected readonly HTMLInputElement = HTMLInputElement;
}
