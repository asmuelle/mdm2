export interface IPropertyType {
  id: number;
  name?: string | null;
  pattern?: string | null;
}

export type NewPropertyType = Omit<IPropertyType, 'id'> & { id: null };
