import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

import { IPeer, NewPeer } from './peer.model';

export const sampleWithRequiredData: IPeer = {
  id: 8097,
};

export const sampleWithPartialData: IPeer = {
  id: 3664,
  name: 'Southeast Southeast Dynamic',
  loadType: 'CHILL_PROCESS',
};

export const sampleWithFullData: IPeer = {
  id: 9810,
  name: 'Illinois',
  utility: 'SOLARPOWER',
  loadType: 'CHILL',
};

export const sampleWithNewData: NewPeer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
