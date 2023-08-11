import { IContract } from 'app/entities/contract/contract.model';
import { IService } from 'app/entities/service/service.model';

export interface IScope {
  id: number;
  meterDescription?: string | null;
  meterName?: string | null;
  meterUtility?: string | null;
  pricePerMonth?: number | null;
  contract?: Pick<IContract, 'id'> | null;
  service?: Pick<IService, 'id'> | null;
}

export type NewScope = Omit<IScope, 'id'> & { id: null };
