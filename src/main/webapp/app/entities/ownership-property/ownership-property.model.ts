import { IPropertyType } from 'app/entities/property-type/property-type.model';
import { IOwnership } from 'app/entities/ownership/ownership.model';

export interface IOwnershipProperty {
  id: number;
  value?: string | null;
  type?: Pick<IPropertyType, 'id'> | null;
  ownership?: Pick<IOwnership, 'id'> | null;
}

export type NewOwnershipProperty = Omit<IOwnershipProperty, 'id'> & { id: null };
