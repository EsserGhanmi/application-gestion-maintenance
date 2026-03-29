import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { Intervention } from '../../../../core/models/intervention.model';
import { InterventionHistory } from '../../../../core/models/intervention-history.model';
import { InterventionService } from '../../../../core/services/intervention';

@Component({
  selector: 'app-intervention-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './intervention-detail.html',
  styleUrl: './intervention-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InterventionDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly interventionService = inject(InterventionService);

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly intervention = signal<Intervention | null>(null);
  readonly history = signal<InterventionHistory[]>([]);

  constructor() {
    this.loadData();
  }

  private loadData(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const interventionId = Number(idParam);

    if (!idParam || Number.isNaN(interventionId)) {
      this.errorMessage.set('Identifiant intervention invalide.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.interventionService.getById(interventionId).subscribe({
      next: (intervention) => {
        this.intervention.set(intervention);
      },
      error: () => {
        this.errorMessage.set("Impossible de charger le détail de l'intervention.");
        this.loading.set(false);
      },
    });

    this.interventionService.getHistory(interventionId).subscribe({
      next: (historyEntries) => {
        this.history.set(historyEntries);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set("Impossible de charger l'historique.");
        this.loading.set(false);
      },
    });
  }
}
