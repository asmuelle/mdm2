import { Utility } from 'app/entities/enumerations/utility.model';

export interface IMeterImport {
  id: number;
  provider?: string | null;
  utility?: keyof typeof Utility | null;
  namespace?: string | null;
  clientRef?: string | null;
  meterName?: string | null;
  contactEmail?: string | null;
  ownership?: string | null;
  owner?: string | null;
  postcode?: string | null;
  addresslines?: string | null;
  lat?: number | null;
  lon?: number | null;
  classifications?: string | null;
}

export type NewMeterImport = Omit<IMeterImport, 'id'> & { id: null };
