import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { environment } from '../../../environments/environment';

import { InterventionHistory } from '../models/intervention-history.model';
import { Intervention, InterventionAssignRequest, InterventionStatusUpdateRequest } from '../models/intervention.model';
import { InterventionStatus } from '../models/intervention-status.model';
import { Priority } from '../models/priority.model';

export interface InterventionFilters {
  status?: InterventionStatus | '';
  priority?: Priority | '';
  assignedTechnicianId?: number | null;
}

@Injectable({ providedIn: 'root' })
export class InterventionService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/interventions`;

  getAll(filters?: InterventionFilters) {
    let params = new HttpParams();

    if (filters?.status) {
      params = params.set('status', filters.status);
    }

    if (filters?.priority) {
      params = params.set('priority', filters.priority);
    }

    if (filters?.assignedTechnicianId !== undefined && filters.assignedTechnicianId !== null) {
      params = params.set('assignedTechnicianId', filters.assignedTechnicianId);
    }

    return this.http.get<Intervention[]>(this.baseUrl, { params });
  }

  getById(id: number) {
    return this.http.get<Intervention>(`${this.baseUrl}/${id}`);
  }

  getHistory(id: number) {
    return this.http.get<InterventionHistory[]>(`${this.baseUrl}/${id}/history`);
  }

  updateStatus(id: number, payload: InterventionStatusUpdateRequest) {
    return this.http.patch<Intervention>(`${this.baseUrl}/${id}/status`, payload);
  }

  assign(id: number, payload: InterventionAssignRequest) {
    return this.http.patch<Intervention>(`${this.baseUrl}/${id}/assign`, payload);
  }

 
}
