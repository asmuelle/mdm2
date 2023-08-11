import { IService, NewService } from './service.model';

export const sampleWithRequiredData: IService = {
  id: 6326,
};

export const sampleWithPartialData: IService = {
  id: 31566,
  name: 'Oriental Licensed',
};

export const sampleWithFullData: IService = {
  id: 7661,
  name: 'Extended Bicycle',
};

export const sampleWithNewData: NewService = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
