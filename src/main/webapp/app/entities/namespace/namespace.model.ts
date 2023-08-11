export interface INamespace {
  id: number;
  name?: string | null;
}

export type NewNamespace = Omit<INamespace, 'id'> & { id: null };
