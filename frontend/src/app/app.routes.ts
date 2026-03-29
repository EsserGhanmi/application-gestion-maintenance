import { Routes } from '@angular/router';


export const routes: Routes = [
   {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login').then((m) => m.LoginComponent),
  },
  {
    path: 'interventions',
    loadComponent: () =>
      import('./features/interventions/pages/intervention-list/intervention-list').then((m) => m.InterventionListComponent),
  },
  {
    path: 'interventions/:id',
    loadComponent: () =>
      import('./features/interventions/pages/intervention-detail/intervention-detail').then((m) => m.InterventionDetail),
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
 { path: '**', redirectTo: '/login' },
];