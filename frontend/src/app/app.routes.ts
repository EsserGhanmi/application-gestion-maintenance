import { Routes } from '@angular/router';
import { InterventionListComponent } from './features/interventions/pages/intervention-list/intervention-list.component';

export const routes: Routes = [
  { path: '', component: InterventionListComponent },
  { path: '**', redirectTo: '' }
];