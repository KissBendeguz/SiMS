import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AppComponent } from './app.component';
import { AuthGuard } from './services/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CreateBusinessComponent } from './components/create-business/create-business.component';
import { CreateInventoryComponent } from './components/create-inventory/create-inventory.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'create', component: CreateBusinessComponent, canActivate: [AuthGuard] },
  { path: ':businessId/inventory/create', component: CreateInventoryComponent, canActivate: [AuthGuard] },
  { path: '', component: DashboardComponent, pathMatch: 'full', canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/' },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
