import { IOwnership } from 'app/entities/ownership/ownership.model';
import { ICountry } from 'app/entities/country/country.model';

export interface IAddress {
  id: number;
  city?: string | null;
  postcode?: string | null;
  addresslines?: string | null;
  lat?: number | null;
  lon?: number | null;
  ownership?: Pick<IOwnership, 'id'> | null;
  country?: Pick<ICountry, 'id'> | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
