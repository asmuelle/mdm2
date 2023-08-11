import { IProvider, NewProvider } from './provider.model';

export const sampleWithRequiredData: IProvider = {
  id: 12385,
  name: 'mockingly Gasoline',
};

export const sampleWithPartialData: IProvider = {
  id: 13347,
  name: 'Rolls',
};

export const sampleWithFullData: IProvider = {
  id: 21279,
  name: 'South Electric',
};

export const sampleWithNewData: NewProvider = {
  name: 'Fluorine Javier',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
