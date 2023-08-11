import dayjs from 'dayjs/esm';

import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

import { IMeter, NewMeter } from './meter.model';

export const sampleWithRequiredData: IMeter = {
  id: 8926,
};

export const sampleWithPartialData: IMeter = {
  id: 26479,
  name: 'evoke',
  amrWeek: 7136,
  amrYear: 30993,
};

export const sampleWithFullData: IMeter = {
  id: 6799,
  name: 'South Northwest',
  amrWeek: 20449,
  amrYear: 28819,
  utility: 'WIND',
  loadType: 'CHILL',
  price: 4178,
  lastReading: dayjs('2023-08-11'),
  contactEmail: 'Manat Southwest',
};

export const sampleWithNewData: NewMeter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
