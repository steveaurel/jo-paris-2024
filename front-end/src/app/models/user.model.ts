import { Role } from './role.enum';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
  password: string;
  accessToken?: string;
  refreshToken?: string;
}
