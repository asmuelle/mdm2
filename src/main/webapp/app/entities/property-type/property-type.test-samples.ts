import { IPropertyType, NewPropertyType } from './property-type.model';

export const sampleWithRequiredData: IPropertyType = {
  id: 11780,
  name: 'sardonic Northeast Views',
  pattern: 'invoice Vermont Northeast',
};

export const sampleWithPartialData: IPropertyType = {
  id: 4982,
  name: 'lavender',
  pattern: 'Regional',
};

export const sampleWithFullData: IPropertyType = {
  id: 18619,
  name: 'gee',
  pattern: 'Electric Liberia Infrastructure',
};

export const sampleWithNewData: NewPropertyType = {
  name: 'um eyeballs',
  pattern: 'fooey Island Marketing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
