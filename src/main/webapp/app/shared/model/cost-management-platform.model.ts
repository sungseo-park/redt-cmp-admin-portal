import { IHonestBuilding } from 'app/shared/model/honest-building.model';
import { IStarGate } from 'app/shared/model/star-gate.model';

export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface ICostManagementPlatform {
  id?: number;
  role?: string;
  access?: Access;
  hbId?: number;
  honestbuilding?: IHonestBuilding;
  stargate?: IStarGate;
}

export const defaultValue: Readonly<ICostManagementPlatform> = {};
