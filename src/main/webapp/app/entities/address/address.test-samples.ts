import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 2325,
};

export const sampleWithPartialData: IAddress = {
  id: 28768,
  city: 'Mantefort',
  addresslines: 'Volvo maroon',
  lon: 17725,
};

export const sampleWithFullData: IAddress = {
  id: 7550,
  city: "O'Haraborough",
  postcode: 'Bedfordshire temporibus quietly',
  addresslines: 'Kuhic assumenda',
  lat: 7127,
  lon: 21605,
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
