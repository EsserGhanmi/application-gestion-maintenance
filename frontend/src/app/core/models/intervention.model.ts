export interface Intervention {
  id: number;
  title: string;
  description?: string;
  status: string;
  assignedTo?: number | null;
  equipmentId?: number | null;
  createdAt?: string;
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