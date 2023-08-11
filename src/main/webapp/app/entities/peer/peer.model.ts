import { IOwner } from 'app/entities/owner/owner.model';
import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

export interface IPeer {
  id: number;
  name?: string | null;
  utility?: keyof typeof Utility | null;
  loadType?: keyof typeof LoadType | null;
  owner?: Pick<IOwner, 'id'> | null;
}

export type NewPeer = Omit<IPeer, 'id'> & { id: null };
