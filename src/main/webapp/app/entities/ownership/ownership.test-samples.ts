import dayjs from 'dayjs/esm';

import { IOwnership, NewOwnership } from './ownership.model';

export const sampleWithRequiredData: IOwnership = {
  id: 3968,
  name: 'doloremque Usability',
};

export const sampleWithPartialData: IOwnership = {
  id: 26368,
  name: 'North wireless',
  endDate: dayjs('2023-08-11'),
};

export const sampleWithFullData: IOwnership = {
  id: 29269,
  name: 'Cyclocross',
  clientRef: 'bus networks',
  startDate: dayjs('2023-08-11'),
  endDate: dayjs('2023-08-11'),
};

export const sampleWithNewData: NewOwnership = {
  name: 'volt deliver Berkshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
