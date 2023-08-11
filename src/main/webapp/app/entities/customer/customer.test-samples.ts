import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 15640,
};

export const sampleWithPartialData: ICustomer = {
  id: 15637,
  name: 'Polonium Manors Frozen',
};

export const sampleWithFullData: ICustomer = {
  id: 21263,
  name: 'male male Vanadium',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
