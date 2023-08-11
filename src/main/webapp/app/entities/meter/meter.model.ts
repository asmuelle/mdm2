import dayjs from 'dayjs/esm';
import { IPeer } from 'app/entities/peer/peer.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { INamespace } from 'app/entities/namespace/namespace.model';
import { IOwnership } from 'app/entities/ownership/ownership.model';
import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

export interface IMeter {
  id: number;
  name?: string | null;
  amrWeek?: number | null;
  amrYear?: number | null;
  utility?: keyof typeof Utility | null;
  loadType?: keyof typeof LoadType | null;
  price?: number | null;
  lastReading?: dayjs.Dayjs | null;
  contactEmail?: string | null;
  parent?: Pick<IMeter, 'id'> | null;
  alternative?: Pick<IMeter, 'id'> | null;
  peer?: Pick<IPeer, 'id'> | null;
  provider?: Pick<IProvider, 'id'> | null;
  namespace?: Pick<INamespace, 'id'> | null;
  ownerships?: Pick<IOwnership, 'id'>[] | null;
}

export type NewMeter = Omit<IMeter, 'id'> & { id: null };
