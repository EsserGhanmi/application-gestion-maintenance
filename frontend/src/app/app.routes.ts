import { Routes } from '@angular/router';
import { Login } from './features/auth/pages/login/login';
import { InterventionList } from './features/interventions/pages/intervention-list/intervention-list';
import { InterventionDetail } from './features/interventions/pages/intervention-detail/intervention-detail';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'interventions', component: InterventionList },
  { path: 'interventions/:id', component: InterventionDetail },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];