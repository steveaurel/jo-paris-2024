import { NgModule } from '@angular/core';
import {Router, RouterModule, Routes} from '@angular/router';
import {EventComponent} from "./pages/event/event.component";
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {EventDetailsComponent} from "./pages/event-details/event-details.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {NotFoundComponent} from "./pages/not-found/not-found.component";
import {AuthGuard} from "./guards/auth.guard";
import {Role} from "./models/role.enum";
import {UnauthorizedComponent} from "./pages/unauthorized/unauthorized.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {UserManagementComponent} from "./pages/user-management/user-management.component";
import {VenueManagementComponent} from "./pages/venue-management/venue-management.component";
import {EventManagementComponent} from "./pages/event-management/event-management.component";

const routes: Routes = [
  {path: '', redirectTo: 'event', pathMatch: 'full'},
  {path: 'event', component: EventComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'event-details/:id', component: EventDetailsComponent},

  { path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard],
    data:{roles: [Role.USER, Role.ADMIN]}
  },
  { path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    data:{roles: [Role.ADMIN]},
    children:[
      {path: 'events', component: EventManagementComponent},
      {path: 'users', component: UserManagementComponent},
      {path: 'venues', component: VenueManagementComponent}
    ]
  },

  {path: '404', component: NotFoundComponent},
  {path: '401', component: UnauthorizedComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  constructor(private router: Router) {
    this.router.errorHandler = (error: any) => {
      this.router.navigate(['/404']);
    }
  }
}
