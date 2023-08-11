import { IOwnershipProperty, NewOwnershipProperty } from './ownership-property.model';

export const sampleWithRequiredData: IOwnershipProperty = {
  id: 14034,
  value: 'weakly',
};

export const sampleWithPartialData: IOwnershipProperty = {
  id: 11147,
  value: 'Gasoline',
};

export const sampleWithFullData: IOwnershipProperty = {
  id: 5689,
  value: 'Diesel Alessandra',
};

export const sampleWithNewData: NewOwnershipProperty = {
  value: 'striking Barrows',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
