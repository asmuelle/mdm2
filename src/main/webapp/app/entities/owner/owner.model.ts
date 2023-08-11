import { Stage } from 'app/entities/enumerations/stage.model';

export interface IOwner {
  id: number;
  name?: string | null;
  fullName?: string | null;
  ownerKey?: string | null;
  ownerGroup?: string | null;
  meters?: number | null;
  lastWeek?: number | null;
  beforeLastWeek?: number | null;
  amr?: number | null;
  lastYear?: number | null;
  contactEmail?: string | null;
  electricityPrice?: number | null;
  gasPrice?: number | null;
  gasStage?: keyof typeof Stage | null;
  electricityStage?: keyof typeof Stage | null;
  waterStage?: keyof typeof Stage | null;
  heatStage?: keyof typeof Stage | null;
  solarHeat?: keyof typeof Stage | null;
  solarPowerStage?: keyof typeof Stage | null;
  windStage?: keyof typeof Stage | null;
  cogenPowerStage?: keyof typeof Stage | null;
}

export type NewOwner = Omit<IOwner, 'id'> & { id: null };
