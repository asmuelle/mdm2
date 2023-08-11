import { Utility } from 'app/entities/enumerations/utility.model';

import { IMeterImport, NewMeterImport } from './meter-import.model';

export const sampleWithRequiredData: IMeterImport = {
  id: 18624,
};

export const sampleWithPartialData: IMeterImport = {
  id: 19720,
  provider: 'lumen West',
  owner: 'Southeast Northeast ivory',
  addresslines: 'Paradigm Luxurious',
  classifications: 'program',
};

export const sampleWithFullData: IMeterImport = {
  id: 5342,
  provider: 'extremely Southwest cold',
  utility: 'HEAT',
  namespace: 'magenta International',
  clientRef: 'Bigender',
  meterName: 'transmitting',
  contactEmail: 'Demiflux concerning male',
  ownership: 'markets turquoise',
  owner: 'fooey Future',
  postcode: 'mid',
  addresslines: 'monitor',
  lat: 3322,
  lon: 5380,
  classifications: 'Northwest Account Fiat',
};

export const sampleWithNewData: NewMeterImport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
