import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { environment } from '../../../environments/environment';
import {
  Intervention,
  InterventionAssignRequest,
  InterventionHistoryEntry,
  InterventionStatusUpdateRequest,
} from '../models/intervention.model';

@Injectable({ providedIn: 'root' })
export class InterventionService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/interventions`;

  getAll() {
    return this.http.get<Intervention[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Intervention>(`${this.baseUrl}/${id}`);
  }

  updateStatus(id: number, payload: InterventionStatusUpdateRequest) {
    return this.http.patch<Intervention>(`${this.baseUrl}/${id}/status`, payload);
  }

  assign(id: number, payload: InterventionAssignRequest) {
    return this.http.patch<Intervention>(`${this.baseUrl}/${id}/assign`, payload);
  }

  getHistory(id: number) {
    return this.http.get<InterventionHistoryEntry[]>(`${this.baseUrl}/${id}/history`);
  }
}