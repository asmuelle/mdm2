import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 11467,
  name: 'Boyer Security',
  isocode: 'Mississippi Unbranded plum',
};

export const sampleWithPartialData: ICountry = {
  id: 27601,
  name: 'Card',
  isocode: 'generate fiery Delaware',
};

export const sampleWithFullData: ICountry = {
  id: 3408,
  name: 'Electric Metal Dollar',
  isocode: 'zowie veniam',
};

export const sampleWithNewData: NewCountry = {
  name: 'tesla',
  isocode: 'neural',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
