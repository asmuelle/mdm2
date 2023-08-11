import { Stage } from 'app/entities/enumerations/stage.model';

import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  id: 11468,
  name: 'Diesel Rodriguez sill',
};

export const sampleWithPartialData: IOwner = {
  id: 8076,
  name: 'Caesium invoice',
  fullName: 'virtual ump Illinois',
  ownerKey: 'gray Gasoline driver',
  ownerGroup: 'Small spicy mortified',
  lastWeek: 4897,
  beforeLastWeek: 6556,
  lastYear: 5160,
  heatStage: 'PRODUCTION',
  solarHeat: 'PRODUCTION',
  solarPowerStage: 'PRODUCTION',
  windStage: 'TRIAL',
  cogenPowerStage: 'TRIAL',
};

export const sampleWithFullData: IOwner = {
  id: 1211,
  name: 'integrated',
  fullName: 'Cambridgeshire Keyboard',
  ownerKey: 'ROI lest',
  ownerGroup: 'second',
  meters: 9502,
  lastWeek: 31995,
  beforeLastWeek: 25482,
  amr: 17540,
  lastYear: 15449,
  contactEmail: 'Shoes Pizza phooey',
  electricityPrice: 23483,
  gasPrice: 22693,
  gasStage: 'METERS_LISTED',
  electricityStage: 'TRIAL',
  waterStage: 'METERS_LISTED',
  heatStage: 'PRODUCTION',
  solarHeat: 'METERS_LISTED',
  solarPowerStage: 'TRIAL',
  windStage: 'DEMO',
  cogenPowerStage: 'PRODUCTION',
};

export const sampleWithNewData: NewOwner = {
  name: 'Polonium',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
