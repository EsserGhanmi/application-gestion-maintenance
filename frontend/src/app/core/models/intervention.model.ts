export interface Intervention {
  id: number;
  title: string;
  description?: string;
  status: string;
  priority?: string;
  assignedTo?: number | null;
  assignedTechnicianId?: number | null;
  equipmentId?: number | null;
  createdById?: number | null;
  createdAt?: string;
  scheduledAt?: string | null;
  closedAt?: string | null;
  updatedAt?: string;
}

export interface InterventionHistoryEntry {
  id: number;
  interventionId: number;
  fromStatus?: string | null;
  toStatus: string;
  changedBy?: number | null;
  changedAt: string;
  comment?: string | null;
}

export interface InterventionStatusUpdateRequest {
  status: string;
}

export interface InterventionAssignRequest {
  userId: number;
}