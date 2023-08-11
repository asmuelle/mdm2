import { IOwnershipClassification, NewOwnershipClassification } from './ownership-classification.model';

export const sampleWithRequiredData: IOwnershipClassification = {
  id: 26164,
};

export const sampleWithPartialData: IOwnershipClassification = {
  id: 19257,
  name: 'condominium withstand absent',
};

export const sampleWithFullData: IOwnershipClassification = {
  id: 26342,
  name: 'Sausages',
};

export const sampleWithNewData: NewOwnershipClassification = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
