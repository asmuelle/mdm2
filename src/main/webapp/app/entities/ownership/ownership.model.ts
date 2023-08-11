import dayjs from 'dayjs/esm';
import { IMeter } from 'app/entities/meter/meter.model';
import { IOwnershipClassification } from 'app/entities/ownership-classification/ownership-classification.model';
import { IOwner } from 'app/entities/owner/owner.model';

export interface IOwnership {
  id: number;
  name?: string | null;
  clientRef?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  meters?: Pick<IMeter, 'id'>[] | null;
  classifications?: Pick<IOwnershipClassification, 'id'>[] | null;
  owner?: Pick<IOwner, 'id'> | null;
}

export type NewOwnership = Omit<IOwnership, 'id'> & { id: null };
