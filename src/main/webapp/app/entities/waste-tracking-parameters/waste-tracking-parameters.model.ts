export interface IWasteTrackingParameters {
  id: number;
  name?: string | null;
  wasteIssueCreationThreshold?: number | null;
  maxWasteIssueCreationRate?: number | null;
  maxActiveWasteIssues?: number | null;
  autoCreateWasteIssues?: boolean | null;
}

export type NewWasteTrackingParameters = Omit<IWasteTrackingParameters, 'id'> & { id: null };
