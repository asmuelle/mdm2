import { IScope, NewScope } from './scope.model';

export const sampleWithRequiredData: IScope = {
  id: 6133,
};

export const sampleWithPartialData: IScope = {
  id: 1457,
};

export const sampleWithFullData: IScope = {
  id: 16484,
  meterDescription: 'wax Factors BMW',
  meterName: 'Hungary',
  meterUtility: 'invoice Crew Borders',
  pricePerMonth: 2373,
};

export const sampleWithNewData: NewScope = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
