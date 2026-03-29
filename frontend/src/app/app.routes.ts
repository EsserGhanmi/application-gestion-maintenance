import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login').then((m) => m.LoginComponent),
  },
  {
    path: 'interventions',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/interventions/pages/intervention-list/intervention-list').then((m) => m.InterventionListComponent),
  },
  {
    path: 'interventions/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/interventions/pages/intervention-detail/intervention-detail').then((m) => m.InterventionDetailComponent),
  },
{ path: '', redirectTo: '/interventions', pathMatch: 'full' },
  { path: '**', redirectTo: '/interventions' },
]; 