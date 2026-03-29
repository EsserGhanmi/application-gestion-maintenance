
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-intervention-list',
  templateUrl: './intervention-list.html',
  styleUrl: './intervention-list.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InterventionListComponent {}