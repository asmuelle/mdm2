export interface IService {
  id: number;
  name?: string | null;
}

export type NewService = Omit<IService, 'id'> & { id: null };
