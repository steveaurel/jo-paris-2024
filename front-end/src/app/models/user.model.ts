import { Role } from './role.enum';

export class User {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  role?: Role = Role.USER;
  password?: string;
  accessToken?: string;
  refreshToken?: string;
}
