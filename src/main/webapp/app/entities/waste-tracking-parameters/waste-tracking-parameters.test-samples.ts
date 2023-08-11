import { IWasteTrackingParameters, NewWasteTrackingParameters } from './waste-tracking-parameters.model';

export const sampleWithRequiredData: IWasteTrackingParameters = {
  id: 18656,
  name: 'female dimly architecture',
};

export const sampleWithPartialData: IWasteTrackingParameters = {
  id: 26952,
  name: 'female Technician Scandium',
  maxActiveWasteIssues: 20036,
};

export const sampleWithFullData: IWasteTrackingParameters = {
  id: 15065,
  name: 'overriding misty aw',
  wasteIssueCreationThreshold: 24371,
  maxWasteIssueCreationRate: 9472,
  maxActiveWasteIssues: 9777,
  autoCreateWasteIssues: false,
};

export const sampleWithNewData: NewWasteTrackingParameters = {
  name: 'silver',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
