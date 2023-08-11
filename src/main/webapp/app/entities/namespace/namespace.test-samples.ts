import { INamespace, NewNamespace } from './namespace.model';

export const sampleWithRequiredData: INamespace = {
  id: 24664,
  name: 'Minivan handrail',
};

export const sampleWithPartialData: INamespace = {
  id: 32422,
  name: 'Account',
};

export const sampleWithFullData: INamespace = {
  id: 28393,
  name: 'Samarium East',
};

export const sampleWithNewData: NewNamespace = {
  name: 'Tasty Congolese Creative',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
