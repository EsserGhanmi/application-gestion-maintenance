export interface InterventionHistory {
  id: number;
  actionType: string;
  oldValue?: string | null;
  newValue?: string | null;
  changedAt: string;
  changedByName?: string | null;
}