export interface IProvider {
  id: number;
  name?: string | null;
}

export type NewProvider = Omit<IProvider, 'id'> & { id: null };
