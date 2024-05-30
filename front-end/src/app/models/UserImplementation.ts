import { Role } from './role.enum';
import { User } from './user.model';

export class UserImplementation implements User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
  password: string;
  accessToken: string;
  refreshToken: string;

  constructor() {
    this.id = 0;
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.role = Role.USER;
    this.password = '';
    this.accessToken = '';
    this.refreshToken = '';
  }
}
