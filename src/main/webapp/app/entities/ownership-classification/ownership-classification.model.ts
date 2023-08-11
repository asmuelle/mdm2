import { IOwnership } from 'app/entities/ownership/ownership.model';

export interface IOwnershipClassification {
  id: number;
  name?: string | null;
  ownerships?: Pick<IOwnership, 'id'>[] | null;
}

export type NewOwnershipClassification = Omit<IOwnershipClassification, 'id'> & { id: null };
