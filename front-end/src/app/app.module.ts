import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {RouterOutlet} from "@angular/router";
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HeaderComponent } from './pages/header/header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { EventComponent } from './pages/event/event.component';
import { EventDetailsComponent } from './pages/event-details/event-details.component';
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatToolbar} from "@angular/material/toolbar";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import { ProfileComponent } from './pages/profile/profile.component';
import {MatTab, MatTabGroup} from "@angular/material/tabs";
import {MatList, MatListItem} from "@angular/material/list";
import {MatSelectModule} from "@angular/material/select";
import {MatDialogModule} from "@angular/material/dialog";
import {NgOptimizedImage} from "@angular/common";
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import {authInterceptorProviders} from "./interceptors/auth.interceptor";
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { VenueManagementComponent } from './pages/venue-management/venue-management.component';
import { EventManagementComponent } from './pages/event-management/event-management.component';
import {MatTableModule} from "@angular/material/table";
import { EditEventDialogComponent } from './pages/edit-event-dialog/edit-event-dialog.component';
import { LoginDialogComponent } from './pages/login-dialog/login-dialog.component';
import { RegisterDialogComponent } from './pages/register-dialog/register-dialog.component';
import { ShowEventDialogComponent } from './pages/show-event-dialog/show-event-dialog.component';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HeaderComponent,
    FooterComponent,
    EventComponent,
    EventDetailsComponent,
    ProfileComponent,
    UnauthorizedComponent,
    NotFoundComponent,
    DashboardComponent,
    UserManagementComponent,
    VenueManagementComponent,
    EventManagementComponent,
    EditEventDialogComponent,
    LoginDialogComponent,
    RegisterDialogComponent,
    ShowEventDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    FontAwesomeModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatDialogModule,
    BrowserAnimationsModule,
    RouterOutlet,
    MatToolbar,
    MatGridList,
    MatGridTile,
    MatTabGroup,
    MatTab,
    MatList,
    MatListItem,
    NgOptimizedImage,
    MatTableModule,
    ReactiveFormsModule
  ],
  providers: [
    //provideAnimationsAsync()
    authInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
