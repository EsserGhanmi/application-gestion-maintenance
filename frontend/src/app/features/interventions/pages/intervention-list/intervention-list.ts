
import { AsyncPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { InterventionStatus } from '../../../../core/models/intervention-status.model';
import { Priority } from '../../../../core/models/priority.model';
import { InterventionFilters, InterventionService } from '../../../../core/services/intervention';

@Component({
  selector: 'app-intervention-list',
  imports: [ReactiveFormsModule, AsyncPipe, DatePipe, RouterLink],
  templateUrl: './intervention-list.html',
  styleUrl: './intervention-list.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InterventionListComponent {
  private readonly fb = inject(FormBuilder);
  private readonly interventionService = inject(InterventionService);

  readonly statusOptions: InterventionStatus[] = ['TO_DO', 'PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];
  readonly priorityOptions: Priority[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly interventions = signal<
    {
      id: number;
      title: string;
      status: string;
      priority?: string;
      assignedTechnicianId?: number | null;
      createdAt?: string;
      description?: string;
    }[]
  >([]);

  readonly filtersForm = this.fb.nonNullable.group({
    status: '' as InterventionStatus | '',
    priority: '' as Priority | '',
    assignedTechnicianId: '',
  });

  constructor() {
    this.loadInterventions();
  }

  applyFilters(): void {
    this.loadInterventions();
  }

  resetFilters(): void {
    this.filtersForm.reset({
      status: '',
      priority: '',
      assignedTechnicianId: '',
    });
    this.loadInterventions();
  }

  private loadInterventions(): void {
    const raw = this.filtersForm.getRawValue();
    const assignedTechnicianId = raw.assignedTechnicianId.trim() === '' ? null : Number(raw.assignedTechnicianId);

    const filters: InterventionFilters = {
      status: raw.status,
      priority: raw.priority,
      assignedTechnicianId: Number.isNaN(assignedTechnicianId) ? null : assignedTechnicianId,
    };

    this.loading.set(true);
    this.errorMessage.set('');

    this.interventionService.getAll(filters).subscribe({
      next: (items) => {
        this.interventions.set(items);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Impossible de charger les interventions.');
        this.loading.set(false);
      },
    });
  }
}